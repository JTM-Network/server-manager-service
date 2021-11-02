package com.jtm.server.entrypoint.handler

import com.jtm.server.core.domain.model.client.ConnectEvent
import com.jtm.server.core.domain.entity.ServerInfo
import com.jtm.server.core.usecase.provider.TokenProvider
import com.jtm.server.core.usecase.repository.SessionRepository
import com.jtm.server.data.service.ServerInfoService
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.*

@RunWith(SpringRunner::class)
class ConnectedHandlerTest {

    private val sessionRepository: SessionRepository = mock()
    private val infoService: ServerInfoService = mock()
    private val tokenProvider: TokenProvider = mock()
    private val connectedHandler = ConnectedHandler(sessionRepository, infoService, tokenProvider)

    private val socketSession: WebSocketSession = mock()
    private val event: ConnectEvent = ConnectEvent(token = "token", serverId = UUID.randomUUID(), info = ServerInfo(UUID.randomUUID(), UUID.randomUUID(), "localhost"))

    @Test
    fun onEvent_thenSessionExists() {
        `when`(sessionRepository.exists(anyOrNull())).thenReturn(true)

        val returned = connectedHandler.onEvent(socketSession, event)

        verify(sessionRepository, times(1)).exists(anyOrNull())
        verifyNoMoreInteractions(sessionRepository)

        StepVerifier.create(returned)
            .verifyComplete()
    }

    @Test
    fun onEvent() {
        val socketMessage: WebSocketMessage = mock()
        val info = ServerInfo(UUID.randomUUID(), UUID.randomUUID())

        `when`(socketSession.id).thenReturn("id")
        `when`(socketSession.textMessage(anyString())).thenReturn(socketMessage)
        `when`(sessionRepository.exists(anyOrNull())).thenReturn(false)
        `when`(tokenProvider.getAccountId(anyString())).thenReturn(UUID.randomUUID())
        `when`(infoService.connected(anyOrNull())).thenReturn(Mono.just(info))

        val returned = connectedHandler.onEvent(socketSession, event)

        verify(infoService, times(1)).connected(anyOrNull())
        verifyNoMoreInteractions(infoService)

        verify(sessionRepository, times(1)).exists(anyOrNull())
        verify(sessionRepository, times(1)).addSession(anyOrNull(), anyOrNull())
        verifyNoMoreInteractions(sessionRepository)

        verify(tokenProvider, times(1)).getAccountId(anyString())
        verifyNoMoreInteractions(tokenProvider)

        StepVerifier.create(returned)
            .assertNext { assertThat(it).isInstanceOf(WebSocketMessage::class.java) }
            .verifyComplete()
    }
}