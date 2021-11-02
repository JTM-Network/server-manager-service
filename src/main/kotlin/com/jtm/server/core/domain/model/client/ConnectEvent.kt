package com.jtm.server.core.domain.model.client

import com.jtm.server.core.domain.dto.ServerInfoDto
import com.jtm.server.core.domain.entity.ServerInfo
import java.util.*

data class ConnectEvent(val token: String, val serverId: UUID?, val info: ServerInfoDto = ServerInfoDto())