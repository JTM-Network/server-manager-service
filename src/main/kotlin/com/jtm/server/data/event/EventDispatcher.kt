package com.jtm.server.data.event

import com.jtm.server.core.domain.model.event.IncomingEvent
import com.jtm.server.data.security.AuthenticationManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.CloseStatus
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

@Component
class EventDispatcher @Autowired constructor(private val eventAggregator: EventAggregator, private val authenticationManager: AuthenticationManager) {

    fun dispatch(session: WebSocketSession, event: IncomingEvent): Mono<Void> {
        if (!authenticationManager.authenticate(event.token)) return session.close(CloseStatus.create(1015, "Failed authentication."))
        val handler = eventAggregator.getHandler(event.name) ?: return session.close(CloseStatus.create(1020, "Failed to find event."))
        return handler.handleEvent(session, event)
    }
}