package com.jtm.server.core.domain.model.event.impl

import com.jtm.server.core.domain.dto.RuntimeDto
import java.util.*

data class RuntimeEntryEvent(val serverId: UUID, val runtime: RuntimeDto)