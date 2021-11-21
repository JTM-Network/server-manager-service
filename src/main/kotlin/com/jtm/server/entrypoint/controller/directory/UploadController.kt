package com.jtm.server.entrypoint.controller.directory

import com.jtm.server.core.domain.entity.UploadRequest
import com.jtm.server.data.service.directory.UploadService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping("/dir/upload")
class UploadController @Autowired constructor(private val uploadService: UploadService) {

    @PostMapping("/{serverId}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun upload(@PathVariable serverId: UUID, @RequestPart("file") file: FilePart, @RequestPart("path") path: String): Mono<UploadRequest> {
        return uploadService.upload(serverId, path, file)
    }

    @GetMapping("/{id}/dl")
    fun download(@PathVariable id: UUID, response: ServerHttpResponse): Mono<Resource> {
        return uploadService.download(id, response)
    }
}