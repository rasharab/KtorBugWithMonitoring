package com.example

import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.util.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlin.test.*
import io.ktor.server.testing.*
import com.example.plugins.*
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.defaultRequest

class ApplicationTest {
    private fun ApplicationTestBuilder.createCustomClient(): HttpClient {
        val authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJpc3MiOiJpc3N1ZXIiLCJhdWQiOiJhdWRpZW5jZSJ9.y7ZJOcm4dRxeMhprH8l1Ohrh4FqATCcVhf4MENd_SK8"
        return createClient {
            install(Auth) {
                bearer {
                    loadTokens {
                        io.ktor.client.plugins.auth.providers.BearerTokens(
                            accessToken = authToken,
                            refreshToken = "",
                        )
                    }
                }
            }
        }
    }

    @Test
    fun `Without monitoring authentication works as expected`() = testApplication {
        application {
            configureSecurity()
            configureRouting()
        }
        val client = this.createCustomClient()

        client.get("/teams/123e4567-e89b-12d3-a456-556642440000").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }

    @Test
    fun `With monitoring authentication fails when it should not fail`() = testApplication {
        application {
            configureSecurity()
            configureRouting()
            configureMonitoring()
        }
        val client = this.createCustomClient()

        client.get("/teams/123e4567-e89b-12d3-a456-556642440000").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }
}