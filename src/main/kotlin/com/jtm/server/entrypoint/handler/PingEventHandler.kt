package com.jtm.server.entrypoint.handler

import com.jtm.server.ServerManagerService
import com.jtm.server.core.domain.model.event.OutgoingEvent
import com.jtm.server.core.domain.model.ping.PingPong
import com.jtm.server.core.usecase.event.EventHandler
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

class PingEventHandler: EventHandler<PingPong>(PingPong::class.java) {

    private val logger = LoggerFactory.getLogger(ServerManagerService::class.java)

    override fun onEvent(session: WebSocketSession, value: PingPong): Mono<Void> {
        logger.info("Message received: ${value.value}")

        return sendEvent(session, OutgoingEvent("pong"), PingPong("pong"))
    }
}