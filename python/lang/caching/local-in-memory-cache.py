import os
import shelve

import cachetools
import diskcache
import redis
from cachetools import TTLCache, cached

# ruleid: local-in-memory-cache
profile_cache = cachetools.TTLCache(maxsize=1024, ttl=300)

# ruleid: local-in-memory-cache
token_cache = TTLCache(maxsize=64, ttl=60)

# ruleid: local-in-memory-cache
disk = diskcache.Cache("/tmp/app-cache")

_manual = {}


# ruleid: local-in-memory-cache
@cached(cache=TTLCache(maxsize=32, ttl=30))
def load_settings(tenant):
    return {"tenant": tenant}


def lookup_user(user_id):
    # ruleid: local-in-memory-cache
    if user_id in _manual:
        return _manual[user_id]
    user = fetch_user(user_id)
    _manual[user_id] = user
    return user


def open_local_store():
    # ruleid: local-in-memory-cache
    return shelve.open("sessions.db")


shared = redis.Redis.from_url(os.environ["REDIS_URL"])


# ok: local-in-memory-cache
def lookup_user_shared(user_id):
    cached_value = shared.get(f"user:{user_id}")
    if cached_value:
        return cached_value
    user = fetch_user(user_id)
    shared.setex(f"user:{user_id}", 300, user)
    return user


def fetch_user(user_id):
    return {"id": user_id}
