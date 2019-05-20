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

import me.albmoriconi.ajvm.program.Program;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

/**
 * Text file writer for for AJVM assembly program.
 * <p>
 * See {@link #write} for detailed description.
 */
public class TextWriter extends BaseProgramWriter {

    private final BufferedWriter writer;

    /**
     * Constructor.
     *
     * @param fileName The path of the output file.
     *
     * @throws IOException If file can't be opened for any reason.
     */
    public TextWriter(String fileName) throws IOException {
        this.writer = new BufferedWriter(new FileWriter(fileName));
    }

    /**
     * Text file writer for AJVM assembly program.
     *
     * @param program An AJVM assembly program.
     * @param constantAreaStart Address of the first word in the constant area.
     * @param methodAreaStart Address of the first word in the method area.
     *
     * @throws IOException If an IO error occurs.
     */
    @Override public void write(Program program, int constantAreaStart, int methodAreaStart) throws IOException {
        Objects.requireNonNull(program, "Unexpected null reference in TextWriter#write");
        writer.write("@" + constantAreaStart + "\n");
        // Match an empty string that has the last match (\G) followed by 8 characters (........) before it ((?<= ))
        // Split according to matches
        // Join with newlines
        writer.write(String.join("\n",
                program.getConstantAreaAsString().split("(?<=\\G........)")) + "\n");
        writer.write("@" + methodAreaStart + "\n");
        writer.write(String.join("\n",
                program.getMethodAreaAsString().split("(?<=\\G........)")) + "\n");
        writer.flush();
    }
}
