package com.jtm.server.core.domain.model.client

import java.util.*

data class ConnectResponseEvent(val serverId: UUID, val sessionId: String)