# Git Advanced Commands Cheatsheet

## Interactive Rebase
```bash
# Squash last 3 commits into one
git rebase -i HEAD~3
# In editor: change 'pick' to 'squash' (or 's') for commits to merge

# Reorder commits, edit messages — in the editor you can:
# pick   = keep
# reword = keep, edit message
# edit   = stop here to amend
# squash = merge into previous commit
# drop   = remove commit
```

## Cherry Pick
```bash
# Apply a specific commit from another branch
git cherry-pick <commit-hash>

# Cherry-pick without committing (stage changes only)
git cherry-pick --no-commit <hash>

# Cherry-pick a range
git cherry-pick A..B
```

## Stash Advanced
```bash
git stash push -m "WIP: feature login"   # Named stash
git stash list                            # List all stashes
git stash pop stash@{2}                   # Pop specific stash
git stash branch new-feature stash@{0}    # Create branch from stash
git stash drop stash@{0}                  # Delete specific stash
```

## Finding Bugs (git bisect)
```bash
# Binary search for the commit that introduced a bug
git bisect start
git bisect bad                # Current commit is bad
git bisect good <old-hash>    # This commit was good
# Git checks out middle commit, you test, then:
git bisect good  # or  git bisect bad
# Repeat until found. Automate:
git bisect run mvn test  # or ./gradlew test
git bisect reset
```

## Useful Aliases (Java/Maven projects)
```bash
git config --global alias.lg "log --oneline --graph --all --decorate"
git config --global alias.st "status -sb"
git config --global alias.undo "reset HEAD~1 --mixed"
git config --global alias.amend "commit --amend --no-edit"
# Run tests before commit:
git config --global alias.smart-commit "!mvn test && git commit"
```

## Cleanup
```bash
git branch --merged | grep -v main | xargs git branch -d  # Delete merged branches
git remote prune origin   # Remove stale remote references
git gc --aggressive        # Garbage collect (compress objects)
git reflog expire --expire=30.days --all  # Expire old reflog
```

## Pre-commit Hooks (for Java)
```bash
# .git/hooks/pre-commit (chmod +x)
#!/bin/bash
mvn spotless:check -q || { echo 'Format check failed. Run: mvn spotless:apply'; exit 1; }
mvn test -q || { echo 'Tests failed'; exit 1; }
```


# Git Advanced Commands Cheatsheet

## Interactive Rebase
```bash
# Squash last 3 commits into one
git rebase -i HEAD~3
# In editor: change 'pick' to 'squash' (or 's') for commits to merge

# Reorder commits, edit messages — in the editor you can:
# pick   = keep
# reword = keep, edit message
# edit   = stop here to amend
# squash = merge into previous commit
# drop   = remove commit
```

## Cherry Pick
```bash
# Apply a specific commit from another branch
git cherry-pick <commit-hash>

# Cherry-pick without committing (stage changes only)
git cherry-pick --no-commit <hash>

# Cherry-pick a range
git cherry-pick A..B
```

## Stash Advanced
```bash
git stash push -m "WIP: feature login"   # Named stash
git stash list                            # List all stashes
git stash pop stash@{2}                   # Pop specific stash
git stash branch new-feature stash@{0}    # Create branch from stash
git stash drop stash@{0}                  # Delete specific stash
```

## Finding Bugs (git bisect)
```bash
# Binary search for the commit that introduced a bug
git bisect start
git bisect bad                # Current commit is bad
git bisect good <old-hash>    # This commit was good
# Git checks out middle commit, you test, then:
git bisect good  # or  git bisect bad
# Repeat until found. Automate:
git bisect run mvn test  # or ./gradlew test
git bisect reset
```

## Useful Aliases (Java/Maven projects)
```bash
git config --global alias.lg "log --oneline --graph --all --decorate"
git config --global alias.st "status -sb"
git config --global alias.undo "reset HEAD~1 --mixed"
git config --global alias.amend "commit --amend --no-edit"
# Run tests before commit:
git config --global alias.smart-commit "!mvn test && git commit"
```

## Cleanup
```bash
git branch --merged | grep -v main | xargs git branch -d  # Delete merged branches
git remote prune origin   # Remove stale remote references
git gc --aggressive        # Garbage collect (compress objects)
git reflog expire --expire=30.days --all  # Expire old reflog
```

## Pre-commit Hooks (for Java)
```bash
# .git/hooks/pre-commit (chmod +x)
#!/bin/bash
mvn spotless:check -q || { echo 'Format check failed. Run: mvn spotless:apply'; exit 1; }
mvn test -q || { echo 'Tests failed'; exit 1; }
```
