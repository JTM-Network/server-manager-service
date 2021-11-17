package com.jtm.server.entrypoint.controller

import com.jtm.server.core.domain.entity.Directory
import com.jtm.server.data.service.DirectoryService
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
@WebFluxTest(DirectoryController::class)
@AutoConfigureWebTestClient
class DirectoryControllerTest {

    @Autowired
    lateinit var testClient: WebTestClient

    @MockBean
    lateinit var directoryService: DirectoryService

    private val dir = Directory(UUID.randomUUID(), "test")

    @Test
    fun getDirectory() {
        `when`(directoryService.getDirectory(anyOrNull())).thenReturn(Mono.just(dir))

        testClient.get()
                .uri("/dir/${UUID.randomUUID()}")
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.serverId").isEqualTo(dir.serverId.toString())
                .jsonPath("$.name").isEqualTo("test")

        verify(directoryService, times(1)).getDirectory(anyOrNull())
        verifyNoMoreInteractions(directoryService)
    }

    @Test
    fun getDirectories() {
        `when`(directoryService.getDirectories()).thenReturn(Flux.just(dir))

        testClient.get()
                .uri("/dir/all")
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$[0].serverId").isEqualTo(dir.serverId.toString())
                .jsonPath("$[0].name").isEqualTo("test")

        verify(directoryService, times(1)).getDirectories()
        verifyNoMoreInteractions(directoryService)
    }

    @Test
    fun deleteDirectory() {
        `when`(directoryService.removeDirectory(anyOrNull())).thenReturn(Mono.just(dir))

        testClient.delete()
                .uri("/dir/${UUID.randomUUID()}")
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.serverId").isEqualTo(dir.serverId.toString())
                .jsonPath("$.name").isEqualTo("test")

        verify(directoryService, times(1)).removeDirectory(anyOrNull())
        verifyNoMoreInteractions(directoryService)
    }
}