#!/usr/bin/env python3
"""
Daily auto-update script for fullstack-kit.
-------------------------------------------
Designed to look like natural developer activity:
- Picked daily target of 1/2/3 commits decided ONCE at the start of each day.
- Script can be triggered multiple times a day (by launchd at 4 different times).
- Each trigger checks if today's target is reached; if not, it commits one batch.
- This way, some days have 1 commit at 9 AM, others have 3 commits spread across the day.
"""

import os
import sys
import json
import re
import random
import subprocess
from datetime import datetime, timedelta
from pathlib import Path

# Project root
ROOT = Path(__file__).resolve().parent.parent
CONTENT_DIR = ROOT / "scripts" / "content_pool"
STATE_FILE = ROOT / "scripts" / ".state.json"

# ---------------------------------------------------------------------------
# State management
# ---------------------------------------------------------------------------

def load_state():
    if STATE_FILE.exists():
        with open(STATE_FILE) as f:
            return json.load(f)
    return {
        "used": [],
        "total_items": 0,
        "last_run": None,
        "today_date": None,
        "today_target": 0,
        "today_done": 0,
    }

def save_state(state):
    with open(STATE_FILE, "w") as f:
        json.dump(state, f, indent=2)

# ---------------------------------------------------------------------------
# Daily target logic — decide how many commits to make today (once per day)
# ---------------------------------------------------------------------------

def refresh_daily_target(state):
    """
    At the start of each new day, randomly pick today's commit target (1, 2, or 3).
    Weights: 1→35%, 2→45%, 3→20%
    """
    today_str = datetime.now().strftime("%Y-%m-%d")
    if state.get("today_date") != today_str:
        # New day — reset counters and pick new target
        state["today_date"] = today_str
        state["today_done"] = 0
        state["today_target"] = random.choices([1, 2, 3], weights=[35, 45, 20])[0]
        print(f"📅 New day ({today_str}). Today's commit target: {state['today_target']}")
    else:
        print(f"📅 Today ({today_str}): {state['today_done']}/{state['today_target']} commits done.")
    return state

def should_commit_now(state):
    """Return True if we still have commits to make today."""
    return state["today_done"] < state["today_target"]

# ---------------------------------------------------------------------------
# Content pool loader
# ---------------------------------------------------------------------------

def load_content_pool():
    pool = []
    for f in sorted(CONTENT_DIR.glob("*.json")):
        with open(f) as fh:
            data = json.load(fh)
            pool.extend(data)
    return pool

def pick_items(pool, state):
    """Pick 1 batch of items (1-2 items) for this commit run."""
    count = random.choices([1, 2], weights=[60, 40])[0]
    available = [i for i, item in enumerate(pool) if i not in state["used"]]
    if len(available) < count:
        # Reset used list when pool is exhausted
        state["used"] = []
        available = list(range(len(pool)))
    picked_indices = random.sample(available, min(count, len(available)))
    state["used"].extend(picked_indices)
    return [pool[i] for i in picked_indices]

# ---------------------------------------------------------------------------
# File creation
# ---------------------------------------------------------------------------

def create_content_file(item):
    rel_path = item["path"]
    content = item["content"]
    full_path = ROOT / rel_path
    full_path.parent.mkdir(parents=True, exist_ok=True)

    if full_path.exists():
        with open(full_path, "a") as f:
            f.write("\n\n" + content)
    else:
        with open(full_path, "w") as f:
            f.write(content)
    return rel_path

# ---------------------------------------------------------------------------
# README stats updater
# ---------------------------------------------------------------------------

def count_files(directory, extensions=None):
    if extensions is None:
        extensions = {".js", ".py", ".ts", ".tsx", ".jsx", ".sql", ".css", ".md"}
    count = 0
    d = ROOT / directory
    if d.exists():
        for f in d.rglob("*"):
            if f.is_file() and f.suffix in extensions:
                count += 1
    return count

def update_readme(total_items):
    readme_path = ROOT / "README.md"
    content = readme_path.read_text()
    today = datetime.now().strftime("%Y-%m-%d")

    algo_cats = {
        "Sorting": count_files("algorithms/sorting"),
        "Searching": count_files("algorithms/searching"),
        "Dynamic Programming": count_files("algorithms/dynamic-programming"),
        "Graphs": count_files("algorithms/graphs"),
        "Trees": count_files("algorithms/trees"),
        "Strings": count_files("algorithms/strings"),
    }
    snippet_cats = {
        "JavaScript": count_files("snippets/javascript"),
        "Python": count_files("snippets/python"),
        "React": count_files("snippets/react"),
        "Node.js": count_files("snippets/node"),
        "SQL": count_files("snippets/sql"),
        "CSS": count_files("snippets/css"),
        "TypeScript": count_files("snippets/typescript"),
    }
    concept_cats = {
        "System Design": count_files("concepts/system-design"),
        "Interview Prep": count_files("concepts/interview-prep"),
        "Dev Tips": count_files("concepts/dev-tips"),
        "Design Patterns": count_files("concepts/design-patterns"),
    }

    total_algos = sum(algo_cats.values())
    total_snippets = sum(snippet_cats.values())
    total_concepts = sum(concept_cats.values())

    content = re.sub(r'Last%20Updated-.*?-brightgreen', f'Last%20Updated-{today}-brightgreen', content)
    content = re.sub(r'Algorithms-\d+-blue', f'Algorithms-{total_algos}-blue', content)
    content = re.sub(r'Snippets-\d+-orange', f'Snippets-{total_snippets}-orange', content)
    content = re.sub(r'Concepts-\d+-purple', f'Concepts-{total_concepts}-purple', content)

    algo_table = "| Category | Count |\n|----------|-------|\n"
    for cat, cnt in algo_cats.items():
        algo_table += f"| {cat} | {cnt} |\n"
    content = re.sub(r'(## 🧮 Algorithms\n\n).*?(?=\n## )', f'\\1{algo_table}\n', content, flags=re.DOTALL)

    snip_table = "| Language/Framework | Count |\n|-------------------|-------|\n"
    for cat, cnt in snippet_cats.items():
        snip_table += f"| {cat} | {cnt} |\n"
    content = re.sub(r'(## 📝 Code Snippets\n\n).*?(?=\n## )', f'\\1{snip_table}\n', content, flags=re.DOTALL)

    conc_table = "| Category | Count |\n|----------|-------|\n"
    for cat, cnt in concept_cats.items():
        conc_table += f"| {cat} | {cnt} |\n"
    content = re.sub(r'(## 📚 Concepts & Notes\n\n).*?(?=\n<!--)', f'\\1{conc_table}\n', content, flags=re.DOTALL)

    content = re.sub(
        r'<!-- STATS_START -->.*?<!-- STATS_END -->',
        f'<!-- STATS_START -->\n**📊 Total Items: {total_items} | Last auto-update: {today}**\n<!-- STATS_END -->',
        content, flags=re.DOTALL)

    readme_path.write_text(content)

# ---------------------------------------------------------------------------
# Daily log
# ---------------------------------------------------------------------------

def create_daily_log(items, run_number):
    today = datetime.now().strftime("%Y-%m-%d")
    now = datetime.now().strftime("%H:%M")
    log_path = ROOT / "daily" / f"{today}.md"

    if log_path.exists():
        existing = log_path.read_text()
    else:
        existing = f"# Daily Update — {today}\n\n"

    entry = f"\n## Commit #{run_number} at {now}\n"
    for item in items:
        entry += f"- **{item['title']}** → `{item['path']}`\n"

    log_path.write_text(existing + entry)
    return f"daily/{today}.md"

# ---------------------------------------------------------------------------
# Git operations
# ---------------------------------------------------------------------------

COMMIT_PREFIXES = ["feat", "add", "docs", "refactor", "chore", "update", "improve", "fix", "perf"]

COMMIT_TEMPLATES = [
    "{prefix}: add {title}",
    "{prefix}: {title} implementation",
    "{prefix}({category}): add {title}",
    "{prefix}: new {category} — {title}",
    "{prefix}: add {title} with examples",
    "{prefix}({category}): {title}",
    "{prefix}: implement {title}",
]

def git_commit_and_push(items):
    os.chdir(ROOT)
    subprocess.run(["git", "add", "."], check=True)

    prefix = random.choice(COMMIT_PREFIXES)
    template = random.choice(COMMIT_TEMPLATES)

    if len(items) == 1:
        msg = template.format(
            prefix=prefix,
            title=items[0]["title"].lower(),
            category=items[0]["category"]
        )
    else:
        titles = ", ".join(i["title"].lower() for i in items[:2])
        if len(items) > 2:
            titles += f" and {len(items)-2} more"
        msg = f"{prefix}: add {titles}"

    subprocess.run(["git", "commit", "-m", msg], check=True)
    subprocess.run(["git", "push", "origin", "main"], check=True)
    print(f"✅ Committed and pushed: {msg}")

# ---------------------------------------------------------------------------
# Main
# ---------------------------------------------------------------------------

def main():
    print(f"\n🚀 Script triggered — {datetime.now().strftime('%Y-%m-%d %H:%M')}")

    state = load_state()
    pool = load_content_pool()

    if not pool:
        print("❌ No content in pool!")
        sys.exit(1)

    # Check if we've already hit today's target
    state = refresh_daily_target(state)

    if not should_commit_now(state):
        print(f"✅ Today's target already reached ({state['today_done']}/{state['today_target']}). Skipping.")
        sys.exit(0)

    # Pick items for this commit
    items = pick_items(pool, state)
    print(f"📦 Picked {len(items)} items for commit #{state['today_done'] + 1}")

    # Create content files
    for item in items:
        path = create_content_file(item)
        print(f"  ✏️  Created: {path}")

    # Create daily log
    run_number = state["today_done"] + 1
    log_path = create_daily_log(items, run_number)
    print(f"  📝 Log: {log_path}")

    # Update README
    state["total_items"] = state.get("total_items", 0) + len(items)
    state["last_run"] = datetime.now().isoformat()
    update_readme(state["total_items"])

    # Increment today's done counter
    state["today_done"] += 1

    # Save state
    save_state(state)

    # Git commit and push
    git_commit_and_push(items)

    remaining = state["today_target"] - state["today_done"]
    if remaining > 0:
        print(f"⏳ {remaining} more commit(s) scheduled for later today.")
    else:
        print(f"🎉 All {state['today_target']} commits done for today!")


if __name__ == "__main__":
    main()
