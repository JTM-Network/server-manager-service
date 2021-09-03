package com.jtm.server.entrypoint.controller

import com.jtm.server.data.service.LogService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/logs")
class LogController @Autowired constructor(private val logService: LogService) {

    @GetMapping("/{id}")
    fun getLogs(@PathVariable id: String): Flux<ServerSentEvent<String>> {
        return logService.getLogs(id)
    }
}