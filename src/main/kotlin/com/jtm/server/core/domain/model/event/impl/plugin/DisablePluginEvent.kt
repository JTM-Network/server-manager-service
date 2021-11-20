package com.jtm.server.core.domain.model.event.impl.plugin

import java.util.*

data class DisablePluginEvent(val serverId: UUID, val name: String)
