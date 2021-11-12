package com.jtm.server.core.usecase.repository

import com.jtm.server.core.domain.constants.TimeType
import com.jtm.server.core.domain.entity.RuntimeStats
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.util.*

@Repository
interface RuntimeStatsRepository: ReactiveMongoRepository<RuntimeStats, Long> {

    fun findByServerId(serverId: UUID): Flux<RuntimeStats>

    fun findByServerIdAndTime(serverId: UUID, time: TimeType): Flux<RuntimeStats>
}