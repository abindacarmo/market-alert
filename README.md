# market-alert
A location-based reminder app that alerts you with your shopping list when you enter a store.


Geofencing-powered shopping reminder app built with Kotlin & Jetpack Compose.

Never forget what to buy again — Market Alert automatically notifies you with your saved shopping list the moment you step into one of your registered stores.

## 📌 The Problem

It's easy to remember what you need to buy while you're at home, but by the time you actually reach the store, the list has slipped your mind. Market Alert solves this by tying your shopping list directly to the physical location of the store, so the reminder appears exactly when and where you need it.

## ✨ Features (MVP)

- [ ] Register a store with its name and location (latitude/longitude)
- [ ] Set a custom geofence radius per store
- [ ] Add shopping items linked to a specific store
- [ ] Get an automatic notification when entering a store's geofence, showing the linked shopping list
- [ ] Mark items as bought

## 🚧 Planned for Future Versions

- Edit/delete stores and items
- Item categories
- Shopping history and spending tracking
- Multiple shopping lists per store

## 🛠️ Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose
- **Local Database:** Room
- **Location Services:** Google Play Services — FusedLocationProviderClient & Geofencing API
- **Architecture:** MVVM (planned)

## 🏗️ Project Status

🚧 Currently in early development — database structure and core UI are being built before location features are added.

## 📂 Project Structure

```
market-alert/
├── data/        # Room entities, DAOs, database
├── ui/          # Jetpack Compose screens
├── service/     # Geofencing & notification logic
└── util/        # Helper functions
```

## 🚀 Getting Started

### Prerequisites

- Android Studio (latest stable version)
- Android device or emulator running API 26+
- Google Maps SDK API key

### Setup

1. Clone the repository
   ```bash
   git clone https://github.com/abindacarmo/market-alert.git
   ```

## 🗄️ Database Overview (Draft)

| Entity | Key Fields |
|---|---|
| **Store** | id, name, latitude, longitude, radius, created_at |
| **ShoppingItem** | id, name, is_bought, store_id (FK) |


GitHub: [@abindacarmo](https://github.com/abindacarmo)

---

*Build. Learn. Repeat.*