package com.jtm.server.entrypoint.socket

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class ServerSocketHandler: WebSocketHandler {

    private val logger = LoggerFactory.getLogger(ServerSocketHandler::class.java)

    override fun handle(session: WebSocketSession): Mono<Void> {
        val messages: Flux<WebSocketMessage> = session.receive()
            .map { session.textMessage("Echo: ${it.payloadAsText}") }

        return session.send(messages)
    }
}