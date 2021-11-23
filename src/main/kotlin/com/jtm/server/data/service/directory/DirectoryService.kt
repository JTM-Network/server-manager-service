package com.jtm.server.data.service.directory

import com.jtm.server.core.domain.entity.Directory
import com.jtm.server.core.domain.entity.DirectoryEntity
import com.jtm.server.core.domain.exceptions.DirectoryNotFound
import com.jtm.server.core.usecase.repository.DirectoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Service
class DirectoryService @Autowired constructor(private val directoryRepository: DirectoryRepository) {

    fun addDirectory(dir: DirectoryEntity): Mono<DirectoryEntity> {
        return directoryRepository.findById(dir.serverId)
                .switchIfEmpty(Mono.defer { directoryRepository.save(dir) })
    }

    fun getDirectory(serverId: UUID): Mono<DirectoryEntity> {
        return directoryRepository.findById(serverId)
                .switchIfEmpty(Mono.defer { Mono.error(DirectoryNotFound()) })
    }

    fun getDirectoryPath(serverId: UUID, path: String): Mono<Directory> {
        return directoryRepository.findById(serverId)
                .switchIfEmpty(Mono.defer { Mono.error(DirectoryNotFound()) })
                .flatMap {
                    val dir = it.dir.findDirectory(path) ?: return@flatMap Mono.error { DirectoryNotFound() }
                    return@flatMap Mono.just(dir)
                }
    }

    fun getDirectories(): Flux<DirectoryEntity> {
        return directoryRepository.findAll()
    }

    fun removeDirectory(serverId: UUID): Mono<DirectoryEntity> {
        return directoryRepository.findById(serverId)
                .switchIfEmpty(Mono.defer { Mono.error(DirectoryNotFound()) })
                .flatMap { directoryRepository.delete(it).thenReturn(it) }
    }
}