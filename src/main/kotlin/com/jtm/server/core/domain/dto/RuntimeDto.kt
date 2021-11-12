package com.jtm.server.core.domain.dto

import com.jtm.server.core.domain.constants.TimeType
import java.lang.management.MemoryUsage

data class RuntimeDto(val id: Long, val cpuUsage: Double, val memoryUsage: Long, val maxMemory: Long, val time: TimeType)
