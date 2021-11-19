package com.jtm.server.core.domain.model.directory

data class File(val name: String, val size: Long, val lastModified: Long, val format: String)