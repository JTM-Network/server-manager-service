package com.jtm.server.data.service

import com.jtm.server.core.domain.dto.CommandDto
import com.jtm.server.core.domain.model.client.server.Command
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*

@Service
class CommandService @Autowired constructor(private val sessionService: SessionService) {

    private val logger = LoggerFactory.getLogger(CommandService::class.java)

    fun sendCommand(dto: CommandDto): Mono<Void> {
        logger.info("Received command.")
        return sessionService.getSession(UUID.fromString(dto.serverId))
                .flatMap {
                    it.sendEvent("send_command", Command(dto.command))
                }
                .then()
    }
}