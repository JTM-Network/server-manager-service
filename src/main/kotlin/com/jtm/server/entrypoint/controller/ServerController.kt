package com.jtm.server.entrypoint.controller

import com.jtm.server.core.domain.entity.Server
import com.jtm.server.data.service.ServerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping("/server-info")
class ServerController @Autowired constructor(private val serverInfoService: ServerService) {

    @GetMapping("/{id}")
    fun getInfo(@PathVariable id: UUID): Mono<Server> {
        return serverInfoService.getInfo(id)
    }

    @GetMapping("/account")
    fun getInfosByAccount(request: ServerHttpRequest): Flux<Server> {
        return serverInfoService.getInfoByAccount(request)
    }

    @GetMapping("/all")
    fun getInfos(): Flux<Server> {
        return serverInfoService.getInfos()
    }
}