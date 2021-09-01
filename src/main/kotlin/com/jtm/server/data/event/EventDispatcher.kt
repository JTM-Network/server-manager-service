package com.jtm.server.data.event

import com.jtm.server.core.domain.model.event.IncomingEvent
import com.jtm.server.data.security.AuthenticationManager
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.CloseStatus
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

@Component
class EventDispatcher @Autowired constructor(private val eventAggregator: EventAggregator, private val authenticationManager: AuthenticationManager) {

    private val logger = LoggerFactory.getLogger(EventDispatcher::class.java)

    fun dispatch(session: WebSocketSession, event: IncomingEvent): Mono<WebSocketMessage> {
        logger.info("Dispatching to correct event.")
        if (!authenticationManager.authenticate(event.token)) return session.close(CloseStatus.create(1015, "Failed authentication.")).then(Mono.empty())
        val handler = eventAggregator.getHandler(event.name) ?: return session.close(CloseStatus.create(1020, "Failed to find event.")).then(Mono.empty())
        return handler.handleEvent(session, event)
    }
}