package com.jtm.server.entrypoint.handler

import com.jtm.server.core.domain.entity.RuntimeStats
import com.jtm.server.core.domain.model.event.impl.RuntimeEntryEvent
import com.jtm.server.core.usecase.event.EventHandler
import com.jtm.server.core.usecase.sink.RuntimeSink
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

@Component
class RuntimeStatStreamHandler @Autowired constructor(private val runtimeSink: RuntimeSink): EventHandler<RuntimeEntryEvent>("runtime_stream", RuntimeEntryEvent::class.java) {

    override fun onEvent(session: WebSocketSession, value: RuntimeEntryEvent): Mono<WebSocketMessage> {
        val stat = RuntimeStats(value.serverId, value.runtime)
        runtimeSink.sendMessage(stat)
        return Mono.empty()
    }
}