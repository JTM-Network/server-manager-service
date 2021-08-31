package com.jtm.server.entrypoint.socket

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

class ServerSocketHandler: WebSocketHandler {

    private val logger = LoggerFactory.getLogger(ServerSocketHandler::class.java)

    override fun handle(session: WebSocketSession): Mono<Void> {
        return session.send(session.receive()
            .map {
                logger.info("Message: ${it.payloadAsText}")
                session.textMessage("Echo: ${it.payloadAsText}")
            })
    }
}