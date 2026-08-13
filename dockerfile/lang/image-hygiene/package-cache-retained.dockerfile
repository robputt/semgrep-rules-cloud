# syntax=docker/dockerfile:1
FROM python:3.12-slim
WORKDIR /app
COPY requirements.txt package-lock.json package.json ./

# ruleid: package-cache-retained
RUN pip install -r requirements.txt

# ruleid: package-cache-retained
RUN npm ci --omit=dev

# ruleid: package-cache-retained
RUN apk add jq

# ok: package-cache-retained
RUN pip install --no-cache-dir -r requirements.txt

# ok: package-cache-retained
RUN --mount=type=cache,target=/root/.cache/pip pip install -r requirements.txt

# ok: package-cache-retained
RUN npm ci --omit=dev && npm cache clean --force

# ok: package-cache-retained
RUN apk add --no-cache jq

USER 10001
CMD ["python", "app.py"]
