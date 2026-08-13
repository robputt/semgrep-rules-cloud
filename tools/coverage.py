#!/usr/bin/env python3
"""Print a language x anti-pattern-category coverage matrix for the ruleset.

Gaps in this table are the point: an empty cell is either a deliberate
non-applicable combination or a rule nobody has written yet.
"""

from __future__ import annotations

import re
import sys
from collections import defaultdict
from pathlib import Path

LANGUAGES = [
    "python",
    "javascript",
    "typescript",
    "java",
    "csharp",
    "kotlin",
    "go",
    "dockerfile",
]

CATEGORIES = [
    "statefulness",
    "caching",
    "connection-management",
    "configuration",
    "secrets",
    "filesystem",
    "scheduling",
    "lifecycle",
    "networking",
    "observability",
    "privileges",
    "image-hygiene",
    "resilience",
]

# Combinations that intentionally do not apply.
NOT_APPLICABLE = {
    ("dockerfile", "statefulness"),
    ("dockerfile", "caching"),
    ("dockerfile", "connection-management"),
    ("dockerfile", "scheduling"),
    ("dockerfile", "networking"),
    ("dockerfile", "observability"),
    ("dockerfile", "resilience"),
    ("python", "privileges"),
    ("javascript", "privileges"),
    ("typescript", "privileges"),
    ("java", "privileges"),
    ("csharp", "privileges"),
    ("kotlin", "privileges"),
    ("go", "privileges"),
    ("python", "image-hygiene"),
    ("javascript", "image-hygiene"),
    ("typescript", "image-hygiene"),
    ("java", "image-hygiene"),
    ("csharp", "image-hygiene"),
    ("kotlin", "image-hygiene"),
    ("go", "image-hygiene"),
}

CATEGORY_RE = re.compile(r"^\s*cloud-antipattern:\s*(\S+)\s*$", re.MULTILINE)


def collect(root: Path) -> dict[tuple[str, str], int]:
    counts: dict[tuple[str, str], int] = defaultdict(int)
    for language in LANGUAGES:
        for rule in sorted((root / language).rglob("*.yaml")):
            for category in CATEGORY_RE.findall(rule.read_text(encoding="utf-8")):
                counts[(language, category)] += 1
    return counts


def main() -> int:
    root = Path(__file__).resolve().parent.parent
    counts = collect(root)

    width = max(len(c) for c in CATEGORIES) + 2
    header = "".join(f"{lang[:6]:>8}" for lang in LANGUAGES)
    print(f"{'category':<{width}}{header}")
    print("-" * (width + 8 * len(LANGUAGES)))

    unwritten: list[tuple[str, str]] = []
    for category in CATEGORIES:
        row = f"{category:<{width}}"
        for language in LANGUAGES:
            n = counts.get((language, category), 0)
            if n:
                cell = str(n)
            elif (language, category) in NOT_APPLICABLE:
                cell = "-"
            else:
                cell = "."
                unwritten.append((language, category))
            row += f"{cell:>8}"
        print(row)

    print()
    print(f"total rules: {sum(counts.values())}    "
          f"legend: number = rules, '-' = n/a, '.' = gap")

    unknown = sorted({c for (_, c) in counts} - set(CATEGORIES))
    if unknown:
        print(f"\nWARNING: categories not in the taxonomy: {', '.join(unknown)}")
        return 1

    if unwritten:
        print(f"\n{len(unwritten)} unwritten combinations:")
        for language, category in unwritten:
            print(f"  {language}/{category}")
    return 0


if __name__ == "__main__":
    sys.exit(main())
