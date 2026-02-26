# Copilot Instructions for KMPExam (Leben in Deutschland)

## Project Overview

This is a **Kotlin Multiplatform (KMP)** project for a German citizenship test preparation app ("Einbürgerungstest"). It uses **Compose Multiplatform** for shared UI across Android and iOS.

## Architecture

```
KMPExam/
├── composeApp/          # Main app entry point (Android Application + iOS framework)
├── core/                # Core utilities, models, platform abstractions
├── data/                # Data layer (repositories, local storage, seeding)
├── domain/              # Domain layer (use cases, business logic)
├── designsystem/        # Shared UI components and theming
├── resources/           # Centralized string resources (all languages)
├── feature/             # Feature modules
│   ├── home/            # Home screen
│   ├── learn/           # Learning mode
│   ├── exam/            # Exam simulation
│   ├── profile/         # User profile & settings
│   └── onboarding/      # First-launch onboarding
├── analytics/           # Analytics tracking
└── iosApp/              # iOS-specific Swift code & Xcode project
```

## Module Dependencies

```
composeApp → feature:* → designsystem → core
                      → domain       → core
                      → data         → core
                      → resources
```

## Key Technologies

- **Kotlin Multiplatform** (KMP) for shared business logic
- **Compose Multiplatform** for shared UI (Android + iOS)
- **Koin** for dependency injection
- **Kotlinx Coroutines** for async operations
- **Kotlinx Serialization** for JSON parsing
- **Compose Resources** for localization (5 languages: DE, EN, TR, AR, RU)

## Platform-Specific Code Locations

### Shared Code (commonMain)
- `*/src/commonMain/kotlin/` - Shared Kotlin code
- `resources/src/commonMain/composeResources/` - String resources

### Android-Specific
- `composeApp/src/androidMain/` - Android entry point, platform utils
- `*/src/androidMain/` - Android implementations in any module

### iOS-Specific
- `composeApp/src/iosMain/` - iOS platform implementations
- `iosApp/` - Swift code, Xcode project, native iOS configurations

## Coding Conventions

### Compose UI
- Use `stringResource(Res.string.*)` for all user-facing strings
- Import resources from `kmpexam.resources.generated.resources.*`
- Follow Material 3 design with custom theming in `designsystem`

### ViewModel Pattern
- ViewModels in `commonMain` using `androidx.lifecycle.viewmodel`
- UI state exposed via `StateFlow`
- Inject via Koin: `koinViewModel<SomeViewModel>()`

### Dependency Injection
- Module definitions in `di/` folders within each module
- Register in `composeApp` Koin configuration

## Localization

All strings are centralized in the `:resources` module:
- `resources/src/commonMain/composeResources/values/strings.xml` (German - default)
- `resources/src/commonMain/composeResources/values-en/strings.xml` (English)
- `resources/src/commonMain/composeResources/values-tr/strings.xml` (Turkish)
- `resources/src/commonMain/composeResources/values-ar/strings.xml` (Arabic)
- `resources/src/commonMain/composeResources/values-ru/strings.xml` (Russian)

## Build Commands

```bash
# Android build
./gradlew :composeApp:assembleDebug

# iOS framework (run from root)
./gradlew :composeApp:linkDebugFrameworkIosSimulatorArm64

# All tests
./gradlew test

# Sync Gradle
./gradlew --refresh-dependencies
```

## Navigation

- Navigation uses `androidx.navigation.compose` with type-safe routes
- Routes defined as `@Serializable` data classes/objects in `composeApp`
- Bottom navigation tabs: Home, Learn, Exam, Profile
