/*
 * Copyright (C) 2019 Alberto Moriconi
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package me.albmoriconi.ajvm;

import me.albmoriconi.ajvm.antlr.AjvmBaseListener;
import me.albmoriconi.ajvm.antlr.AjvmParser;
import me.albmoriconi.ajvm.program.Instruction;

import me.albmoriconi.ajvm.program.Method;
import me.albmoriconi.ajvm.program.Program;
import me.albmoriconi.ajvm.program.ProgramUtils;
import org.antlr.v4.runtime.tree.ErrorNode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Listens to events on the parsed AJVM assembly source, producing a translated
 * program.
 */
public class Translator extends AjvmBaseListener {

    private Program translatedProgram;
    private int currentWordInMethodArea;
    private Method currentMethod;
    private Map<String, Short> constantOffsetFor;
    private Map<String, Short> variableOffsetFor;
    private Map<String, Short> labelWordFor;
    private Map<String, List<Instruction>> instructionsWaitingOn;

    /**
     * Constructor.
     */
    public Translator() {
        translatedProgram = new Program();
        currentWordInMethodArea = 0;
        currentMethod = new Method();
        constantOffsetFor = new HashMap<>();
        variableOffsetFor = new HashMap<>();
        labelWordFor = new HashMap<>();
        instructionsWaitingOn = new HashMap<>();
    }

    /**
     * Getter for translatedProgram.
     *
     * @return The translated AJVM assembly program.
     */
    public Program getTranslatedProgram() {
        return translatedProgram;
    }

    /**
     * {@inheritDoc} TODO Solve dotnames
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitProgram(AjvmParser.ProgramContext ctx) { }

    /**
     * Add value to program constant area and (:name -> offset) entry to translator table.
     *
     * @param ctx The rule invocation context for parsing.
     */
    @Override public void enterConstantDeclaration(AjvmParser.ConstantDeclarationContext ctx) {
        String name = ctx.NAME().getText();
        String value = ctx.VALUE().getText();

        if (constantOffsetFor.containsKey(name))
            throw new RuntimeException("Constant " + name + " is already defined");

        constantOffsetFor.put(name, (short) translatedProgram.getConstantValues().size());

        try {
            translatedProgram.getConstantValues().add(ProgramUtils.parseIntOrHex(value));
        } catch (NumberFormatException ex) {
            throw new RuntimeException("Invalid value " + value + " for constant " + name);
        }
    }

    /**
     * Add starting address to program constant area and (:name -> offset) entry to translator table.
     *
     * @param ctx The rule invocation context for parsing.
     */
    @Override public void enterMainDeclaration(AjvmParser.MainDeclarationContext ctx) {
        constantOffsetFor.put(".main", (short) translatedProgram.getConstantValues().size());
        translatedProgram.getConstantValues().add(currentWordInMethodArea);

        currentWordInMethodArea += 2;
        currentMethod = new Method();
        variableOffsetFor = new HashMap<>();
        labelWordFor = new HashMap<>();
    }

    /**
     * {@inheritDoc} TODO Solve goto labels and push method
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitMainDeclaration(AjvmParser.MainDeclarationContext ctx) { }

    /**
     * Add starting address to program constant area and (:name -> offset) entry to translator table.
     *
     * @param ctx The rule invocation context for parsing.
     */
    @Override public void enterMethodDeclaration(AjvmParser.MethodDeclarationContext ctx) {
        String dotname = ctx.DOTNAME().getText();

        if (constantOffsetFor.containsKey(dotname))
            throw new RuntimeException("Method " + dotname + " is already defined");

        constantOffsetFor.put(dotname, (short) translatedProgram.getConstantValues().size());
        translatedProgram.getConstantValues().add(currentWordInMethodArea);

        currentWordInMethodArea += 2;
        currentMethod = new Method();
        variableOffsetFor = new HashMap<>();
        labelWordFor = new HashMap<>();
    }

    /**
     * {@inheritDoc} TODO Solve goto labels and push method
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitMethodDeclaration(AjvmParser.MethodDeclarationContext ctx) { }

    /**
     * Add (:name -> offset) entry to translator table and adjust method parameter size.
     *
     * @param ctx The rule invocation context for parsing.
     */
    @Override public void enterParameterDeclaration(AjvmParser.ParameterDeclarationContext ctx) {
        String name = ctx.NAME().getText();

        if (variableOffsetFor.containsKey(name))
            throw new RuntimeException("Parameter " + name + " is already defined");

        variableOffsetFor.put(name, currentMethod.getParametersSize());

        currentMethod.setParametersSize((short) (currentMethod.getParametersSize() + 1));
    }

    /**
     * Add (:name -> offset) entry to translator table and adjust method variable size.
     *
     * @param ctx The rule invocation context for parsing.
     */
    @Override public void enterVariableDeclaration(AjvmParser.VariableDeclarationContext ctx) {
        String name = ctx.NAME().getText();

        if (variableOffsetFor.containsKey(name))
            throw new RuntimeException("Variable " + name + " is already defined");

        variableOffsetFor.put(name, (short) (currentMethod.getParametersSize() + currentMethod.getVariableSize()));

        currentMethod.setVariableSize((short) (currentMethod.getVariableSize() + 1));
    }

    /**
     * Add (:name -> word) entry to translator table.
     *
     * @param ctx The rule invocation context for parsing.
     */
    @Override public void enterLabel(AjvmParser.LabelContext ctx) {
        String name = ctx.NAME().getText();

        if (labelWordFor.containsKey(name))
            throw new RuntimeException("Label " + name + " is already defined");

        labelWordFor.put(name, (short) currentWordInMethodArea);
    }

    /**
     * Add instruction to method.
     *
     * @param ctx The rule invocation context for parsing.
     */
    @Override public void enterNoOperandInstruction(AjvmParser.NoOperandInstructionContext ctx) {
        String mnemonic = ctx.NO_OPERAND_MNEMONIC().getText();
        Instruction instr = new Instruction();
        instr.setOpcode(ProgramUtils.opcodeFor(mnemonic));
        currentWordInMethodArea += 1;

        // HALT is a no operand instruction but translates as a GOTO (short value)
        if (mnemonic.equals("HALT")) {
            instr.getOperands().add((byte) 0);
            instr.getOperands().add((byte) 0);
            currentWordInMethodArea += 2;
        }

        currentMethod.getInstructions().add(instr);
    }

    /**
     * Add instruction to method.
     *
     * @param ctx The rule invocation context for parsing.
     */
    @Override public void enterByteValueInstruction(AjvmParser.ByteValueInstructionContext ctx) {
        String mnemonic = ctx.BYTE_VALUE_MNEMONIC().getText();
        Instruction instr = new Instruction();
        instr.setOpcode(ProgramUtils.opcodeFor(mnemonic));
        instr.getOperands().add((byte) ProgramUtils.parseIntOrHex(ctx.VALUE().getText()));
        currentWordInMethodArea += 2;
        currentMethod.getInstructions().add(instr);
    }

    /**
     * Add instruction to method.
     *
     * @param ctx The rule invocation context for parsing.
     */
    @Override public void enterShortNameInstruction(AjvmParser.ShortNameInstructionContext ctx) {
        String mnemonic = ctx.SHORT_NAME_MNEMONIC().getText();
        String name = ctx.NAME().getText();
        Instruction instr = new Instruction();
        instr.setOpcode(ProgramUtils.opcodeFor(mnemonic));

        // Special case: NAME refers to a constant, can be solved now
        if (mnemonic.equals("LDC_W")) {
            if (!constantOffsetFor.containsKey(name))
                throw new RuntimeException("Constant " + name + " is undefined");
            Short offset = constantOffsetFor.get(name);
            instr.getOperands().add((byte) (offset >> 8));
            instr.getOperands().add(offset.byteValue());
        } else {
            // Another special case: WIDE variants
            if (mnemonic.equals("ILOAD") || mnemonic.equals("ISTORE")) {
                Instruction wide = new Instruction();
                wide.setOpcode(ProgramUtils.opcodeFor("WIDE"));
                currentWordInMethodArea += 1;
                currentMethod.getInstructions().add(wide);
                instr.getOperands().add((byte) 0);
                instr.getOperands().add((byte) 0);
            } else {
                // A bit hacky: remember the position in the instruction operand field
                instr.getOperands().add((byte) (currentWordInMethodArea >> 24));
                instr.getOperands().add((byte) currentWordInMethodArea);
            }

            if (!instructionsWaitingOn.containsKey(name))
                instructionsWaitingOn.put(name, new LinkedList<>());
            instructionsWaitingOn.get(name).add(instr);
        }

        currentWordInMethodArea += 3;
        currentMethod.getInstructions().add(instr);
    }

    /**
     * Add instruction to method.
     *
     * @param ctx The rule invocation context for parsing.
     */
    @Override public void enterByteNameByteValueInstruction(AjvmParser.ByteNameByteValueInstructionContext ctx) {
        String mnemonic = ctx.BYTE_NAME_BYTE_VALUE_MNEMONIC().getText();
        String name = ctx.NAME().getText();

        if (!variableOffsetFor.containsKey(name))
            throw new RuntimeException("Variable or parameter " + name + " is undefined");

        Instruction instr = new Instruction();
        instr.setOpcode(ProgramUtils.opcodeFor(mnemonic));
        instr.getOperands().add(variableOffsetFor.get(ctx.NAME().getText()).byteValue());
        instr.getOperands().add((byte) ProgramUtils.parseIntOrHex(ctx.VALUE().getText()));
        currentWordInMethodArea += 3;
        currentMethod.getInstructions().add(instr);
    }

    /**
     * Add instruction to method.
     *
     * @param ctx The rule invocation context for parsing.
     */
    @Override public void enterByteNameInstruction(AjvmParser.ByteNameInstructionContext ctx) {
        String mnemonic = ctx.BYTE_NAME_MNEMONIC().getText();
        String name = ctx.NAME().getText();
        Instruction instr = new Instruction();
        instr.setOpcode(ProgramUtils.opcodeFor(mnemonic));
        instr.getOperands().add((byte) 0);

        if (!instructionsWaitingOn.containsKey(name))
            instructionsWaitingOn.put(name, new LinkedList<>());
        instructionsWaitingOn.get(name).add(instr);

        currentWordInMethodArea += 2;
        currentMethod.getInstructions().add(instr);
    }

    /**
     * Add instruction to method.
     *
     * @param ctx The rule invocation context for parsing.
     */
    @Override public void enterShortDotnameInstruction(AjvmParser.ShortDotnameInstructionContext ctx) {
        String mnemonic = ctx.SHORT_DOTNAME_MNEMONIC().getText();
        String dotname = ctx.DOTNAME().getText();
        Instruction instr = new Instruction();
        instr.setOpcode(ProgramUtils.opcodeFor(mnemonic));

        // Dotname can't be solved at this moment, instruction is pending
        if (!instructionsWaitingOn.containsKey(dotname))
            instructionsWaitingOn.put(dotname, new LinkedList<>());
        instructionsWaitingOn.get(dotname).add(instr);

        instr.getOperands().add((byte) 0);
        instr.getOperands().add((byte) 0);

        currentWordInMethodArea += 3;
        currentMethod.getInstructions().add(instr);
    }

    /**
     * Handle a parsing error.
     *
     * @param node The node that caused a parsing error.
     */
    @Override public void visitErrorNode(ErrorNode node) {
        throw new RuntimeException("Invalid syntax");
    }
}
