package com.jtm.server.entrypoint.configuration

import com.jtm.server.entrypoint.socket.ServerSocketHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.WebSocketHandler

@Configuration
open class SocketConfiguration {

    @Autowired
    lateinit var serverSocketHandler: ServerSocketHandler

    @Bean
    open fun handlerMapping(): HandlerMapping {
        val map: MutableMap<String, WebSocketHandler> = HashMap()
        map["/manager"] = serverSocketHandler
        return SimpleUrlHandlerMapping(map, -1)
    }
}