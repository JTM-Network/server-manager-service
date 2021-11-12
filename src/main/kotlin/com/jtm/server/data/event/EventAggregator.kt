package com.jtm.server.data.event

import com.jtm.server.core.usecase.event.EventHandler
import com.jtm.server.entrypoint.handler.ConnectedHandler
import com.jtm.server.entrypoint.handler.RuntimeStatEntryHandler
import com.jtm.server.entrypoint.handler.ServerLogHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class EventAggregator @Autowired constructor(private val context: ApplicationContext) {

    private val handlers: MutableMap<String, EventHandler<*>> = HashMap()

    @PostConstruct
    fun init() {
        registerHandler(context.getBean(ConnectedHandler::class.java))
        registerHandler(context.getBean(ServerLogHandler::class.java))
        registerHandler(context.getBean(RuntimeStatEntryHandler::class.java))
    }

    fun registerHandler(handler: EventHandler<*>) {
        this.handlers[handler.name] = handler
    }

    fun getHandler(name: String): EventHandler<*>? {
        return this.handlers[name]
    }
}