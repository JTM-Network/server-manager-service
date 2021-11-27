package com.jtm.server.entrypoint.controller

import com.jtm.server.core.domain.dto.CommandDto
import com.jtm.server.data.service.CommandService
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
@WebFluxTest(CommandController::class)
@AutoConfigureWebTestClient
class CommandControllerTest {

    @Autowired
    lateinit var testClient: WebTestClient

    @MockBean
    lateinit var commandService: CommandService

    @Test
    fun sendCommand() {
        `when`(commandService.sendCommand(anyOrNull())).thenReturn(Mono.empty())

        testClient.post()
                .uri("/command/send")
                .bodyValue(CommandDto(UUID.randomUUID().toString(), "test"))
                .exchange()
                .expectStatus().isOk

        verify(commandService, times(1)).sendCommand(anyOrNull())
        verifyNoMoreInteractions(commandService)
    }
}