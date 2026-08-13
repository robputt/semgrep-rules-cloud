# semgrep-rules-cloud

Semgrep rules for cloud anti-patterns rather than security. Detect statefulness,
process-local caching, poor connection pooling, config and secrets baked into
code or images, and other markers that tell you how well a codebase will behave
when it is containerised, replicated and rescheduled.

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

Semgrep only auto-detects `Dockerfile`, `*.dockerfile` and `Containerfile`. If
you use variants such as `Dockerfile.prod`, there is a target that finds them and
passes them explicitly:

```sh
make scan-dockerfiles TARGET=../my-app
```

## What it finds

70 rules across Python, JavaScript, TypeScript, Java, C#, Kotlin, Go and
Dockerfile.

| Language | Rules | Frameworks covered |
| --- | --- | --- |
| `python` | 15 | Flask, Django, SQLAlchemy |
| `dockerfile` | 14 | — |
| `csharp` | 9 | ASP.NET Core, Kestrel, Hangfire, Serilog |
| `kotlin` | 8 | Spring, Ktor |
| `javascript` | 8 | Express (also applies to `.ts`) |
| `java` | 6 | Spring |
| `go` | 6 | — |
| `typescript` | 4 | NestJS, TypeORM |

The `javascript` rules declare `languages: [javascript, typescript]`, so they
already apply to `.ts` files. The `typescript` tree holds rules that need TS-only
syntax: decorators and typed class members.

| Category | Examples |
| --- | --- |
| `statefulness` | module-level mutable collections, `global` mutation, Flask filesystem sessions, Django file session backend, `HttpSession` attributes, static mutable collections in Java/C#/TypeScript, Kotlin `object` singletons, package-level maps in Go, Express MemoryStore, Ktor `SessionStorageMemory`, ASP.NET Data Protection keys on local disk, NestJS singleton providers holding state |
| `caching` | `cachetools`/`diskcache`/`shelve`, hand-rolled dict memoisation, `node-cache`/`lru-cache`, Django `LocMemCache`/`FileBasedCache`, Spring `ConcurrentMapCacheManager`, Caffeine/Guava, ASP.NET `AddMemoryCache` and `AddDistributedMemoryCache`, `bigcache`/`go-cache` |
| `connection-management` | SQLAlchemy engines with default pooling, `NullPool`, connections opened inside Flask handlers, unpooled `pg.Client`, `DriverManager.getConnection`, `new HttpClient()`, TypeORM `DataSource` with no pool bound, `sql.Open` without `SetMaxOpenConns` |
| `configuration` | hardcoded Postgres/MySQL/Mongo/Redis/AMQP/JDBC/ADO.NET connection strings, unpinned base images |
| `secrets` | `os.getenv("SECRET_KEY", "changeme")` fallbacks, `ENV API_KEY=...` baked into layers, secrets passed via `--build-arg`, `COPY .env` / `id_rsa` / `*.pem` |
| `filesystem` | writes to relative paths and `/tmp` treated as durable storage |
| `scheduling` | APScheduler, `schedule`, `threading.Timer`, `node-cron`, `@Scheduled` and NestJS `@Cron` without a distributed lock, Hangfire in-memory storage |
| `networking` | servers bound to `127.0.0.1` instead of `0.0.0.0`, Kestrel `UseUrls("http://localhost")` and `ListenLocalhost`, Ktor `host = "127.0.0.1"` |
| `lifecycle` | Go HTTP servers started without SIGTERM handling, shell-form `CMD` that stops signals reaching PID 1 |
| `privileges` | images with no `USER`, `USER root`, `EXPOSE` on privileged ports |
| `image-hygiene` | apt lists left in the layer, missing `--no-install-recommends`, retained pip/npm/apk caches, `COPY . .`, `ADD https://...`, `curl \| sh` |
| `observability` | logging to files instead of stdout, Serilog `WriteTo.File`, NLog `FileTarget` |

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
typescript/nestjs/scheduling/uncoordinated-cron.yaml
java/spring/scheduling/uncoordinated-scheduled-task.yaml
csharp/aspnetcore/caching/in-process-memory-cache.yaml
kotlin/ktor/statefulness/in-memory-session-storage.yaml
go/lang/connections/missing-db-pool-configuration.yaml
dockerfile/lang/lifecycle/shell-form-entrypoint.yaml
dockerfile/lang/lifecycle/shell-form-entrypoint.dockerfile
```

The test files are worth reading on their own: each one shows the anti-pattern
next to the cloud-native alternative.

## Severity and confidence

`severity` reflects whether the code is already broken in a container:

- `ERROR` — broken now. Bound to localhost, credential committed, sessions on
  local disk, shell-form `CMD` swallowing SIGTERM, image that fails
  `runAsNonRoot` admission.
- `WARNING` — works on one instance, degrades or corrupts as you scale.
- `INFO` — worth fixing but costs you bytes, not correctness.

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

The Dockerfile rules are a good example of the payoff: a single Dockerfile with
`FROM python:latest`, `ENV API_KEY=...`, `COPY . .`, `EXPOSE 80` and
`CMD python app.py` produces seven findings spanning reproducibility, secrets
management, cache behaviour, privileges and signal handling.

See [CONTRIBUTING.md](CONTRIBUTING.md) for the layout rules, the category
taxonomy, and a list of Semgrep pattern gotchas worth knowing before you write a
rule.

## Licence

Rules are MIT licensed. See [LICENSE](LICENSE).
