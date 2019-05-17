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

program : constantBlock? methodBlock?;

constantBlock : '.constant' constantDeclaration* '.endconstant';

constantDeclaration : NAME VALUE;

methodBlock : mainDeclaration methodDeclaration*;

mainDeclaration : '.main' varBlock? labeledInstruction* '.endmethod';

methodDeclaration : DOTNAME '(' parBlock? ')' varBlock? instruction* '.endmethod';

parBlock : parDeclaration (',' parDeclaration)*;

parDeclaration : NAME;

varBlock : '.var' variableDeclaration* '.endvar';

variableDeclaration : NAME;

labeledInstruction : label? instruction;

label : NAME ':';

instruction : 'BIPUSH'          VALUE                   #bipushInstruction
            | 'DUP'                                     #dupInstruction
            | 'GOTO'            NAME                    #gotoInstruction
            | 'HALT'                                    #haltInstruction
            | 'IADD'                                    #iaddInstruction
            | 'IAND'                                    #iandInstruction
            | 'IFEQ'            NAME                    #ifeqInstruction
            | 'IFLT'            NAME                    #ifltInstruction
            | 'IF_ICMPEQ'       NAME                    #ificmpeqInstruction
            | 'IINC'            NAME        VALUE       #iincInstruction
            | 'ILOAD'           NAME                    #iloadInstruction
            | 'INVOKEVIRTUAL'   DOTNAME                 #invokeVirtualInstruction
            | 'IOR'                                     #iorInstruction
            | 'IRETURN'                                 #ireturnInstruction
            | 'ISTORE'          NAME                    #istoreInstruction
            | 'ISUB'                                    #isubInstruction
            | 'LDC_W'           NAME                    #ldcwInstruction
            | 'NOP'                                     #nopInstruction
            | 'POP'                                     #popInstruction
            | 'SWAP'                                    #swapInstruction
            | 'WIDE'                                    #wideInstruction
            ;

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
