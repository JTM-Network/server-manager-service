package com.jtm.server.entrypoint.controller

import com.jtm.server.core.domain.entity.ServerInfo
import com.jtm.server.data.service.ServerInfoService
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
class ServerInfoController @Autowired constructor(private val serverInfoService: ServerInfoService) {

    @GetMapping("/{id}")
    fun getInfo(@PathVariable id: UUID): Mono<ServerInfo> {
        return serverInfoService.getInfo(id)
    }

    @GetMapping("/account")
    fun getInfosByAccount(request: ServerHttpRequest): Flux<ServerInfo> {
        return serverInfoService.getInfoByAccount(request)
    }

    @GetMapping("/all")
    fun getInfos(): Flux<ServerInfo> {
        return serverInfoService.getInfos()
    }
}