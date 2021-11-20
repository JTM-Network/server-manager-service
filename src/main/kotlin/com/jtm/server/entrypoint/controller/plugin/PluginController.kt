package com.jtm.server.entrypoint.controller.plugin

import com.jtm.server.core.domain.entity.ServerPlugins
import com.jtm.server.data.service.plugin.PluginService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping("/plugins")
class PluginController @Autowired constructor(private val pluginService: PluginService) {

    @GetMapping("/{id}")
    fun getPlugins(@PathVariable id: UUID): Mono<ServerPlugins> {
        return pluginService.getPlugins(id)
    }

    @GetMapping("/all")
    fun getAllPlugins(): Flux<ServerPlugins> {
        return pluginService.getAllPlugins()
    }

    @GetMapping("/{id}/stream", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getPluginsStream(@PathVariable id: UUID): Flux<ServerSentEvent<ServerPlugins>> {
        return pluginService.getPluginsStream(id)
    }

    @DeleteMapping("/{id}")
    fun deletePlugins(@PathVariable id: UUID): Mono<ServerPlugins> {
        return pluginService.deletePlugins(id)
    }
}