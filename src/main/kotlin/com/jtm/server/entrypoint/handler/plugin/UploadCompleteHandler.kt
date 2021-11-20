package com.jtm.server.entrypoint.handler.plugin

import com.jtm.server.core.domain.constants.DownloadStatus
import com.jtm.server.core.domain.model.event.impl.plugin.UploadCompleteEvent
import com.jtm.server.core.usecase.event.EventHandler
import com.jtm.server.core.usecase.repository.DownloadRequestRepository
import com.jtm.server.core.usecase.sink.RequestSink
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

@Component
class UploadCompleteHandler @Autowired constructor(private val requestRepository: DownloadRequestRepository, private val requestSink: RequestSink): EventHandler<UploadCompleteEvent>("upload_complete", UploadCompleteEvent::class.java) {
    override fun onEvent(session: WebSocketSession, value: UploadCompleteEvent): Mono<WebSocketMessage> {
        return requestRepository.findById(value.id)
                .flatMap { requestRepository.save(it.updateStatus(DownloadStatus.DOWNLOAD_AVAILABLE))
                        .flatMap { request ->
                            requestSink.sendMessage(request)
                            Mono.empty()
                        }
                }
    }
}