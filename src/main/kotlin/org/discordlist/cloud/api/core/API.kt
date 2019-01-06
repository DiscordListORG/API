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

package org.discordlist.cloud.api.core

import com.fasterxml.jackson.databind.ObjectMapper
import io.javalin.Javalin
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.discordlist.cloud.api.io.Cassandra
import org.discordlist.cloud.api.io.ConfigLoader
import org.discordlist.cloud.api.net.http.endpointloader.Loader
import org.discordlist.cloud.api.util.ResponseUtil
import org.simpleyaml.configuration.file.YamlFile
import redis.clients.jedis.Jedis

class API(args: Array<String>) : IAPI, ResponseUtil() {

    private val log = LogManager.getLogger(API::class.java)
    override val config: YamlFile = ConfigLoader("api.yml").load()
    override val javalin: Javalin
    override val cassandra: Cassandra
    override val jedis: Jedis
    override val mode: Boolean = args.contains("dev-mode")

    companion object {
        @JvmStatic
        lateinit var instance: API
    }

    init {
        instance = this
        javalin = Javalin.create().apply {
            port(config.getInt("api.port"))
            requestLogger { ctx, executionTimeMs ->
                log.log(Level.forName("REQUEST", 350), "${ctx.method()} ${ctx.host() + ctx.path()} took ${executionTimeMs*1000} μs")
            }
        }.start()

        cassandra = Cassandra(config)
        cassandra.connect()

        jedis = Jedis(config.getString("redis.host"))
        jedis.connect()
        jedis.auth(config.getString("redis.password"))
        jedis.flushAll()

        Loader()

    }
}