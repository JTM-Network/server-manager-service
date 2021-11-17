package com.jtm.server.core.domain.entity

import com.jtm.server.core.domain.dto.DirectoryDto
import com.jtm.server.core.domain.model.File
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("directories")
data class Directory(@Id val serverId: UUID, val name: String, val directories: MutableList<Directory> = mutableListOf(), val files: MutableList<File> = mutableListOf()) {

    constructor(serverId: UUID, dto: DirectoryDto): this(serverId = serverId, name = dto.name, directories = dto.directories, files = dto.files)

    fun findDirectory(path: String): Directory {
        val folders = path.split("/")
        var dir = this
        for (folder in folders) dir = dir.getDirectory(folder)
        return dir
    }

    fun getDirectory(name: String): Directory {
        return directories.first { it.name == name }
    }
}