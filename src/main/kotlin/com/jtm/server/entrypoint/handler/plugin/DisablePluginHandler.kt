package com.jtm.server.entrypoint.handler.plugin

import com.jtm.server.core.domain.model.event.impl.plugin.DisablePluginEvent
import com.jtm.server.core.usecase.event.EventHandler
import com.jtm.server.core.usecase.repository.PluginRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

@Component
class DisablePluginHandler @Autowired constructor(private val pluginRepository: PluginRepository): EventHandler<DisablePluginEvent>("disable_plugin", DisablePluginEvent::class.java) {
    override fun onEvent(session: WebSocketSession, value: DisablePluginEvent): Mono<WebSocketMessage> {
        return pluginRepository.findById(value.serverId)
                .flatMap { pluginRepository.save(it.disabled(value.name)).then(Mono.empty()) }
    }
}