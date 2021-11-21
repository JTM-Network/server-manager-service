package com.jtm.server.core.usecase.repository

import com.jtm.server.core.domain.entity.UploadRequest
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UploadRequestRepository: ReactiveMongoRepository<UploadRequest, UUID>