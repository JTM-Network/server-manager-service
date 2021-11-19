package com.jtm.server.core.domain.model

data class File(val name: String, val size: Long, val lastModified: Long, val format: String, val path: String)