import collections
import queue
from collections import defaultdict, deque
from typing import TYPE_CHECKING

# ruleid: module-level-mutable-state
sessions = {}

# ruleid: module-level-mutable-state
_pending_jobs = []

# ruleid: module-level-mutable-state
seen_ids = set()

# ruleid: module-level-mutable-state
counters = collections.defaultdict(int)

# ruleid: module-level-mutable-state
recent = deque(maxlen=100)

# ruleid: module-level-mutable-state
work = queue.Queue()

# ruleid: module-level-mutable-state
lookup = defaultdict(list)

# ok: module-level-mutable-state
DEFAULT_HEADERS = {}

# ok: module-level-mutable-state
ALLOWED_HOSTS = []

if TYPE_CHECKING:
    # ok: module-level-mutable-state
    annotations = {}


def handler(event):
    # ok: module-level-mutable-state
    local_buffer = []
    # ok: module-level-mutable-state
    local_index = {}
    local_buffer.append(event)
    return local_index


async def async_handler(event):
    # ok: module-level-mutable-state
    scratch = {}
    return scratch


class Aggregator:
    # ok: module-level-mutable-state
    registry = {}

    def __init__(self):
        # ok: module-level-mutable-state
        self.items = []
