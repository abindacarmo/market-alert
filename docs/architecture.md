# Market Alert — Architecture

Market Alert is an Android shopping-reminder app built around **geofencing**: the user adds stores and shopping lists, and the app fires a notification when the user gets close to a store.

Current status: **UI MVP + CRUD (Room) is complete.** Geofencing, notifications, and maps are not implemented yet.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin 2.0.21 |
| UI | Jetpack Compose (Material 3), Compose BOM 2024.12.01 |
| Navigation | Navigation-Compose 2.8.5 |
| Persistence | Room 2.6.1 (via KSP 2.0.21-1.0.28) |
| Architecture | MVVM (ViewModel + StateFlow), manual factory — **no DI framework** |
| Build | AGP 8.7.3, single module `:app`, compileSdk 35, minSdk 26 |
| Planned (disabled) | Firebase (plugin commented out), FusedLocationProvider, Geofencing API |

---

## High-Level Data Flow

```
┌─────────────┐   events    ┌──────────────────┐   DAO calls   ┌─────────────┐
│   Screens   │ ──────────▶ │ ShoppingViewModel │ ────────────▶ │  Room DB    │
│(Composables │ ◀────────── │   (StateFlow)     │ ◀──────────── │ market_alert│
│   in ui/)   │  collect    │                   │   Flow emit   │   .db       │
└─────────────┘             └──────────────────┘               └─────────────┘
        ▲                          ▲
        │        NavGraph          │ Single instance created in MainActivity,
        │    (passes route +       │  passed to every screen
        │      shared VM)          │
```

Key principles:
1. **One shared `ShoppingViewModel` instance** is created in `MainActivity`, passed to `NavGraph`, then to every screen → all screens read the same data (single source of truth).
2. **Room Flow → StateFlow → Compose**: DAOs return `Flow`, the ViewModel exposes it as `StateFlow`, screens call `collectAsState()` → the UI **updates automatically** when data changes. No manual refresh anywhere.
3. **No Repository layer** — the ViewModel calls DAOs directly (deliberate, keeps the MVP simple).

---

## Project Structure

```
app/src/main/java/com/example/marketalert/
├── MainActivity.kt              # Entry point: creates DB + ViewModel, sets content
├── ShoppingViewModel.kt         # Single ViewModel (all CRUD operations)
├── data/                        # Persistence layer (Room)
│   ├── Store.kt                 # @Entity stores
│   ├── ShoppingItem.kt          # @Entity shopping_items (FK → stores, CASCADE)
│   ├── StoreDao.kt              # Store DAO
│   ├── ShoppingItemDao.kt       # Shopping item DAO
│   └── MarketAlertDatabase.kt   # @Database singleton (market_alert.db)
└── ui/
    ├── navigation/
    │   ├── Screen.kt            # Route definitions (sealed class)
    │   └── NavGraph.kt          # NavHost: 5 routes, wires the viewModel
    ├── templates/               # 5 screen composables
    │   ├── dashboardScreen.kt
    │   ├── marketListScreen.kt
    │   ├── marketFormScreen.kt
    │   ├── detailListScreen.kt
    │   └── formaddShoppingScreen.kt
    └── theme/                   # Color.kt, Theme.kt, Type.kt (default template)
```

---

## Navigation Map

Defined in `ui/navigation/Screen.kt`, wired in `NavGraph.kt`.

```
Dashboard (startDestination)
├── → market_list        MarketListScreen
│     ├── → (back)
│     ├── → market_form  MarketFormScreen (add store)
│     └── → detail_list/{storeId}   DetailItemScreen (arg: Int)
├── → market_form        MarketFormScreen
│     └── → (back)
├── → add_shopping       FormAddShoppingScreen
│     └── → (back)
└── → detail_list/{storeId}         DetailItemScreen (arg: Int)
      └── → (back)

detail_list uses NavType.IntType (storeId: Int), not a name —
an ID is unique, a store name can be duplicated.
```

---

## Data Layer (Room)

### Entity relationships

```
stores 1 ──────< shopping_items
  (onDelete = CASCADE: deleting a store removes its items too)
```

**`stores`** (`Store.kt`)
| Column | Type | Notes |
|---|---|---|
| id | Int, autoGenerate PK | |
| name | String | |
| latitude / longitude | Double | Placeholder 0.0 (issue #5) |
| radius | Float | Meters, geofence radius |
| createdAt | Long | default `System.currentTimeMillis()`, ordered DESC |

**`shopping_items`** (`ShoppingItem.kt`)
| Column | Type | Notes |
|---|---|---|
| id | Int, autoGenerate PK | |
| name | String | |
| isBought | Boolean | default false |
| timestamp | Long | default now, ordered DESC |
| storeId | Int, FK → stores.id | ON DELETE CASCADE, indexed |

### DAO highlights
- `StoreDao.getAll(): Flow<List<Store>>` — ordered by `createdAt DESC`
- `ShoppingItemDao.getByStore(storeId): Flow<List<ShoppingItem>>` — ordered by `timestamp DESC`
- `ShoppingItemDao.toggleBought(itemId)` — raw SQL `UPDATE ... SET isBought = NOT isBought` (no read-then-write → race-free)
- `ShoppingItemDao.countRemaining(storeId): Flow<Int>` — count of `isBought = 0`

`MarketAlertDatabase.kt` = thread-safe singleton (`@Volatile` + double-checked lock), DB name `market_alert.db`, `exportSchema = false`.

---

## ViewModel API (`ShoppingViewModel.kt`)

| Member | Purpose |
|---|---|
| `stores: StateFlow<List<Store>>` | store list (from `storeDao.getAll()`) |
| `addStore(name, lat, lng, radius)` | insert a store (ignores blank name) |
| `deleteStore(store)` | delete a store (items cascade) |
| `getStoreById(id): Flow<Store?>` | single store by ID |
| `itemByStore(storeId): Flow<List<ShoppingItem>>` | items for one store |
| `countRemaining(storeId): Flow<Int>` | remaining unbought items |
| `addItem(name, storeId)` | insert an item (ignores blank name) |
| `toggleBought(item)` | check / uncheck an item |
| `deleteItem(item)` | delete an item |
| `factory(storeDao, itemDao)` | `ViewModelProvider.Factory` companion |

Seed data: `init` inserts 3 demo stores only when the `stores` table is empty (demo convenience — consider removing before production).

---

## UI Flows (5 Screens)

1. **Dashboard** — home card, + buttons (add store/item), Market button, store list from Room → tap opens Detail (`storeId`).
2. **MarketList** — store list with per-store remaining unbought count (`countRemaining`), FAB → add-store form.
3. **MarketForm** — add-store form: name, radius slider (50–500 m), placeholder map & location button are **not functional yet** (lat/lng still 0.0) → see issue #5.
4. **DetailItemScreen** — item list for one store, progress card (bought/total), tapping a row calls `toggleBought` (persisted). Route: `detail_list/{storeId}`.
5. **FormAddShoppingScreen** — **batch add** flow: type an item → "Add Item" pushes it to an in-memory list (removable per row) → pick a store from the dropdown → "Save N Items" writes all items to Room at once.

---

## Build & Verification

```bash
# Compile (fast, enough to verify a change)
./gradlew :app:compileDebugKotlin

# Full debug build
./gradlew :app:assembleDebug
```

The first build can be slow; use `timeout 300` when running from a script.

---

## Roadmap / Not Implemented Yet

- [ ] **Issue #5**: interactive map & real location picker in MarketForm (replace lat/lng 0.0)
- [ ] `ACCESS_FINE_LOCATION` + `ACCESS_COARSE_LOCATION` permissions (manifest is still empty)
- [ ] Geofencing API + BroadcastReceiver + notifications (course material)
- [ ] Firebase (`google-services` plugin commented out; requires `google-services.json`)
- [ ] Repository layer + Hilt (once the MVP grows)
- [ ] Room migrations (once the schema changes)
- [ ] Unit tests & UI tests (test directories are still empty defaults)

---

## Known Technical Debt

- `allowBackup="true"` with no `fullBackupContent` (data security)
- `isMinifyEnabled=false` for release
- `.gitignore` does not exclude `google-services.json` / keystores yet
- `theme/` still uses the default purple template (does not match the black/white screens)
- Unused `java.sql.Timestamp` import in `ShoppingItem.kt`
- `navigation-compose` is hardcoded in `build.gradle.kts`, not in the version catalog
