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

package org.discordlist.cloud.api.net.http.endpointloader

import org.discordlist.cloud.api.net.http.get.Index
import org.discordlist.cloud.api.net.http.get.Guild

class Loader {

    val manager:Manager = Manager()

    init {
        manager.registerEndpoints(
                Index(),
                Guild(),
                org.discordlist.cloud.api.net.http.post.Guild()
        )
    }

}