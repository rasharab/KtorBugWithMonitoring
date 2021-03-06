package com.example.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.request.path

fun Application.configureMonitoring() {
    install(CallLogging) {
        disableDefaultColors()
        filter { call -> call.request.path().startsWith("/") }

        // BUG: Calling principal somehow causes application call path parameters to be reset during authentication
        mdc("identityId") {
            it.principal<JWTPrincipal>()?.subject ?: "No subject"
        }
    }
}
