package com.jtm.server.data.service

import com.jtm.server.core.domain.exceptions.DirectoryFound
import com.jtm.server.core.domain.exceptions.DirectoryNotFound
import com.jtm.server.core.domain.model.Directory
import com.jtm.server.core.usecase.repository.DirectoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Service
class DirectoryService @Autowired constructor(private val directoryRepository: DirectoryRepository) {

    fun addDirectory(dir: Directory): Mono<Directory> {
        return directoryRepository.findById(dir.serverId)
                .flatMap<Directory?> { Mono.defer { Mono.error(DirectoryFound()) } }
                .switchIfEmpty(Mono.defer { directoryRepository.save(dir) })
    }

    fun getDirectory(serverId: UUID): Mono<Directory> {
        return directoryRepository.findById(serverId)
                .switchIfEmpty(Mono.defer { Mono.error(DirectoryNotFound()) })
    }

    fun getDirectories(): Flux<Directory> {
        return directoryRepository.findAll()
    }

    fun removeDirectory(serverId: UUID): Mono<Directory> {
        return directoryRepository.findById(serverId)
                .switchIfEmpty(Mono.defer { Mono.error(DirectoryNotFound()) })
                .flatMap { directoryRepository.delete(it).thenReturn(it) }
    }
}