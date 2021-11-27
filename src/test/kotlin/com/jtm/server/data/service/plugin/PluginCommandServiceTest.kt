package com.jtm.server.data.service.plugin

import com.jtm.server.core.domain.dto.PluginCommandDto
import com.jtm.server.core.domain.exceptions.ServerOffline
import com.jtm.server.core.domain.exceptions.SessionNotFound
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
import org.springframework.test.context.junit4.SpringRunner
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.*

@RunWith(SpringRunner::class)
class PluginCommandServiceTest {

    private val sessionService: SessionService = mock()
    private val commandService = PluginCommandService(sessionService)
    private val socketSession: SocketSession = mock()
    private val dto = PluginCommandDto(UUID.randomUUID().toString(), "test")

    @Test
    fun enableCmd_thenServerOffline() {
        `when`(sessionService.getSession(anyOrNull())).thenReturn(Mono.empty())

        val returned = commandService.enableCmd(dto)

        verify(sessionService, times(1)).getSession(anyOrNull())
        verifyNoMoreInteractions(sessionService)

        StepVerifier.create(returned)
                .expectError(ServerOffline::class.java)
                .verify()
    }

    @Test
    fun enableCmd() {
        `when`(sessionService.getSession(anyOrNull())).thenReturn(Mono.just(socketSession))
        `when`(socketSession.sendEvent(anyString(), anyOrNull())).thenReturn(Mono.empty())

        val returned = commandService.enableCmd(dto)

        verify(sessionService, times(1)).getSession(anyOrNull())
        verifyNoMoreInteractions(sessionService)

        StepVerifier.create(returned)
                .verifyComplete()
    }

    @Test
    fun disableCmd_thenServerOffline() {
        `when`(sessionService.getSession(anyOrNull())).thenReturn(Mono.empty())

        val returned = commandService.disableCmd(dto)

        verify(sessionService, times(1)).getSession(anyOrNull())
        verifyNoMoreInteractions(sessionService)

        StepVerifier.create(returned)
                .expectError(ServerOffline::class.java)
                .verify()
    }

    @Test
    fun disableCmd() {
        `when`(sessionService.getSession(anyOrNull())).thenReturn(Mono.just(socketSession))
        `when`(socketSession.sendEvent(anyString(), anyOrNull())).thenReturn(Mono.empty())

        val returned = commandService.disableCmd(dto)

        verify(sessionService, times(1)).getSession(anyOrNull())
        verifyNoMoreInteractions(sessionService)

        StepVerifier.create(returned)
                .verifyComplete()
    }
}