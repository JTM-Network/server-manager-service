package com.jtm.server

import com.jtm.server.core.usecase.provider.TokenProviderTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(value = [
    TokenProviderTest::class,
])
class ServerManagerServiceTestSuite