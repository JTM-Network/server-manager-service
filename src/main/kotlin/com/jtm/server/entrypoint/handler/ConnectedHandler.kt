package com.jtm.server.entrypoint.handler

import com.jtm.server.core.domain.model.client.ConnectEvent
import com.jtm.server.core.usecase.event.EventHandler
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

class ConnectedHandler: EventHandler<ConnectEvent>(ConnectEvent::class.java) {

    private val logger = LoggerFactory.getLogger(ConnectedHandler::class.java)

    override fun onEvent(session: WebSocketSession, value: ConnectEvent): Mono<WebSocketMessage> {
        logger.info("Client connected: ${value.id} | ${value.name}")
        return Mono.empty()
    }
}