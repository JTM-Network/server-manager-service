package com.jtm.server.data.service.plugin

import com.jtm.server.core.domain.entity.ServerPlugins
import com.jtm.server.core.domain.exceptions.ServerNotFound
import com.jtm.server.core.usecase.repository.PluginRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.springframework.test.context.junit4.SpringRunner
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.*

@RunWith(SpringRunner::class)
class PluginServiceTest {

    private val pluginRepository: PluginRepository = mock()
    private val pluginService = PluginService(pluginRepository)
    private val plugins = ServerPlugins(UUID.randomUUID())

    @Test
    fun addPlugins() {
        `when`(pluginRepository.save(anyOrNull())).thenReturn(Mono.just(plugins))

        val returned = pluginService.addPlugins(plugins)

        verify(pluginRepository, times(1)).save(anyOrNull())
        verifyNoMoreInteractions(pluginRepository)

        StepVerifier.create(returned)
                .assertNext { assertThat(it.id).isEqualTo(plugins.id) }
                .verifyComplete()
    }

    @Test
    fun getPlugins_thenServerNotFound() {
        `when`(pluginRepository.findById(any(UUID::class.java))).thenReturn(Mono.empty())

        val returned = pluginService.getPlugins(UUID.randomUUID())

        verify(pluginRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(pluginRepository)

        StepVerifier.create(returned)
                .expectError(ServerNotFound::class.java)
                .verify()
    }

    @Test
    fun getPlugins() {
        `when`(pluginRepository.findById(any(UUID::class.java))).thenReturn(Mono.just(plugins))

        val returned = pluginService.getPlugins(UUID.randomUUID())

        verify(pluginRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(pluginRepository)

        StepVerifier.create(returned)
                .assertNext { assertThat(it.id).isEqualTo(plugins.id) }
                .verifyComplete()
    }

    @Test
    fun getAllPlugins() {
        `when`(pluginRepository.findAll()).thenReturn(Flux.just(plugins))

        val returned = pluginService.getAllPlugins()

        verify(pluginRepository, times(1)).findAll()
        verifyNoMoreInteractions(pluginRepository)

        StepVerifier.create(returned)
                .assertNext { assertThat(it.id).isEqualTo(plugins.id) }
                .verifyComplete()
    }

    @Test
    fun deletePlugins_thenServerNotFound() {
        `when`(pluginRepository.findById(any(UUID::class.java))).thenReturn(Mono.empty())

        val returned = pluginService.deletePlugins(UUID.randomUUID())

        verify(pluginRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(pluginRepository)

        StepVerifier.create(returned)
                .expectError(ServerNotFound::class.java)
                .verify()
    }

    @Test
    fun deletePlugins() {
        `when`(pluginRepository.findById(any(UUID::class.java))).thenReturn(Mono.just(plugins))
        `when`(pluginRepository.delete(anyOrNull())).thenReturn(Mono.empty())

        val returned = pluginService.deletePlugins(UUID.randomUUID())

        verify(pluginRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(pluginRepository)

        StepVerifier.create(returned)
                .assertNext { assertThat(it.id).isEqualTo(plugins.id) }
                .verifyComplete()
    }
}