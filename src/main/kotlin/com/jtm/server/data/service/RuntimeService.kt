package com.jtm.server.data.service

import com.jtm.server.core.domain.constants.TimeType
import com.jtm.server.core.domain.entity.RuntimeStats
import com.jtm.server.core.domain.exceptions.RuntimeNotFound
import com.jtm.server.core.usecase.repository.RuntimeStatsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Service
class RuntimeService @Autowired constructor(private val runtimeStatsRepository: RuntimeStatsRepository) {

    fun getRuntime(id: Long): Mono<RuntimeStats> {
        return runtimeStatsRepository.findById(id)
                .switchIfEmpty(Mono.defer { Mono.error(RuntimeNotFound()) })
    }

    fun getRuntimesByServerId(serverId: UUID): Flux<RuntimeStats> {
        return runtimeStatsRepository.findByServerId(serverId)
    }

    fun getRuntimesByServerIdAndTime(serverId: UUID, time: TimeType): Flux<RuntimeStats> {
        return runtimeStatsRepository.findByServerIdAndTime(serverId, time)
    }

    fun deleteRuntime(id: Long): Mono<RuntimeStats> {
        return runtimeStatsRepository.findById(id)
                .switchIfEmpty(Mono.defer { Mono.error(RuntimeNotFound()) })
                .flatMap { runtimeStatsRepository.delete(it).thenReturn(it) }
    }
}