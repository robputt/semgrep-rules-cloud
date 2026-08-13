FROM debian:12-slim

# ruleid: runs-as-root
USER root
RUN apt-get update \
    && apt-get install -y --no-install-recommends ca-certificates \
    && rm -rf /var/lib/apt/lists/*

# ruleid: runs-as-root
USER 0

RUN useradd --uid 10001 --create-home appuser

# ok: runs-as-root
USER 10001

# ok: runs-as-root
USER appuser

CMD ["/app/server"]
