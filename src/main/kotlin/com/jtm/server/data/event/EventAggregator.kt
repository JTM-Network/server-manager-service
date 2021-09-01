package com.jtm.server.data.event

import com.jtm.server.core.usecase.event.EventHandler
import com.jtm.server.entrypoint.handler.PingEventHandler
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class EventAggregator {

    private val handlers: MutableMap<String, EventHandler<*>> = HashMap()

    @PostConstruct
    fun init() {
        registerHandler("ping", PingEventHandler())
    }

    fun registerHandler(name: String, handler: EventHandler<*>) {
        this.handlers[name] = handler
    }

    fun getHandler(name: String): EventHandler<*>? {
        return this.handlers[name]
    }
}