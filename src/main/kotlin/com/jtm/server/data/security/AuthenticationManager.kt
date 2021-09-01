package com.jtm.server.data.security

import org.springframework.stereotype.Component

@Component
class AuthenticationManager {

    fun authenticate(token: String): Boolean {
        return true
    }
}