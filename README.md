# Campus Companion 🎓🤖

**Campus Companion** is an offline-first personal campus management and student assistance Android application built using modern Jetpack Compose, Material 3, and Kotlin.

---

## 🌟 Key Features

- **Dynamic Campus Dashboard**: Live greeting, current date, and daily offline motivational quotes.
- **Smart Next-Class Tracker**: Real-time room, timing, faculty, and countdown indicators.
- **Mess Menu Manager**: Automatically calculates current and upcoming meals with weekday/weekend shifts.
- **Class Photo History**: Associative timetable logging with local sandboxed photo storage.
- **Biometric App Lock**: Fingerprint/Face authentication for sensitive academic and personal records.
- **NOVA Assistant**: One-tap personal campus AI.
- **Modern UI & Theming**: Material You dynamic colors, customizable app icons, and glance widgets.

---

## 🛠️ Tech Stack & Architecture

- **Language**: Kotlin 2.0+
- **UI Toolkit**: Jetpack Compose & Material 3
- **Architecture**: Clean Architecture + MVVM (Unidirectional Data Flow)
- **Local Persistence**: Room Database & Jetpack DataStore
- **Async & Scheduling**: Kotlin Coroutines, StateFlow, AlarmManager, WorkManager
- **Security**: BiometricPrompt & Android Keystore

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug (or newer)
- JDK 17
- Android SDK 35 (Min SDK: 26)

### Clone & Build
```bash
git clone [https://github.com/](https://github.com/)<your-username>/campus-companion.git
cd campus-companion
./gradlew assembleDebug
