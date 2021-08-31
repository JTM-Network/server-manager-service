package com.jtm.server.core.usecase.provider

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import junit.framework.Assert.assertNotNull
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class TokenProviderTest {

    private val tokenProvider = TokenProvider()
    private val uuid = UUID.randomUUID()
    private lateinit var token: String

    @Before
    fun setup() {
        tokenProvider.pluginKey = "pluginKey"
        token = createToken("pluginKey", uuid, "test@gmail.com")
    }

    @Test
    fun resolveToken() {
        val token = tokenProvider.resolveToken("Bearer test")

        assertThat(token).isEqualTo("test")
    }

    @Test
    fun getAccountId() {
        val id = tokenProvider.getAccountId(token)

        assertNotNull(id)
        assertThat(id).isEqualTo(uuid)
    }

    @Test
    fun getAccountEmail() {
        val email = tokenProvider.getAccountEmail(token)

        assertNotNull(email)
        assertThat(email).isEqualTo("test@gmail.com")
    }

    private fun createToken(key: String, id: UUID, email: String): String {
        return Jwts.builder()
            .signWith(SignatureAlgorithm.HS256, key)
            .setSubject(email)
            .claim("id", id.toString())
            .compact()
    }
}