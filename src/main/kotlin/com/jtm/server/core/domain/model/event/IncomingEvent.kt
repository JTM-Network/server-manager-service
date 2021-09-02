package com.jtm.server.core.domain.model.event

open class IncomingEvent(name: String, val token: String, value: String): Event(name, value)