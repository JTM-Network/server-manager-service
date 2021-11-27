package com.jtm.server.entrypoint.controller.plugin

import com.jtm.server.core.domain.dto.PluginCommandDto
import com.jtm.server.data.service.plugin.PluginCommandService
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
import reactor.core.publisher.Mono
import java.util.*

@RunWith(SpringRunner::class)
@WebFluxTest(PluginCommandController::class)
@AutoConfigureWebTestClient
class PluginCommandControllerTest {

    @Autowired
    lateinit var testClient: WebTestClient

    @MockBean
    lateinit var commandService: PluginCommandService

    @Test
    fun enableCmd() {
        `when`(commandService.enableCmd(anyOrNull())).thenReturn(Mono.empty())

        testClient.post()
                .uri("/plugin/command/enable")
                .bodyValue(PluginCommandDto(UUID.randomUUID().toString(), "test"))
                .exchange()
                .expectStatus().isOk

        verify(commandService, times(1)).enableCmd(anyOrNull())
        verifyNoMoreInteractions(commandService)
    }

    @Test
    fun disableCmd() {
        `when`(commandService.disableCmd(anyOrNull())).thenReturn(Mono.empty())

        testClient.post()
                .uri("/plugin/command/disable")
                .bodyValue(PluginCommandDto(UUID.randomUUID().toString(), "test"))
                .exchange()
                .expectStatus().isOk

        verify(commandService, times(1)).disableCmd(anyOrNull())
        verifyNoMoreInteractions(commandService)
    }
}