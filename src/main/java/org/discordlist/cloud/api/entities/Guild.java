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

package org.discordlist.cloud.api.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.discordlist.cloud.api.core.JsonSerializable;

import java.io.IOException;
import java.util.Objects;

public class Guild extends JsonSerializable {

    private Long id;
    private String prefix;

    public Guild(Long id, String prefix) {
        this.id = id;
        this.prefix = prefix;
    }

    public Guild() {
    }

    public static Guild fromJson(String jsonData) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonData, Guild.class);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guild that = (Guild) o;
        return id.equals(that.id) &&
                prefix.equals(that.prefix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, prefix);
    }

    @Override
    public String toString() {
        return "Guild{" +
                "id=" + id +
                ", prefix='" + prefix +
                '}';
    }

}
