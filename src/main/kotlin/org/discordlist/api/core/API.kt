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

package org.discordlist.api.core

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import netscape.javascript.JSObject
import org.apache.logging.log4j.LogManager
import org.discordlist.api.io.ConfigLoader
import org.json.JSONObject
import org.simpleyaml.configuration.file.YamlFile

class API : IAPI {

    private val log = LogManager.getLogger(API::class.java)
    override val config: YamlFile = ConfigLoader("api.yml").load()
    override val javalin: Javalin

    init {
        javalin = Javalin.create().apply {
            port(config.getInt("api.port"))
            requestLogger { ctx, executionTimeMs ->
                log.debug("[REQUEST] ${ctx.method()} ${ctx.path()} took $executionTimeMs ms")
            }
        }.start()

        javalin.routes {
            get("/") {ctx ->
                ctx.result(JSONObject().put("data", JSONObject().put("message","Is this thing on?")).toString()).header("Content-Type","application/json")
            }
        }
    }

}