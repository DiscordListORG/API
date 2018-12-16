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

import com.datastax.driver.core.DataType
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import org.apache.logging.log4j.LogManager
import org.discordlist.api.io.Cassandra
import org.discordlist.api.io.ConfigLoader
import org.discordlist.api.util.ResponseUtil
import org.json.JSONObject
import org.simpleyaml.configuration.file.YamlFile
import redis.clients.jedis.Jedis

class API : IAPI, ResponseUtil() {

    private val log = LogManager.getLogger(API::class.java)
    override val config: YamlFile = ConfigLoader("api.yml").load()
    override val javalin: Javalin
    override val cassandra: Cassandra
    override val jedis: Jedis

    init {
        javalin = Javalin.create().apply {
            port(config.getInt("api.port"))
            requestLogger { ctx, executionTimeMs ->
                log.debug("[REQUEST] ${ctx.method()} ${ctx.path()} took $executionTimeMs ms")
            }
        }.start()

        cassandra = Cassandra(config)
        cassandra.connect()

        jedis = Jedis(config.getString("redis.host"))
        jedis.connect()
        jedis.auth(config.getString("redis.password"))
        jedis.flushAll()

        javalin.routes {
            get("/") { ctx ->
                ctx.result(JSONObject().put("data", JSONObject().put("message", "Is this thing on?")).toString())
                    .header("Content-Type", "application/json")
            }
            get("/guild/:id") { ctx ->
                //TODO: Uncomment when finished
                /*if (ctx.header("Authorization") != config.getString("api.token"))
                    ctx.result(formatError(401, "Unauthorized"))
                        .status(401)
                        .header("Content-Type", "application/json")*/
                if (jedis.exists(ctx.pathParam("id"))) {
                    ctx.result(formatResult("SUCCESS", jedis.get(ctx.pathParam("id")))).header(
                        "Content-Type",
                        "application/json"
                    ).status(200)
                } else {
                    val select =
                        cassandra.session.execute("SELECT * FROM guilds WHERE id=?", ctx.pathParam("id").toLong()).one()
                    if (select == null) {
                        val created = cassandra.session.execute(
                            "INSERT INTO guilds (id, prefix) VALUES (" +
                                    "?," +
                                    "?" +
                                    ")", ctx.pathParam("id").toLong(), config.getString("discord.prefix")
                        )
                        log.info(created)
                        //ctx.result(formatResult("CREATED", created.one(), 201))
                    }else {
                        val data = JSONObject()
                        select.columnDefinitions.forEach { it ->
                            when(it.type) {
                                DataType.bigint() -> data.put(it.name, select.getLong(it.name))
                                DataType.varchar() -> data.put(it.name, select.getString(it.name))
                            }
                        }
                        ctx.result(formatResult("SUCCESS", data)).status(200).header("Content-Type","application/json")
                    }

                }
            }
        }
    }

}