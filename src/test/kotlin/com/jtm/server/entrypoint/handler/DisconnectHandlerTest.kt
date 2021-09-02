package com.jtm.server.entrypoint.handler

import com.jtm.server.core.domain.model.client.DisconnectEvent
import com.jtm.server.core.usecase.repository.SessionRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.test.StepVerifier

@RunWith(SpringRunner::class)
class DisconnectHandlerTest {

    private val sessionRepository: SessionRepository = mock()
    private val disconnectHandler = DisconnectHandler(sessionRepository)

    private val socketSession: WebSocketSession = mock()
    private val event = DisconnectEvent("id")

    @Before
    fun setup() {
        `when`(socketSession.id).thenReturn("id")
    }

    @Test
    fun onEvent_thenSessionNotExists() {
        `when`(sessionRepository.exists(anyString())).thenReturn(false)

        val returned = disconnectHandler.onEvent(socketSession, event)

        verify(socketSession, times(1)).id
        verifyNoMoreInteractions(socketSession)

        verify(sessionRepository, times(1)).exists(anyString())
        verifyNoMoreInteractions(sessionRepository)

        StepVerifier.create(returned)
            .verifyComplete()
    }

    @Test
    fun onEvent() {
        `when`(sessionRepository.exists(anyString())).thenReturn(true)

        val returned = disconnectHandler.onEvent(socketSession, event)

        verify(socketSession, times(3)).id
        verifyNoMoreInteractions(socketSession)

        verify(sessionRepository, times(1)).exists(anyString())
        verify(sessionRepository, times(1)).removeSession(anyString())
        verifyNoMoreInteractions(sessionRepository)

        StepVerifier.create(returned)
            .verifyComplete()
    }
}