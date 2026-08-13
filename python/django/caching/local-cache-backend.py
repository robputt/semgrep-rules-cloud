import os

CACHES = {
    # ruleid: local-cache-backend
    "default": {
        "BACKEND": "django.core.cache.backends.locmem.LocMemCache",
        "LOCATION": "app-cache",
    },
    # ruleid: local-cache-backend
    "pages": {
        "BACKEND": "django.core.cache.backends.filebased.FileBasedCache",
        "LOCATION": "/var/tmp/django_cache",
    },
    # ok: local-cache-backend
    "sessions": {
        "BACKEND": "django.core.cache.backends.redis.RedisCache",
        "LOCATION": os.environ["REDIS_URL"],
    },
}

# ok: local-cache-backend
OTHER = {"BACKEND": "django.core.cache.backends.locmem.LocMemCache"}
