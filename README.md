# Aura - Todo Notes App

A modern, feature-rich todo and notes application built with Jetpack Compose and Clean Architecture.


## Features

- **Notes Management** - Create, edit, delete, pin, and archive notes
- **Task Lists** - Add checkbox tasks within your notes
- **Color Coding** - Color-code your notes for better organization
- **Search** - Quick search across all notes
- **Lock Notes** - Secure individual notes with PIN protection
- **Dark/Light Mode** - System-aware theme with manual override
- **Trash & Archive** - Safe storage for deleted and archived notes
- **Smooth Animations** - Beautiful transitions and micro-interactions

## Tech Stack

- **Language**: Kotlin 2.3.21
- **UI**: Jetpack Compose (BOM 2026.05.00)
- **Architecture**: MVVM + Clean Architecture
- **Database**: Room 2.6.1
- **DI**: Hilt 2.59.2
- **Navigation**: Navigation Compose 2.8.5
- **Preferences**: DataStore Preferences
- **Build**: AGP 9.2.1, Gradle 9.5.1

## Project Structure

```
app/
├── data/
│   ├── local/         (Room Database, DAOs, Entities)
│   ├── preferences/   (DataStore Preferences)
│   ├── model/         (Data Models)
│   ├── repository/    (Repository Implementations)
│   └── mapper/        (Entity-Model Mappers)
├── domain/
│   ├── model/         (Domain Models)
│   ├── repository/    (Repository Interfaces)
│   └── usecase/       (Business Logic)
├── ui/
│   ├── screens/      (Compose UI Screens)
│   ├── components/    (Reusable UI Components)
│   ├── navigation/    (Navigation Setup)
│   ├── theme/         (Material3 Theme)
│   └── animations/    (Animation Utilities)
├── di/                (Hilt Modules)
└── util/              (Utilities)
```

## Build

```bash
./gradlew assembleDebug
```

## Screens

1. **Home** - Main notes list with grid/list view
2. **Add/Edit** - Create or edit notes with tasks
3. **Detail** - View note details
4. **Search** - Full-text search across notes
5. **Trash** - Deleted notes management
6. **Archive** - Archived notes
7. **Settings** - App preferences
8. **PIN Setup** - Configure note lock PIN
9. **Locked** - PIN entry screen for locked notes


## License

MIT License
