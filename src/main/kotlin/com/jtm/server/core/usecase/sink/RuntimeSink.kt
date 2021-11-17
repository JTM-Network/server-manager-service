package com.jtm.server.core.usecase.sink

import com.jtm.server.core.domain.entity.RuntimeStats
import org.slf4j.LoggerFactory
import org.springframework.http.codec.ServerSentEvent
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import reactor.util.concurrent.Queues
import java.util.*
import kotlin.collections.HashMap

@Component
class RuntimeSink {

    private val logger = LoggerFactory.getLogger(RuntimeSink::class.java)
    private val sinks: MutableMap<UUID, Sinks.Many<RuntimeStats>> = HashMap()

    fun addSink(id: UUID): Sinks.Many<RuntimeStats> {
        val sink: Sinks.Many<RuntimeStats> = Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false)
        sinks[id] = sink
        return sink
    }

    fun stream(serverId: UUID): Flux<ServerSentEvent<RuntimeStats>> {
        val sink = sinks[serverId] ?: return Flux.empty()
        return sink.asFlux().map { ServerSentEvent.builder(it).build() }
    }

    fun sendMessage(stats: RuntimeStats) {
        val sink = sinks[stats.serverId] ?: addSink(stats.serverId)
        val result = sink.tryEmitNext(stats)
        if (result.isFailure) logger.error("Failed to send message to sink: ${result.name}")
    }
}