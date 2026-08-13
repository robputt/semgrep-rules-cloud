package com.example.orders

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.sessions.SessionStorageMemory
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie
import io.ktor.server.sessions.directorySessionStorage
import java.io.File

data class UserSession(val id: String)

fun Application.badSessions() {
    install(Sessions) {
        // ruleid: in-memory-session-storage
        cookie<UserSession>("SESSION", SessionStorageMemory())
    }
}

fun Application.alsoBadSessions() {
    install(Sessions) {
        // ruleid: in-memory-session-storage
        cookie<UserSession>("SESSION", directorySessionStorage(File("/tmp/sessions")))
    }
}

fun Application.goodSessions(storage: RedisSessionStorage) {
    install(Sessions) {
        // ok: in-memory-session-storage
        cookie<UserSession>("SESSION", storage)
    }
}

class RedisSessionStorage
