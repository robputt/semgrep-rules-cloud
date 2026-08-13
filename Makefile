SEMGREP ?= ./venv/bin/semgrep
LANG_DIRS := python javascript java go

.PHONY: help test validate scan stats clean

help:
	@echo "test      Run the annotated rule tests for every language directory"
	@echo "validate  Check every rule file parses and has valid metadata"
	@echo "scan      Scan a target with the whole ruleset: make scan TARGET=../my-app"
	@echo "stats     Count rules per language and per anti-pattern category"

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

stats:
	@echo "rules per language:"
	@for dir in $(LANG_DIRS); do \
		printf '  %-12s %s\n' "$$dir" "$$(find $$dir -name '*.yaml' | wc -l | tr -d ' ')"; \
	done
	@echo "rules per anti-pattern category:"
	@grep -rh 'cloud-antipattern:' $(LANG_DIRS) \
		| sed 's/.*cloud-antipattern: *//' | sort | uniq -c | sed 's/^/  /'

clean:
	rm -rf .semgrep_logs
