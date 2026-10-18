# AGENTS.md — Guidance for AI Agents

Guidance for AI agents (and new contributors) working in this **Market Alert** repo.

Project overview & data flow: see [`docs/architecture.md`](docs/architecture.md).

---

## Commands

```bash
# Verify a change (ALWAYS run before committing)
timeout 300 ./gradlew :app:compileDebugKotlin 2>&1 | grep "BUILD\|error:" | tail -10

# Full debug build
./gradlew :app:assembleDebug

# Git
git status && git log --oneline -10
gh pr create / gh pr view <n> / gh pr merge <n>
```

Exit = `BUILD SUCCESSFUL` with no `error:` = safe. If `error:` appears, fix it before committing.

---

## Code Conventions

### Language
- **UI strings**: Simple English (the app UI is in English).
- **Code identifiers**: English.
- **Comments**: English.
- **Commit messages**: English, short imperative convention, starting with a verb: `Add ...`, `Wire ...`, `Connect ...`, `Fix ...`.

### Kotlin / Compose style
- Root package: `com.example.marketalert`.
- Screen composable = a `@Composable` function in `ui/templates/`, file named `xxxScreen.kt`, PascalCase function (`DashboardScreen`).
- **One screen file contains**: the main screen + private helper composables + (optionally) a `@Preview`.
- Do not add comments unless asked. Do not add `@Preview` to screens that need a `ViewModel` (previews cannot construct a VM).
- Local Compose state: `remember { mutableStateOf(...) }` with the `by` delegate.
- Data from Room → `collectAsState(initial = ...)`.

### Architecture rules (follow the existing patterns)
- **One `ShoppingViewModel`** is created in `MainActivity`, passed to `NavGraph(navController, viewModel)`, then to every screen. Do not create a new ViewModel per screen, do not add Hilt/Koin right now.
- **No Repository** — the ViewModel calls DAOs directly. Do not add that layer unless asked.
- All database access goes through DAOs (`data/`); do not write raw SQL in the ViewModel/UI apart from the existing `toggleBought` pattern.
- Navigation routes are **only** declared in `ui/navigation/Screen.kt`; wiring only in `NavGraph.kt`. Routes with parameters use an ID (`storeId: Int`), never a name.
- Room data types are added under `data/` as `@Entity` + `@Dao` and registered in `MarketAlertDatabase.kt` (remember: adding an entity / changing the schema = bump `version` + migration).

### Pitfalls that have happened before (do not repeat)
- `toggleBought` originally took a `String` while the PK is `Int` → match the entity's types.
- Wrong entity import (`ShoppingItem` from another package) → double-check imports when touching `data/`.
- Form field saved but not displayed (forgot `collectAsState`) → make sure every screen reads the flow from the VM.
- Placeholder lat/lng is still `0.0, 0.0` — that is intentional; real location = issue #5.

---

## Workflow (Git / PR)

Branching:
- Work on a `feature/<topic>` or `docs/<topic>` branch, always created from `main`.
- One PR = one topic. Commit in small steps (many commits in one PR is fine).

Commit & PR:
- **Only commit / push / merge when the user explicitly asks.**
- Short English message: `Add Room entities, DAOs, and database`.
- Before committing: check `git status` + `git diff`, run the build verification, stage only the relevant files.
- Never commit: secrets (`google-services.json`, `*.jks`, `*.keystore`), build artifacts (`.gradle/`, `build/`), or unrequested changes.
- Do not force-push / amend pushed commits / change git config without instructions.
- PR description: summary, commit list when relevant, how to verify, manual test steps.

Issues:
- Open a new issue for separate work (example: #5 real location picking).
- Run `gh issue create` when asked.

---

## Current State

- PR #6 (Room persistence across all 5 screens) is **merged** into `main`.
- Working features: store & item CRUD, FK cascade relation, toggle bought, batch item add, store dropdown picker.
- Not built yet: real location, map, location permissions, geofencing, notifications, Firebase, tests.
- 3 demo stores are seeded in `ShoppingViewModel.init` (only when the DB is empty).
- UI strings are in English.

Full structure & roadmap: [`docs/architecture.md`](docs/architecture.md).

---

## When Editing Files

- Read the file before editing; follow the style & patterns of neighboring files.
- Large changes = split into several small edits when possible.
- Never comment out old code with `// old` — delete it if unused.
- After editing: build verification → explain the changes to the user (line-by-line when asked) → commit only if asked.
