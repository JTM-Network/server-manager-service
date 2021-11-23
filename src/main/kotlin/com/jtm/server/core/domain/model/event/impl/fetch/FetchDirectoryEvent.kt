package com.jtm.server.core.domain.model.event.impl.fetch

import com.jtm.server.core.domain.dto.DirectoryDto
import java.util.*

data class FetchDirectoryEvent(val name: String, val directory: DirectoryDto)