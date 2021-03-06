package com.jtm.server.data.security

import com.jtm.server.core.usecase.provider.TokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class AuthenticationManager @Autowired constructor(private val tokenProvider: TokenProvider) {

    fun authenticate(token: String): Boolean {
        tokenProvider.getAccountId(token) ?: return false
        tokenProvider.getAccountEmail(token) ?: return false
        return true
    }
}