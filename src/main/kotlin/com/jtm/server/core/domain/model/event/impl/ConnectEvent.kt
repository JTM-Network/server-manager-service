package com.jtm.server.core.domain.model.event.impl

import com.jtm.server.core.domain.model.client.server.ServerInfo
import java.util.*

data class ConnectEvent(val token: String, val serverId: UUID?, val info: ServerInfo = ServerInfo())