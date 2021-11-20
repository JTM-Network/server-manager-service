package com.jtm.server.data.service.directory

import com.jtm.server.core.domain.constants.DownloadStatus
import com.jtm.server.core.domain.dto.DownloadRequestDto
import com.jtm.server.core.domain.entity.DownloadRequest
import com.jtm.server.core.domain.exceptions.DownloadRequestNotFound
import com.jtm.server.core.domain.model.ServerUploadStatus
import com.jtm.server.core.domain.model.event.impl.plugin.FileRequestEvent
import com.jtm.server.core.usecase.FileHandler
import com.jtm.server.core.usecase.repository.DownloadRequestRepository
import com.jtm.server.core.usecase.sink.DownloadSink
import com.jtm.server.core.usecase.sink.RequestSink
import com.jtm.server.data.service.SessionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.codec.ServerSentEvent
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Service
class DownloadService @Autowired constructor(private val sessionService: SessionService, private val requestRepository: DownloadRequestRepository, private val requestSink: RequestSink, private val fileHandler: FileHandler) {

    fun addRequest(dto: DownloadRequestDto): Flux<ServerSentEvent<DownloadRequest>> {
        return requestRepository.save(DownloadRequest(dto))
                .flatMap { sessionService.getSession(dto.serverId)
                        .flatMap { server -> server.sendEvent("file_request", FileRequestEvent(it.id, dto.path)) }
                        .thenReturn(it)
                }
                .flatMapMany {
                    requestSink.addRequest(it.id)
                    requestSink.getRequestUpdates(it.id)
                }
    }

    fun uploadRequestFile(id: UUID, file: FilePart): Mono<Void> {
        return requestRepository.findById(id)
                .flatMap { request -> fileHandler.save(request.id.toString(), request.file, file)
                        .flatMap { requestRepository.save(request.updateStatus(DownloadStatus.SERVER_UPLOAD)).then() }
                }

    }

    fun clearDownloads(id: UUID): Mono<Void> {
        return fileHandler.clearDownloads(id)
    }

    fun getRequest(id: UUID): Mono<DownloadRequest> {
        return requestRepository.findById(id)
                .switchIfEmpty(Mono.defer { Mono.error(DownloadRequestNotFound()) })
    }

    fun getRequests(): Flux<DownloadRequest> {
        return requestRepository.findAll()
    }

    fun deleteRequest(id: UUID): Mono<DownloadRequest> {
        return requestRepository.findById(id)
                .switchIfEmpty(Mono.defer { Mono.error(DownloadRequestNotFound()) })
                .flatMap { requestRepository.delete(it).thenReturn(it) }
    }
}