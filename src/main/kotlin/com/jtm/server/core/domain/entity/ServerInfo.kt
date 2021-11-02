package com.jtm.server.core.domain.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("server_information")
data class ServerInfo(@Id val id: UUID, val accountId: UUID, var ip: String = "", var isConnected: Boolean = false, var lastConnected: Long = System.currentTimeMillis(), val created: Long = System.currentTimeMillis()) {

    fun connect(): ServerInfo {
        this.isConnected = true
        this.lastConnected = System.currentTimeMillis()
        return this
    }

    fun disconnect(): ServerInfo {
        this.isConnected = false
        this.lastConnected = System.currentTimeMillis()
        return this
    }
}