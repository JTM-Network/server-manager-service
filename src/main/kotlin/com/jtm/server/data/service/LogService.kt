package com.jtm.server.data.service

import com.jtm.server.core.domain.exceptions.LogsNotFound
import com.jtm.server.core.domain.exceptions.SessionNotFound
import com.jtm.server.core.usecase.repository.LogRepository
import com.jtm.server.core.usecase.repository.SessionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.codec.ServerSentEvent
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.util.*

@Service
class LogService @Autowired constructor(private val logRepository: LogRepository, private val sessionRepository: SessionRepository) {

    fun getLogs(id: UUID): Flux<ServerSentEvent<String>> {
//        val session = sessionRepository.getSession(id) ?: return Flux.error(SessionNotFound())
        val logs = logRepository.getLog(id) ?: return Flux.error { LogsNotFound() }
        return logs.asFlux().map { ServerSentEvent.builder(it).build() }
    }
}