package com.jtm.server.core.usecase.sink

import com.jtm.server.core.domain.model.ServerUploadStatus
import org.springframework.http.codec.ServerSentEvent
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import java.util.*
import kotlin.collections.HashMap

@Component
class DownloadSink {

    private val serverUploadSink: Map<UUID, Sinks.Many<ServerUploadStatus>> = HashMap()

    fun addDownloads() {

    }

    fun getDownloads(): Flux<ServerSentEvent<ServerUploadStatus>> {
        return Flux.empty()
    }
}