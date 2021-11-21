package com.jtm.server.core.domain.model.event.impl.plugin

import java.util.*

data class DownloadRequestEvent(val downloadId: UUID, val serverId: UUID, val path: String)
