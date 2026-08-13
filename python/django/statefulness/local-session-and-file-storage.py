import os

# ruleid: local-session-and-file-storage
SESSION_ENGINE = "django.contrib.sessions.backends.file"

# ruleid: local-session-and-file-storage
DEFAULT_FILE_STORAGE = "django.core.files.storage.FileSystemStorage"

STORAGES = {
    # ruleid: local-session-and-file-storage
    "default": {
        "BACKEND": "django.core.files.storage.FileSystemStorage",
    },
    # ok: local-session-and-file-storage
    "staticfiles": {
        "BACKEND": "storages.backends.s3boto3.S3Boto3Storage",
    },
}

MEDIA_URL = os.environ.get("MEDIA_URL", "/media/")
