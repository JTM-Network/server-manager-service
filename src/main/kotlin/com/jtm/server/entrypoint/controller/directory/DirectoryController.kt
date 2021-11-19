package com.jtm.server.entrypoint.controller.directory

import com.jtm.server.core.domain.entity.Directory
import com.jtm.server.data.service.directory.DirectoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping("/dir")
class DirectoryController @Autowired constructor(private val directoryService: DirectoryService) {

    @GetMapping("/{id}")
    fun getDirectory(@PathVariable id: UUID, @RequestParam("path", required = false) path: String?): Mono<Directory> {
        return if (path != null) directoryService.getDirectoryPath(id, path) else directoryService.getDirectory(id)
    }

    @GetMapping("/all")
    fun getDirectories(): Flux<Directory> {
        return directoryService.getDirectories()
    }

    @DeleteMapping("/{id}")
    fun deleteDirectory(@PathVariable id: UUID): Mono<Directory> {
        return directoryService.removeDirectory(id)
    }
}