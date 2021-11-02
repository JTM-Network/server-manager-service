package com.jtm.server.data.service

import com.jtm.server.core.domain.entity.Server
import com.jtm.server.core.domain.exceptions.ServerInfoFound
import com.jtm.server.core.domain.exceptions.ServerInfoNotFound
import com.jtm.server.core.domain.model.client.server.ServerInfo
import com.jtm.server.core.usecase.provider.TokenProvider
import com.jtm.server.core.usecase.repository.ServerInfoRepository
import org.assertj.core.api.Assertions.assertThat
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
import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.test.context.junit4.SpringRunner
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.*

@RunWith(SpringRunner::class)
class ServerServiceTest {

    private val infoRepository: ServerInfoRepository = mock()
    private val tokenProvider: TokenProvider = mock()
    private val infoService = ServerService(infoRepository, tokenProvider)
    private val info = Server(UUID.randomUUID(), UUID.randomUUID(), info = ServerInfo())

    @Test
    fun createInfo_thenFound() {
        `when`(infoRepository.findById(any(UUID::class.java))).thenReturn(Mono.just(info))

        val returned = infoService.createInfo(info)

        verify(infoRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(infoRepository)

        StepVerifier.create(returned)
                .expectError(ServerInfoFound::class.java)
                .verify()
    }

    @Test
    fun createInfo() {
        `when`(infoRepository.findById(any(UUID::class.java))).thenReturn(Mono.empty())
        `when`(infoRepository.save(anyOrNull())).thenReturn(Mono.just(info))

        val returned = infoService.createInfo(info)

        verify(infoRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(infoRepository)

        StepVerifier.create(returned)
                .assertNext {
                    assertThat(it.id).isEqualTo(info.id)
                    assertThat(it.accountId).isEqualTo(info.accountId)
                }
                .verifyComplete()
    }

    @Test
    fun connected() {
        `when`(infoRepository.findById(any(UUID::class.java))).thenReturn(Mono.just(info))
        `when`(infoRepository.save(anyOrNull())).thenReturn(Mono.just(info.connect()))

        val returned = infoService.connected(UUID.randomUUID())

        verify(infoRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(infoRepository)

        StepVerifier.create(returned)
                .assertNext {
                    assertThat(it.id).isEqualTo(info.id)
                    assertThat(it.accountId).isEqualTo(info.accountId)
                }
                .verifyComplete()
    }

    @Test
    fun disconnected_thenNotFound() {
        `when`(infoRepository.findById(any(UUID::class.java))).thenReturn(Mono.empty())

        val returned = infoService.disconnected(UUID.randomUUID())

        verify(infoRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(infoRepository)

        StepVerifier.create(returned)
                .expectError(ServerInfoNotFound::class.java)
                .verify()
    }

    @Test
    fun disconnected() {
        `when`(infoRepository.findById(any(UUID::class.java))).thenReturn(Mono.just(info))
        `when`(infoRepository.save(anyOrNull())).thenReturn(Mono.just(info))

        val returned = infoService.disconnected(UUID.randomUUID())

        verify(infoRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(infoRepository)

        StepVerifier.create(returned)
                .assertNext {
                    assertThat(it.id).isEqualTo(info.id)
                    assertThat(it.accountId).isEqualTo(info.accountId)
                }
                .verifyComplete()
    }

    @Test
    fun getInfo_thenNotFound() {
        `when`(infoRepository.findById(any(UUID::class.java))).thenReturn(Mono.empty())

        val returned = infoService.getInfo(UUID.randomUUID())

        verify(infoRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(infoRepository)

        StepVerifier.create(returned)
                .expectError(ServerInfoNotFound::class.java)
                .verify()
    }

    @Test
    fun getInfo() {
        `when`(infoRepository.findById(any(UUID::class.java))).thenReturn(Mono.just(info))

        val returned = infoService.getInfo(UUID.randomUUID())

        verify(infoRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(infoRepository)

        StepVerifier.create(returned)
                .assertNext {
                    assertThat(it.id).isEqualTo(info.id)
                    assertThat(it.accountId).isEqualTo(info.accountId)
                }
                .verifyComplete()
    }

    @Test
    fun getInfoByAccount() {
        val request: ServerHttpRequest = mock()
        val headers: HttpHeaders = mock()

        `when`(request.headers).thenReturn(headers)
        `when`(headers.getFirst(anyString())).thenReturn("Bearer token")
        `when`(tokenProvider.resolveToken(anyString())).thenReturn("token")
        `when`(tokenProvider.getAccessAccountId(anyString())).thenReturn(info.accountId)
        `when`(infoRepository.findAll()).thenReturn(Flux.just(info))

        val returned = infoService.getInfoByAccount(request)

        verify(request, times(1)).headers
        verifyNoMoreInteractions(request)

        verify(headers, times(1)).getFirst(anyString())
        verifyNoMoreInteractions(headers)

        verify(tokenProvider, times(1)).resolveToken(anyString())
        verify(tokenProvider, times(1)).getAccessAccountId(anyString())
        verifyNoMoreInteractions(tokenProvider)

        verify(infoRepository, times(1)).findAll()
        verifyNoMoreInteractions(infoRepository)

        StepVerifier.create(returned)
                .assertNext {
                    assertThat(it.id).isEqualTo(info.id)
                    assertThat(it.accountId).isEqualTo(info.accountId)
                }
                .verifyComplete()
    }

    @Test
    fun getInfos() {
        `when`(infoRepository.findAll()).thenReturn(Flux.just(info))

        val returned = infoService.getInfos()

        verify(infoRepository, times(1)).findAll()
        verifyNoMoreInteractions(infoRepository)

        StepVerifier.create(returned)
                .assertNext {
                    assertThat(it.id).isEqualTo(info.id)
                    assertThat(it.accountId).isEqualTo(info.accountId)
                }
                .verifyComplete()
    }
}