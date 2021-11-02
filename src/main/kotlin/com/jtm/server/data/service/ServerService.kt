package com.jtm.server.data.service

import com.jtm.server.core.domain.entity.Server
import com.jtm.server.core.domain.exceptions.ServerInfoFound
import com.jtm.server.core.domain.exceptions.ServerInfoNotFound
import com.jtm.server.core.usecase.provider.TokenProvider
import com.jtm.server.core.usecase.repository.ServerInfoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Service
class ServerService @Autowired constructor(private val infoRepository: ServerInfoRepository, private val tokenProvider: TokenProvider) {

    fun createInfo(info: Server): Mono<Server> {
        return infoRepository.findById(info.id)
                .flatMap<Server> { Mono.defer { Mono.error(ServerInfoFound()) } }
                .switchIfEmpty(Mono.defer { infoRepository.save(info) })
    }

    fun connected(id: UUID): Mono<Server> {
        return infoRepository.findById(id)
                .flatMap { infoRepository.save(it.connect()) }
    }

    fun disconnected(id: UUID): Mono<Server> {
        return infoRepository.findById(id)
                .switchIfEmpty(Mono.defer { Mono.error(ServerInfoNotFound()) })
                .flatMap { infoRepository.save(it.disconnect()) }
    }

    fun getInfo(id: UUID): Mono<Server> {
        return infoRepository.findById(id)
                .switchIfEmpty(Mono.defer { Mono.error(ServerInfoNotFound()) })
    }

    fun getInfoByAccount(request: ServerHttpRequest): Flux<Server> {
        val bearer = request.headers.getFirst(HttpHeaders.AUTHORIZATION) ?: return Flux.empty()
        val token = tokenProvider.resolveToken(bearer)
        val accountId = tokenProvider.getAccessAccountId(token)
        return infoRepository.findAll().filter { it.accountId == accountId }
    }

    fun getInfos(): Flux<Server> {
        return infoRepository.findAll()
    }
}