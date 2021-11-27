package com.jtm.server.data.service.directory

import com.jtm.server.core.domain.dto.DownloadRequestDto
import com.jtm.server.core.domain.entity.DownloadRequest
import com.jtm.server.core.domain.exceptions.DownloadRequestNotFound
import com.jtm.server.core.domain.model.socket.SocketSession
import com.jtm.server.core.usecase.FileHandler
import com.jtm.server.core.usecase.repository.DownloadRequestRepository
import com.jtm.server.core.usecase.sink.RequestSink
import com.jtm.server.data.service.SessionService
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.springframework.test.context.junit4.SpringRunner
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.*

@RunWith(SpringRunner::class)
class DownloadServiceTest {

    private val sessionService: SessionService = mock()
    private val requestRepository: DownloadRequestRepository = mock()
    private val requestSink: RequestSink = mock()
    private val fileHandler: FileHandler = mock()
    private val downloadService = DownloadService(requestRepository, requestSink, fileHandler)

    private val request = DownloadRequest(DownloadRequestDto(UUID.randomUUID(), "/", "test.jar"))

    @Test
    fun addRequest() {
        val session: SocketSession = mock()

        `when`(requestRepository.save(anyOrNull())).thenReturn(Mono.just(request))
        `when`(sessionService.getSession(anyOrNull())).thenReturn(Mono.just(session))
        `when`(session.sendEvent(anyString(), anyOrNull())).thenReturn(Mono.empty())

        val returned = downloadService.addRequest(DownloadRequestDto(UUID.randomUUID(), "/", "test.jar"), sessionService)

        verify(requestRepository, times(1)).save(anyOrNull())
        verifyNoMoreInteractions(requestRepository)

        StepVerifier.create(returned)
                .assertNext {
                    assertThat(it.serverId).isEqualTo(request.serverId)
                    assertThat(it.path).isEqualTo("/")
                    assertThat(it.file).isEqualTo("test.jar")
                }
                .verifyComplete()
    }

    @Test
    fun clearDownloads() {
        `when`(fileHandler.clearDownloads()).thenReturn(Mono.empty())

        val returned = downloadService.clearDownloads()

        verify(fileHandler, times(1)).clearDownloads()
        verifyNoMoreInteractions(fileHandler)

        StepVerifier.create(returned)
                .verifyComplete()
    }

    @Test
    fun getRequest_thenNotFound() {
        `when`(requestRepository.findById(any(UUID::class.java))).thenReturn(Mono.empty())

        val returned = downloadService.getRequest(UUID.randomUUID())

        verify(requestRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(requestRepository)

        StepVerifier.create(returned)
                .expectError(DownloadRequestNotFound::class.java)
                .verify()
    }

    @Test
    fun getRequest() {
        `when`(requestRepository.findById(any(UUID::class.java))).thenReturn(Mono.just(request))

        val returned = downloadService.getRequest(UUID.randomUUID())

        verify(requestRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(requestRepository)

        StepVerifier.create(returned)
                .assertNext {
                    assertThat(it.serverId).isEqualTo(request.serverId)
                    assertThat(it.path).isEqualTo("/")
                    assertThat(it.file).isEqualTo("test.jar")
                }
                .verifyComplete()
    }

    @Test
    fun getRequests() {
        `when`(requestRepository.findAll()).thenReturn(Flux.just(request))

        val returned = downloadService.getRequests()

        verify(requestRepository, times(1)).findAll()
        verifyNoMoreInteractions(requestRepository)

        StepVerifier.create(returned)
                .assertNext {
                    assertThat(it.serverId).isEqualTo(request.serverId)
                    assertThat(it.path).isEqualTo("/")
                    assertThat(it.file).isEqualTo("test.jar")
                }
                .verifyComplete()
    }

    @Test
    fun deleteRequest_thenNotFound() {
        `when`(requestRepository.findById(any(UUID::class.java))).thenReturn(Mono.empty())

        val returned = downloadService.deleteRequest(UUID.randomUUID())

        verify(requestRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(requestRepository)

        StepVerifier.create(returned)
                .expectError(DownloadRequestNotFound::class.java)
                .verify()
    }

    @Test
    fun deleteRequest() {
        `when`(requestRepository.findById(any(UUID::class.java))).thenReturn(Mono.just(request))
        `when`(requestRepository.delete(anyOrNull())).thenReturn(Mono.empty())

        val returned = downloadService.deleteRequest(UUID.randomUUID())

        verify(requestRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(requestRepository)

        StepVerifier.create(returned)
                .assertNext {
                    assertThat(it.serverId).isEqualTo(request.serverId)
                    assertThat(it.path).isEqualTo("/")
                    assertThat(it.file).isEqualTo("test.jar")
                }
                .verifyComplete()
    }
}