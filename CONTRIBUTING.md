# Contributing

## Layout

Rules follow the upstream `semgrep-rules` convention:

```
<language>/<framework-or-lang>/<category>/<rule-id>.yaml
<language>/<framework-or-lang>/<category>/<rule-id>.<ext>   # annotated test file
```

`lang` is used when a rule applies to the language generally; otherwise use the
framework or library name (`flask`, `django`, `sqlalchemy`, `express`, `nestjs`,
`typeorm`, `spring`, `aspnetcore`, `ktor`). Dockerfile rules live under
`dockerfile/lang/<category>/` with `.dockerfile` test files.

Rules in `javascript/` declare `languages: [javascript, typescript]` and so apply
to `.ts` files as well. Put a rule in `typescript/` only when it needs TS-only
syntax, such as decorators or typed class members; otherwise add it to
`javascript/` and let both languages benefit.
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
| `privileges` | Containers running with more privilege than the workload needs |
| `image-hygiene` | Image size, layer caching and build reproducibility |
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

### C# specifics

- `new T(...) { ... }` is the general object-creation pattern: it matches the
  plain form, the argument form and the object/collection-initializer form.
  `new T(...)` alone misses `new T { ... }` and `new T("x") { ... }`.
- Target-typed `new()` needs its own branch. `static $T $N = new $I(...) { ... };`
  and `static $T $N = new(...) { ... };` together cover all four field shapes.
- With target-typed `new()` there is no implementation metavariable to filter on,
  so put the `metavariable-regex` on the declared type (`$TYPE`) instead.

### Kotlin specifics

- Generic type arguments are not addressable. `val $N = mutableMapOf<...>()` is a
  parse error, but the non-generic `val $N = mutableMapOf()` still matches
  `mutableMapOf<String, String>()`.
- `class $C` matches the entire class declaration, which makes it a usable
  `pattern-not-inside`. `class $C { ... }` and `object $O { ... }` are parse
  errors. Because `object` declarations are not matched by `class $C`, excluding
  classes still leaves singleton `object` state flagged, which is what you want.
- `companion object` does not parse, so companion-object state is currently a
  known gap in `top-level-mutable-state`.
- Expression-body functions need `fun $F(...) = $EXPR`; a `return $EXPR` pattern
  will not match them. Note the match starts at the `fun` keyword, so test
  annotations go before that line.

### TypeScript specifics

- Class members do not parse as standalone patterns. Match the whole class
  (`class $C { static $NAME = new Map(...); ... }`) and use
  `focus-metavariable: $NAME` to report on the member rather than the class.
- `@Injectable()` requires zero arguments, so it does not match
  `@Injectable({ scope: Scope.REQUEST })`. That happens to give the right
  behaviour for singleton-scope rules, but be deliberate about it.

### Dockerfile specifics

- `RUN apt-get install ...` matches inside a `&&` chain, so `pattern-not:
  RUN ... rm -rf /var/lib/apt/lists/*` is the way to require cleanup in the same
  layer. Match flags positionally with care: `apt-get install
  --no-install-recommends` misses `apt-get install -y --no-install-recommends`,
  so use `RUN ... --no-install-recommends ...`.
- BuildKit mount flags are not addressable as patterns — `RUN --mount=... pip
  install ...` matches nothing. The matched range does cover the flag text, so
  exclude them with `pattern-not-regex: --mount=type=cache`.
- Shell form versus exec form: `CMD ...` matches both, `CMD [...]` matches only
  exec form. Combine them to detect shell form.
- The `ENV` variable *name* is not a metavariable slot. `ENV $K=$V` matches
  nothing while `ENV API_KEY=$V` and `ARG $K=$V` both work. Use `pattern-regex`
  when you need to match on the variable name.
- `metavariable-comparison` does not bind `EXPOSE` ports. Use
  `metavariable-regex` with an explicit numeric range instead.
- Absence checks such as "no `USER` before the entrypoint" work by matching the
  entrypoint and excluding `pattern-not-inside: "USER $U\n..."`. Do not match
  `FROM $IMAGE\n...` directly: it produces one finding per following
  instruction. Note the `USER ... / ...` range extends to end of file and
  ignores stage boundaries.
- Semgrep only recognises `Dockerfile`, `*.dockerfile` and `Containerfile`.
  Variants like `Dockerfile.prod` must be named explicitly alongside
  `--scan-unknown-extensions`; see the `scan-dockerfiles` Make target.

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
