package com.jtm.server.core.domain.entity

import com.jtm.server.core.domain.model.plugin.Plugin
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("server_plugins")
data class ServerPlugins(@Id val id: UUID, val plugins: MutableList<Plugin> = mutableListOf()) {

    fun enabled(name: String): ServerPlugins {
        val plugin = plugins.firstOrNull { it.name == name } ?: return this
        plugin.enabled = true
        return this
    }

    fun disabled(name: String): ServerPlugins {
        val plugin = plugins.firstOrNull { it.name == name } ?: return this
        plugin.enabled = false
        return this
    }
}
