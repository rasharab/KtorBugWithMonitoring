package com.example.plugins

import io.ktor.server.auth.*
import io.ktor.util.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureSecurity() {

    authentication {
        jwt("bearerAuth") {
            verifier(
                JWT
                    .require(Algorithm.HMAC256("secret"))
                    .withAudience("audience")
                    .withIssuer("issuer")
                    .build()
            )
            validate { credential ->
                val teamId = this.parameters["teamId"]
                if (teamId == null) {
                    println("TeamID is unexpectedly null!")
                    throw Exception("TeamID is null when it should't be")
                }
                if (credential.payload.audience.contains("audience")) JWTPrincipal(credential.payload) else null
            }
        }
    }

}
