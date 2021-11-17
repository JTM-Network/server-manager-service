package com.jtm.server.core.domain.dto

import com.jtm.server.core.domain.entity.Directory
import com.jtm.server.core.domain.model.File

data class DirectoryDto(val name: String, val directories: MutableList<Directory>, val files: MutableList<File>)