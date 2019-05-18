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

package me.albmoriconi.ajvm.writer;

import me.albmoriconi.ajvm.program.Instruction;
import me.albmoriconi.ajvm.program.Method;
import me.albmoriconi.ajvm.program.Program;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * Binary file writer for AJVM assembly program.
 * <p>
 * See {@link #write} for detailed description.
 */
public class BinaryWriter extends BaseProgramWriter {

    private final static int MAGIC_NUMBER = 1234567890;

    private final BufferedOutputStream writer;

    /**
     * Constructor.
     *
     * @param fileName The path of the output file.
     *
     * @throws IOException If file can't be opened for any reason.
     */
    public BinaryWriter(String fileName) throws IOException {
        this.writer = new BufferedOutputStream(new FileOutputStream(fileName));
    }

    /**
     * Binary file writer for AJVM assembly program.
     *
     * @param program An AJVM assembly program.
     * @param constantAreaStart Address of the first word in the constant area.
     * @param methodAreaStart Address of the first word in the method area.
     *
     * @throws IOException If an IO error occurs.
     */
    @Override public void write(Program program, int constantAreaStart, int methodAreaStart) throws IOException {
        Objects.requireNonNull(program, "Unexpected null reference in BinaryWriter#write");
        write32(MAGIC_NUMBER);
        write32(constantAreaStart);
        write32(methodAreaStart);

        for (int c : program.getConstantValues())
            write32(c);

        for (Method m : program.getMethods()) {
            write16(m.getParametersSize());
            write16(m.getVariableSize());
            for (Instruction i : m.getInstructions()) {
                writer.write(i.getOpcode());

                for (byte b : i.getOperands())
                    writer.write(b);
            }
        }

        writer.flush();
    }

    private void write32(int i) throws IOException {
        writer.write((byte) (i >> 24));
        writer.write((byte) (i >> 16));
        writer.write((byte) (i >> 8));
        writer.write((byte) i);
    }

    private void write16(short s) throws IOException {
        writer.write((byte) (s >> 8));
        writer.write((byte) s);
    }
}
