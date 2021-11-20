package com.jtm.server.core.usecase.sink

import com.jtm.server.core.domain.exceptions.DownloadRequestNotFound
import com.jtm.server.core.domain.model.ServerUploadStatus
import org.slf4j.LoggerFactory
import org.springframework.http.codec.ServerSentEvent
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import reactor.util.concurrent.Queues
import java.util.*
import kotlin.collections.HashMap

@Component
class DownloadSink {

    private val logger = LoggerFactory.getLogger(DownloadSink::class.java)
    private val serverUploadSink: MutableMap<UUID, Sinks.Many<ServerUploadStatus>> = HashMap()

    fun addDownloads(id: UUID) {
        serverUploadSink[id] = Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false)
    }

    fun getDownloads(id: UUID): Flux<ServerSentEvent<ServerUploadStatus>> {
        val sink = serverUploadSink[id] ?: return Flux.error(DownloadRequestNotFound())
        return sink.asFlux().map { ServerSentEvent.builder(it).build() }
    }

    fun sendMessage(status: ServerUploadStatus) {
        val sink = serverUploadSink[status.serverId] ?: return
        val result = sink.tryEmitNext(status) ?: return
        if (result.isFailure) logger.error("Failed to send message.")
    }
}