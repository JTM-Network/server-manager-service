package com.jtm.server.entrypoint.controller

import com.jtm.server.core.domain.constants.TimeType
import com.jtm.server.core.domain.entity.RuntimeStats
import com.jtm.server.data.service.RuntimeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping("/runtime")
class RuntimeController @Autowired constructor(private val runtimeService: RuntimeService) {

    @GetMapping("/{id}")
    fun getRuntime(@PathVariable id: Long): Mono<RuntimeStats> {
        return runtimeService.getRuntime(id)
    }

    @GetMapping("/server/{serverId}")
    fun getRuntimesByServerId(@PathVariable serverId: UUID): Flux<RuntimeStats> {
        return runtimeService.getRuntimesByServerId(serverId)
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