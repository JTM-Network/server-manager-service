package com.jtm.server.data.event

import com.jtm.server.core.usecase.event.EventHandler
import com.jtm.server.entrypoint.handler.ConnectedHandler
import com.jtm.server.entrypoint.handler.DisconnectHandler
import com.jtm.server.entrypoint.handler.PingHandler
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class EventAggregator {

    private val handlers: MutableMap<String, EventHandler<*>> = HashMap()

    @PostConstruct
    fun init() {
        registerHandler(ConnectedHandler())
        registerHandler(DisconnectHandler())
    }

    fun registerHandler(handler: EventHandler<*>) {
        this.handlers[handler.name] = handler
    }

    fun getHandler(name: String): EventHandler<*>? {
        return this.handlers[name]
    }
}