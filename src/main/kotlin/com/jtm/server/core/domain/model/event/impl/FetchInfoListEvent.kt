package com.jtm.server.core.domain.model.event.impl

import com.jtm.server.core.domain.entity.PlayerInfo
import java.util.*

data class FetchInfoListEvent(val id: UUID, val infoList: List<PlayerInfo>)