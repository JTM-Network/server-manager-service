package com.jtm.server.core.usecase.repository

import com.jtm.server.core.domain.entity.DirectoryEntity
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DirectoryRepository: ReactiveMongoRepository<DirectoryEntity, UUID>