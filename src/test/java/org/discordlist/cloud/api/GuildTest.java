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

package org.discordlist.cloud.api;

import org.discordlist.cloud.api.entities.Guild;
import org.junit.Test;

import java.io.IOException;

public class GuildTest {

    @Test
    public void testGuildConverting() throws IOException {

        Guild guild = new Guild(507517900127469568L, "gl!");

        // Serialization
        String jsonData = guild.toJson();
        System.out.println(jsonData);

        // Deserialization
        System.out.println(Guild.fromJson(jsonData));
    }

}
