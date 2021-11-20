package com.jtm.server.core.domain.model.event.impl.plugin

import java.util.*

data class FileRequestEvent(val id: UUID, val path: String)