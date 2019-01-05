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

package org.discordlist.cloud.api.net.http.post

import com.datastax.driver.core.DataType
import io.javalin.Context
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.discordlist.cloud.api.core.Endpoint
import org.discordlist.cloud.api.core.RequestMethod
import org.discordlist.cloud.api.util.AuthUtil

class Guild : Endpoint, AuthUtil() {
    override val logger: Logger = LogManager.getLogger(this.javaClass)
    override val route: String = "guild/:id"
    override val methode: RequestMethod = RequestMethod.POST

    override fun run(ctx: Context): Context {
        if (ctx.pathParam("id").length != 18) {
            return ctx.json(instace.formatError(400, "GUILD ID IS NO VALID SNOWFLAKE")).status(400)
                    .header("Content-Type", "application/json")
        } else if (instace.jedis.exists(ctx.pathParam("id"))) {
            return ctx.json(instace.formatResult("SUCCESS", mapper.readTree(instace.jedis.get(ctx.pathParam("id")))))
                    .header(
                            "Content-Type",
                            "application/json"
                    ).status(200)
        } else {
            val select =
                    instace.cassandra.session.execute("SELECT * FROM guilds WHERE id=?", ctx.pathParam("id").toLong()).one()
            if (select == null) {
                instace.cassandra.session.execute(
                        "INSERT INTO guilds (id, prefix) VALUES (" +
                                "?," +
                                "?" +
                                ")", ctx.pathParam("id").toLong(), instace.config.getString("discord.prefix")
                )
                val created =
                        instace.cassandra.session.execute("SELECT * FROM guilds WHERE id=?", ctx.pathParam("id").toLong())
                                .one()
                val data = mapper.createObjectNode()
                created.columnDefinitions.forEach { it ->
                    when (it.type) {
                        DataType.bigint() -> data.put(it.name, created.getLong(it.name).toString())
                        DataType.varchar() -> data.put(it.name, created.getString(it.name))
                    }
                }
                logger.info("220 " + data.toString())
                instace.jedis.set(ctx.pathParam("id"), data.asText())
                return ctx.json(instace.formatResult("CREATED", data, 201)).status(201)
                        .header("Content-Type", "application/json")
            } else {
                val data = mapper.createObjectNode()
                select.columnDefinitions.forEach { it ->
                    when (it.type) {
                        DataType.bigint() -> data.put(it.name, select.getLong(it.name).toString())
                        DataType.varchar() -> data.put(it.name, select.getString(it.name))
                    }
                }
                instace.jedis.set(ctx.pathParam("id"), data.toString())
                return ctx.json(instace.formatResult("SUCCESS", data)).status(200).header("Content-Type", "application/json")
            }

        }
    }
}