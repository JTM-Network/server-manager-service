package com.jtm.server.core.domain.entity

import com.jtm.server.core.domain.constants.DownloadStatus
import com.jtm.server.core.domain.dto.DownloadRequestDto
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("directory_download_request")
data class DownloadRequest(@Id val id: UUID = UUID.randomUUID(), val serverId: UUID, val path: String, var file: String, var status: DownloadStatus = DownloadStatus.REQUESTED, val requested: Long = System.currentTimeMillis()) {

    constructor(dto: DownloadRequestDto): this(serverId = dto.serverId, path = dto.path, file = dto.name)

    fun updateStatus(status: DownloadStatus): DownloadRequest {
        this.status = status
        return this
    }
}