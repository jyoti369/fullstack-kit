#!/bin/bash
# One-time reminder to switch to manual commits

# Send macOS notification
osascript -e 'display notification "Consider switching to manual commits for fullstack-kit. Run: launchctl unload ~/Library/LaunchAgents/com.jyoti369.fullstack-kit.daily.plist" with title "GitHub Automation Reminder" sound name "Glass"'

# Log the reminder
echo "$(date): Reminder sent to switch to manual commits" >> ~/Code/Personal/fullstack-kit/scripts/logs/reminder.log

# Unload this reminder agent (one-time use)
launchctl unload ~/Library/LaunchAgents/com.jyoti369.reminder.plist 2>/dev/null
