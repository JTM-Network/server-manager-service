package com.jtm.server.entrypoint.handler

import com.jtm.server.core.domain.dto.DirectoryDto
import com.jtm.server.core.domain.entity.Directory
import com.jtm.server.core.domain.entity.DirectoryEntity
import com.jtm.server.core.domain.model.event.impl.directory.AddedDirectoryEvent
import com.jtm.server.core.domain.model.event.impl.fetch.FetchDirectoryEvent
import com.jtm.server.core.usecase.event.EventHandler
import com.jtm.server.data.service.directory.DirectoryService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import java.util.*

@Component
class FetchDirectoryHandler @Autowired constructor(private val directoryService: DirectoryService): EventHandler<FetchDirectoryEvent>("fetch_directory", FetchDirectoryEvent::class.java) {

    override fun onEvent(session: WebSocketSession, value: FetchDirectoryEvent): Mono<WebSocketMessage> {
        return directoryService.addDirectory(constructDirectory(UUID.fromString(value.directory.serverId), value.directory))
                .flatMap { sendMessage("added_directory", session, AddedDirectoryEvent(value.name)) }
    }

    private fun constructDirectory(serverId: UUID, dto: DirectoryDto): DirectoryEntity {
        return DirectoryEntity(serverId, dto.name, dto.root, dto.date)
    }
}