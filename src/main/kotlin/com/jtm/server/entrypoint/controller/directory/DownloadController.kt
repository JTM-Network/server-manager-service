package com.jtm.server.entrypoint.controller.directory

import com.jtm.server.core.domain.dto.DownloadRequestDto
import com.jtm.server.core.domain.entity.DownloadRequest
import com.jtm.server.core.domain.model.ServerUploadStatus
import com.jtm.server.data.service.directory.DownloadService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerSentEvent
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping("/dir/download")
class DownloadController @Autowired constructor(private val downloadService: DownloadService) {

    @PostMapping("/request")
    fun addRequest(@RequestBody dto: DownloadRequestDto): Flux<ServerSentEvent<ServerUploadStatus>> {
        return downloadService.addRequest(dto)
    }

    @PostMapping("/upload", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadRequest(@RequestPart("file") file: FilePart, @RequestPart("id") id: String): Mono<Void> {
        return downloadService.uploadRequestFile(UUID.fromString(id), file)
    }

    @GetMapping("/{id}/clear")
    fun clearDownloads(@PathVariable id: UUID): Mono<Void> {
        return downloadService.clearDownloads(id)
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