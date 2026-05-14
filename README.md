# Aura Notes 📝

A beautiful and feature-rich notes app built with React Native and Expo.

![React Native](https://img.shields.io/badge/React%20Native-0.83.1-61DAFB?style=flat-square&logo=react)
![Expo](https://img.shields.io/badge/Expo-SDK%2055-000020?style=flat-square&logo=expo)
![React](https://img.shields.io/badge/React-19.0.0-61DAFB?style=flat-square&logo=react)

## ✨ Features

### Core Features
- ✅ **Create Notes** - Title, description, priority, color
- ✅ **Edit & Delete** - Full CRUD operations
- ✅ **Pin Notes** - Keep important notes at the top
- ✅ **Favorites** - Mark notes as favorites
- ✅ **Complete/Uncomplete** - Track your progress
- ✅ **Archive** - Keep notes but hide from main list

### Organization
- 📁 **Folder System** - Organize notes by Work, Personal, Ideas, Important
- 🏷️ **Priority Levels** - High, Medium, Low priority
- 🎨 **Note Colors** - 5 beautiful color themes
- 📋 **Subtasks** - Break down tasks into smaller steps

### Security
- 🔐 **Private Note PIN Lock** - Individual note protection
- 🔑 **Global PIN** - Set app-level security PIN
- 🔒 **Secure Storage** - PINs encrypted with SHA-256

### User Experience
- 📅 **Due Dates** - Set deadlines for notes
- 🔔 **Reminders** - Get notified when tasks are due
- 🔍 **Search** - Find notes quickly
- 🎯 **Filters** - View by All, Active, Done, Pinned, Favorites
- 🌙 **Dark Mode Ready** - Modern UI design

### Data Management
- 💾 **Local Storage** - All data stored on device
- 📤 **Export Ready** - Export notes as JSON
- 🔄 **No Backend Required** - Works offline

## 📸 Screenshots

*(Coming soon)*

## 🚀 Getting Started

### Prerequisites

- Node.js 18+
- npm or yarn
- Expo CLI (`npm install -g expo-cli`)
- Android Studio (for Android development)
- Xcode (for iOS development, Mac only)

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/senkuboy0-cyber/Aura.git
cd Aura
```

2. **Install dependencies**
```bash
npm install
# or
yarn install
```

3. **Start the development server**
```bash
expo start
```

4. **Run on device/emulator**
```bash
# Android
npm run android

# iOS
npm run ios
```

### Build for Production

```bash
# Generate native projects
expo prebuild

# Build Android APK
eas build --platform android

# Build iOS
 eas build --platform ios
```

## 🛠️ Tech Stack

| Category | Technology |
|----------|------------|
| Framework | Expo SDK 55 |
| Language | JavaScript |
| UI | React Native |
| Navigation | React Navigation v7 |
| State | React Context API |
| Storage | AsyncStorage |
| Secure Storage | Expo Secure Store |
| Animations | React Native Reanimated |
| Icons | @expo/vector-icons |

## 📁 Project Structure

```
Aura/
├── App.js                    # Main entry point
├── src/
│   ├── components/           # Reusable UI components
│   │   ├── NoteCard.js
│   │   ├── EmptyState.js
│   │   ├── FilterTabs.js
│   │   ├── FolderTabs.js
│   │   ├── ColorPicker.js
│   │   ├── PriorityPicker.js
│   │   ├── FolderPicker.js
│   │   ├── DateTimePicker.js
│   │   ├── SubtaskInput.js
│   │   ├── LockToggle.js
│   │   └── LockedNoteModal.js
│   ├── context/
│   │   └── NotesContext.js  # Global state management
│   ├── screens/
│   │   ├── HomeScreen.js
│   │   ├── AddNoteScreen.js
│   │   ├── EditNoteScreen.js
│   │   ├── ArchiveScreen.js
│   │   └── SettingsScreen.js
│   ├── navigation/
│   │   └── AppNavigator.js
│   └── constants/
│       └── theme.js         # Colors, constants
└── assets/                   # Images, fonts
```

## 🔐 Security

- All PINs are hashed using SHA-256
- PINs stored securely using expo-secure-store
- No data sent to external servers
- Works completely offline

## 📱 App Screens

1. **Home** - View all notes with filters and folders
2. **Add Note** - Create new notes with all features
3. **Edit Note** - Modify existing notes
4. **Archive** - View and restore archived notes
5. **Settings** - App settings, PIN management, statistics

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**senkuboy0-cyber**

- GitHub: [@senkuboy0-cyber](https://github.com/senkuboy0-cyber)

## 🙏 Acknowledgments

- [Expo](https://expo.dev) - For the amazing framework
- [React Native](https://reactnative.dev) - For the cross-platform framework
- [React Navigation](https://reactnavigation.org) - For navigation solutions
- All the open-source contributors

---

Made with ❤️ using React Native & Expo
