package com.jtm.server.entrypoint.handler

import com.jtm.server.core.domain.model.server.ServerLog
import com.jtm.server.core.usecase.event.EventHandler
import com.jtm.server.core.usecase.repository.LogRepository
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

class ServerLogHandler(private val logRepository: LogRepository): EventHandler<ServerLog>("server_log", ServerLog::class.java) {

    private val logger = LoggerFactory.getLogger(ServerLogHandler::class.java)

    override fun onEvent(session: WebSocketSession, value: ServerLog): Mono<WebSocketMessage> {
        val id = session.id
        val logs = value.logs
        if (!logRepository.exists(id)) {
            logRepository.addLog(id)
            logger.info("Added new console logs.")
        }
        for (i in 0 until logs.size) logRepository.sendLogMessage(id, logs[i])
        return Mono.empty()
    }
}