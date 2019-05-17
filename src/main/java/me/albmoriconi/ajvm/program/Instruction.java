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

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an AJVM assembly instruction.
 */
public class Instruction {

    private byte opcode;
    private List<Byte> operands;

    /**
     * Constructor.
     */
    public Instruction() {
        operands = new ArrayList<>();
    }

    /**
     * Getter for opcode.
     *
     * @return The instruction opcode.
     */
    public byte getOpcode() {
        return opcode;
    }

    /**
     * Setter for opcode.
     *
     * @param opcode The instruction opcode.
     */
    public void setOpcode(byte opcode) {
        this.opcode = opcode;
    }

    /**
     * Getter for operands.
     *
     * @return A list of bytes, each an operand to the instruction.
     */
    public List<Byte> getOperands() {
        return operands;
    }

    /**
     * Converts an instruction to string.
     *
     * @return A string that represents the instruction.
     */
    @Override public String toString() {
        StringBuilder instructionBuilder = new StringBuilder();
        instructionBuilder.append(zeroFilledStringFromByte(opcode));

        for (byte operand : operands)
            instructionBuilder.append(zeroFilledStringFromByte(operand));

        return instructionBuilder.toString();
    }

    private String zeroFilledStringFromByte(byte b) {
        // Discard sign extension and extend to the left
        return String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(" ", "0");
    }
}
