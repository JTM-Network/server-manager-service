package com.jtm.server.core.domain.entity

import com.jtm.server.core.domain.dto.DirectoryDto
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("directories")
data class DirectoryEntity(@Id val serverId: UUID, val name: String, val dir: Directory, val date: Long) {

    constructor(dto: DirectoryDto): this(UUID.fromString(dto.serverId), dto.name, dto.root, dto.date)
}