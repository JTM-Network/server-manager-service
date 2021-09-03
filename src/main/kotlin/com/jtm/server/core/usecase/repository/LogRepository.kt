package com.jtm.server.core.usecase.repository

import org.springframework.stereotype.Component
import reactor.core.publisher.Sinks

@Component
class LogRepository {

    private val logs: MutableMap<String, Sinks.Many<String>> = HashMap()

    fun addLog(id: String) {
        logs[id] = Sinks.many().replay().latest()
    }

    fun getLog(id: String): Sinks.Many<String>? {
        return logs[id]
    }

    fun removeLog(id: String) {
        logs.remove(id)
    }
}