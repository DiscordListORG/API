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

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import org.discordlist.cloud.api.core.JsonSerializable
import org.eclipse.jetty.http.HttpStatus
import java.util.*

/**
 *  General utility for formatting Json Responses
 */
open class ResponseUtil {


    private val mapper: ObjectMapper = ObjectMapper()

    /**
     * Format an Error with stacktrace and status code
     *
     * @param code Http-Status Code of response
     * @param stack Stacktrace of Error. Will be converted to String.
     * @return returns an [com.fasterxml.jackson.databind.node.ObjectNode] with given data
     */
    fun formatError(code: Int, stack: Any): Any {
        val message = HttpStatus.getCode(code)
        val jsonObject: ObjectNode = mapper.createObjectNode()
        return jsonObject.put("message", "$message").putObject("data").set("error", mapper.createObjectNode().putObject("error").put("code", code).put("stack", stack.toString()))

    }

    /**
     * Format an Response with data and message
     *
     * @param message Response message
     * @param data Data object of the response. Contains object
     * @return returns an [com.fasterxml.jackson.databind.node.ObjectNode] with given data
     */
    fun formatResult(message: String, data: JsonNode): Any {
        return mapper.createObjectNode().put("message", message).put("timestamp", Date().time).set("data",data)
    }

    /**
     * Format an Response with data, message and status code
     *
     * @param message Response message
     * @param data Data object of the response. Contains object
     * @param code Http-Status Code of response
     * @return returns an [com.fasterxml.jackson.databind.node.ObjectNode] with given data
     */
    fun formatResult(message: String, data: ObjectNode, code: Int): Any {
        return mapper.createObjectNode().put("message", message).put("timestamp", Date().time).put("code", code).set("data", data)
    }

    /**
     * Format an Response with data, message and status code
     *
     * @param message Response message
     * @param data API Entity
     * @param code Http-Status Code of response
     * @return returns an [com.fasterxml.jackson.databind.node.ObjectNode] with given data
     */
    fun formatResult(message: String, data: JsonSerializable, code: Int): Any {
        return mapper.createObjectNode().put("message", message).put("timestamp", Date().time).put("code", code).putPOJO("data", data)
    }

}