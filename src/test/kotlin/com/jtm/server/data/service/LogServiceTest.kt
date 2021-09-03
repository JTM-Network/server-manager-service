package com.jtm.server.data.service

import com.jtm.server.core.domain.exceptions.LogsNotFound
import com.jtm.server.core.usecase.repository.LogRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.springframework.http.codec.ServerSentEvent
import org.springframework.test.context.junit4.SpringRunner
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import reactor.test.StepVerifier

@RunWith(SpringRunner::class)
class LogServiceTest {

    private val logRepository: LogRepository = mock()
    private val logService = LogService(logRepository)

    private val sink: Sinks.Many<String> = mock()
    private val flux = Flux.just("test")

    @Test
    fun getLogs_thenNotFound() {
        `when`(logRepository.getLog(anyString())).thenReturn(null)

        val returned = logService.getLogs("id")

        verify(logRepository, times(1)).getLog(anyString())
        verifyNoMoreInteractions(logRepository)

        StepVerifier.create(returned)
            .expectError(LogsNotFound::class.java)
            .verify()
    }

    @Test
    fun getLogsTest() {
        `when`(logRepository.getLog(anyString())).thenReturn(sink)
        `when`(sink.asFlux()).thenReturn(flux)

        val returned = logService.getLogs("id")

        verify(logRepository, times(1)).getLog(anyString())
        verifyNoMoreInteractions(logRepository)

        StepVerifier.create(returned)
            .assertNext { assertThat(it).isInstanceOf(ServerSentEvent::class.java) }
            .verifyComplete()
    }
}