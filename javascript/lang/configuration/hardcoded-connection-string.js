const { Pool } = require('pg');
const Redis = require('ioredis');

// ruleid: hardcoded-connection-string
const DATABASE_URL = 'postgresql://app:s3cret@prod-db.internal:5432/orders';

// ruleid: hardcoded-connection-string
const cache = new Redis('redis://prod-cache.internal:6379/1');

// ruleid: hardcoded-connection-string
const config = { broker: 'amqps://user:pw@rabbit.internal:5671/vhost' };

function mongoUri() {
  // ruleid: hardcoded-connection-string
  return 'mongodb+srv://svc:pw@cluster0.example.mongodb.net/app';
}

// ok: hardcoded-connection-string
const pool = new Pool({ connectionString: process.env.DATABASE_URL });

// ok: hardcoded-connection-string
const sharedCache = new Redis(process.env.REDIS_URL);

// ok: hardcoded-connection-string
const DOCS_URL = 'https://docs.example.com/database';

module.exports = { DATABASE_URL, cache, config, mongoUri, pool, sharedCache, DOCS_URL };
