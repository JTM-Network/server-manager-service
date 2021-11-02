package com.jtm.server.entrypoint.controller

import com.jtm.server.core.domain.entity.ServerInfo
import com.jtm.server.data.service.ServerInfoService
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
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RunWith(SpringRunner::class)
@WebFluxTest(ServerInfoController::class)
@AutoConfigureWebTestClient
class ServerInfoControllerTest {

    @Autowired
    lateinit var testClient: WebTestClient

    @MockBean
    lateinit var serverInfoService: ServerInfoService

    private val info = ServerInfo(UUID.randomUUID(), UUID.randomUUID(), ip = "ip")

    @Test
    fun getInfoTest() {
        `when`(serverInfoService.getInfo(anyOrNull())).thenReturn(Mono.just(info))

        testClient.get()
                .uri("/server-info/${UUID.randomUUID()}")
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.id").isEqualTo(info.id.toString())
                .jsonPath("$.accountId").isEqualTo(info.accountId.toString())
                .jsonPath("$.ip").isEqualTo(info.ip)

        verify(serverInfoService, times(1)).getInfo(anyOrNull())
        verifyNoMoreInteractions(serverInfoService)
    }

    @Test
    fun getInfosByAccountTest() {
        `when`(serverInfoService.getInfoByAccount(anyOrNull())).thenReturn(Flux.just(info))

        testClient.get()
                .uri("/server-info/account")
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(info.id.toString())
                .jsonPath("$[0].accountId").isEqualTo(info.accountId.toString())
                .jsonPath("$[0].ip").isEqualTo(info.ip)

        verify(serverInfoService, times(1)).getInfoByAccount(anyOrNull())
        verifyNoMoreInteractions(serverInfoService)
    }

    @Test
    fun getInfos() {
        `when`(serverInfoService.getInfos()).thenReturn(Flux.just(info))

        testClient.get()
                .uri("/server-info/all")
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(info.id.toString())
                .jsonPath("$[0].accountId").isEqualTo(info.accountId.toString())
                .jsonPath("$[0].ip").isEqualTo(info.ip)

        verify(serverInfoService, times(1)).getInfos()
        verifyNoMoreInteractions(serverInfoService)
    }
}