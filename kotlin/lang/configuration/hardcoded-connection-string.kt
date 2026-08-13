package com.example.orders

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

// ruleid: hardcoded-connection-string
val JDBC_URL = "jdbc:postgresql://prod-db.internal:5432/orders?user=app&password=s3cret"

class DataSourceFactory {

    // ruleid: hardcoded-connection-string
    fun redisUri(): String = "redis://prod-cache.internal:6379/1"

    fun unsafe(): DataSource {
        val config = HikariConfig()
        // ruleid: hardcoded-connection-string
        config.setJdbcUrl("jdbc:mysql://10.0.3.14:3306/inventory")
        return HikariDataSource(config)
    }

    fun fromEnvironment(): DataSource {
        val config = HikariConfig()
        // ok: hardcoded-connection-string
        config.jdbcUrl = System.getenv("JDBC_URL")
        config.maximumPoolSize = System.getenv("DB_POOL_MAX").toInt()
        return HikariDataSource(config)
    }

    // ok: hardcoded-connection-string
    val docs = "https://docs.example.com/database"
}
