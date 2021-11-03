package com.jtm.server.entrypoint.socket

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.jtm.server.core.domain.model.event.IncomingEvent
import com.jtm.server.core.usecase.repository.SessionRepository
import com.jtm.server.data.event.EventDispatcher
import com.jtm.server.data.service.ServerService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

@Component
class ServerSocketHandler @Autowired constructor(private val eventDispatcher: EventDispatcher, private val sessionRepository: SessionRepository, private val serverService: ServerService): WebSocketHandler {

    private val logger = LoggerFactory.getLogger(ServerSocketHandler::class.java)
    private val mapper = ObjectMapper().registerModule(KotlinModule())

    override fun handle(session: WebSocketSession): Mono<Void> {
        return session.send(session.receive()
                .flatMap { eventDispatcher.dispatch(session, mapper.readValue(it.payloadAsText, IncomingEvent::class.java)) }

                .doFinally {
                    session.close()
                    sessionRepository.removeSessionId(session.id)
                    logger.info("Client disconnected: ${session.id}")
                }
        )
    }
}