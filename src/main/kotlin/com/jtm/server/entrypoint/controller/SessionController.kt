package com.jtm.server.entrypoint.controller

import com.jtm.server.core.domain.model.socket.SocketSession
import com.jtm.server.data.service.SessionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/session")
class SessionController @Autowired constructor(private val sessionService: SessionService) {

    @GetMapping("/{id}")
    fun getSession(@PathVariable id: String): Mono<SocketSession> {
        return sessionService.getSession(id)
    }

    @GetMapping("/account")
    fun getSessionByAccount(request: ServerHttpRequest): Mono<List<SocketSession>> {
        return sessionService.getSessionByAccount(request)
    }

    @GetMapping("/all")
    fun getSessions(): Mono<List<SocketSession>> {
        return sessionService.getSessions()
    }

    @DeleteMapping("/{id}")
    fun deleteSession(@PathVariable id: String): Mono<SocketSession> {
        return sessionService.removeSession(id)
    }
}