import os

import psycopg2
import redis
from flask import Flask, jsonify
from sqlalchemy import create_engine

app = Flask(__name__)

pool_engine = create_engine(
    os.environ["DATABASE_URL"], pool_size=5, pool_pre_ping=True
)
shared_cache = redis.Redis.from_url(os.environ["REDIS_URL"])


@app.route("/orders")
def list_orders():
    # ruleid: connection-created-per-request
    conn = psycopg2.connect(os.environ["DATABASE_URL"])
    with conn.cursor() as cur:
        cur.execute("SELECT id FROM orders")
        return jsonify(cur.fetchall())


@app.post("/orders")
def create_order():
    # ruleid: connection-created-per-request
    engine = create_engine(os.environ["DATABASE_URL"])
    with engine.begin() as conn:
        conn.execute("INSERT INTO orders DEFAULT VALUES")
    return "", 201


@app.get("/session")
def read_session():
    # ruleid: connection-created-per-request
    client = redis.Redis(host="cache", port=6379)
    return client.get("key") or ""


@app.route("/healthy-orders")
def healthy_orders():
    # ok: connection-created-per-request
    with pool_engine.connect() as conn:
        return jsonify(list(conn.execute("SELECT id FROM orders")))


@app.route("/healthy-session")
def healthy_session():
    # ok: connection-created-per-request
    return shared_cache.get("key") or ""
