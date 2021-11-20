package com.jtm.server.data.service.directory

import com.jtm.server.core.domain.entity.DownloadRequest
import com.jtm.server.core.usecase.repository.DownloadRequestRepository
import com.jtm.server.core.usecase.sink.DownloadSink
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class DownloadService @Autowired constructor(private val requestRepository: DownloadRequestRepository, private val downloadSink: DownloadSink) {

    fun addRequest(): Mono<DownloadRequest> {
        return Mono.empty()
    }

    fun getRequest(): Mono<DownloadRequest> {
        return Mono.empty()
    }

    fun getRequests(): Flux<DownloadRequest> {
        return Flux.empty()
    }

    fun deleteRequest(): Mono<DownloadRequest> {
        return Mono.empty()
    }
}