package com.jtm.server.core.domain.dto

import com.jtm.server.core.domain.model.Directory
import com.jtm.server.core.domain.model.File

data class DirectoryDto(val name: String, val directories: Array<Directory>, val files: Array<File>)