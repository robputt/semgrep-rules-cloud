// ruleid: module-level-mutable-state
const sessions = new Map();

// ruleid: module-level-mutable-state
let pendingJobs = [];

// ruleid: module-level-mutable-state
let featureOverrides = {};

// ruleid: module-level-mutable-state
const seenIds = new Set();

// ok: module-level-mutable-state
const DEFAULT_HEADERS = { 'content-type': 'application/json' };

// ok: module-level-mutable-state
const RETRY_LIMIT = 3;

function handleRequest(req) {
  // ok: module-level-mutable-state
  const scratch = new Map();
  // ok: module-level-mutable-state
  let collected = [];
  collected.push(req.body);
  return { scratch, collected };
}

const transform = (rows) => {
  // ok: module-level-mutable-state
  const index = new Map();
  for (const row of rows) index.set(row.id, row);
  return index;
};

class Aggregator {
  constructor() {
    // ok: module-level-mutable-state
    this.items = [];
  }

  reduce() {
    // ok: module-level-mutable-state
    const buckets = new Map();
    return buckets;
  }
}

module.exports = { sessions, pendingJobs, handleRequest, transform, Aggregator };
