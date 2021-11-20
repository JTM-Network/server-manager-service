package com.jtm.server.core.domain.dto

import java.util.*

data class DownloadRequestDto(val serverId: UUID, val path: String, val name: String)