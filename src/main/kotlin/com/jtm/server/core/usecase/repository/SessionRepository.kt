package com.jtm.server.core.usecase.repository

import com.jtm.server.core.domain.model.socket.SocketSession
import org.springframework.stereotype.Component
import java.util.*
import kotlin.collections.HashMap

@Component
class SessionRepository {

    private val sessions: MutableMap<String, SocketSession> = HashMap()

    fun addSession(session: SocketSession) {
        sessions[session.session.id] = session
    }

    fun exists(id: String): Boolean {
        return sessions.containsKey(id)
    }

    fun getSession(id: String): SocketSession? {
        return sessions[id]
    }

    fun getSessionsByAccount(id: UUID): List<SocketSession> {
        return sessions.values.filter { it.accountId != id }
    }

    fun getSessions(): List<SocketSession> {
        return sessions.values.toList()
    }

    fun removeSession(id: String) {
        sessions.remove(id)
    }
}