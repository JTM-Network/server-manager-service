package com.jtm.server.core.domain.model.directory

data class FileInfo(val size: Long, val lastModified: Long, val path: String, val format: String)
