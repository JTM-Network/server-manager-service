package com.jtm.server.core.domain.model.client

import com.jtm.server.core.domain.model.server.ServerInfo

data class ConnectEvent(val info: ServerInfo = ServerInfo())