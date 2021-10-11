package com.jtm.server.data.event

import com.jtm.server.core.usecase.event.EventHandler
import com.jtm.server.entrypoint.handler.ConnectedHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class EventAggregator @Autowired constructor(private val connectedHandler: ConnectedHandler) {

    private val handlers: MutableMap<String, EventHandler<*>> = HashMap()

    @PostConstruct
    fun init() {
        registerHandler(connectedHandler)
    }

    fun registerHandler(handler: EventHandler<*>) {
        this.handlers[handler.name] = handler
    }

    fun getHandler(name: String): EventHandler<*>? {
        return this.handlers[name]
    }
}