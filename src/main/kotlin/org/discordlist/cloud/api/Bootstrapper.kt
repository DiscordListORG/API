/*
 * API - The API component of the discordlist.org cloud
 *
 * Copyright (C) 2018  Leon Kappes & Yannick Seeger
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see https://www.gnu.org/licenses/.
 */

package org.discordlist.cloud.api

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.core.config.ConfigurationSource
import org.apache.logging.log4j.core.config.Configurator
import org.discordlist.cloud.api.core.API

fun main(args: Array<String>) {
    Configurator.setRootLevel(if (args.isEmpty()) Level.INFO else Level.toLevel(args[0], Level.INFO))
    Configurator.initialize(
        ClassLoader.getSystemClassLoader(),
        ConfigurationSource(ClassLoader.getSystemResourceAsStream("log4j2.xml"))
    )

    API()
}