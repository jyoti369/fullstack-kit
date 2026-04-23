#!/bin/bash
# Dynamic update runner — called by launchd multiple times daily
# Randomly decides whether to commit to avoid bot-like patterns

export PATH="/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin:$PATH"
export HOME="/Users/debojyoti.mandal"

# State file to track last commit
STATE_FILE="$HOME/Code/Personal/fullstack-kit/scripts/.last_commit"
TODAY=$(date +%Y-%m-%d)

# Check if already committed today
if [ -f "$STATE_FILE" ]; then
    LAST_COMMIT=$(cat "$STATE_FILE")
    if [ "$LAST_COMMIT" == "$TODAY" ]; then
        echo "Already committed today. Skipping..."
        exit 0
    fi
fi

# Random chance to commit (30% probability)
# This creates natural variation - some days commit early, some late
RANDOM_NUM=$((RANDOM % 100))
if [ $RANDOM_NUM -gt 30 ]; then
    echo "Random skip (rolled $RANDOM_NUM/100). Will try next window..."
    exit 0
fi

# Ensure SSH agent has the personal key loaded
eval "$(ssh-agent -s)" > /dev/null 2>&1
ssh-add ~/.ssh/github-personal-key 2>/dev/null

# Log output
LOG_DIR="$HOME/Code/Personal/fullstack-kit/scripts/logs"
mkdir -p "$LOG_DIR"
LOG_FILE="$LOG_DIR/$(date +%Y-%m-%d).log"

echo "=== Daily Update $(date) ===" >> "$LOG_FILE"

# Run the daily update script
cd "$HOME/Code/Personal/fullstack-kit"
python3 scripts/daily_update.py >> "$LOG_FILE" 2>&1

# Save commit date
echo "$TODAY" > "$STATE_FILE"

echo "=== Done $(date) ===" >> "$LOG_FILE"
