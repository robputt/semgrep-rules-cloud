const pg = require('pg');
const { Client, Pool } = require('pg');
const mysql = require('mysql2/promise');

// ruleid: unpooled-database-client
const direct = new pg.Client({ connectionString: process.env.DATABASE_URL });

// ruleid: unpooled-database-client
const destructured = new Client({ connectionString: process.env.DATABASE_URL });

async function queryMysql(sql) {
  // ruleid: unpooled-database-client
  const conn = await mysql.createConnection(process.env.MYSQL_URL);
  return conn.execute(sql);
}

// ok: unpooled-database-client
const pool = new Pool({
  connectionString: process.env.DATABASE_URL,
  max: Number(process.env.DB_POOL_MAX ?? 10),
  idleTimeoutMillis: 30_000,
});

// ok: unpooled-database-client
const mysqlPool = mysql.createPool({
  uri: process.env.MYSQL_URL,
  connectionLimit: 10,
});

module.exports = { direct, destructured, queryMysql, pool, mysqlPool };
