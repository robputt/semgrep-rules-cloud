import os
import socket
from http.server import HTTPServer, SimpleHTTPRequestHandler

import uvicorn
from flask import Flask

app = Flask(__name__)


def serve_flask():
    # ruleid: bind-to-localhost
    app.run(host="127.0.0.1", port=8080)


def serve_asgi():
    # ruleid: bind-to-localhost
    uvicorn.run("main:app", host="localhost", port=8000)


def serve_http():
    # ruleid: bind-to-localhost
    HTTPServer(("127.0.0.1", 8000), SimpleHTTPRequestHandler).serve_forever()


def raw_socket():
    sock = socket.socket()
    # ruleid: bind-to-localhost
    sock.bind(("localhost", 9000))
    return sock


def serve_flask_all_interfaces():
    # ok: bind-to-localhost
    app.run(host="0.0.0.0", port=int(os.environ.get("PORT", "8080")))


def serve_asgi_all_interfaces():
    # ok: bind-to-localhost
    uvicorn.run("main:app", host="0.0.0.0", port=int(os.environ["PORT"]))
