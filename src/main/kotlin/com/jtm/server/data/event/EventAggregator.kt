package com.jtm.server.data.event

import com.jtm.server.core.usecase.event.EventHandler
import org.springframework.stereotype.Component

@Component
class EventAggregator {

    private val handlers: MutableMap<String, EventHandler> = HashMap()

    fun registerHandler(name: String, handler: EventHandler) {
        this.handlers[name] = handler
    }

    fun getHandler(name: String): EventHandler? {
        return this.handlers[name]
    }
}