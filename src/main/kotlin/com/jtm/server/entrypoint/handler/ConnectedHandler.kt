package com.jtm.server.entrypoint.handler

import com.jtm.server.core.domain.entity.ServerInfo
import com.jtm.server.core.domain.model.client.ConnectEvent
import com.jtm.server.core.domain.model.client.ConnectResponseEvent
import com.jtm.server.core.domain.model.socket.SocketSession
import com.jtm.server.core.usecase.event.EventHandler
import com.jtm.server.core.usecase.provider.TokenProvider
import com.jtm.server.core.usecase.repository.SessionRepository
import com.jtm.server.data.service.ServerInfoService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import java.util.*

@Component
class ConnectedHandler @Autowired constructor(private val sessionRepository: SessionRepository, private val infoService: ServerInfoService, private val tokenProvider: TokenProvider): EventHandler<ConnectEvent>("connect", ConnectEvent::class.java) {

    private val logger = LoggerFactory.getLogger(ConnectedHandler::class.java)

    override fun onEvent(session: WebSocketSession, value: ConnectEvent): Mono<WebSocketMessage> {
        val serverId = value.serverId ?: UUID.randomUUID()
        if (sessionRepository.exists(serverId)) {
            logger.debug("Session already exists.")
            return Mono.empty()
        }

        val accountId = tokenProvider.getAccountId(value.token) ?: return Mono.empty()
        logger.info("Account ID: $accountId")
        val socketSession = SocketSession(serverId, accountId, session)
        sessionRepository.addSession(serverId, socketSession)
        logger.info("Client connected: $serverId")
        return infoService.connected(serverId)
                .switchIfEmpty(Mono.defer { infoService.createInfo(ServerInfo(serverId, accountId, value.info.ip)) })
                .flatMap { sendMessage("connect_response", session, ConnectResponseEvent(serverId, session.id)) }
//        return sendMessage("connect_response", session, ConnectResponseEvent(serverId, session.id))
    }
}