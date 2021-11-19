package com.jtm.server.entrypoint.controller.plugin

import com.jtm.server.core.domain.entity.ServerPlugins
import com.jtm.server.data.service.plugin.PluginService
import org.springframework.beans.factory.annotation.Autowired
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

    @DeleteMapping("/{id}")
    fun deletePlugins(@PathVariable id: UUID): Mono<ServerPlugins> {
        return pluginService.deletePlugins(id)
    }
}