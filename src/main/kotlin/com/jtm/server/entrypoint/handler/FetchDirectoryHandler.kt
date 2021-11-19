package com.jtm.server.entrypoint.handler

import com.jtm.server.core.domain.dto.DirectoryDto
import com.jtm.server.core.domain.entity.Directory
import com.jtm.server.core.domain.model.event.impl.FetchDirectoryEvent
import com.jtm.server.core.usecase.event.EventHandler
import com.jtm.server.data.service.DirectoryService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import java.util.*

@Component
class FetchDirectoryHandler @Autowired constructor(private val directoryService: DirectoryService): EventHandler<FetchDirectoryEvent>("fetch_directory", FetchDirectoryEvent::class.java) {

    private val logger = LoggerFactory.getLogger(FetchDirectoryHandler::class.java)

    override fun onEvent(session: WebSocketSession, value: FetchDirectoryEvent): Mono<WebSocketMessage> {
        logger.info("Fetch directory here.")
        return try {
            directoryService.addDirectory(constructDirectory(value.serverId, value.directory))
                    .flatMap { Mono.empty() }
        } catch (ex: Exception) {
            ex.printStackTrace()
            Mono.empty()
        }
    }

    private fun constructDirectory(serverId: UUID, dto: DirectoryDto): Directory {
        return Directory(serverId, dto)
    }
}