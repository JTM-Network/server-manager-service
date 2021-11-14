package com.jtm.server

import com.jtm.server.core.usecase.provider.TokenProviderTest
import com.jtm.server.data.event.EventDispatcherTest
import com.jtm.server.data.security.AuthenticationManagerTest
import com.jtm.server.data.service.LogServiceTest
import com.jtm.server.data.service.RuntimeServiceTest
import com.jtm.server.data.service.ServerServiceTest
import com.jtm.server.data.service.SessionServiceTest
import com.jtm.server.entrypoint.controller.RuntimeControllerTest
import com.jtm.server.entrypoint.controller.ServerControllerTest
import com.jtm.server.entrypoint.controller.SessionControllerTest
import com.jtm.server.entrypoint.handler.ConnectedHandlerTest
import com.jtm.server.entrypoint.handler.ServerLogHandlerTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(value = [
    EventDispatcherTest::class,
    AuthenticationManagerTest::class,
    TokenProviderTest::class,

    ConnectedHandlerTest::class,
    ServerLogHandlerTest::class,

    SessionServiceTest::class,
    SessionControllerTest::class,

    ServerServiceTest::class,
    ServerControllerTest::class,

    LogServiceTest::class,

    RuntimeServiceTest::class,
    RuntimeControllerTest::class
])
class ServerManagerServiceTestSuite