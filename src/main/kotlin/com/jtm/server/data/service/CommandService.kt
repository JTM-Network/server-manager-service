package com.jtm.server.data.service

import com.jtm.server.core.domain.dto.CommandDto
import com.jtm.server.core.domain.exceptions.ServerOffline
import com.jtm.server.core.domain.model.client.server.Command
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*

@Service
class CommandService @Autowired constructor(private val sessionService: SessionService) {

    fun sendCommand(dto: CommandDto): Mono<Void> {
        return sessionService.getSession(UUID.fromString(dto.serverId))
                .switchIfEmpty(Mono.defer { Mono.error(ServerOffline()) })
                .flatMap { it.sendEvent("send_command", Command(dto.command)) }
                .then()
    }
}