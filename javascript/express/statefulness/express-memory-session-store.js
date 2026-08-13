const express = require('express');
const session = require('express-session');
const RedisStore = require('connect-redis').default;
const Redis = require('ioredis');

const app = express();

// ruleid: express-memory-session-store
const memorySessions = session({
  secret: process.env.SESSION_SECRET,
  resave: false,
  saveUninitialized: false,
});
app.use(memorySessions);

const shared = express();
// ok: express-memory-session-store
const redisSessions = session({
  store: new RedisStore({ client: new Redis(process.env.REDIS_URL) }),
  secret: process.env.SESSION_SECRET,
  resave: false,
  saveUninitialized: false,
});
shared.use(redisSessions);

module.exports = { app, shared };
