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

package org.discordlist.cloud.api.io


import com.datastax.driver.core.Cluster
import com.datastax.driver.core.ConsistencyLevel
import com.datastax.driver.core.QueryOptions
import com.datastax.driver.core.Session
import org.simpleyaml.configuration.file.YamlFile

class Cassandra(config: YamlFile) {

    val cluster: Cluster
    lateinit var session: Session
    private val keyspace: String = config.getString("cassandra.keyspace")

    init {
        cluster = Cluster.builder().apply {
            addContactPoints(*config.getStringList("cassandra.contact_points").toTypedArray())
            withCredentials(config.getString("cassandra.username"), config.getString("cassandra.password"))
            withoutJMXReporting()
            withQueryOptions(QueryOptions().apply {
                fetchSize = Integer.MAX_VALUE
                consistencyLevel = ConsistencyLevel.ALL
            })
        }.build()
    }

    fun connect() {
        session = cluster.connect(keyspace)
        createDefaults()
    }

    fun createDefaults() {
        session.execute("CREATE TABLE IF NOT EXISTS guilds (" +
                "id bigint," +
                "prefix varchar," +
                "PRIMARY KEY (id)" +
                ")")
    }

}

