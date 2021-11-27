package com.jtm.server.entrypoint.controller.directory

import com.jtm.server.data.service.SessionService
import com.jtm.server.data.service.directory.DownloadService
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient

@RunWith(SpringRunner::class)
@WebFluxTest(DownloadController::class)
@AutoConfigureWebTestClient
class DownloadControllerTest {

    @Autowired
    lateinit var testClient: WebTestClient

    @MockBean
    lateinit var downloadService: DownloadService

    @MockBean
    lateinit var sessionService: SessionService

    @Test
    fun addRequest() {}

    @Test
    fun clearDownloads() {}

    @Test
    fun getRequest() {}

    @Test
    fun getRequests() {}

    @Test
    fun deleteRequest() {}
}