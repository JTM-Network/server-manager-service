package com.jtm.server.entrypoint.handler

import com.jtm.server.core.domain.model.client.ConnectEvent
import com.jtm.server.core.usecase.event.EventHandler
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

class ConnectedHandler: EventHandler<ConnectEvent>("connect", ConnectEvent::class.java) {

    private val logger = LoggerFactory.getLogger(ConnectedHandler::class.java)

    override fun onEvent(session: WebSocketSession, value: ConnectEvent): Mono<WebSocketMessage> {
        logger.info("Client connected.")
        logger.info("ID: ${session.id}")
        logger.info("IP: ${session.handshakeInfo.remoteAddress!!.address.hostAddress}")
        return Mono.empty()
    }
}