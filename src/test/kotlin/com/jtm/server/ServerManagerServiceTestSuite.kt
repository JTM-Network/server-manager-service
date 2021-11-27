package com.jtm.server

import com.jtm.server.core.usecase.provider.TokenProviderTest
import com.jtm.server.data.event.EventDispatcherTest
import com.jtm.server.data.security.AuthenticationManagerTest
import com.jtm.server.data.service.*
import com.jtm.server.data.service.directory.DirectoryServiceTest
import com.jtm.server.data.service.directory.DownloadServiceTest
import com.jtm.server.data.service.plugin.PluginCommandServiceTest
import com.jtm.server.data.service.plugin.PluginServiceTest
import com.jtm.server.entrypoint.controller.CommandControllerTest
import com.jtm.server.entrypoint.controller.directory.DirectoryControllerTest
import com.jtm.server.entrypoint.controller.RuntimeControllerTest
import com.jtm.server.entrypoint.controller.ServerControllerTest
import com.jtm.server.entrypoint.controller.SessionControllerTest
import com.jtm.server.entrypoint.controller.directory.DownloadControllerTest
import com.jtm.server.entrypoint.controller.plugin.PluginControllerTest
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
    RuntimeControllerTest::class,

    DirectoryServiceTest::class,
    DirectoryControllerTest::class,

    PluginControllerTest::class,
    PluginServiceTest::class,

    DownloadServiceTest::class,
    DownloadControllerTest::class,

    CommandServiceTest::class,
    CommandControllerTest::class,

    PluginCommandServiceTest::class
])
class ServerManagerServiceTestSuite