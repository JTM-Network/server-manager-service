package com.jtm.server.core.domain.entity

import com.jtm.server.core.domain.constants.UploadStatus
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("upload_request")
data class UploadRequest(@Id val id: UUID = UUID.randomUUID(), val serverId: UUID, val path: String, val file: String, var status: UploadStatus = UploadStatus.REQUESTED, val requested: Long = System.currentTimeMillis()) {

    fun updateStatus(status: UploadStatus): UploadRequest {
        this.status = status
        return this
    }
}