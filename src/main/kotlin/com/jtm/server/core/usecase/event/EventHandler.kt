package com.jtm.server.core.usecase.event

import com.fasterxml.jackson.databind.ObjectMapper
import com.jtm.server.core.domain.model.event.IncomingEvent
import com.jtm.server.core.domain.model.event.OutgoingEvent
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

abstract class EventHandler(private val clazz: Class<*>) {

    private val mapper = ObjectMapper()

    abstract fun onEvent(session: WebSocketSession, value: Any): Mono<Void>

    fun handleEvent(session: WebSocketSession, event: IncomingEvent): Mono<Void> {
        val value = getObject(event)
        return onEvent(session, value)
    }

    private fun getObject(event: IncomingEvent): Any {
         return event.getObject(clazz)
    }

    fun sendEvent(session: WebSocketSession, outgoingEvent: OutgoingEvent, value: Any): Mono<Void> {
        val event = outgoingEvent.writeObject(value)
        return session.send { session.textMessage(mapper.writeValueAsString(event)) }
    }
}