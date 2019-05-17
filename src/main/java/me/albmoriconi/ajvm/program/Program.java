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

import java.util.*;

/**
 * Represents an AJVM assembly program.
 */
public class Program {

    private List<Integer> constantValues;
    private List<Method> methods;

    /**
     * Constructor.
     */
    public Program() {
        constantValues = new ArrayList<>(255);
        methods = new LinkedList<>();
    }

    /**
     * Getter for constantValues
     *
     * @return The constant area of the program.
     */
    public List<Integer> getConstantValues() {
        return constantValues;
    }

    /**
     * Getter for methods
     *
     * @return The method area of the program.
     */
    public List<Method> getMethods() {
        return methods;
    }

    /**
     * Gets the text of the constant area as a string.
     *
     * @return A {@link String} object that represents the costant area of the program in binary.
     */
    public String getConstantAreaAsString() {
        StringBuilder constantAreaBuilder = new StringBuilder();

        for (Integer i : constantValues)
            constantAreaBuilder.append(ProgramUtils.zeroFilledStringFromInt(i));

        return constantAreaBuilder.toString();
    }

    /**
     * Gets the text of the method area as a string.
     *
     * @return A {@link String} object that represents the method area of the program in binary.
     */
    public String getMethodAreaAsString() {
        StringBuilder methodAreaBuilder = new StringBuilder();

        for (Method m : methods)
            methodAreaBuilder.append(m);

        return methodAreaBuilder.toString();
    }
}
