package com.example.plugins

import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.response.*

fun Application.configureRouting() {
    routing {
        authenticate("bearerAuth") {
            get("/teams/{teamId}") {
                call.respondText("Hello World!")
            }
        }
    }
}
