package com.jtm.server.entrypoint.controller

import com.jtm.server.core.domain.constants.TimeType
import com.jtm.server.core.domain.entity.RuntimeStats
import com.jtm.server.data.service.RuntimeService
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyLong
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
@WebFluxTest(RuntimeController::class)
@AutoConfigureWebTestClient
class RuntimeControllerTest {

    @Autowired
    lateinit var testClient: WebTestClient

    @MockBean
    lateinit var runtimeService: RuntimeService
    private val runtime = RuntimeStats()

    @Test
    fun getRuntime() {
        `when`(runtimeService.getRuntime(anyLong())).thenReturn(Mono.just(runtime))

        testClient.get()
                .uri("/runtime/${System.currentTimeMillis()}")
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.time").isEqualTo(TimeType.FIVE.name)
                .jsonPath("$.serverId").isEqualTo(runtime.serverId.toString())
                .jsonPath("$.cpuUsage").isEqualTo(0.10)

        verify(runtimeService, times(1)).getRuntime(anyLong())
        verifyNoMoreInteractions(runtimeService)
    }

    @Test
    fun getRuntimesByServerId() {
        `when`(runtimeService.getRuntimesByServerId(anyOrNull())).thenReturn(Flux.just(runtime))

        testClient.get()
                .uri("/runtime/server/${UUID.randomUUID()}")
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$[0].time").isEqualTo(TimeType.FIVE.name)
                .jsonPath("$[0].serverId").isEqualTo(runtime.serverId.toString())
                .jsonPath("$[0].cpuUsage").isEqualTo(0.10)

        verify(runtimeService, times(1)).getRuntimesByServerId(anyOrNull())
        verifyNoMoreInteractions(runtimeService)
    }

    @Test
    fun getRuntimesByServerIdAndTime() {
        `when`(runtimeService.getRuntimesByServerIdAndTime(anyOrNull(), anyOrNull())).thenReturn(Flux.just(runtime))

        testClient.get()
                .uri("/runtime/server/${UUID.randomUUID()}/${TimeType.FIVE}")
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$[0].time").isEqualTo(TimeType.FIVE.name)
                .jsonPath("$[0].serverId").isEqualTo(runtime.serverId.toString())
                .jsonPath("$[0].cpuUsage").isEqualTo(0.10)

        verify(runtimeService, times(1)).getRuntimesByServerIdAndTime(anyOrNull(), anyOrNull())
        verifyNoMoreInteractions(runtimeService)
    }

    @Test
    fun deleteRuntime() {
        `when`(runtimeService.deleteRuntime(anyLong())).thenReturn(Mono.just(runtime))

        testClient.delete()
                .uri("/runtime/${System.currentTimeMillis()}")
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.time").isEqualTo(TimeType.FIVE.name)
                .jsonPath("$.serverId").isEqualTo(runtime.serverId.toString())
                .jsonPath("$.cpuUsage").isEqualTo(0.10)

        verify(runtimeService, times(1)).deleteRuntime(anyLong())
        verifyNoMoreInteractions(runtimeService)
    }
}