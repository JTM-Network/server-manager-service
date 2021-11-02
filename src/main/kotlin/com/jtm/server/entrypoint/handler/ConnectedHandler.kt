package com.jtm.server.entrypoint.handler

import com.jtm.server.core.domain.model.client.ConnectEvent
import com.jtm.server.core.domain.model.client.ConnectResponseEvent
import com.jtm.server.core.domain.model.socket.SocketSession
import com.jtm.server.core.usecase.event.EventHandler
import com.jtm.server.core.usecase.provider.TokenProvider
import com.jtm.server.core.usecase.repository.SessionRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import java.util.*

@Component
class ConnectedHandler @Autowired constructor(private val sessionRepository: SessionRepository, private val tokenProvider: TokenProvider): EventHandler<ConnectEvent>("connect", ConnectEvent::class.java) {

    private val logger = LoggerFactory.getLogger(ConnectedHandler::class.java)

    override fun onEvent(session: WebSocketSession, value: ConnectEvent): Mono<WebSocketMessage> {
        logger.info("Checking server id...")
        val serverId = value.serverId ?: UUID.randomUUID()
        logger.info("Server Id valid.")
        if (sessionRepository.exists(serverId)) {
            logger.info("Session already exists.")
            return Mono.empty()
        }

        val accountId = tokenProvider.getAccountId(value.token) ?: return Mono.empty()
        logger.info("Account Id found.")
        val socketSession = SocketSession(serverId, accountId, session)
        sessionRepository.addSession(serverId, socketSession)
        logger.info("Client connected: ${session.id}")
        return sendMessage("connect_response", session, ConnectResponseEvent(serverId, session.id))
    }
}