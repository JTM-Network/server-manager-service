package com.jtm.server.core.domain.model.event

import com.fasterxml.jackson.databind.ObjectMapper

class IncomingEvent(name: String, val token: String, value: String): Event(name, value) {

    private val mapper = ObjectMapper()

    fun getObject(clazz: Class<*>): Any {
        return mapper.readValue(value, clazz)
    }
}