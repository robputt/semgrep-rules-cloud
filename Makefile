SEMGREP ?= ./venv/bin/semgrep
LANG_DIRS := python javascript typescript java csharp kotlin go dockerfile

.PHONY: help test validate scan scan-dockerfiles stats coverage clean

help:
	@echo "test      Run the annotated rule tests for every language directory"
	@echo "validate  Check every rule file parses and has valid metadata"
	@echo "scan      Scan a target with the whole ruleset: make scan TARGET=../my-app"
	@echo "stats     Count rules per language and per anti-pattern category"
	@echo "coverage  Show the language x category matrix and list unwritten gaps"

# `semgrep --test` accepts a single root, so iterate the language directories.
test:
	@set -e; for dir in $(LANG_DIRS); do \
		echo "==> $$dir"; \
		$(SEMGREP) --test --quiet "$$dir"; \
	done

validate:
	@set -e; for dir in $(LANG_DIRS); do \
		$(SEMGREP) --validate --quiet --config "$$dir"; \
	done
	@echo "all rules valid"

TARGET ?= .
scan:
	@set -e; for dir in $(LANG_DIRS); do \
		$(SEMGREP) --config "$$dir" --metrics off "$(TARGET)"; \
	done

# Semgrep only recognises `Dockerfile`, `*.dockerfile` and `Containerfile`.
# Variants like `Dockerfile.prod` need to be named explicitly together with
# --scan-unknown-extensions.
scan-dockerfiles:
	@files=$$(find "$(TARGET)" \( -name 'Dockerfile*' -o -name '*.dockerfile' \
		-o -name 'Containerfile*' \) -not -path '*/venv/*' -not -path '*/.git/*'); \
	if [ -z "$$files" ]; then echo "no Dockerfiles under $(TARGET)"; exit 0; fi; \
	$(SEMGREP) --config dockerfile --metrics off --scan-unknown-extensions $$files

stats:
	@echo "rules per language:"
	@for dir in $(LANG_DIRS); do \
		printf '  %-12s %s\n' "$$dir" "$$(find $$dir -name '*.yaml' | wc -l | tr -d ' ')"; \
	done
	@echo "rules per anti-pattern category:"
	@grep -rh 'cloud-antipattern:' $(LANG_DIRS) \
		| sed 's/.*cloud-antipattern: *//' | sort | uniq -c | sed 's/^/  /'

PYTHON ?= ./venv/bin/python
coverage:
	@$(PYTHON) tools/coverage.py

clean:
	rm -rf .semgrep_logs
