# ruleid: unpinned-base-image
FROM python:latest AS bad-latest

# ruleid: unpinned-base-image
FROM node AS bad-untagged

# ok: unpinned-base-image
FROM golang:1.22-alpine AS good-tagged

# ok: unpinned-base-image
FROM python:3.12-slim@sha256:2f1a1e3c0f5f1d4d0f4d3f2e1a0b9c8d7e6f5a4b3c2d1e0f9a8b7c6d5e4f3a2b AS good-digest

# ok: unpinned-base-image
FROM debian@sha256:1b0a1c2d3e4f5a6b7c8d9e0f1a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b

USER 10001
CMD ["python", "app.py"]
