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

import java.io.IOException;

/**
 * Abstract writer for AJVM assembly program.
 */
abstract public class BaseProgramWriter {

    /**
     * Abstract writer method for AJVM assembly program.
     *
     * @param program An AJVM assembly program.
     * @param constantAreaStart Address of the first word in the constant area.
     * @param methodAreaStart Address of the first word in the method area.
     *
     * @throws IOException If an IO error occurs.
     */
    abstract public void write(Program program, int constantAreaStart, int methodAreaStart) throws IOException;
}
