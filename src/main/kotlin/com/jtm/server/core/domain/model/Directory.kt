package com.jtm.server.core.domain.model

import com.jtm.server.core.domain.dto.DirectoryDto
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("directories")
data class Directory(@Id val serverId: UUID, val name: String, val directories: Array<Directory> = arrayOf(), val files: Array<File> = arrayOf()) {

    constructor(serverId: UUID, dto: DirectoryDto): this(serverId = serverId, name = dto.name, directories = dto.directories, files = dto.files)

    fun addDirectory(dir: Directory): Directory {
        this.directories[directories.size] = dir
        return this
    }

    fun addFiles(file: File): Directory {
        this.files[files.size] = file
        return this
    }
}