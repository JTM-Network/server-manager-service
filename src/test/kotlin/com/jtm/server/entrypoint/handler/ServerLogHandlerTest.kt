package com.jtm.server.entrypoint.handler

import com.jtm.server.core.domain.model.server.ServerLog
import com.jtm.server.core.domain.model.socket.SocketSession
import com.jtm.server.core.usecase.repository.LogRepository
import com.jtm.server.core.usecase.repository.SessionRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.test.StepVerifier
import java.util.*

@RunWith(SpringRunner::class)
class ServerLogHandlerTest {

    private val logRepository: LogRepository = mock()
    private val sessionRepository: SessionRepository = mock()
    private val logHandler = ServerLogHandler(logRepository, sessionRepository)

    private val session: WebSocketSession = mock()
    private val log = ServerLog("test...")

    @Before
    fun setup() {
        `when`(session.id).thenReturn("test")
    }

    @Test
    fun onEvent_thenAddLog() {
        `when`(sessionRepository.getSessionBySessionId(anyOrNull())).thenReturn(SocketSession(UUID.randomUUID(), UUID.randomUUID(), session))
        `when`(logRepository.exists(anyOrNull())).thenReturn(false)

        val returned = logHandler.onEvent(session, log)

        verify(logRepository, times(1)).exists(anyOrNull())
        verify(logRepository, times(1)).addLog(anyOrNull())
        verify(logRepository, times(1)).sendLogMessage(anyOrNull(), anyString())
        verifyNoMoreInteractions(logRepository)

        StepVerifier.create(returned)
            .verifyComplete()
    }

    @Test
    fun onEvent() {
        `when`(sessionRepository.getSessionBySessionId(anyOrNull())).thenReturn(SocketSession(UUID.randomUUID(), UUID.randomUUID(), session))
        `when`(logRepository.exists(anyOrNull())).thenReturn(true)

        val returned = logHandler.onEvent(session, log)

        verify(logRepository, times(1)).exists(anyOrNull())
        verify(logRepository, times(1)).sendLogMessage(anyOrNull(), anyString())
        verifyNoMoreInteractions(logRepository)

        StepVerifier.create(returned)
            .verifyComplete()
    }
}