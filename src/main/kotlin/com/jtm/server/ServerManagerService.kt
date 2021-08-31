package com.jtm.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@EnableDiscoveryClient
@SpringBootApplication
open class ServerManagerService

fun main(args: Array<String>) {
    runApplication<ServerManagerService>(*args)
}