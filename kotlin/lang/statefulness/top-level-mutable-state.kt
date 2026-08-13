package com.example.orders

import java.util.concurrent.ConcurrentHashMap

// ruleid: top-level-mutable-state
val sessions = mutableMapOf<String, String>()

// ruleid: top-level-mutable-state
val pending = mutableListOf<String>()

// ruleid: top-level-mutable-state
val counters = ConcurrentHashMap<String, Int>()

// ok: top-level-mutable-state
val STATUS_LABELS = mapOf("open" to "Open", "closed" to "Closed")

// ok: top-level-mutable-state
const val MAX_RETRIES = 3

object Registry {
    // ruleid: top-level-mutable-state
    val cache = mutableMapOf<String, String>()
}

class OrderHandler {
    // ok: top-level-mutable-state
    val perInstance = mutableMapOf<String, String>()

    fun handle(id: String): Map<String, String> {
        // ok: top-level-mutable-state
        val local = mutableMapOf<String, String>()
        local[id] = id
        return local
    }
}
