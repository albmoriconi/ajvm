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

class InstructionSpecification extends Specification {

    @Unroll
    def "converting #opcode #operand1 #operand2 #operand3 to String gives #string"() {
        given:
        def instruction = new Instruction()

        when:
        instruction.setOpcode((byte) opcode)
        instruction.getOperands().add((byte) operand1)
        instruction.getOperands().add((byte) operand2)
        instruction.getOperands().add((byte) operand3)

        then:
        instruction.toString() == string

        where:
        opcode | operand1 | operand2 | operand3 | string
        -93    | 42       | 28       | -36      | "10100011001010100001110011011100"
        163    | 42       | 28       | 220      | "10100011001010100001110011011100"
    }
}
