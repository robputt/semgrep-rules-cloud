FROM node:22-slim

# ruleid: hardcoded-secret-in-image
ENV API_KEY=sk-live-abc123

# ruleid: hardcoded-secret-in-image
ENV DB_PASSWORD="hunter2"

# ruleid: hardcoded-secret-in-image
ARG NPM_TOKEN=npm_examplevalue

# ruleid: hardcoded-secret-in-image
ENV JWT_SIGNING_KEY=changeme

# ok: hardcoded-secret-in-image
ENV LOG_LEVEL=info

# ok: hardcoded-secret-in-image
ENV NODE_ENV=production

# ok: hardcoded-secret-in-image
ENV API_KEY_FILE=/run/secrets/api_key

# ok: hardcoded-secret-in-image
RUN --mount=type=secret,id=npm_token npm ci --omit=dev && npm cache clean --force

USER 10001
CMD ["node", "server.js"]
