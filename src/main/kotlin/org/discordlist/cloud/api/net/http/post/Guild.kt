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

import io.javalin.Context
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.discordlist.cloud.api.core.Endpoint
import org.discordlist.cloud.api.core.RequestMethod
import org.discordlist.cloud.api.entities.Guild
import org.discordlist.cloud.api.util.AuthUtil

class Guild : Endpoint, AuthUtil() {
    override val route: String = "guild/:id"
    override val methode: RequestMethod = RequestMethod.POST
    override val logger: Logger = LogManager.getLogger(this.javaClass)

    override fun run(ctx: Context): Context {
        if (!checkDefaultAuth(ctx)) {
            return respondUnauthorized(ctx)
        }
        if (ctx.pathParam("id").length != 18) {
            return ctx.json(instace.formatError(400, "GUILD ID IS NO VALID SNOWFLAKE")).status(400)
                    .header("Content-Type", "application/json")
        }

        val guild:Guild
        if (instace.jedis.exists(ctx.pathParam("id")))
            guild = org.discordlist.cloud.api.entities.Guild.fromJson(instace.jedis.get(ctx.pathParam("id")))
        else {
            val select = instace.cassandra.session.execute("SELECT json * FROM guilds WHERE id=?", ctx.pathParam("id").toLong()).one()
            if (select != null) {
                guild = org.discordlist.cloud.api.entities.Guild.fromJson(select.getString(0))
                instace.jedis.set(ctx.pathParam("id"), guild.toJson())
            } else {
                instace.cassandra.session.execute(
                        "INSERT INTO guilds (id, prefix) VALUES (" +
                                "?," +
                                "?" +
                                ")", ctx.pathParam("id").toLong(), instace.config.getString("discord.prefix")
                )
                val created = instace.cassandra.session.execute("SELECT json * FROM guilds WHERE id=?", ctx.pathParam("id").toLong())
                        .one()
                guild = org.discordlist.cloud.api.entities.Guild.fromJson(created.getString(0))
                instace.jedis.set(ctx.pathParam("id"), guild.toJson())
            }
        }
        if(!ctx.formParams("prefix").isEmpty()){
            guild.prefix = ctx.formParams("prefix")[0]
        }

        instace.cassandra.session.execute("UPDATE guilds SET prefix=? WHERE id=?",guild.prefix,guild.id)
        instace.jedis.set(guild.id.toString(), guild.toJson())


        return ctx.json(instace.formatResult("SUCCESS", guild, 200))
    }
}