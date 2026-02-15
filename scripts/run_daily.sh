#!/bin/bash
# Daily update runner — called by launchd
# Adds SSH key to agent and runs the Python script

export PATH="/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin:$PATH"
export HOME="/Users/debojyoti.mandal"

# Ensure SSH agent has the personal key loaded
eval "$(ssh-agent -s)" > /dev/null 2>&1
ssh-add ~/.ssh/id_ed25519_github_personal 2>/dev/null

# Log output
LOG_DIR="$HOME/personal/fullstack-kit/scripts/logs"
mkdir -p "$LOG_DIR"
LOG_FILE="$LOG_DIR/$(date +%Y-%m-%d).log"

echo "=== Daily Update $(date) ===" >> "$LOG_FILE"

# Run the daily update script
cd "$HOME/personal/fullstack-kit"
/Users/debojyoti.mandal/miniconda3/bin/python3 scripts/daily_update.py >> "$LOG_FILE" 2>&1

echo "=== Done $(date) ===" >> "$LOG_FILE"
