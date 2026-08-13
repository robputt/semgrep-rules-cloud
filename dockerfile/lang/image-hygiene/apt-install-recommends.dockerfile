FROM debian:12-slim

# ruleid: apt-install-recommends
RUN apt-get update \
    && apt-get install -y curl \
    && rm -rf /var/lib/apt/lists/*

# ruleid: apt-install-recommends
RUN apt-get update && apt-get install -y git build-essential && rm -rf /var/lib/apt/lists/*

# ok: apt-install-recommends
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl ca-certificates \
    && rm -rf /var/lib/apt/lists/*

USER 10001
CMD ["/app/server"]
