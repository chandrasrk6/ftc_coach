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

---

## 🌿 Feature Branch Workflow

A **feature branch** isolates one person's work so it never breaks the `main` branch that the whole team depends on.

```
main  ──●──────────────────────────────────●── (always stable)
         \                                /
feature   ●── ●── ●── ●── ●── ●── ●── ●    (your work in isolation)
```

### Step 1 — Start from updated main
```bash
git checkout main
git pull origin main          # Always get latest first!
```

### Step 2 — Create and switch to feature branch
```bash
git checkout -b feature/flywheel-shooter
```
> **Naming conventions:** `feature/`, `fix/`, `lesson/` + short description

### Step 3 — Commit your work regularly
```bash
git add .
git commit -m "feat: Add flywheel velocity PIDF control"
```

### Step 4 — Push feature branch to GitHub
```bash
git push origin feature/flywheel-shooter
```

### Step 5 — Open a Pull Request on GitHub
- Go to `github.com/chandrasrk6/ftc_coach`
- Click **"Compare & pull request"** banner
- Add description, request coach review → **"Merge pull request"**

### Step 6 — Clean up after merge
```bash
git checkout main
git pull origin main                               # Get the merged changes
git branch -d feature/flywheel-shooter             # Delete local branch
git push origin --delete feature/flywheel-shooter  # Delete remote branch
```

---

## 🔄 Keeping Feature Branch in Sync with main

When teammates merge new things to `main` while you're still working on your branch:

### Option A: `merge` (simpler — recommended for beginners)
```bash
git checkout feature/flywheel-shooter
git merge main                   # Pull main's new changes into your branch
```

### Option B: `rebase` (cleaner history — advanced)
```bash
git checkout feature/flywheel-shooter
git rebase main                  # Replay your commits on top of latest main
```

> ⚠️ **FTC Team Rule:** Use `merge` until comfortable. `rebase` rewrites history and can confuse beginners.

---

## ⚔️ Resolving Merge Conflicts

When two people edit the **same line** in the same file:

```java
<<<<<<< HEAD  (your branch version)
private static final double KP_TURN = 0.03;
=======
private static final double KP_TURN = 0.025;
>>>>>>> main  (incoming version)
```

**Fix Steps:**
1. Edit the file — delete `<<<<`, `====`, `>>>>` markers, keep the correct line
2. `git add FlywheelShooterOpMode.java`
3. `git commit -m "fix: Resolve merge conflict in KP_TURN constant"`

---

## 🏆 FTC Team Branch Strategy

```
main                               ← Stable, coach-reviewed only
  ├── feature/flywheel-shooter     ← Student A
  ├── feature/color-detection      ← Student B
  ├── lesson/practice-exercise-6  ← Student C
  └── fix/deadman-threshold        ← Student D
```

Each student works independently → Pull Request → Coach reviews → Merged to `main`. ✅

---

## 🌿 Branch Command Cheat Sheet

| Command | What it Does |
|---|---|
| `git checkout -b feature/name` | Create + switch to new branch |
| `git checkout main` | Switch back to main |
| `git branch` | List all local branches |
| `git branch -a` | List local + remote branches |
| `git merge main` | Sync latest main into your branch |
| `git rebase main` | Replay your commits on top of latest main |
| `git push origin feature/name` | Push feature branch to GitHub |
| `git branch -d feature/name` | Delete local branch (after merge) |
| `git push origin --delete feature/name` | Delete remote branch |
