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

/**
 * Shared utility static methods for classes in the program package.
 */
class ProgramUtils {

    private ProgramUtils() { }

    static String zeroFilledStringFromByte(byte b) {
        // Discard sign extension and extend to the left
        return String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(" ", "0");
    }

    static String zeroFilledStringFromShort(short s) {
        // Discard sign extension and extend to the left
        return String.format("%16s", Integer.toBinaryString(s & 0xFFFF)).replace(" ", "0");
    }

    static String zeroFilledStringFromInt(int i) {
        // Discard sign extension and extend to the left
        return String.format("%32s", Integer.toBinaryString(i)).replace(" ", "0");
    }
}
