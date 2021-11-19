package com.jtm.server.core.usecase.repository

import com.jtm.server.core.domain.entity.ServerPlugins
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PluginRepository: ReactiveMongoRepository<ServerPlugins, UUID>