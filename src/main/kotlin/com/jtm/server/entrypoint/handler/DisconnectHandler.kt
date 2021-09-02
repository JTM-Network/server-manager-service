package com.jtm.server.entrypoint.handler

import com.jtm.server.core.domain.model.client.DisconnectEvent
import com.jtm.server.core.usecase.event.EventHandler
import com.jtm.server.core.usecase.repository.SessionRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

@Component
class DisconnectHandler @Autowired constructor(private val sessionRepository: SessionRepository): EventHandler<DisconnectEvent>("disconnect", DisconnectEvent::class.java) {

    private val logger = LoggerFactory.getLogger(DisconnectHandler::class.java)

    override fun onEvent(session: WebSocketSession, value: DisconnectEvent): Mono<WebSocketMessage> {
        if (!sessionRepository.exists(session.id)) {
            logger.info("Session not active.")
            return Mono.empty()
        }

        sessionRepository.removeSession(session.id)
        logger.info("Client disconnected: ${session.id}")
        return Mono.empty()
    }
}