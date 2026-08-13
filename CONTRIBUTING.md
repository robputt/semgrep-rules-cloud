# Contributing

## Layout

Rules follow the upstream `semgrep-rules` convention:

```
<language>/<framework-or-lang>/<category>/<rule-id>.yaml
<language>/<framework-or-lang>/<category>/<rule-id>.<ext>   # annotated test file
```

`lang` is used when a rule applies to the language generally; otherwise use the
framework or library name (`flask`, `django`, `sqlalchemy`, `express`, `spring`).
The rule `id` is the filename without its extension; the directory path supplies
the namespace, so the registry ID becomes
`python.flask.statefulness.flask-local-session-store`.

## Categories

Every rule lives under one of these category directories and repeats it in
`metadata.cloud-antipattern`:

| Category | What it covers |
| --- | --- |
| `statefulness` | In-process state that breaks when replicas scale or restart |
| `caching` | Process-local caches instead of a shared cache tier |
| `connection-management` | Missing, disabled or per-request connection pools |
| `configuration` | Config baked into code rather than read from the environment |
| `secrets` | Credentials committed to source or defaulted in code |
| `filesystem` | Treating ephemeral container disk as durable or shared storage |
| `scheduling` | In-process timers and cron that fire once per replica |
| `lifecycle` | Startup and shutdown behaviour, signal handling, readiness |
| `networking` | Port binding and addressing that does not work in a container |
| `observability` | Logs and metrics that never leave the container |
| `resilience` | Missing timeouts, retries and backoff against backing services |

## Writing a rule

Start from [`template.yaml`](template.yaml).

1. Prefer syntactic patterns over `pattern-regex`. Reach for regex only when
   matching opaque strings such as connection URLs.
2. Write the message for someone who has never heard of the anti-pattern. State
   what breaks in a horizontally scaled, ephemeral environment and name the
   concrete replacement. "Use Redis" beats "avoid local state".
3. Set `severity: ERROR` only when the code is broken in a container today
   (binding to localhost, a committed credential). Use `WARNING` for designs
   that degrade as you scale.
4. Set `confidence` honestly. Heuristic rules such as
   `module-level-mutable-state` are `LOW` and expected to need triage.
5. Keep the rule ID identical to the filename.

### Semgrep gotchas found while writing these rules

- `metavariable-regex` is applied with `re.match`, so it is anchored at the
  start. Write `^['"].*Foo['"]$`, not `Foo['"]$`.
- In JavaScript patterns, `var $X = ...` also matches `let` and `const`, but
  `var $X = {}` then matches *any* object literal rather than only empty ones.
  Add `pattern-not: "var $X = {..., $K: $V, ...}"` when you mean empty.
- Semgrep resolves `const { Client } = require('pg')`, so `new pg.Client(...)`
  catches the destructured form too.
- `pattern-not` only cancels a match at the *same* range. To exclude "this call,
  but only when a later call also appears", use `pattern-not-inside` with the
  multi-statement sequence instead.
- Quote any pattern or regex containing `: `, `{` or `&`, otherwise YAML parsing
  fails.
- Generic instantiation such as `pkg.New[$K, $V](...)` does not parse in Go
  patterns. Match `pkg.New(...)` instead.

## Tests

Every rule needs a sibling test file with the same basename. Annotate the line
*immediately before* each expected finding:

```python
# ruleid: local-in-memory-cache
profile_cache = cachetools.TTLCache(maxsize=1024, ttl=300)

# ok: local-in-memory-cache
shared = redis.Redis.from_url(os.environ["REDIS_URL"])
```

Include realistic `ok` cases showing the cloud-native alternative. The test file
doubles as the rule's documentation.

Annotations bind to the line where the match *starts*. If a finding spans a
multi-line call, the annotation goes before the first line of the matched range,
which is not always the statement you expect. Restructure the test snippet if it
gets awkward.

Run:

```sh
make test       # annotated rule tests
make validate   # rule syntax and metadata
```

Both run in CI on every pull request.
