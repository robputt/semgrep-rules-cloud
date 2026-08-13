# semgrep-rules-cloud

Semgrep rules for cloud anti-patterns rather than security. Detect statefulness,
process-local caching, poor connection pooling, config and secrets baked into
code, and other markers that tell you how well a codebase will behave when it is
containerised, replicated and rescheduled.

Security scanners answer "can this be exploited?". These rules answer "will this
survive being run as three replicas that get killed and rescheduled?".

## Quick start

```sh
python3 -m venv venv
./venv/bin/pip install semgrep

# scan your application with the whole ruleset
make scan TARGET=../my-app
```

Or point Semgrep at a single language tree:

```sh
semgrep --config python ../my-app
```

## What it finds

35 rules across Python, JavaScript/TypeScript, Java and Go.

| Category | Examples |
| --- | --- |
| `statefulness` | module-level mutable collections, `global` mutation, Flask filesystem sessions, Django file session backend, `HttpSession` attributes, static mutable maps in Java, package-level maps in Go, Express MemoryStore |
| `caching` | `cachetools`/`diskcache`/`shelve`, hand-rolled dict memoisation, `node-cache`/`lru-cache`, Django `LocMemCache`/`FileBasedCache`, Spring `ConcurrentMapCacheManager`, `bigcache`/`go-cache` |
| `connection-management` | SQLAlchemy engines with default pooling, `NullPool`, connections opened inside Flask handlers, unpooled `pg.Client`, `DriverManager.getConnection`, `sql.Open` without `SetMaxOpenConns` |
| `configuration` | hardcoded Postgres/MySQL/Mongo/Redis/AMQP/JDBC connection strings |
| `secrets` | `os.getenv("SECRET_KEY", "changeme")` style hardcoded fallbacks |
| `filesystem` | writes to relative paths and `/tmp` treated as durable storage |
| `scheduling` | APScheduler, `schedule`, `threading.Timer`, `node-cron`, `@Scheduled` without a distributed lock |
| `networking` | servers bound to `127.0.0.1` instead of `0.0.0.0` |
| `lifecycle` | Go HTTP servers started without SIGTERM handling |
| `observability` | logging to files instead of stdout |

Findings map to the [twelve-factor](https://12factor.net) factors they violate
via `metadata.twelve-factor`, so you can slice a report by concern:

```sh
semgrep --config python --json ../my-app \
  | jq -r '.results[].extra.metadata["cloud-antipattern"]' | sort | uniq -c
```

## Layout

Standard `semgrep-rules` layout: `<language>/<framework-or-lang>/<category>/`,
with an annotated test file beside every rule.

```
python/lang/statefulness/module-level-mutable-state.yaml
python/lang/statefulness/module-level-mutable-state.py
python/flask/statefulness/flask-local-session-store.yaml
python/flask/statefulness/flask-local-session-store.py
javascript/express/statefulness/express-memory-session-store.yaml
java/spring/scheduling/uncoordinated-scheduled-task.yaml
go/lang/connections/missing-db-pool-configuration.yaml
```

The test files are worth reading on their own: each one shows the anti-pattern
next to the cloud-native alternative.

## Severity and confidence

`severity` reflects whether the code is already broken in a container:

- `ERROR` — broken now. Bound to localhost, credential committed, sessions on
  local disk.
- `WARNING` — works on one instance, degrades or corrupts as you scale.

`confidence` tells you how much triage to expect. `LOW` rules such as
`module-level-mutable-state` are deliberately broad heuristics: a module-level
`Map` is sometimes a legitimate lookup table. Filter them out for a first pass:

```sh
semgrep --config python --severity ERROR ../my-app
```

## Development

```sh
make test       # run the annotated rule tests
make validate   # check rule syntax and metadata
make stats      # rule counts per language and category
```

See [CONTRIBUTING.md](CONTRIBUTING.md) for the layout rules, the category
taxonomy, and a list of Semgrep pattern gotchas worth knowing before you write a
rule.

## Licence

Rules are MIT licensed. See [LICENSE](LICENSE).
