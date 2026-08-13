import os

import sqlalchemy.pool
from sqlalchemy import create_engine
from sqlalchemy.pool import NullPool, QueuePool

URL = os.environ["DATABASE_URL"]

# ruleid: disabled-connection-pool
engine = create_engine(URL, poolclass=NullPool)

# ruleid: disabled-connection-pool
static_engine = create_engine(URL, poolclass=sqlalchemy.pool.StaticPool)

# ok: disabled-connection-pool
pooled = create_engine(URL, poolclass=QueuePool, pool_size=10, max_overflow=5)
