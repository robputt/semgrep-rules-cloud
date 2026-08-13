FROM debian:12-slim

# ruleid: apt-cache-not-cleaned
RUN apt-get update && apt-get install -y --no-install-recommends curl

# ruleid: apt-cache-not-cleaned
RUN apt-get update \
    && apt-get install -y --no-install-recommends ca-certificates tini

# ok: apt-cache-not-cleaned
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl ca-certificates \
    && rm -rf /var/lib/apt/lists/*

# ok: apt-cache-not-cleaned
RUN apt-get update \
    && apt-get install -y --no-install-recommends jq \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

USER 10001
CMD ["/app/server"]
