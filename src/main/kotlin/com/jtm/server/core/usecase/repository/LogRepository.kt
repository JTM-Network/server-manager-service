package com.jtm.server.core.usecase.repository

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Sinks

@Component
class LogRepository {

    private val logger = LoggerFactory.getLogger(LogRepository::class.java)
    private val logs: MutableMap<String, Sinks.Many<String>> = HashMap()

    fun addLog(id: String) {
        logs[id] = Sinks.many().replay().latest()
    }

    fun exists(id: String): Boolean {
        return logs.containsKey(id)
    }

    fun getLog(id: String): Sinks.Many<String>? {
        return logs[id]
    }

    fun removeLog(id: String) {
        logs.remove(id)
    }

    fun sendLogMessage(id: String, message: String) {
        val sink = getLog(id) ?: return
        val result = sink.tryEmitNext(message)
    }
}