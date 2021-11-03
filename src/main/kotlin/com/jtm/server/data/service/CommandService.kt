package com.jtm.server.data.service

import com.jtm.server.core.domain.dto.CommandDto
import com.jtm.server.core.domain.model.client.server.Command
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CommandService @Autowired constructor(private val sessionService: SessionService) {

    fun sendCommand(dto: CommandDto): Mono<Void> {
        return sessionService.getSession(dto.serverId)
                .flatMap { it.sendMessage("send_command", Command(dto.command)).then() }
    }
}