package com.jtm.server.entrypoint.controller.directory

import com.jtm.server.core.domain.dto.DownloadRequestDto
import com.jtm.server.core.domain.entity.DownloadRequest
import com.jtm.server.core.domain.model.ServerUploadStatus
import com.jtm.server.data.service.SessionService
import com.jtm.server.data.service.directory.DownloadService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerSentEvent
import org.springframework.http.codec.multipart.FilePart
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping("/dir/download")
class DownloadController @Autowired constructor(private val downloadService: DownloadService, private val sessionService: SessionService) {

    @PostMapping("/request")
    fun addRequest(@RequestBody dto: DownloadRequestDto): Mono<DownloadRequest> {
        return downloadService.addRequest(dto, sessionService)
    }

    @GetMapping("/request/{id}/stream", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getRequestStream(@PathVariable id: UUID): Flux<ServerSentEvent<DownloadRequest>> {
        return downloadService.getRequestStream(id)
    }

    @PostMapping("/upload", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadRequest(@RequestPart("file") file: FilePart, @RequestPart("id") id: String): Mono<Void> {
        return downloadService.uploadRequestFile(UUID.fromString(id), file)
    }

    @GetMapping("/{id}/dl")
    fun getDownload(@PathVariable id: UUID, response: ServerHttpResponse): Mono<Resource> {
        return downloadService.getDownload(id, response)
    }

    @GetMapping("/clear")
    fun clearDownloads(): Mono<Void> {
        return downloadService.clearDownloads()
    }

    @GetMapping("/{id}")
    fun getRequest(@PathVariable id: UUID): Mono<DownloadRequest> {
         return downloadService.getRequest(id)
    }

    @GetMapping("/all")
    fun getRequests(): Flux<DownloadRequest> {
        return downloadService.getRequests()
    }

    @DeleteMapping("/{id}")
    fun deleteRequest(@PathVariable id: UUID): Mono<DownloadRequest> {
        return downloadService.deleteRequest(id)
    }
}