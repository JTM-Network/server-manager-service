package com.jtm.server.entrypoint.controller.directory

import com.jtm.server.core.domain.entity.DownloadRequest
import com.jtm.server.data.service.directory.DownloadService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping("/dir/download")
class DownloadController @Autowired constructor(private val downloadService: DownloadService) {

    @PostMapping("/request")
    fun addRequest(): Mono<DownloadRequest> {
        return Mono.empty()
    }

    @GetMapping("/{id}")
    fun getRequest(@PathVariable id: UUID): Mono<DownloadRequest> {
         return Mono.empty()
    }

    @GetMapping("/all")
    fun getRequests(): Flux<DownloadRequest> {
        return Flux.empty()
    }

    @DeleteMapping("/{id}")
    fun deleteRequest(@PathVariable id: UUID): Mono<DownloadRequest> {
        return Mono.empty()
    }
}