package com.jtm.server.data.service.directory

import com.jtm.server.core.domain.constants.UploadStatus
import com.jtm.server.core.domain.entity.UploadRequest
import com.jtm.server.core.domain.exceptions.FileNotFound
import com.jtm.server.core.domain.model.event.impl.plugin.DownloadRequestEvent
import com.jtm.server.core.usecase.FileHandler
import com.jtm.server.core.usecase.repository.UploadRequestRepository
import com.jtm.server.data.service.SessionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.codec.multipart.FilePart
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*

@Service
class UploadService @Autowired constructor(private val uploadRequestRepository: UploadRequestRepository, private val fileHandler: FileHandler, private val sessionService: SessionService) {

    fun upload(serverId: UUID, path: String, file: FilePart): Mono<UploadRequest> {
        return uploadRequestRepository.save(UploadRequest(serverId = serverId, path = path, file = file.filename()))
                .flatMap { uploadRequest ->
                    fileHandler.saveUpload(uploadRequest.id.toString(), uploadRequest.file, file)
                            .flatMap { sessionService.getSession(serverId) }
                            .flatMap { it.sendEvent("download_request", DownloadRequestEvent(uploadRequest.id, serverId, path)) }
                            .thenReturn(uploadRequest)
                }
    }

    fun download(id: UUID, response: ServerHttpResponse): Mono<Resource> {
        return uploadRequestRepository.findById(id)
                .flatMap { request ->
                    fileHandler.fetchUpload("${request.id}/${request.file}")
                            .flatMap { file ->
                                if (!file.exists()) return@flatMap Mono.error(FileNotFound())
                                response.headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${file.name}")
                                return@flatMap Mono.just(FileSystemResource(file))
                            }
                            .flatMap {
                                uploadRequestRepository.save(request.updateStatus(UploadStatus.COMPLETED))
                                        .thenReturn(it)
                            }
                }
    }
}