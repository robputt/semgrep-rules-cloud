FROM python:3.12-slim AS shell-form
USER 10001
# ruleid: shell-form-entrypoint
CMD python app.py

FROM python:3.12-slim AS shell-form-entrypoint
USER 10001
# ruleid: shell-form-entrypoint
ENTRYPOINT /app/server --port 8080

FROM python:3.12-slim AS exec-form
USER 10001
# ok: shell-form-entrypoint
CMD ["python", "app.py"]

FROM python:3.12-slim AS exec-form-entrypoint
USER 10001
# ok: shell-form-entrypoint
ENTRYPOINT ["/app/server", "--port", "8080"]
# ok: shell-form-entrypoint
CMD ["--verbose"]
