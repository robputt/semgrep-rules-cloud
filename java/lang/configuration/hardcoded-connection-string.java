package com.example.orders;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;

public class DataSourceFactory {

  // ruleid: hardcoded-connection-string
  private static final String JDBC_URL =
      "jdbc:postgresql://prod-db.internal:5432/orders?user=app&password=s3cret";

  public String redisUri() {
    // ruleid: hardcoded-connection-string
    return "redis://prod-cache.internal:6379/1";
  }

  public DataSource unsafe() {
    HikariConfig config = new HikariConfig();
    // ruleid: hardcoded-connection-string
    config.setJdbcUrl("jdbc:mysql://10.0.3.14:3306/inventory");
    return new HikariDataSource(config);
  }

  public DataSource fromEnvironment() {
    HikariConfig config = new HikariConfig();
    // ok: hardcoded-connection-string
    config.setJdbcUrl(System.getenv("JDBC_URL"));
    config.setMaximumPoolSize(Integer.parseInt(System.getenv("DB_POOL_MAX")));
    return new HikariDataSource(config);
  }

  // ok: hardcoded-connection-string
  private static final String DOCS = "https://docs.example.com/database";
}
