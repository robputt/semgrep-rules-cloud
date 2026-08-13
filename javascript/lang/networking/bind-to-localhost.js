const express = require('express');
const Fastify = require('fastify');

const app = express();
const port = Number(process.env.PORT ?? 8080);

// ruleid: bind-to-localhost
app.listen(port, '127.0.0.1');

const legacy = express();
// ruleid: bind-to-localhost
legacy.listen(3000, 'localhost', () => console.log('up'));

const fastify = Fastify();
// ruleid: bind-to-localhost
fastify.listen({ port, host: '127.0.0.1' });

const good = express();
// ok: bind-to-localhost
good.listen(port, '0.0.0.0');

const goodFastify = Fastify();
// ok: bind-to-localhost
goodFastify.listen({ port, host: '0.0.0.0' });

module.exports = { app, legacy, good };
