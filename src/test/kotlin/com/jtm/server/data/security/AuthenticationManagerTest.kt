package com.jtm.server.data.security

import com.jtm.server.core.usecase.provider.TokenProvider
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.springframework.test.context.junit4.SpringRunner
import java.util.*

@RunWith(SpringRunner::class)
class AuthenticationManagerTest {

    private val tokenProvider: TokenProvider = mock()
    private val authenticationManager = AuthenticationManager(tokenProvider)

    @Test
    fun authenticate_thenIdNull() {
        `when`(tokenProvider.getAccountId(anyString())).thenReturn(null)

        val returned = authenticationManager.authenticate("test")

        verify(tokenProvider, times(1)).getAccountId(anyString())
        verifyNoMoreInteractions(tokenProvider)

        assertFalse { returned }
    }

    @Test
    fun authenticate_thenEmailNull() {
        `when`(tokenProvider.getAccountId(anyString())).thenReturn(UUID.randomUUID())
        `when`(tokenProvider.getAccountEmail(anyString())).thenReturn(null)

        val returned = authenticationManager.authenticate("test")

        verify(tokenProvider, times(1)).getAccountId(anyString())
        verify(tokenProvider, times(1)).getAccountEmail(anyString())
        verifyNoMoreInteractions(tokenProvider)

        assertFalse { returned }
    }

    @Test
    fun authenticate() {
        `when`(tokenProvider.getAccountId(anyString())).thenReturn(UUID.randomUUID())
        `when`(tokenProvider.getAccountEmail(anyString())).thenReturn("test@gmail.com")

        val returned = authenticationManager.authenticate("test")

        verify(tokenProvider, times(1)).getAccountId(anyString())
        verify(tokenProvider, times(1)).getAccountEmail(anyString())
        verifyNoMoreInteractions(tokenProvider)

        assertTrue { returned }
    }
}