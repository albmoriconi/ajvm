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

class MethodSpecification extends Specification {

    def "converting a method to its string representation"() {
        given:
        def method = new Method()
        def instruction = new Instruction()

        when:
        method.setParametersSize((short) 3)
        method.getVariableOffsetTable().put("var1", (short) 0)
        method.getVariableOffsetTable().put("var2", (short) 1)
        method.getVariableOffsetTable().put("var3", (short) 2)
        method.getVariableOffsetTable().put("var4", (short) 3)
        instruction.setOpcode((byte) 163)
        instruction.getOperands().add((byte) 42)
        instruction.getOperands().add((byte) 28)
        instruction.getOperands().add((byte) 220)
        method.getInstructions().add(instruction)

        then:
        method.toString() == "0000000000000011000000000000010010100011001010100001110011011100"
    }
}
