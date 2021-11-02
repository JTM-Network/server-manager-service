package com.jtm.server.data.service

import com.jtm.server.core.domain.exceptions.InvalidJwtToken
import com.jtm.server.core.domain.exceptions.SessionNotFound
import com.jtm.server.core.domain.model.socket.SocketSession
import com.jtm.server.core.usecase.provider.TokenProvider
import com.jtm.server.core.usecase.repository.SessionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*

@Service
class SessionService @Autowired constructor(private val sessionRepository: SessionRepository, private val tokenProvider: TokenProvider) {

    fun getSession(id: UUID): Mono<SocketSession> {
        val session = sessionRepository.getSession(id) ?: return Mono.error { SessionNotFound() }
        return Mono.just(session)
    }

    fun getSessionByAccount(request: ServerHttpRequest): Mono<List<SocketSession>> {
        val bearer = request.headers.getFirst(HttpHeaders.AUTHORIZATION) ?: return Mono.error { InvalidJwtToken() }
        val token = tokenProvider.resolveToken(bearer)
        val id = tokenProvider.getAccessAccountId(token) ?: return Mono.error { InvalidJwtToken() }
        return Mono.just(sessionRepository.getSessionsByAccount(id))
    }

    fun getSessions(): Mono<List<SocketSession>> {
        return Mono.just(sessionRepository.getSessions())
    }

    fun removeSession(id: UUID): Mono<SocketSession> {
        val session = sessionRepository.getSession(id) ?: return Mono.error { SessionNotFound() }
        sessionRepository.removeSession(id)
        return Mono.just(session)
    }
}