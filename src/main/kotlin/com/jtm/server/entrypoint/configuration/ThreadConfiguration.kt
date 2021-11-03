package com.jtm.server.entrypoint.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor

@Configuration
open class ThreadConfiguration {

    @Bean(name = ["coreExecutor"])
    open fun taskExecutor(): Executor {
        val poolExecutor = ThreadPoolTaskExecutor()
        poolExecutor.corePoolSize = 50
        poolExecutor.maxPoolSize = 100
        poolExecutor.threadNamePrefix = "corePoolExecutor"
        return poolExecutor
    }
}