import os
import pathlib
import shutil
from pathlib import Path

import boto3


def save_report(report):
    # ruleid: local-file-persistence
    with open("reports/latest.json", "w", encoding="utf-8") as handle:
        handle.write(report)


def append_audit(line):
    # ruleid: local-file-persistence
    with open("/tmp/audit.log", "a") as handle:
        handle.write(line)


def cache_blob(blob):
    # ruleid: local-file-persistence
    Path("./cache/blob.bin").write_bytes(blob)


def ensure_upload_dir():
    # ruleid: local-file-persistence
    os.makedirs("uploads/incoming", exist_ok=True)


def archive(src):
    # ruleid: local-file-persistence
    shutil.copyfile(src, "/var/data/archive.tar")


def stage(src):
    # ruleid: local-file-persistence
    pathlib.Path("/tmp/staging/input.csv").write_text(src)


def read_config():
    # ok: local-file-persistence
    with open("config/settings.yaml", "r", encoding="utf-8") as handle:
        return handle.read()


def upload_report(report):
    # ok: local-file-persistence
    boto3.client("s3").put_object(
        Bucket=os.environ["REPORT_BUCKET"], Key="latest.json", Body=report
    )


def write_absolute_mount(report):
    # ok: local-file-persistence
    with open("/mnt/shared/reports/latest.json", "w") as handle:
        handle.write(report)
