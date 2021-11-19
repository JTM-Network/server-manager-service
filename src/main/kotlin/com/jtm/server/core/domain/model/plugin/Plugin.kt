package com.jtm.server.core.domain.model.plugin

data class Plugin(val name: String, val description: String?, val enabled: Boolean, val version: String, val apiVersion: String?, val authors: List<String>, val depend: List<String>, val softDepend: List<String>)