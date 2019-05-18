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

package me.albmoriconi.ajvm.program

import spock.lang.*

class ProgramUtilsSpecification extends Specification {

    @Unroll
    def "converting #mnemonic to opcode gives #opcode"() {
        expect:
        ProgramUtils.opcodeFor(mnemonic) == opcode

        where:
        mnemonic    | opcode
        "IF_ICMPEQ" | (byte) 161
        "ISUB"      | (byte) 92
    }

    @Unroll
    def "parsing #string gives #value"() {
        expect:
        ProgramUtils.parseIntOrHex(string) == value

        where:
        string | value
        "120"  | 120
        "256"  | 256
        "0xFF" | 255
        "0x5B" | 91
    }
}
