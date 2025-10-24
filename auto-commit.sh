#!/bin/bash

# Auto-commit script for successful builds
# This script commits changes when the build succeeds

echo "🔍 Checking for changes..."

# Check if there are any changes to commit
if [[ -z $(git status -s) ]]; then
    echo "✅ No changes to commit"
    exit 0
fi

# Show what changed
echo "📝 Changes detected:"
git status -s

# Get a commit message (you can customize this)
TIMESTAMP=$(date '+%Y-%m-%d %H:%M:%S')
COMMIT_MSG="Auto-commit: Successful build at $TIMESTAMP"

# Add all changes
git add -A

# Commit with timestamp
git commit -m "$COMMIT_MSG"

echo "✅ Changes committed successfully!"
echo "📤 To push to GitHub, run: git push"
