package com.jtm.server

import com.jtm.server.core.usecase.provider.TokenProviderTest
import com.jtm.server.data.event.EventDispatcherTest
import com.jtm.server.data.security.AuthenticationManagerTest
import com.jtm.server.data.service.SessionServiceTest
import com.jtm.server.entrypoint.controller.SessionControllerTest
import com.jtm.server.entrypoint.handler.ConnectedHandlerTest
import com.jtm.server.entrypoint.handler.DisconnectHandlerTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(value = [
    EventDispatcherTest::class,
    AuthenticationManagerTest::class,
    TokenProviderTest::class,

    ConnectedHandlerTest::class,
    DisconnectHandlerTest::class,

    SessionServiceTest::class,
    SessionControllerTest::class
])
class ServerManagerServiceTestSuite