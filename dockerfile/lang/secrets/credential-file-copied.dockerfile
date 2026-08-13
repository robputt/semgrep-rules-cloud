FROM python:3.12-slim
WORKDIR /app

# ruleid: credential-file-copied
COPY .env /app/.env

# ruleid: credential-file-copied
COPY .npmrc /root/.npmrc

# ruleid: credential-file-copied
COPY secrets/id_rsa /root/.ssh/id_rsa

# ruleid: credential-file-copied
COPY certs/client.pem /etc/ssl/client.pem

# ruleid: credential-file-copied
ADD service-account.json /app/sa.json

# ok: credential-file-copied
COPY requirements.txt ./

# ok: credential-file-copied
COPY app/ /app/

# ok: credential-file-copied
COPY config/settings.yaml /app/settings.yaml

USER 10001
CMD ["python", "app.py"]
