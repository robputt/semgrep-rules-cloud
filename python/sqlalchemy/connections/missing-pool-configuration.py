import os

import sqlalchemy
from sqlalchemy import create_engine
from sqlalchemy.ext.asyncio import create_async_engine

URL = os.environ["DATABASE_URL"]

# ruleid: missing-pool-configuration
engine = create_engine(URL)

# ruleid: missing-pool-configuration
noisy_engine = sqlalchemy.create_engine(URL, echo=True)

# ruleid: missing-pool-configuration
async_engine = create_async_engine(URL)

# ok: missing-pool-configuration
tuned_engine = create_engine(
    URL,
    pool_size=int(os.environ["DB_POOL_SIZE"]),
    max_overflow=2,
    pool_recycle=300,
    pool_pre_ping=True,
)

# ok: missing-pool-configuration
tuned_async_engine = create_async_engine(URL, pool_pre_ping=True, pool_recycle=300)

POOL_OPTS = {"pool_size": 5, "pool_pre_ping": True}

# ok: missing-pool-configuration
kwargs_engine = create_engine(URL, **POOL_OPTS)
