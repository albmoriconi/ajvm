# AJVM ≈

[![Build Status](https://travis-ci.com/albmoriconi/ajvm.svg?token=YSE4xmZpZB4FSHR7YHVu&branch=master)](https://travis-ci.com/albmoriconi/ajvm)

AJVM is an assembler for the [amic-0](https://github.com/albmoriconi/amic-0) assembly language.

## Table of contents

* [Build](#build)
* [Usage](#usage)
* [Syntax and semantics](#syntax-and-semantics)
* [References](#references)

## Build

### Project

The project is built with [Gradle](https://gradle.org). The required version is
automatically downloaded by the wrapper, so simply clone the repository and run
the `build` task:

```sh
$ git clone https://github.com/albmoriconi/ajvm.git
$ ./gradlew build
```

The built program archives with executables and library dependencies can be
found in the `build/distributions` directory.

### Source code documentation

To build the Javadoc for the project, run the `javadoc` Gradle task. The
produced documentation can be found in the `build/docs/javadoc` directory.

## Usage

```
usage: ajvm [options] <input>
Assembler for AJVM assembly language

options:
 -f <format>   output format: binary (default) | text
 -h,--help     display this help and exit
 -o <file>     write output to <file>
 ```
 
## Syntax and semantics

The language is based on the IJVM ISA described in Tanenbaum (2013). This
section describes the changes made to its syntax and semantics.

### Structure of a program

An AJVM assembly program is composed of two parts: a *constant area* and a
*method area*, in this order.

The constant area is comprised between the `.constant` and `.endconstant`
keywords and contains constant declarations in the form `name value`, where
value can be a decimal or hexadecimal (with the `0x` prefix) integer literal:

```
.constant
c1 0x8
c2 25
.endconstant
```

The method area is composed of the `.main` method definition, that has to be
defined first and without parameters, and, optionally, any number of additional
methods.

Each method definition is comprised between `.name` (a method name always begins
with the `.` character), optionaly followed by the method parameter names
between parentheses, and `.endmethod`, and contains an optional *variable
block*, where the names of local variables are declared, and an *instruction
block*:

```
.main
.var
a
.endvar
LDC_W c1
LDC_W c2
INVOKEVIRTUAL .sum
ISTORE a
HALT
.endmethod

.sum(v1, v2)
ILOAD v1
ILOAD v2
IADD
IRETURN
.endmethod
```

Each instruction can be labeled with the `label: <instruction>` syntax; labels
are local to methods.

### Instructions

An `HALT` instruction has been added to the language; it causes the processor
execution to halt.

### Case

The language is case sensitive. Instructions are upper case, other keywords are
lower case.

### Comments

Comments go from a `#` character to the end of line.

## References

* Tanenbaum, A.S, and Austin, T. (2013). *Structured Computer Organization* (6th
  ed.). Pearson.
