package com.jtm.server.entrypoint.handler

import com.jtm.server.core.domain.model.server.ServerLog
import com.jtm.server.core.usecase.repository.LogRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.test.StepVerifier
import java.util.*

@RunWith(SpringRunner::class)
class ServerLogHandlerTest {

    private val logRepository: LogRepository = mock()
    private val logHandler = ServerLogHandler(logRepository)

    private val session: WebSocketSession = mock()
    private val log = ServerLog(LinkedList(listOf("test...")))

    @Before
    fun setup() {
        `when`(session.id).thenReturn("test")
    }

    @Test
    fun onEvent_thenAddLog() {
        `when`(logRepository.exists(anyString())).thenReturn(false)

        val returned = logHandler.onEvent(session, log)

        verify(logRepository, times(1)).exists(anyString())
        verify(logRepository, times(1)).addLog(anyString())
        verify(logRepository, times(1)).sendLogMessage(anyString(), anyString())
        verifyNoMoreInteractions(logRepository)

        StepVerifier.create(returned)
            .verifyComplete()
    }

    @Test
    fun onEvent() {
        `when`(logRepository.exists(anyString())).thenReturn(true)

        val returned = logHandler.onEvent(session, log)

        verify(logRepository, times(1)).exists(anyString())
        verify(logRepository, times(1)).sendLogMessage(anyString(), anyString())
        verifyNoMoreInteractions(logRepository)

        StepVerifier.create(returned)
            .verifyComplete()
    }
}