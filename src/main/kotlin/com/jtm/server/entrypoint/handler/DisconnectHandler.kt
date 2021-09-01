package com.jtm.server.entrypoint.handler

import com.jtm.server.core.domain.model.client.DisconnectEvent
import com.jtm.server.core.usecase.event.EventHandler
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

class DisconnectHandler: EventHandler<DisconnectEvent>(DisconnectEvent::class.java) {

    private val logger = LoggerFactory.getLogger(DisconnectHandler::class.java)

    override fun onEvent(session: WebSocketSession, value: DisconnectEvent): Mono<WebSocketMessage> {
        logger.info("Client disconnected: ${value.id} | ${value.name}")
        return Mono.empty()
    }
}