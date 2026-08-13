FROM python:3.12-slim AS build
WORKDIR /app
COPY requirements.txt ./
RUN pip install --no-cache-dir --prefix=/install -r requirements.txt

FROM python:3.12-slim AS runtime-root
COPY --from=build /install /usr/local
COPY app.py ./
# ruleid: missing-non-root-user
CMD ["python", "app.py"]

FROM python:3.12-slim AS runtime-nonroot
RUN useradd --uid 10001 --create-home appuser
COPY --from=build /install /usr/local
COPY app.py ./
USER 10001
# ok: missing-non-root-user
CMD ["python", "app.py"]
