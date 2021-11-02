package com.jtm.server.core.domain.model.event.impl

import java.util.*

data class ConnectResponseEvent(val serverId: UUID, val sessionId: String)