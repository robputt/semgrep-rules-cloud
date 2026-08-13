FROM debian:12-slim

# ruleid: remote-add
ADD https://example.com/releases/tool.tar.gz /tmp/tool.tar.gz

# ruleid: remote-add
ADD http://mirror.example.com/data.zip /opt/data.zip

# ok: remote-add
COPY vendor/tool.tar.gz /tmp/tool.tar.gz

# ok: remote-add
ADD tool.tar.gz /opt/

# ok: remote-add
RUN curl -fsSL https://example.com/releases/tool.tar.gz -o /tmp/tool.tar.gz \
    && echo "9f2b1c0d... /tmp/tool.tar.gz" | sha256sum -c - \
    && tar -xzf /tmp/tool.tar.gz -C /opt \
    && rm /tmp/tool.tar.gz

USER 10001
CMD ["/opt/tool"]
