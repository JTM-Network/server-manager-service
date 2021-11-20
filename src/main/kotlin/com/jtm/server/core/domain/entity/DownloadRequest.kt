package com.jtm.server.core.domain.entity

import com.jtm.server.core.domain.constants.DownloadStatus
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("directory_download_request")
data class DownloadRequest(@Id val id: UUID = UUID.randomUUID(), val serverId: UUID, val status: DownloadStatus, val requested: Long = System.currentTimeMillis())