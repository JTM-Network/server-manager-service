package com.jtm.server.core.usecase.sink

import com.jtm.server.core.domain.entity.DownloadRequest
import com.jtm.server.core.domain.exceptions.DownloadRequestNotFound
import org.slf4j.LoggerFactory
import org.springframework.http.codec.ServerSentEvent
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import reactor.util.concurrent.Queues
import java.util.*
import kotlin.collections.HashMap

@Component
class RequestSink {

    private val logger = LoggerFactory.getLogger(RequestSink::class.java)
    private val requestSinks: MutableMap<UUID, Sinks.Many<DownloadRequest>> = HashMap()

    fun addRequest(id: UUID): Sinks.Many<DownloadRequest> {
        val sink: Sinks.Many<DownloadRequest> = Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false)
        requestSinks[id] = sink
        return sink
    }

    fun getRequestUpdates(id: UUID): Flux<ServerSentEvent<DownloadRequest>> {
        val sink = requestSinks[id] ?: return Flux.error(DownloadRequestNotFound())
        return sink.asFlux().map { ServerSentEvent.builder(it).build() }
    }

    fun sendMessage(request: DownloadRequest) {
        logger.info("Sending message of request.")
        val sink = requestSinks[request.id] ?: addRequest(request.id)
        val result = sink.tryEmitNext(request)
        if (result.isFailure) logger.error("Failed to send message: ${result.name}")
    }
}