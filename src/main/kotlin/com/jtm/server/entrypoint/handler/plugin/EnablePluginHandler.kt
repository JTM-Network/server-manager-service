package com.jtm.server.entrypoint.handler.plugin

import com.jtm.server.core.domain.model.event.impl.plugin.EnablePluginEvent
import com.jtm.server.core.usecase.event.EventHandler
import com.jtm.server.core.usecase.repository.PluginRepository
import com.jtm.server.core.usecase.sink.PluginSink
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

@Component
class EnablePluginHandler @Autowired constructor(private val pluginRepository: PluginRepository, private val pluginSink: PluginSink): EventHandler<EnablePluginEvent>("enable_plugin", EnablePluginEvent::class.java) {
    override fun onEvent(session: WebSocketSession, value: EnablePluginEvent): Mono<WebSocketMessage> {
        return pluginRepository.findById(value.serverId)
                .flatMap { pluginRepository.save(it.enabled(value.name))
                        .flatMap {
                            pluginSink.sendMessage(it)
                            Mono.empty()
                        }
                }
    }
}