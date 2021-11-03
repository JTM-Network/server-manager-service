package com.jtm.server.entrypoint.controller

import com.jtm.server.core.domain.dto.CommandDto
import com.jtm.server.data.service.CommandService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/command")
class CommandController @Autowired constructor(private val commandService: CommandService) {

    @PostMapping("/{id}")
    fun sendCommand(@RequestBody dto: CommandDto): Mono<Void> {
        return commandService.sendCommand(dto)
    }
}