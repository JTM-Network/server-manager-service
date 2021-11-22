package com.jtm.server.core.domain.entity

import com.jtm.server.core.domain.dto.DirectoryDto
import com.jtm.server.core.domain.model.directory.DirectoryInfo
import com.jtm.server.core.domain.model.directory.File
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("directories")
data class Directory(@Id val serverId: UUID, val name: String, val directories: MutableList<Directory> = mutableListOf(), val files: MutableList<File> = mutableListOf(), val folders: Int = 0, val filesLength: Int = 0) {

    constructor(serverId: UUID, dto: DirectoryDto): this(serverId = serverId, name = dto.name)

    fun findDirectory(path: String): Directory? {
        val folders = path.split("/")
        var dir = this
        for (folder in folders) if (folder.isNotBlank()) dir = dir.getDirectory(folder) ?: return null
        return dir
    }

    fun getDirectory(name: String): Directory? {
        return directories.firstOrNull { it.name == name }
    }
}