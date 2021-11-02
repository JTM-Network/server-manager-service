package com.jtm.server.core.domain.model.socket

import org.springframework.web.reactive.socket.WebSocketSession
import java.util.*

data class SocketSession(val id: UUID, val accountId: UUID, val session: WebSocketSession)