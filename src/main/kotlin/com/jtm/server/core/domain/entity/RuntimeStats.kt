package com.jtm.server.core.domain.entity

import com.jtm.server.core.domain.constants.TimeType
import com.jtm.server.core.domain.dto.RuntimeDto
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("runtime_stats")
data class RuntimeStats(@Id val id: Long, val serverId: UUID, val cpuUsage: Double, val memoryUsage: Long, val maxMemory: Long, val time: TimeType) {

    constructor(serverId: UUID, dto: RuntimeDto): this(id = dto.id, serverId = serverId, cpuUsage = dto.cpuUsage, memoryUsage = dto.memoryUsage, maxMemory = dto.maxMemory, time = dto.time)


}