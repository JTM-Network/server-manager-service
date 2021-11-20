package com.jtm.server.entrypoint.controller.plugin

import com.jtm.server.core.domain.dto.PluginCommandDto
import com.jtm.server.data.service.plugin.PluginCommandService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/plugin/command")
class PluginCommandController @Autowired constructor(private val commandService: PluginCommandService) {

    @PostMapping("/enable")
    fun enable(@RequestBody dto: PluginCommandDto): Mono<Void> {
        return commandService.enableCmd(dto)
    }

    @PostMapping("/disable")
    fun disable(@RequestBody dto: PluginCommandDto): Mono<Void> {
        return commandService.disableCmd(dto)
    }
}