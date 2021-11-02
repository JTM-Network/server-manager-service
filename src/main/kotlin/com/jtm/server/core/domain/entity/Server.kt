package com.jtm.server.core.domain.entity

import com.jtm.server.core.domain.model.client.server.ServerInfo
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("server_information")
data class Server(@Id val id: UUID, val accountId: UUID, var info: ServerInfo, var isConnected: Boolean = false, var lastConnected: Long = System.currentTimeMillis(), val created: Long = System.currentTimeMillis()) {

    fun connect(): Server {
        this.isConnected = true
        this.lastConnected = System.currentTimeMillis()
        return this
    }

    fun disconnect(): Server {
        this.isConnected = false
        this.lastConnected = System.currentTimeMillis()
        return this
    }
}