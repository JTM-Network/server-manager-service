package com.jtm.server.data.service

import com.jtm.server.core.domain.dto.CommandDto
import com.jtm.server.core.domain.exceptions.ServerOffline
import com.jtm.server.core.domain.model.socket.SocketSession
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
class CommandServiceTest {

    private val sessionService: SessionService = mock()
    private val commandService = CommandService(sessionService)
    private val socketSession: SocketSession = mock()

    @Test
    fun sendCommand_thenServerOffline() {
        `when`(sessionService.getSession(anyOrNull())).thenReturn(Mono.empty())

        val returned = commandService.sendCommand(CommandDto(UUID.randomUUID().toString(), "test"))

        verify(sessionService, times(1)).getSession(anyOrNull())
        verifyNoMoreInteractions(sessionService)

        StepVerifier.create(returned)
                .expectError(ServerOffline::class.java)
                .verify()
    }

    @Test
    fun sendCommand() {
        `when`(sessionService.getSession(anyOrNull())).thenReturn(Mono.just(socketSession))
        `when`(socketSession.sendEvent(anyString(), anyOrNull())).thenReturn(Mono.empty())

        val returned = commandService.sendCommand(CommandDto(UUID.randomUUID().toString(), "test"))

        verify(sessionService, times(1)).getSession(anyOrNull())
        verifyNoMoreInteractions(sessionService)

        StepVerifier.create(returned)
                .verifyComplete()
    }
}