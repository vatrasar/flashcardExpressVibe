<p align="center">
  <img src="app/src/main/ic_launcher-playstore.png" width="96" height="96" alt="FlashExpress Logo" />
</p>

<h1 align="center">FlashExpress</h1>

<p align="center">
  <b>A modern, native Android application designed for mastering foreign languages with ease through the Leitner Spaced Repetition System (SRS).</b>
</p>

<p align="center">
  <a href="https://kotlinlang.org"><img src="https://img.shields.io/badge/Kotlin-2.0.21-7F52FF.svg?style=flat&logo=kotlin&logoColor=white" alt="Kotlin" /></a>
  <a href="https://developer.android.com/jetpack/compose"><img src="https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4.svg?style=flat&logo=jetpackcompose&logoColor=white" alt="Compose" /></a>
  <a href="https://dagger.dev/hilt/"><img src="https://img.shields.io/badge/DI-Hilt%202.57-00599C.svg?style=flat" alt="Hilt" /></a>
  <a href="https://developer.android.com/training/data-storage/room"><img src="https://img.shields.io/badge/Database-Room%202.6-F44336.svg?style=flat" alt="Room" /></a>
  <a href="https://developer.android.com/guide/topics/manifest/uses-sdk-element"><img src="https://img.shields.io/badge/Min%20SDK-33-00C853.svg?style=flat" alt="Min SDK" /></a>
  <a href="https://developer.android.com/guide/topics/manifest/uses-sdk-element"><img src="https://img.shields.io/badge/Target%20SDK-36-0091EA.svg?style=flat" alt="Target SDK" /></a>
  <a href="#-architecture--code-quality"><img src="https://img.shields.io/badge/Architecture-Clean%20%2B%20MVI-FF6D00.svg?style=flat" alt="Architecture" /></a>
</p>

---

## 📖 Overview

**FlashExpress** is an Android language learning assistant built from the ground up with **modern Android development** best practices. It empowers learners to build their own custom vocabulary decks, practice with native voice pronunciation, and retain knowledge permanently using an intelligent **spaced repetition algorithm** based on the Leitner system.

---

## ✨ Key Features

### 🗂️ Category & Flashcard Management
- **Organize by Language & Subject**: Group flashcards into dedicated categories with custom target languages.
- **Full CRUD Support**: Effortlessly create, view, modify, and delete categories and flashcards.
- **Safety First**: Cascading removals, unique naming checks, and inline length validations.
- **Smart Sorting**: Flashcards are organized intuitively for fast review and management.

### 🧠 Spaced Repetition Engine (Leitner System)
- **Scientifically Proven Intervals**: Cards progress across progressive memory stages:
  - 📍 **Today / New**: Immediate daily introduction
  - ⏳ **1 Day**: First review
  - ⏳ **3 Days**: Reinforcement
  - ⏳ **10 Days**: Intermediate consolidation
  - ⏳ **30 Days**: Long-term retention
  - 🏆 **Mastered**: Permanently acquired vocabulary
- **Daily Repetition Hub**: Automatic calculation of flashcards that are due for review today.
- **Interactive Study Sessions**: Flip cards to reveal answers, assess recall, and dynamically adjust intervals.

### 🔊 Integrated Text-to-Speech (TTS)
- **Pronunciation on Demand**: Listen to authentic pronunciation with the tap of a button.
- **Automatic Locale Resolution**: Resolves TTS engine voice models matching the category’s configured language.
- **Optimal Learning Rate**: Tuned speech rate ensuring clear enunciation for language learners.

### 📊 Deep Learning Statistics
- **Mastery Dashboard**: Track total cards, mastered vocabulary, words in progress, and cards due today.
- **Visual Progress Gauges**: At-a-glance percentage mastery indicators.
- **Stage Distribution**: Complete breakdown of card counts across all Leitner intervals.
- **Per-Category Analytics**: Measure fluency progress across individual decks.

---

## 🏛️ Architecture & Code Quality

FlashExpress adheres strictly to **Clean Architecture** and the **MVI (Model-View-Intent)** pattern:

```
┌────────────────────────────────────────────────────────┐
│                   Presentation Layer                   │
│  Jetpack Compose • Material 3 • ViewModels • MVI Flow  │
└───────────────────────────┬────────────────────────────┘
                            │
┌───────────────────────────▼────────────────────────────┐
│                      Domain Layer                      │
│     Use Cases • Pure Domain Models • Repositories      │
└───────────────────────────┬────────────────────────────┘
                            │
┌───────────────────────────▼────────────────────────────┐
│                       Data Layer                       │
│     Room Database • Entities • Mappers • DAOs • TTS    │
└────────────────────────────────────────────────────────┘
```

### 🧩 MVI Screen Contract Pattern
Every screen enforces predictable unidirectional state flow via a dedicated Screen Contract:
- **`State`**: `@Immutable` representation of the UI.
- **`Event`**: User interactions dispatched to the ViewModel.
- **`Effect`**: One-off UI side-effects (e.g., custom Snackbars) handled via `SharedFlow`.
- **`NavEffect`**: Navigation side-effects decoupled from UI logic and collected in Compose Navigation graphs.

### 🗄️ SQL-First Data Management
- Room Database with reactive Kotlin `Flow` streams for real-time UI synchronization.
- Filtering and sorting implemented directly at the database layer (DAO level).
- Strict layer isolation: Database `Entity` instances never leak into the UI, handled exclusively through bidirectional domain mappers.

---

## 📂 Project Structure

```bash
app/src/main/java/com/example/flashcardexpress/
├── FlashcardExpressApplication.kt   # Hilt Application entry point
├── MainActivity.kt                  # Main Compose Activity
├── common/                          # Reusable UI components, theme, global snackbars
│   ├── theme/                       # Material 3 colors, shapes, typography
│   └── ui/components/               # Generic buttons, lists, snackbar hosts
├── core/                            # App-wide data & system components
│   ├── data/
│   │   ├── androidTools/            # TTSManager (Text-to-Speech)
│   │   ├── local/                   # Room DB (FlashcardDb), DAOs, Entities, Mappers
│   │   └── repository/              # Repository implementations
│   ├── di/                          # Global Hilt injection modules
│   └── domain/                      # Core models, repository contracts, errors
├── feature/                         # Modular feature packages
│   ├── questionManagement/          # Decks, flashcard creation & editing
│   ├── repeat/                      # Study sessions & Leitner repetition logic
│   └── statistics/                  # Dashboards, stage breakdowns & metrics
└── navigation/                      # AppBottomBar, NavHost & type-safe destinations
```

---

## 🛠️ Tech Stack & Dependencies

| Area | Library / Technology | Description |
| :--- | :--- | :--- |
| **Language** | [Kotlin 2.0](https://kotlinlang.org/) | Modern, concise language with Coroutines & StateFlow |
| **UI Framework** | [Jetpack Compose (BOM)](https://developer.android.com/jetpack/compose) | Declarative UI toolkit |
| **Design System** | [Material 3](https://m3.material.io/) | Modern Material Design components and dynamic themes |
| **Navigation** | [Navigation Compose](https://developer.android.com/guide/navigation) | Type-safe navigation with Kotlin Serialization |
| **Dependency Injection** | [Dagger Hilt](https://dagger.dev/hilt/) | Standardized dependency injection for Android |
| **Persistence** | [Room Database](https://developer.android.com/training/data-storage/room) | SQLite abstraction with KSP code generation and Flow support |
| **Speech Engine** | [Android TextToSpeech](https://developer.android.com/reference/android/speech/tts/TextToSpeech) | Hardware-accelerated multi-language voice synthesis |
| **Architecture** | Clean Architecture + MVI | Scalable, testable, decoupled codebase |

---

## 🚀 Getting Started

### Prerequisites
- **JDK**: Java 17 LTS (e.g. OpenJDK 17)
- **Android Studio**: Android Studio Ladybug | 2024.2.1+ or newer
- **Android SDK**: Compile SDK `36`, Minimum SDK `33` (Android 13+)

### Build & Run

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/flashcardExpress.git
   cd flashcardExpress
   ```

2. **Run Unit Tests**:
   ```bash
   ./gradlew test
   ```

3. **Assemble Debug APK**:
   ```bash
   ./gradlew assembleDebug
   ```

4. **Install on connected device/emulator**:
   ```bash
   ./gradlew installDebug
   ```

---

## 🧪 Testing

The codebase maintains unit and instrumented test coverage for critical components:
- **Unit Tests (`app/src/test`)**: Testing ViewModels, Use Cases, Session Managers, and Mappers.
- **Instrumented Tests (`app/src/androidTest`)**: Testing Room DAOs, Compose UI interactions, and database migrations.

Run all tests via command line:
```bash
./gradlew testDebugUnitTest
```

---

## 📄 License

This project is licensed under the [MIT License](LICENSE) (or applicable project license).
