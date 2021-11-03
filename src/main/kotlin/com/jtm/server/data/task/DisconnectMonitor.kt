package com.jtm.server.data.task

import com.jtm.server.core.usecase.repository.SessionRepository
import com.jtm.server.data.service.ServerService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Component

@Component
class DisconnectMonitor @Autowired constructor(@Qualifier("coreExecutor") coreExecutor: ThreadPoolTaskExecutor, private val sessionRepository: SessionRepository, private val serverService: ServerService): Runnable {

    private val logger = LoggerFactory.getLogger(DisconnectMonitor::class.java)

    init {
        coreExecutor.submit(this)
    }

    override fun run() {
        logger.info("Monitoring disconnections.")
        serverService.getInfos()
                .filter { it.isConnected && (!sessionRepository.exists(it.id)) }
                .flatMap {
                    logger.info("Confirming disconnection: ${it.id}")
                    serverService.disconnected(it.id)
                }
    }
}