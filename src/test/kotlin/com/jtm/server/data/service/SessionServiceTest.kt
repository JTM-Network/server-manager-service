package com.jtm.server.data.service

import com.jtm.server.core.domain.exceptions.InvalidJwtToken
import com.jtm.server.core.domain.exceptions.SessionNotFound
import com.jtm.server.core.domain.model.socket.SocketSession
import com.jtm.server.core.usecase.provider.TokenProvider
import com.jtm.server.core.usecase.repository.SessionRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
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
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.*

@RunWith(SpringRunner::class)
class SessionServiceTest {

    private val sessionRepository: SessionRepository = mock()
    private val tokenProvider: TokenProvider = mock()
    private val sessionService = SessionService(sessionRepository, tokenProvider)

    private val session: SocketSession = mock()
    private val accountId: UUID = UUID.randomUUID()
    private val request: ServerHttpRequest = mock()

    @Before
    fun setup() {
        val headers: HttpHeaders = mock()

        `when`(request.headers).thenReturn(headers)
        `when`(headers.getFirst(anyString())).thenReturn("Bearer test")
        `when`(session.accountId).thenReturn(accountId)
    }

    @Test
    fun getSession_thenNotFound() {
        `when`(sessionRepository.getSession(anyString())).thenReturn(null)

        val returned = sessionService.getSession("id")

        verify(sessionRepository, times(1)).getSession(anyString())
        verifyNoMoreInteractions(sessionRepository)

        StepVerifier.create(returned)
            .expectError(SessionNotFound::class.java)
            .verify()
    }

    @Test
    fun getSessionTest() {
        `when`(sessionRepository.getSession(anyString())).thenReturn(session)

        val returned = sessionService.getSession("id")

        verify(sessionRepository, times(1)).getSession(anyString())
        verifyNoMoreInteractions(sessionRepository)

        StepVerifier.create(returned)
            .assertNext { assertThat(it.accountId).isEqualTo(accountId) }
            .verifyComplete()
    }

    @Test
    fun getSessionByAccount_thenInvalidAccountId() {
        `when`(tokenProvider.resolveToken(anyString())).thenReturn("test")
        `when`(tokenProvider.getAccessAccountId(anyString())).thenReturn(null)

        val returned = sessionService.getSessionByAccount(request)

        verify(request, times(1)).headers
        verifyNoMoreInteractions(request)

        verify(tokenProvider, times(1)).resolveToken(anyString())
        verify(tokenProvider, times(1)).getAccessAccountId(anyString())
        verifyNoMoreInteractions(tokenProvider)

        StepVerifier.create(returned)
            .expectError(InvalidJwtToken::class.java)
            .verify()
    }

    @Test
    fun getSessionByAccountTest() {
        `when`(tokenProvider.resolveToken(anyString())).thenReturn("test")
        `when`(tokenProvider.getAccessAccountId(anyString())).thenReturn(UUID.randomUUID())
        `when`(sessionRepository.getSessionsByAccount(anyOrNull())).thenReturn(listOf(session))

        val returned = sessionService.getSessionByAccount(request)

        verify(request, times(1)).headers
        verifyNoMoreInteractions(request)

        verify(tokenProvider, times(1)).resolveToken(anyString())
        verify(tokenProvider, times(1)).getAccessAccountId(anyString())
        verifyNoMoreInteractions(tokenProvider)

        verify(sessionRepository, times(1)).getSessionsByAccount(anyOrNull())
        verifyNoMoreInteractions(sessionRepository)

        StepVerifier.create(returned)
            .assertNext { assertThat(it[0].accountId).isEqualTo(accountId) }
            .verifyComplete()
    }

    @Test
    fun getSessionsTest() {
        `when`(sessionRepository.getSessions()).thenReturn(listOf(session))

        val returned = sessionService.getSessions()

        verify(sessionRepository, times(1)).getSessions()
        verifyNoMoreInteractions(sessionRepository)

        StepVerifier.create(returned)
            .assertNext { assertThat(it[0].accountId).isEqualTo(accountId) }
            .verifyComplete()
    }

    @Test
    fun removeSession_thenNotFound() {
        `when`(sessionRepository.getSession(anyString())).thenReturn(null)

        val returned = sessionService.removeSession("id")

        verify(sessionRepository, times(1)).getSession(anyString())
        verifyNoMoreInteractions(sessionRepository)

        StepVerifier.create(returned)
            .expectError(SessionNotFound::class.java)
            .verify()
    }

    @Test
    fun removeSessionTest() {
        `when`(sessionRepository.getSession(anyString())).thenReturn(session)

        val returned = sessionService.removeSession("id")

        verify(sessionRepository, times(1)).getSession(anyString())
        verify(sessionRepository, times(1)).removeSession(anyString())
        verifyNoMoreInteractions(sessionRepository)

        StepVerifier.create(returned)
            .assertNext { assertThat(it.accountId).isEqualTo(accountId) }
            .verifyComplete()
    }
}