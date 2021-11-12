package com.jtm.server.entrypoint.handler

import com.jtm.server.core.domain.entity.RuntimeStats
import com.jtm.server.core.domain.model.event.impl.RuntimeEntryEvent
import com.jtm.server.core.domain.model.event.impl.StatusResponseEvent
import com.jtm.server.core.usecase.event.EventHandler
import com.jtm.server.core.usecase.repository.RuntimeStatsRepository
import io.kubernetes.client.extended.event.legacy.EventAggregator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

@Component
class RuntimeStatEntryHandler @Autowired constructor(private val runtimeStatsRepository: RuntimeStatsRepository): EventHandler<RuntimeEntryEvent>("runtime_entry", RuntimeEntryEvent::class.java) {

    override fun onEvent(session: WebSocketSession, value: RuntimeEntryEvent): Mono<WebSocketMessage> {
        return runtimeStatsRepository.insert(RuntimeStats(value.serverId, value.runtime))
                .flatMap { sendMessage(session, StatusResponseEvent(true)) }
    }
}