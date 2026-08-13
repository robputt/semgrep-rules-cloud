import logging
import logging.handlers
import sys
from logging.handlers import RotatingFileHandler

logger = logging.getLogger(__name__)


def configure_file_logging():
    # ruleid: log-to-local-file
    handler = logging.FileHandler("/var/log/app/app.log")
    logger.addHandler(handler)


def configure_rotating():
    # ruleid: log-to-local-file
    logger.addHandler(RotatingFileHandler("app.log", maxBytes=10_000_000))


def configure_timed():
    # ruleid: log-to-local-file
    handler = logging.handlers.TimedRotatingFileHandler("app.log", when="midnight")
    logger.addHandler(handler)


def configure_basic_file():
    # ruleid: log-to-local-file
    logging.basicConfig(filename="app.log", level=logging.INFO)


def configure_stdout():
    # ok: log-to-local-file
    logging.basicConfig(stream=sys.stdout, level=logging.INFO)


def configure_stream_handler():
    # ok: log-to-local-file
    logger.addHandler(logging.StreamHandler(sys.stdout))
