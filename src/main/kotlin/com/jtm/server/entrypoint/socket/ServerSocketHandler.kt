package com.jtm.server.entrypoint.socket

import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

@Component
class ServerSocketHandler: WebSocketHandler {

    override fun handle(session: WebSocketSession): Mono<Void> {
        return session.send(session.receive()
            .map { session.textMessage("Echo: ${it.payloadAsText}") })
    }
}