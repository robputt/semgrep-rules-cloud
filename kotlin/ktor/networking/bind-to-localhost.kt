package com.example.orders

import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    // ruleid: bind-to-localhost
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        routes()
    }.start(wait = true)
}

fun alsoLoopback() {
    // ruleid: bind-to-localhost
    embeddedServer(Netty, port = 8080, host = "localhost") {
        routes()
    }.start(wait = true)
}

fun allInterfaces() {
    val port = System.getenv("PORT")?.toInt() ?: 8080
    // ok: bind-to-localhost
    embeddedServer(Netty, port = port, host = "0.0.0.0") {
        routes()
    }.start(wait = true)
}

fun io.ktor.server.application.Application.routes() {}
