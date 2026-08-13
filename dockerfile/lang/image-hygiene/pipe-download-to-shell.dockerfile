FROM debian:12-slim

# ruleid: pipe-download-to-shell
RUN curl -sSL https://get.example.com/install.sh | sh

# ruleid: pipe-download-to-shell
RUN curl -fsSL https://deb.nodesource.com/setup_22.x | bash -

# ruleid: pipe-download-to-shell
RUN wget -qO- https://get.example.com/install.sh | sh

# ok: pipe-download-to-shell
RUN curl -fsSL https://get.example.com/install.sh -o /tmp/install.sh \
    && echo "9f2b1c0d... /tmp/install.sh" | sha256sum -c - \
    && sh /tmp/install.sh \
    && rm /tmp/install.sh

# ok: pipe-download-to-shell
RUN apt-get update \
    && apt-get install -y --no-install-recommends nodejs=20.11.1* \
    && rm -rf /var/lib/apt/lists/*

USER 10001
CMD ["/app/server"]
