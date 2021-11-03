package com.jtm.server.core.usecase.repository

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Sinks
import java.util.*
import kotlin.collections.HashMap

@Component
class LogRepository {

    private val logger = LoggerFactory.getLogger(LogRepository::class.java)
    private val logs: MutableMap<UUID, Sinks.Many<String>> = HashMap()

    fun addLog(id: UUID) {
        logs[id] = Sinks.many().replay().all()
    }

    fun exists(id: UUID): Boolean {
        return logs.containsKey(id)
    }

    fun getLog(id: UUID): Sinks.Many<String>? {
        return logs[id]
    }

    fun removeLog(id: UUID) {
        val log = getLog(id) ?: return
        log.tryEmitComplete()
        logs.remove(id)
    }

    fun sendLogMessage(id: UUID, message: String) {
        val sink = getLog(id) ?: return
        val result = sink.tryEmitNext(message)
    }
}