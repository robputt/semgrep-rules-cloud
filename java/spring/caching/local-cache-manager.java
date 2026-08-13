package com.example.orders;

import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
public class CacheConfig {

  @Bean
  public CacheManager simpleCacheManager() {
    // ruleid: local-cache-manager
    return new ConcurrentMapCacheManager("orders", "customers");
  }

  @Bean
  public CacheManager caffeineCacheManager() {
    // ruleid: local-cache-manager
    return new CaffeineCacheManager("orders");
  }

  @Bean
  public CacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
    // ok: local-cache-manager
    return RedisCacheManager.builder(connectionFactory).build();
  }
}
