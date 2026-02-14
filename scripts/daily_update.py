#!/usr/bin/env python3
"""
Daily auto-update script for fullstack-kit.
Picks 1-3 items from the content pool, creates files, updates README, commits & pushes.
Designed to look natural — varies commit count, timing, and messages.
"""

import os
import sys
import json
import random
import subprocess
from datetime import datetime, timedelta
from pathlib import Path

# Project root
ROOT = Path(__file__).resolve().parent.parent
CONTENT_DIR = ROOT / "scripts" / "content_pool"
STATE_FILE = ROOT / "scripts" / ".state.json"

# ---------------------------------------------------------------------------
# State management — tracks which content has been used
# ---------------------------------------------------------------------------

def load_state():
    if STATE_FILE.exists():
        with open(STATE_FILE) as f:
            return json.load(f)
    return {"used": [], "total_items": 0, "last_run": None}

def save_state(state):
    with open(STATE_FILE, "w") as f:
        json.dump(state, f, indent=2)

# ---------------------------------------------------------------------------
# Content pool loader
# ---------------------------------------------------------------------------

def load_content_pool():
    """Load all content JSON files from the content_pool directory."""
    pool = []
    for f in sorted(CONTENT_DIR.glob("*.json")):
        with open(f) as fh:
            data = json.load(fh)
            pool.extend(data)
    return pool

def pick_items(pool, state, count=None):
    """Pick 1-3 unused items from the pool. Resets if all used."""
    if count is None:
        count = random.choices([1, 2, 3], weights=[30, 50, 20])[0]
    available = [i for i, item in enumerate(pool) if i not in state["used"]]
    if len(available) < count:
        state["used"] = []
        available = list(range(len(pool)))
    picked_indices = random.sample(available, min(count, len(available)))
    state["used"].extend(picked_indices)
    return [pool[i] for i in picked_indices]

# ---------------------------------------------------------------------------
# File creation
# ---------------------------------------------------------------------------

def create_content_file(item):
    """Create the actual file from a content item."""
    rel_path = item["path"]
    content = item["content"]
    full_path = ROOT / rel_path
    full_path.parent.mkdir(parents=True, exist_ok=True)
    
    if full_path.exists():
        # Append or enhance existing file
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
    """Count files in a directory recursively."""
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
    """Update README.md with current stats."""
    readme_path = ROOT / "README.md"
    content = readme_path.read_text()
    today = datetime.now().strftime("%Y-%m-%d")
    
    # Count by category
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
    
    # Update badge counts
    total_algos = sum(algo_cats.values())
    total_snippets = sum(snippet_cats.values())
    total_concepts = sum(concept_cats.values())
    
    import re
    content = re.sub(
        r'Last%20Updated-.*?-brightgreen',
        f'Last%20Updated-{today}-brightgreen', content)
    content = re.sub(
        r'Algorithms-\d+-blue',
        f'Algorithms-{total_algos}-blue', content)
    content = re.sub(
        r'Snippets-\d+-orange',
        f'Snippets-{total_snippets}-orange', content)
    content = re.sub(
        r'Concepts-\d+-purple',
        f'Concepts-{total_concepts}-purple', content)
    
    # Update algorithm table
    algo_table = "| Category | Count |\n|----------|-------|\n"
    for cat, cnt in algo_cats.items():
        algo_table += f"| {cat} | {cnt} |\n"
    content = re.sub(
        r'(## 🧮 Algorithms\n\n).*?(?=\n## )',
        f'\\1{algo_table}\n', content, flags=re.DOTALL)
    
    # Update snippets table
    snip_table = "| Language/Framework | Count |\n|-------------------|-------|\n"
    for cat, cnt in snippet_cats.items():
        snip_table += f"| {cat} | {cnt} |\n"
    content = re.sub(
        r'(## 📝 Code Snippets\n\n).*?(?=\n## )',
        f'\\1{snip_table}\n', content, flags=re.DOTALL)
    
    # Update concepts table
    conc_table = "| Category | Count |\n|----------|-------|\n"
    for cat, cnt in concept_cats.items():
        conc_table += f"| {cat} | {cnt} |\n"
    content = re.sub(
        r'(## 📚 Concepts & Notes\n\n).*?(?=\n<!--)',
        f'\\1{conc_table}\n', content, flags=re.DOTALL)
    
    # Update stats line
    content = re.sub(
        r'<!-- STATS_START -->.*?<!-- STATS_END -->',
        f'<!-- STATS_START -->\n**📊 Total Items: {total_items} | Last auto-update: {today}**\n<!-- STATS_END -->',
        content, flags=re.DOTALL)
    
    readme_path.write_text(content)

# ---------------------------------------------------------------------------
# Daily log
# ---------------------------------------------------------------------------

def create_daily_log(items):
    """Create a daily log entry."""
    today = datetime.now().strftime("%Y-%m-%d")
    log_path = ROOT / "daily" / f"{today}.md"
    
    lines = [f"# Daily Update — {today}\n"]
    lines.append(f"Added {len(items)} item(s) today:\n")
    for item in items:
        lines.append(f"- **{item['title']}** → `{item['path']}`")
        lines.append(f"  - Category: {item['category']}")
    lines.append("")
    
    log_path.write_text("\n".join(lines))
    return f"daily/{today}.md"

# ---------------------------------------------------------------------------
# Git operations
# ---------------------------------------------------------------------------

COMMIT_PREFIXES = [
    "feat", "add", "docs", "refactor", "chore", "update", "improve"
]

COMMIT_TEMPLATES = [
    "{prefix}: add {title}",
    "{prefix}: {title} implementation",
    "{prefix}({category}): add {title}",
    "{prefix}: new {category} — {title}",
    "{prefix}: add {title} with examples",
]

def git_commit_and_push(items):
    """Commit and push changes with realistic messages."""
    os.chdir(ROOT)
    subprocess.run(["git", "add", "."], check=True)
    
    # Generate a realistic commit message
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
    print(f"🚀 Daily update — {datetime.now().strftime('%Y-%m-%d %H:%M')}")
    
    state = load_state()
    pool = load_content_pool()
    
    if not pool:
        print("❌ No content in pool!")
        sys.exit(1)
    
    # Pick items
    items = pick_items(pool, state)
    print(f"📦 Picked {len(items)} items")
    
    # Create content files
    for item in items:
        path = create_content_file(item)
        print(f"  ✏️  Created: {path}")
    
    # Create daily log
    log_path = create_daily_log(items)
    print(f"  📝 Log: {log_path}")
    
    # Update README
    state["total_items"] = state.get("total_items", 0) + len(items)
    state["last_run"] = datetime.now().isoformat()
    update_readme(state["total_items"])
    
    # Save state
    save_state(state)
    
    # Git commit and push
    git_commit_and_push(items)
    
    print("🎉 Done!")

if __name__ == "__main__":
    main()
