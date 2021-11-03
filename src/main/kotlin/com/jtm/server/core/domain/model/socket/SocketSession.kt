package com.jtm.server.core.domain.model.socket

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.jtm.server.core.domain.model.event.OutgoingEvent
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import java.util.*

data class SocketSession(val id: UUID, val accountId: UUID, val session: WebSocketSession) {

    private val mapper = ObjectMapper().registerModule(KotlinModule())

    private fun sendMessage(name: String, value: Any): Mono<WebSocketMessage> {
        val outgoingEvent = OutgoingEvent(name)
        val event = outgoingEvent.writeObject(value)
        return Mono.just(session.textMessage(mapper.writeValueAsString(event)))
    }

    fun sendEvent(name: String, value: Any): Mono<Void> {
        return session.send { sendMessage(name, value) }
    }
}