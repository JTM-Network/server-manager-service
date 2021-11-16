package com.jtm.server.core.domain.entity

import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("player_information")
data class PlayerInfo(val id: UUID, var name: String, val lastJoin: Long = System.currentTimeMillis(), val firstJoin: Long = System.currentTimeMillis()) {

    fun update(info: PlayerInfo): PlayerInfo {
        if (info.name != name) this.name = info.name
        return this
    }
}