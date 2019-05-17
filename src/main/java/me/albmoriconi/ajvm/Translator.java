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

import me.albmoriconi.ajvm.program.Program;
import org.antlr.v4.runtime.tree.ErrorNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Listens to events on the parsed AJVM assembly source, producing a translated
 * program.
 */
public class Translator extends AjvmBaseListener {

    private Program translatedProgram;
    private Map<String, Short> constantOffsetFor;
    private Map<String, Short> labelOffsetFor;
    private Map<String, List<Instruction>> instructionsWaitingOn;
    private int currentWordInMethodArea;

    /**
     * Constructor.
     */
    public Translator() {
        translatedProgram = new Program();
        constantOffsetFor = new HashMap<>();
        labelOffsetFor = new HashMap<>();
        instructionsWaitingOn = new HashMap<>();
        currentWordInMethodArea = 0;
    }

    /**
     * Getter for translatedProgram
     *
     * @return The translated AJVM assembly program.
     */
    public Program getTranslatedProgram() {
        return translatedProgram;
    }
}
