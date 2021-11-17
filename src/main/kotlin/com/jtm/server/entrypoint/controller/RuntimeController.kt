package com.jtm.server.entrypoint.controller

import com.jtm.server.core.domain.constants.TimeType
import com.jtm.server.core.domain.entity.RuntimeStats
import com.jtm.server.core.usecase.sink.RuntimeSink
import com.jtm.server.data.service.RuntimeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping("/runtime")
class RuntimeController @Autowired constructor(private val runtimeService: RuntimeService, private val runtimeSink: RuntimeSink) {

    @GetMapping("/{id}")
    fun getRuntime(@PathVariable id: Long): Mono<RuntimeStats> {
        return runtimeService.getRuntime(id)
    }

    @GetMapping("/server/{serverId}")
    fun getRuntimesByServerId(@PathVariable serverId: UUID): Flux<RuntimeStats> {
        return runtimeService.getRuntimesByServerId(serverId)
    }

    @GetMapping("/server/{serverId}/latest")
    fun getRuntimeLatest(@PathVariable serverId: UUID): Mono<RuntimeStats> {
        return runtimeService.getRuntimeLatest(serverId)
    }

    @GetMapping("/server/{serverId}/stream", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getRuntimeStream(@PathVariable serverId: UUID): Flux<ServerSentEvent<RuntimeStats>> {
        return runtimeSink.stream(serverId)
    }

    @GetMapping("/server/{serverId}/{time}")
    fun getRuntimesByServerIdAndTime(@PathVariable serverId: UUID, @PathVariable time: TimeType): Flux<RuntimeStats> {
        return runtimeService.getRuntimesByServerIdAndTime(serverId, time)
    }

    @DeleteMapping("/{id}")
    fun deleteRuntime(@PathVariable id: Long): Mono<RuntimeStats> {
        return runtimeService.deleteRuntime(id)
    }
}