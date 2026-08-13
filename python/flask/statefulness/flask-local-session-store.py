import os

import redis
from flask import Flask
from flask_session import Session

app = Flask(__name__)

# ruleid: flask-local-session-store
app.config["SESSION_TYPE"] = "filesystem"
Session(app)

other = Flask("other")
# ruleid: flask-local-session-store
other.config.update(SESSION_TYPE="cachelib", SESSION_PERMANENT=False)

shared = Flask("shared")
# ok: flask-local-session-store
shared.config["SESSION_TYPE"] = "redis"
shared.config["SESSION_REDIS"] = redis.from_url(os.environ["REDIS_URL"])
Session(shared)
