package com.jtm.server.entrypoint.handler

import com.jtm.server.core.domain.entity.ServerPlugins
import com.jtm.server.core.domain.model.event.impl.fetch.FetchPluginsEvent
import com.jtm.server.core.usecase.event.EventHandler
import com.jtm.server.data.service.plugin.PluginService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

@Component
class FetchPluginsHandler @Autowired constructor(private val pluginService: PluginService): EventHandler<FetchPluginsEvent>("fetch_plugins", FetchPluginsEvent::class.java) {

    override fun onEvent(session: WebSocketSession, value: FetchPluginsEvent): Mono<WebSocketMessage> {
        return pluginService.addPlugins(ServerPlugins(value.serverId, value.plugins))
                .then(Mono.empty())
    }
}