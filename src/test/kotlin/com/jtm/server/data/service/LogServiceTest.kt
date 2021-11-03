package com.jtm.server.data.service

import com.jtm.server.core.domain.exceptions.LogsNotFound
import com.jtm.server.core.domain.model.socket.SocketSession
import com.jtm.server.core.usecase.repository.LogRepository
import com.jtm.server.core.usecase.repository.SessionRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.springframework.http.codec.ServerSentEvent
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks
import reactor.test.StepVerifier
import java.util.*

@RunWith(SpringRunner::class)
class LogServiceTest {

    private val logRepository: LogRepository = mock()
    private val sessionRepository: SessionRepository = mock()
    private val logService = LogService(logRepository, sessionRepository)

    private val sink: Sinks.Many<String> = mock()
    private val flux = Flux.just("test")
    private val webSocketSession: WebSocketSession = mock()

    @Test
    fun getLogs_thenNotFound() {
        `when`(webSocketSession.id).thenReturn("id")
        `when`(sessionRepository.getSession(anyOrNull())).thenReturn(SocketSession(UUID.randomUUID(), UUID.randomUUID(), webSocketSession))
        `when`(logRepository.getLog(anyString())).thenReturn(null)

        val returned = logService.getLogs(UUID.randomUUID())

        verify(sessionRepository, times(1)).getSession(anyOrNull())
        verifyNoMoreInteractions(sessionRepository)

        verify(logRepository, times(1)).getLog(anyString())
        verifyNoMoreInteractions(logRepository)

        StepVerifier.create(returned)
            .expectError(LogsNotFound::class.java)
            .verify()
    }

    @Test
    fun getLogsTest() {
        `when`(webSocketSession.id).thenReturn("id")
        `when`(sessionRepository.getSession(anyOrNull())).thenReturn(SocketSession(UUID.randomUUID(), UUID.randomUUID(), webSocketSession))
        `when`(logRepository.getLog(anyString())).thenReturn(sink)
        `when`(sink.asFlux()).thenReturn(flux)

        val returned = logService.getLogs(UUID.randomUUID())

        verify(sessionRepository, times(1)).getSession(anyOrNull())
        verifyNoMoreInteractions(sessionRepository)

        verify(logRepository, times(1)).getLog(anyString())
        verifyNoMoreInteractions(logRepository)

        StepVerifier.create(returned)
            .assertNext { assertThat(it).isInstanceOf(ServerSentEvent::class.java) }
            .verifyComplete()
    }
}