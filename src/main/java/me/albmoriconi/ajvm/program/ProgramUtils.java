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
public class ProgramUtils {

    private ProgramUtils() { }

    public static String zeroFilledStringFromByte(byte b) {
        // Discard sign extension and extend to the left
        return String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(" ", "0");
    }

    public static String zeroFilledStringFromShort(short s) {
        // Discard sign extension and extend to the left
        return String.format("%16s", Integer.toBinaryString(s & 0xFFFF)).replace(" ", "0");
    }

    public static String zeroFilledStringFromInt(int i) {
        // Discard sign extension and extend to the left
        return String.format("%32s", Integer.toBinaryString(i)).replace(" ", "0");
    }

    public static byte opcodeFor(String s) {
        switch (s) {
            case "DUP":
                return (byte) parseIntOrHex("0x57");
            case "IADD":
                return (byte) parseIntOrHex("0x65");
            case "IAND":
                return (byte) parseIntOrHex("0x7E");
            case "IOR":
                return (byte) parseIntOrHex("0xB6");
            case "IRETURN":
                return (byte) parseIntOrHex("0xAD");
            case "ISUB":
                return (byte) parseIntOrHex("0x5C");
            case "POP":
                return (byte) parseIntOrHex("0x59");
            case "SWAP":
                return (byte) parseIntOrHex("0x5F");
            case "BIPUSH":
                return (byte) parseIntOrHex("0x10");
            case "IINC":
                return (byte) parseIntOrHex("0x84");
            case "GOTO":
            case "HALT":
                return (byte) parseIntOrHex("0xA7");
            case "IFEQ":
                return (byte) parseIntOrHex("0x99");
            case "IFLT":
                return (byte) parseIntOrHex("0x9D");
            case "IF_ICMPEQ":
                return (byte) parseIntOrHex("0xA1");
            case "LDC_W":
                return (byte) parseIntOrHex("0x20");
            case "ILOAD":
                return (byte) parseIntOrHex("0x15");
            case "ISTORE":
                return (byte) parseIntOrHex("0x36");
            case "INVOKEVIRTUAL":
                return (byte) parseIntOrHex("0xB9");
            case "WIDE":
                return (byte) parseIntOrHex("0xCF");
            case "NOP":
            default:
                return 0;
        }
    }

    public static int parseIntOrHex(String s) {
        if (s.startsWith("0x"))
            return Integer.parseInt(s.substring(2), 16);
        else
            return Integer.parseInt(s);
    }
}
