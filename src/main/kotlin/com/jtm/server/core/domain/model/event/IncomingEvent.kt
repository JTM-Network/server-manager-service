package com.jtm.server.core.domain.model.event

open class IncomingEvent(name: String, val token: String, value: Any): Event(name, value)