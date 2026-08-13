import os

# ruleid: hardcoded-secret-fallback
SECRET_KEY = os.getenv("SECRET_KEY", "dev-not-so-secret")

# ruleid: hardcoded-secret-fallback
DB_PASSWORD = os.environ.get("DATABASE_PASSWORD", "postgres")

# ruleid: hardcoded-secret-fallback
GITHUB_TOKEN = os.getenv("GITHUB_API_TOKEN", "ghp_examplevalue")

# ruleid: hardcoded-secret-fallback
SIGNING_KEY = os.environ.get("JWT_SIGNING_KEY", "changeme")

# ok: hardcoded-secret-fallback
STRICT_SECRET = os.environ["SECRET_KEY"]

# ok: hardcoded-secret-fallback
OPTIONAL_SECRET = os.getenv("SECRET_KEY")

# ok: hardcoded-secret-fallback
EMPTY_DEFAULT = os.getenv("API_KEY", "")

# ok: hardcoded-secret-fallback
LOG_LEVEL = os.getenv("LOG_LEVEL", "INFO")

# ok: hardcoded-secret-fallback
TIMEOUT = os.getenv("REQUEST_TIMEOUT", "30")
