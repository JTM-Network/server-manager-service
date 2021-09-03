package com.jtm.server.entrypoint.handler

import com.jtm.server.core.domain.model.server.ServerLog
import com.jtm.server.core.usecase.event.EventHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

class ServerLogHandler: EventHandler<ServerLog>("server_log", ServerLog::class.java) {

    override fun onEvent(session: WebSocketSession, value: ServerLog): Mono<WebSocketMessage> {
        TODO("Not yet implemented")
    }
}