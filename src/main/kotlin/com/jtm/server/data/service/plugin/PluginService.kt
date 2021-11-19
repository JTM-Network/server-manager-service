package com.jtm.server.data.service.plugin

import com.jtm.server.core.domain.entity.ServerPlugins
import com.jtm.server.core.domain.exceptions.ServerNotFound
import com.jtm.server.core.usecase.repository.PluginRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Service
class PluginService @Autowired constructor(private val pluginRepository: PluginRepository) {

    fun addPlugins(plugins: ServerPlugins): Mono<ServerPlugins> {
        return pluginRepository.save(plugins)
    }

    fun getPlugins(id: UUID): Mono<ServerPlugins> {
        return pluginRepository.findById(id)
                .switchIfEmpty(Mono.defer { Mono.error(ServerNotFound()) })
    }

    fun getAllPlugins(): Flux<ServerPlugins> {
        return pluginRepository.findAll()
    }

    fun deletePlugins(id: UUID): Mono<ServerPlugins> {
        return pluginRepository.findById(id)
                .switchIfEmpty(Mono.defer { Mono.error(ServerNotFound()) })
                .flatMap { pluginRepository.delete(it).thenReturn(it) }
    }
}