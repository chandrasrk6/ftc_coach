# 🔀 Git Quick Reference Guide — FTC Coach Team

A cheat sheet for FTC team members to manage code using Git and GitHub.

---

## 📋 The 4 Core Commands (Daily Workflow)

```bash
git status              # See what files changed / are staged
git add .               # Stage ALL changed files for commit
git commit -m "msg"     # Save a labeled snapshot (commit)
git push origin main    # Upload your commits to GitHub
```

---

## 🔁 Full Commit Workflow (Step by Step)

### Step 1 — Check what changed
```bash
git status
```
**Output to look for:**
- `modified:` — files you edited
- `Untracked files:` — new files not yet tracked by git

---

### Step 2 — Stage your files
Stage **everything**:
```bash
git add .
```
Stage a **specific file** only:
```bash
git add TeamCode/src/main/java/org/firstinspires/ftc/teamcode/FlywheelShooterOpMode.java
```

---

### Step 3 — Commit with a clear message
```bash
git commit -m "Short description of what you changed"
```
> ⚠️ **NEVER** run `git commit -a` without `-m "message"` — it opens a text editor which can freeze your terminal!

**Good commit message examples:**
```bash
git commit -m "Add flywheel velocity P-control practice exercise"
git commit -m "Fix deadman trigger threshold from 0.5 to 0.3"
git commit -m "Update README with HSV color recipe instructions"
```

---

### Step 4 — Push to GitHub
```bash
git push origin main
```

---

## ⚡ One-Liner (Stage + Commit + Push)
```bash
git add . ; git commit -m "Your message here" ; git push origin main
```

---

## 📥 Getting Latest Code from GitHub

```bash
git pull origin main
```
> Always **pull before you push** when working in a team to avoid conflicts!

---

## 🔍 Reviewing History

```bash
git log --oneline           # Compact list of all commits
git log --oneline -n 5      # Show only last 5 commits
git diff                    # Show exactly what lines changed (unstaged)
git diff --staged           # Show what is staged and ready to commit
```

---

## ↩️ Undoing Mistakes

| Situation | Command |
|---|---|
| Undo unstaged changes in a file | `git restore FileName.java` |
| Unstage a file (keep changes) | `git restore --staged FileName.java` |
| Amend last commit message | `git commit --amend -m "New message"` |
| Undo last commit (keep files) | `git reset --soft HEAD~1` |

---

## 🌿 Branches (Advanced — Team Workflow)

```bash
git branch                      # List all local branches
git checkout -b feature/my-fix  # Create and switch to a new branch
git checkout main               # Switch back to main
git merge feature/my-fix        # Merge your branch into main
git branch -d feature/my-fix    # Delete branch after merging
```

> **FTC Team Tip:** Create a new branch for each major feature or exercise (e.g. `feature/flywheel-shooter`) so teammates can work in parallel without conflicts!

---

## 🚦 Common Error Messages & Fixes

| Error | What It Means | Fix |
|---|---|---|
| `nothing to commit, working tree clean` | No changes since last commit | Edit a file first, then `git add .` and commit |
| `rejected — remote has changes` | Someone else pushed first | Run `git pull origin main`, resolve any conflicts, then push |
| `fatal: not a git repository` | You're in the wrong folder | `cd` to your project folder first |
| `Please enter a commit message` | You ran `git commit` without `-m` | Press `Ctrl+X` (nano) or `:q!` (vim) to exit, then use `-m "msg"` |

---

## 🛠️ First-Time Git Setup (One Time Only)

If git doesn't know who you are:
```bash
git config --global user.name "Your Name"
git config --global user.email "your@email.com"
```

Clone a repository from GitHub to your computer:
```bash
git clone https://github.com/chandrasrk6/ftc_coach
```

---

## 📌 FTC Team Commit Message Conventions

Use these prefixes to make history readable:

| Prefix | Use For |
|---|---|
| `feat:` | New feature or OpMode added |
| `fix:` | Bug fix or correction |
| `lesson:` | New student lesson or practice exercise |
| `docs:` | README, guide, or documentation update |
| `refactor:` | Code restructure with no behavior change |

**Example:**
```bash
git commit -m "feat: Add FlywheelShooterOpMode with dual target modes"
git commit -m "lesson: Add Practice5_ShooterAutoAim student worksheet"
git commit -m "fix: Correct KP_TURN gain from 0.05 to 0.025"
```
