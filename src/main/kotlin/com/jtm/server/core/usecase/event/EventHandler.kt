package com.jtm.server.core.usecase.event

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.jtm.server.core.domain.model.event.IncomingEvent
import com.jtm.server.core.domain.model.event.OutgoingEvent
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

abstract class EventHandler<T>(val name: String, private val clazz: Class<T>) {

    private val mapper = ObjectMapper().registerModule(KotlinModule())
    private val logger = LoggerFactory.getLogger(EventHandler::class.java)

    abstract fun onEvent(session: WebSocketSession, value: T): Mono<WebSocketMessage>

    fun handleEvent(session: WebSocketSession, incomingEvent: IncomingEvent): Mono<WebSocketMessage> {
        logger.info("Handling event...")
        val event = incomingEvent.value as T
        return onEvent(session, event)
    }

    fun sendMessage(session: WebSocketSession, value: Any): Mono<WebSocketMessage> {
        val outgoingEvent = OutgoingEvent(name, value)
        return Mono.just(session.textMessage(mapper.writeValueAsString(outgoingEvent)))
    }

    fun sendEvent(session: WebSocketSession, value: Any): Mono<Void> {
        return session.send { sendMessage(session, value) }
    }
}