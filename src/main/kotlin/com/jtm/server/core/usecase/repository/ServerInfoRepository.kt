package com.jtm.server.core.usecase.repository

import com.jtm.server.core.domain.entity.ServerInfo
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ServerInfoRepository: ReactiveMongoRepository<ServerInfo, UUID>