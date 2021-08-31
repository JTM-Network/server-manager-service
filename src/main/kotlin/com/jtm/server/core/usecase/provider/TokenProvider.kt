package com.jtm.server.core.usecase.provider

import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.SignatureException
import java.util.*

@Component
class TokenProvider {

    @Value("\${security.jwt.plugin-key:pluginKey}")
    lateinit var pluginKey: String

    fun resolveToken(bearer: String): String {
        return bearer.replace("Bearer ", "")
    }

    fun getAccountId(token: String): UUID? {
        return try {
            val claims = Jwts.parser().setSigningKey(pluginKey).parseClaimsJws(token)
            UUID.fromString(claims.body["id"].toString())
        } catch (ex: SignatureException) {
            null
        }
    }

    fun getAccountEmail(token: String): String? {
        return try {
            Jwts.parser().setSigningKey(pluginKey).parseClaimsJws(token).body.subject
        } catch (ex: SignatureException) {
            null
        }
    }
}