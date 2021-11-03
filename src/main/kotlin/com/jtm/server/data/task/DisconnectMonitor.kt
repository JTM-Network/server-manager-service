package com.jtm.server.data.task

import com.jtm.server.core.usecase.repository.SessionRepository
import com.jtm.server.data.service.ServerService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import java.util.concurrent.Executor

@Component
class DisconnectMonitor @Autowired constructor(@Qualifier("coreExecutor") coreExecutor: Executor, private val sessionRepository: SessionRepository, private val serverService: ServerService): Runnable {

    private val logger = LoggerFactory.getLogger(DisconnectMonitor::class.java)

    init {
        coreExecutor.execute(this)
    }

    override fun run() {
        logger.info("Monitoring disconnections.")
        while (true) {
            serverService.getInfos()
                    .filter { it.isConnected && (!sessionRepository.exists(it.id)) }
                    .flatMap {
                        logger.info("Confirming disconnection: ${it.id}")
                        serverService.disconnected(it.id)
                    }
        }
    }
}