package com.jtm.server.entrypoint.controller

import com.jtm.server.core.domain.model.socket.SocketSession
import com.jtm.server.data.service.SessionService
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import java.util.*

@RunWith(SpringRunner::class)
@WebFluxTest(SessionController::class)
@AutoConfigureWebTestClient
class SessionControllerTest {

    @Autowired
    lateinit var testClient: WebTestClient

    @MockBean
    lateinit var sessionService: SessionService

    private val accountId = UUID.randomUUID()
    private val socketSession: WebSocketSession = mock()
    private val session = SocketSession(UUID.randomUUID(), accountId, socketSession)

    @Test
    fun getSessionTest() {
        `when`(sessionService.getSession(anyOrNull())).thenReturn(Mono.just(session))

        testClient.get()
            .uri("/session/${UUID.randomUUID()}")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.accountId").isEqualTo(accountId.toString())

        verify(sessionService, times(1)).getSession(anyOrNull())
        verifyNoMoreInteractions(sessionService)
    }

    @Test
    fun getSessionByAccountTest() {

        `when`(sessionService.getSessionByAccount(anyOrNull())).thenReturn(Mono.just(listOf(session)))

        testClient.get()
            .uri("/session/account")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$[0].accountId").isEqualTo(accountId.toString())

        verify(sessionService, times(1)).getSessionByAccount(anyOrNull())
        verifyNoMoreInteractions(sessionService)
    }

    @Test
    fun getSessionsTest() {
        `when`(sessionService.getSessions()).thenReturn(Mono.just(listOf(session)))

        testClient.get()
            .uri("/session/all")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$[0].accountId").isEqualTo(accountId.toString())

        verify(sessionService, times(1)).getSessions()
        verifyNoMoreInteractions(sessionService)
    }

    @Test
    fun deleteSessionTest() {
        `when`(sessionService.removeSession(anyOrNull())).thenReturn(Mono.just(session))

        testClient.delete()
            .uri("/session/${UUID.randomUUID()}")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.accountId").isEqualTo(accountId.toString())

        verify(sessionService, times(1)).removeSession(anyOrNull())
        verifyNoMoreInteractions(sessionService)
    }
}