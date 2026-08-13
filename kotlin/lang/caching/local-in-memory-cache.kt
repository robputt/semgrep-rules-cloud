package com.example.orders

import com.github.benmanes.caffeine.cache.Caffeine
import io.lettuce.core.RedisClient
import org.springframework.cache.CacheManager
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.data.redis.cache.RedisCacheManager
import java.time.Duration

class Caches(private val redis: RedisClient) {

    // ruleid: local-in-memory-cache
    val profiles = Caffeine.newBuilder()
        .maximumSize(500)
        .expireAfterWrite(Duration.ofMinutes(5))
        .build<String, String>()

    // ruleid: local-in-memory-cache
    fun springLocalCache(): CacheManager = ConcurrentMapCacheManager("orders")

    // ok: local-in-memory-cache
    fun springSharedCache(factory: org.springframework.data.redis.connection.RedisConnectionFactory):
        CacheManager = RedisCacheManager.builder(factory).build()

    // ok: local-in-memory-cache
    fun sharedLookup(key: String): String? =
        redis.connect().sync().get("profile:$key")
}
