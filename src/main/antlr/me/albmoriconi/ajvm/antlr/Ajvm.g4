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

// Grammar for AJVM Assembly Language

grammar Ajvm;

// Parser rules

program : constantBlock? methodBlock?;

constantBlock : '.constant' constantDeclaration* '.endconstant';

constantDeclaration : NAME VALUE;

methodBlock : mainDeclaration methodDeclaration*;

mainDeclaration : '.main' variableBlock? labeledInstruction* '.endmethod';

methodDeclaration : DOTNAME '(' parameterBlock? ')' variableBlock? instruction* '.endmethod';

parameterBlock : parameterDeclaration (',' parameterDeclaration)*;

parameterDeclaration : NAME;

variableBlock : '.var' variableDeclaration* '.endvar';

variableDeclaration : NAME;

labeledInstruction : label? instruction;

label : NAME ':';

instruction : NO_OPERAND_MNEMONIC                           #noOperandInstruction
            | BYTE_VALUE_MNEMONIC VALUE                     #byteValueInstruction
            | 'WIDE' BYTE_NAME_MNEMONIC NAME                #shortNameInstruction
            | BYTE_NAME_BYTE_VALUE_MNEMONIC NAME VALUE      #byteNamebyteValueInstruction
            | SHORT_NAME_MNEMONIC NAME                      #shortNameInstruction
            | BYTE_NAME_MNEMONIC NAME                       #byteNameInstruction
            | SHORT_DOTNAME_MNEMONIC DOTNAME                #shortDotnameInstruction
            ;

// Lexer rules

NO_OPERAND_MNEMONIC : 'DUP'
                    | 'HALT'
                    | 'IADD'
                    | 'IAND'
                    | 'IOR'
                    | 'IRETURN'
                    | 'ISUB'
                    | 'NOP'
                    | 'POP'
                    | 'SWAP'
                    ;

BYTE_VALUE_MNEMONIC : 'BIPUSH';

BYTE_NAME_BYTE_VALUE_MNEMONIC : 'IINC';

SHORT_NAME_MNEMONIC : 'GOTO'
                    | 'IFEQ'
                    | 'IFLT'
                    | 'IF_ICMPEQ'
                    | 'LDC_W'
                    ;

BYTE_NAME_MNEMONIC : 'ILOAD' | 'ISTORE';

SHORT_DOTNAME_MNEMONIC : 'INVOKEVIRTUAL';

NAME : NAME_FIRST_CHAR NAME_CHAR*;
DOTNAME : '.' NAME;
VALUE : DEC_VALUE | HEX_VALUE;

COMMENT : '#' .*? '\r'? '\n' -> skip;
WHITESPACE : [ \t\r\n]+ -> skip;

fragment NAME_FIRST_CHAR : [a-zA-Z_];
fragment NAME_CHAR : [a-zA-Z0-9_];
fragment HEX_PREFIX : '0x';
fragment HEX_DIGIT : [0-9A-F];
fragment HEX_VALUE : HEX_PREFIX HEX_DIGIT+;
fragment DEC_FIRST : [1-9];
fragment DEC_DIGIT : [0-9];
fragment DEC_VALUE : DEC_FIRST DEC_DIGIT*;
