package com.jtm.server.core.usecase.event

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.jtm.server.core.domain.model.event.IncomingEvent
import com.jtm.server.core.domain.model.event.OutgoingEvent
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

abstract class EventHandler<T>(private val clazz: Class<T>) {

    private val logger = LoggerFactory.getLogger(EventHandler::class.java)
    private val mapper = ObjectMapper().registerModule(KotlinModule())

    abstract fun onEvent(session: WebSocketSession, value: T): Mono<WebSocketMessage>

    fun handleEvent(session: WebSocketSession, event: IncomingEvent): Mono<WebSocketMessage> {
        logger.info("Handling event: ${event.name}")
        val value = getObject(event)
        return onEvent(session, value)
    }

    private fun getObject(event: IncomingEvent): T {
        return mapper.readValue(event.value, clazz)
    }

    fun sendMessage(session: WebSocketSession, outgoingEvent: OutgoingEvent, value: Any): Mono<WebSocketMessage> {
        val event = outgoingEvent.writeObject(value)
        return Mono.just(session.textMessage(mapper.writeValueAsString(event)))
    }

    fun sendEvent(session: WebSocketSession, outgoingEvent: OutgoingEvent, value: Any): Mono<Void> {
        return session.send { sendMessage(session, outgoingEvent, value) }
    }
}