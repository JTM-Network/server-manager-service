package com.jtm.server.entrypoint.controller

import com.jtm.server.core.domain.entity.ServerPlugins
import com.jtm.server.data.service.plugin.PluginService
import com.jtm.server.entrypoint.controller.plugin.PluginController
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RunWith(SpringRunner::class)
@WebFluxTest(PluginController::class)
@AutoConfigureWebTestClient
class PluginControllerTest {

    @Autowired
    lateinit var testClient: WebTestClient

    @MockBean
    lateinit var pluginService: PluginService

    private val created = ServerPlugins(UUID.randomUUID())

    @Test
    fun getPlugins() {
        `when`(pluginService.getPlugins(anyOrNull())).thenReturn(Mono.just(created))

        testClient.get()
                .uri("/plugins/${UUID.randomUUID()}")
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.id").isEqualTo(created.id.toString())

        verify(pluginService, times(1)).getPlugins(anyOrNull())
        verifyNoMoreInteractions(pluginService)
    }

    @Test
    fun getAllPlugins() {
        `when`(pluginService.getAllPlugins()).thenReturn(Flux.just(created))

        testClient.get()
                .uri("/plugins/all")
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(created.id.toString())

        verify(pluginService, times(1)).getAllPlugins()
        verifyNoMoreInteractions(pluginService)
    }

    @Test
    fun deletePlugins() {
        `when`(pluginService.deletePlugins(anyOrNull())).thenReturn(Mono.just(created))

        testClient.delete()
                .uri("/plugins/${UUID.randomUUID()}")
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.id").isEqualTo(created.id.toString())

        verify(pluginService, times(1)).deletePlugins(anyOrNull())
        verifyNoMoreInteractions(pluginService)
    }
}