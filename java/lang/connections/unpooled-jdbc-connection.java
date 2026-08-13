package com.example.orders;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.sql.DataSource;

public class OrderRepository {

  private final DataSource dataSource;

  public OrderRepository(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public Connection openDirect() throws SQLException {
    // ruleid: unpooled-jdbc-connection
    return DriverManager.getConnection(
        System.getenv("JDBC_URL"), System.getenv("DB_USER"), System.getenv("DB_PASSWORD"));
  }

  public Connection openQualified() throws SQLException {
    // ruleid: unpooled-jdbc-connection
    return java.sql.DriverManager.getConnection(System.getenv("JDBC_URL"));
  }

  public Connection openPooled() throws SQLException {
    // ok: unpooled-jdbc-connection
    return dataSource.getConnection();
  }
}
