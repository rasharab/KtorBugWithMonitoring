package com.example

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*

fun main() {
    embeddedServer(Netty, port = 9080, host = "0.0.0.0") {
        configureMonitoring()
        configureSecurity()
        configureRouting()
    }.start(wait = true)
}
