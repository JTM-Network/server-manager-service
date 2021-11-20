package com.jtm.server.core.usecase.repository

import com.jtm.server.core.domain.entity.DownloadRequest
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DownloadRequestRepository: ReactiveMongoRepository<DownloadRequest, UUID>