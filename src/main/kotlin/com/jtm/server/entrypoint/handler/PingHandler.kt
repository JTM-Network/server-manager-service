package com.jtm.server.entrypoint.handler

import com.jtm.server.core.domain.model.event.OutgoingEvent
import com.jtm.server.core.domain.model.ping.PingPong
import com.jtm.server.core.usecase.event.EventHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

class PingHandler: EventHandler<PingPong>("ping", PingPong::class.java) {

    override fun onEvent(session: WebSocketSession, value: PingPong): Mono<WebSocketMessage> {
        return sendMessage(session, PingPong("pong"))
    }
}