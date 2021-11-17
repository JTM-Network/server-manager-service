package com.jtm.server.core.domain.model.event.impl

import com.jtm.server.core.domain.dto.DirectoryDto
import java.util.*

data class FetchDirectoryEvent(val name: String, val serverId: UUID, val directory: DirectoryDto)