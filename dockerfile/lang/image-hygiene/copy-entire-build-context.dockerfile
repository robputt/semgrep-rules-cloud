FROM python:3.12-slim AS greedy
WORKDIR /app
# ruleid: copy-entire-build-context
COPY . .
RUN pip install --no-cache-dir -r requirements.txt

FROM python:3.12-slim AS greedy-add
WORKDIR /app
# ruleid: copy-entire-build-context
ADD . /app

FROM python:3.12-slim AS layered
WORKDIR /app
# ok: copy-entire-build-context
COPY requirements.txt ./
RUN pip install --no-cache-dir -r requirements.txt
# ok: copy-entire-build-context
COPY src/ ./src/
# ok: copy-entire-build-context
COPY --from=greedy /app/dist ./dist
USER 10001
CMD ["python", "-m", "src.main"]
