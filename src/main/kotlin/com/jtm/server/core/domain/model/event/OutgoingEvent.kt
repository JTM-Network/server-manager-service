package com.jtm.server.core.domain.model.event

import com.fasterxml.jackson.databind.ObjectMapper

class OutgoingEvent(name: String, value: String): Event(name, value) {

    private val mapper = ObjectMapper()

    fun writeObject(valueObject: Any): OutgoingEvent {
        this.value = mapper.writeValueAsString(valueObject)
        return this
    }
}