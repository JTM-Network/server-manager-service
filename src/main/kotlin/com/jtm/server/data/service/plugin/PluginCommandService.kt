package com.jtm.server.data.service.plugin

import com.jtm.server.core.domain.dto.PluginCommandDto
import com.jtm.server.core.domain.exceptions.ServerOffline
import com.jtm.server.core.domain.model.event.impl.plugin.DisablePluginEvent
import com.jtm.server.core.domain.model.event.impl.plugin.EnablePluginEvent
import com.jtm.server.data.service.SessionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*

@Service
class PluginCommandService @Autowired constructor(private val sessionService: SessionService) {

    fun enableCmd(dto: PluginCommandDto): Mono<Void> {
        return sessionService.getSession(UUID.fromString(dto.serverId))
                .switchIfEmpty(Mono.defer { Mono.error(ServerOffline()) })
                .flatMap { it.sendEvent("enable_plugin", EnablePluginEvent(UUID.fromString(dto.serverId), dto.name)) }
                .then()
    }

    fun disableCmd(dto: PluginCommandDto): Mono<Void> {
        return sessionService.getSession(UUID.fromString(dto.serverId))
                .switchIfEmpty(Mono.defer { Mono.error(ServerOffline()) })
                .flatMap { it.sendEvent("disable_plugin", DisablePluginEvent(UUID.fromString(dto.serverId), dto.name)) }
                .then()
    }
}