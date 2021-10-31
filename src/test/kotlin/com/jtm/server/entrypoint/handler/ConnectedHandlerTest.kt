package com.jtm.server.entrypoint.handler

import com.jtm.server.core.domain.model.client.ConnectEvent
import com.jtm.server.core.domain.model.server.ServerInfo
import com.jtm.server.core.usecase.provider.TokenProvider
import com.jtm.server.core.usecase.repository.SessionRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.test.StepVerifier
import java.util.*

@RunWith(SpringRunner::class)
class ConnectedHandlerTest {

    private val sessionRepository: SessionRepository = mock()
    private val tokenProvider: TokenProvider = mock()
    private val connectedHandler = ConnectedHandler(sessionRepository, tokenProvider)

    private val socketSession: WebSocketSession = mock()
    private val event: ConnectEvent = ConnectEvent(token = "token", publicKey = "key", info = ServerInfo("localhost"))

    @Test
    fun onEvent_thenSessionExists() {
        `when`(socketSession.id).thenReturn("id")
        `when`(sessionRepository.exists(anyString())).thenReturn(true)

        val returned = connectedHandler.onEvent(socketSession, event)

        verify(socketSession, times(1)).id
        verifyNoMoreInteractions(socketSession)

        verify(sessionRepository, times(1)).exists(anyString())
        verifyNoMoreInteractions(sessionRepository)

        StepVerifier.create(returned)
            .verifyComplete()
    }

    @Test
    fun onEvent() {
        val socketMessage: WebSocketMessage = mock()

        `when`(socketSession.id).thenReturn("id")
        `when`(socketSession.textMessage(anyString())).thenReturn(socketMessage)
        `when`(sessionRepository.exists(anyString())).thenReturn(false)
        `when`(tokenProvider.getAccountId(anyString())).thenReturn(UUID.randomUUID())

        val returned = connectedHandler.onEvent(socketSession, event)

        verify(socketSession, times(3)).id
        verify(socketSession, times(1)).textMessage(anyString())
        verifyNoMoreInteractions(socketSession)

        verify(sessionRepository, times(1)).exists(anyString())
        verify(sessionRepository, times(1)).addSession(anyOrNull())
        verifyNoMoreInteractions(sessionRepository)

        verify(tokenProvider, times(1)).getAccountId(anyString())
        verifyNoMoreInteractions(tokenProvider)

        StepVerifier.create(returned)
            .assertNext { assertThat(it).isInstanceOf(WebSocketMessage::class.java) }
            .verifyComplete()
    }
}