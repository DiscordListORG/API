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

package org.discordlist.cloud.api.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import org.eclipse.jetty.http.HttpStatus

open class ResponseUtil {


    private val mapper:ObjectMapper = ObjectMapper()


    fun formatError(code: Int, stack: Any): String {
        val message = HttpStatus.getCode(code)
        val jsonObject:ObjectNode = mapper.createObjectNode()
        return jsonObject.putObject("data").put("message", "$message").put("error", jsonObject.putObject("error").put("code", code).put("stack", stack.toString())).asText()

    }

}