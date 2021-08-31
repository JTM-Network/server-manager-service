package com.jtm.server.entrypoint.configuration

import com.jtm.server.entrypoint.socket.ServerSocketHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.WebSocketHandler


@Configuration
open class SocketConfiguration {

    @Bean
    open fun handlerMapping(): HandlerMapping {
        val map: MutableMap<String, WebSocketHandler> = HashMap()
        map["/manager"] = ServerSocketHandler()


        val handlerMapping = SimpleUrlHandlerMapping()
        handlerMapping.order = Ordered.HIGHEST_PRECEDENCE
        handlerMapping.urlMap = map
        return handlerMapping
    }
}