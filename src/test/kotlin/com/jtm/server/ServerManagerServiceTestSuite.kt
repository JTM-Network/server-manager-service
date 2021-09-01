package com.jtm.server

import com.jtm.server.core.usecase.provider.TokenProviderTest
import com.jtm.server.data.event.EventDispatcherTest
import com.jtm.server.data.security.AuthenticationManagerTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(value = [
    EventDispatcherTest::class,
    AuthenticationManagerTest::class,
    TokenProviderTest::class,
])
class ServerManagerServiceTestSuite