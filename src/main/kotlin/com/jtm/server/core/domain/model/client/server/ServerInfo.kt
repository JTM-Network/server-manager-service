package com.jtm.server.core.domain.model.client.server

data class ServerInfo(val ip: String = "", val port: Int = 0, val bukkitVersion: String = "bukkit", val minecraftVersion: String = "minecraft", val maxPlayers: Int = 0, val onlineMode: Boolean = true)
