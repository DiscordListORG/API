/*
 * API - The API component of the discordlist.org cloud
 *
 * Copyright (C) 2019  Leon Kappes & Yannick Seeger
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

package org.discordlist.cloud.api.core

import com.fasterxml.jackson.databind.ObjectMapper
import io.javalin.Context
import org.apache.logging.log4j.Logger

interface Endpoint {

    val route:String
    val methode:RequestMethod
    val logger:Logger
    val mapper: ObjectMapper
        get() = ObjectMapper()
    val instace:API
        get() = API.instance

    fun run(ctx:Context): Context
}

enum class RequestMethod {

    GET, POST;

}
