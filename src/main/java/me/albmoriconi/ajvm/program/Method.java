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

package me.albmoriconi.ajvm.program;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents an IJVM assembly method.
 */
public class Method {

    private short parametersSize;
    private short variableSize;
    private List<Instruction> instructions;

    /**
     * Constructor.
     */
    public Method() {
        parametersSize = 1;
        variableSize = 0;
        instructions = new LinkedList<>();
    }

    /**
     * Getter for parameterSize
     *
     * @return The number of parameters to the method.
     */
    public short getParametersSize() {
        return parametersSize;
    }

    /**
     * Setter for parameterSize.
     *
     * @param parametersSize The number of parameters to the method.
     */
    public void setParametersSize(short parametersSize) {
        this.parametersSize = parametersSize;
    }

    /**
     * Getter for variableSize
     *
     * @return The number of variables in the method variable area.
     */
    public short getVariableSize() {
        return variableSize;
    }

    /**
     * Setter for parameterSize.
     *
     * @param variableSize The number of variables in the method variable area.
     */
    public void setVariableSize(short variableSize) {
        this.variableSize = variableSize;
    }

    /**
     * Getter for instructions.
     *
     * @return The method instructions.
     */
    public List<Instruction> getInstructions() {
        return instructions;
    }

    /**
     * Converts a method to a string.
     *
     * @return A {@link String} object that represents the method bits in binary.
     */
    @Override public String toString() {
        StringBuilder methodBuilder = new StringBuilder();
        methodBuilder.append(ProgramUtils.zeroFilledStringFromShort(parametersSize));
        methodBuilder.append(ProgramUtils.zeroFilledStringFromShort(variableSize));

        for (Instruction i : instructions)
            methodBuilder.append(i);

        return methodBuilder.toString();
    }
}
