package com.example.orders

import java.sql.Connection
import java.sql.DriverManager
import javax.sql.DataSource

class OrderRepository(private val dataSource: DataSource) {

    fun openDirect(): Connection {
        // ruleid: unpooled-jdbc-connection
        return DriverManager.getConnection(
            System.getenv("JDBC_URL"),
            System.getenv("DB_USER"),
            System.getenv("DB_PASSWORD"),
        )
    }

    fun openQualified(): Connection {
        // ruleid: unpooled-jdbc-connection
        return java.sql.DriverManager.getConnection(System.getenv("JDBC_URL"))
    }

    fun openPooled(): Connection {
        // ok: unpooled-jdbc-connection
        return dataSource.connection
    }
}
