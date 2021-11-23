package com.jtm.server.core.domain.dto

import com.jtm.server.core.domain.entity.Directory
import com.jtm.server.core.domain.model.directory.DirectoryInfo
import com.jtm.server.core.domain.model.directory.File

data class DirectoryDto(val name: String, val serverId: String, val root: Directory, val date: Long)