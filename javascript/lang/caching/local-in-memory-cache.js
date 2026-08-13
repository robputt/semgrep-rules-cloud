// ruleid: local-in-memory-cache
const NodeCache = require('node-cache');
// ruleid: local-in-memory-cache
const { LRUCache } = require('lru-cache');
const Redis = require('ioredis');

// ruleid: local-in-memory-cache
const tokenCache = new NodeCache({ stdTTL: 300 });

// ruleid: local-in-memory-cache
const profileCache = new LRUCache({ max: 500 });

// ok: local-in-memory-cache
const shared = new Redis(process.env.REDIS_URL);

async function getProfile(id) {
  // ok: local-in-memory-cache
  const hit = await shared.get(`profile:${id}`);
  if (hit) return JSON.parse(hit);
  const profile = await fetchProfile(id);
  await shared.setex(`profile:${id}`, 300, JSON.stringify(profile));
  return profile;
}

async function fetchProfile(id) {
  return { id };
}

module.exports = { tokenCache, profileCache, getProfile };
