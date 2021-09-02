package com.jtm.server.core.domain.model.event

import com.fasterxml.jackson.databind.ObjectMapper

class OutgoingEvent(name: String, value: Any): Event(name, value)