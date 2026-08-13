# syntax=docker/dockerfile:1
FROM node:22-slim

# ruleid: secret-passed-as-build-arg
ARG NPM_TOKEN

# ruleid: secret-passed-as-build-arg
ARG GITHUB_ACCESS_KEY

# ok: secret-passed-as-build-arg
ARG BUILD_REVISION

# ok: secret-passed-as-build-arg
ARG NODE_VERSION=22

# ok: secret-passed-as-build-arg
ARG API_KEY_FILE

WORKDIR /app
COPY package*.json ./
# ok: secret-passed-as-build-arg
RUN --mount=type=secret,id=npm_token \
    NPM_TOKEN="$(cat /run/secrets/npm_token)" npm ci --omit=dev \
    && npm cache clean --force

USER 10001
CMD ["node", "server.js"]
