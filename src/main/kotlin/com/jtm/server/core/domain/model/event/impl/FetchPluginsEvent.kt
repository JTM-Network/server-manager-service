package com.jtm.server.core.domain.model.event.impl

import com.jtm.server.core.domain.model.plugin.Plugin
import java.util.*

data class FetchPluginsEvent(val serverId: UUID, val plugins: MutableList<Plugin>)