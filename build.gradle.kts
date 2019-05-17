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

plugins {
    // Apply the java plugin to add support for Java
    java

    // Apply the application plugin to add support for building an application
    application

    // Support for generating parsers using ANTLR.
    antlr

    // Apply the groovy plugin to also add support for Groovy (needed for Spock)
    groovy
}

repositories {
    // Use jcenter for resolving your dependencies.
    // You can declare any Maven/Ivy/file repository here.
    jcenter()
}

dependencies {
    antlr("org.antlr:antlr4:4.7.2")
    implementation("commons-cli:commons-cli:1.4")

    // Use the latest Groovy version for Spock testing
    testImplementation("org.codehaus.groovy:groovy-all:2.5.6")

    // Use the awesome Spock testing and specification framework even with Java
    testImplementation("org.spockframework:spock-core:1.2-groovy-2.5")
    testImplementation("junit:junit:4.12")
}

application {
    // Define the main class for the application
    mainClassName = "me.albmoriconi.ajvm.App"
}

tasks.generateGrammarSource {
    arguments = arguments + listOf("-package", "me.albmoriconi.ajvm.antlr")
}

tasks.startScripts {
    // Fix a bug in darwin script when launching a link from home dir
    doLast {
        unixScript.writeText(unixScript.readText().
                replaceFirst("cd \"\$(dirname \"\$0\")\"", "cd \"\$SAVED\""))
    }
}
