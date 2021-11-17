package com.jtm.server.core.domain.entity

import com.jtm.server.ServerManagerService
import com.jtm.server.core.domain.dto.DirectoryDto
import com.jtm.server.core.domain.model.File
import org.slf4j.LoggerFactory
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("directories")
data class Directory(@Id val serverId: UUID, val name: String, val directories: MutableList<Directory> = mutableListOf(), val files: MutableList<File> = mutableListOf()) {

    private val logger = LoggerFactory.getLogger(ServerManagerService::class.java)

    constructor(serverId: UUID, dto: DirectoryDto): this(serverId = serverId, name = dto.name, directories = dto.directories, files = dto.files)

    fun findDirectory(path: String): Directory {
        val folders = path.split("/")
        var dir = this
        for (folder in folders) {
            if (folder.isNotBlank()) {
                logger.info("Folder: $folder")
                dir = dir.getDirectory(folder)
            }
        }
        return dir
    }

    fun getDirectory(name: String): Directory {
        return directories.first { it.name == name }
    }
}