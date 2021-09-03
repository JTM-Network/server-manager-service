package com.jtm.server.data.service

import com.jtm.server.core.domain.exceptions.LogsNotFound
import com.jtm.server.core.usecase.repository.LogRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.codec.ServerSentEvent
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class LogService @Autowired constructor(private val logRepository: LogRepository) {

    fun getLogs(id: String): Flux<ServerSentEvent<String>> {
        val logs = logRepository.getLog(id) ?: return Flux.error { LogsNotFound() }
        return logs.asFlux().map { ServerSentEvent.builder(it).build() }
    }
}