import os

import redis
from sqlalchemy import create_engine

# ruleid: hardcoded-connection-string
DATABASE_URL = "postgresql://app:s3cret@prod-db.internal:5432/orders"

# ruleid: hardcoded-connection-string
engine = create_engine("mysql+pymysql://app:pw@10.0.3.14/inventory")

# ruleid: hardcoded-connection-string
cache = redis.Redis.from_url("redis://prod-cache.internal:6379/1")

# ruleid: hardcoded-connection-string
SETTINGS = {"broker": "amqps://user:pw@rabbit.internal:5671/vhost"}


def mongo_uri() -> str:
    # ruleid: hardcoded-connection-string
    return "mongodb+srv://svc:pw@cluster0.example.mongodb.net/app"


def build_engine():
    # ok: hardcoded-connection-string
    return create_engine(os.environ["DATABASE_URL"])


# ok: hardcoded-connection-string
BROKER_URL = os.environ["BROKER_URL"]

# ok: hardcoded-connection-string
DOCS_URL = "https://docs.example.com/database"
