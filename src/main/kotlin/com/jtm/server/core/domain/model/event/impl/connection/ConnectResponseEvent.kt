package com.jtm.server.core.domain.model.event.impl.connection

import java.util.*

data class ConnectResponseEvent(val serverId: UUID, val sessionId: String)