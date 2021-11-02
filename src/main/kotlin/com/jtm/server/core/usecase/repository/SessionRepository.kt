package com.jtm.server.core.usecase.repository

import com.jtm.server.core.domain.model.socket.SocketSession
import org.springframework.stereotype.Component
import java.util.*
import kotlin.collections.HashMap

@Component
class SessionRepository {

    private val sessions: MutableMap<UUID, SocketSession> = HashMap()

    fun addSession(id: UUID, session: SocketSession) {
        sessions[id] = session
    }

    fun exists(id: UUID): Boolean {
        return sessions.containsKey(id)
    }

    fun getSession(id: UUID): SocketSession? {
        return sessions[id]
    }

    fun getSessionsByAccount(id: UUID): List<SocketSession> {
        return sessions.values.filter { it.accountId == id }
    }

    fun getSessions(): List<SocketSession> {
        return sessions.values.toList()
    }

    fun removeSession(id: UUID) {
        sessions.remove(id)
    }

    fun removeSessionId(id: String) {
        val session = sessions.values.firstOrNull { it.session.id == id } ?: return
        sessions.remove(session.id)
    }
}