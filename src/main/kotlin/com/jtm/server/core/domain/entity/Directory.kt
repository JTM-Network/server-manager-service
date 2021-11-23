package com.jtm.server.core.domain.entity

import com.jtm.server.core.domain.model.directory.File

data class Directory(val name: String, val directories: MutableList<Directory> = mutableListOf(), val files: MutableList<File> = mutableListOf(), val folders: Int = 0, val filesLength: Int = 0, val date: Long) {

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