# Copilot Instructions for KMPExam (Leben in Deutschland)

## Project Overview

This is a Kotlin Multiplatform app for German citizenship test preparation. It uses Compose Multiplatform for shared UI across Android and iOS, Koin for DI, SQLDelight for local persistence, and Compose Resources for localization.

## Current Module Layout

```
KMPExam/
├── composeApp/          # App composition root, navigation, Android app target, iOS framework export
├── core/                # Shared models, utilities, platform abstractions
├── data/                # SQLDelight database, repositories, seeding, persistence
├── domain/              # Use cases and business logic
├── designsystem/        # Shared UI components and theme
├── resources/           # Centralized string and file resources
├── tracking/            # Provider-agnostic analytics contracts and dispatcher
├── analytics/           # Firebase analytics sink implementations (Android/iOS)
├── feature/
│   ├── onboarding/      # First-launch onboarding flow
│   ├── home/            # Dashboard/home screen
│   ├── learn/           # Topic learning and question flow
│   ├── exam/            # Exam intro, in-progress, result flow
│   └── profile/         # Profile, settings, statistics
└── iosApp/              # Native iOS Swift entry point and Xcode project
```

## Dependency Shape

Use this mental model when changing architecture:

```
composeApp -> feature:* -> domain -> data -> core
           -> designsystem -> core
           -> resources
           -> tracking
           -> analytics

tracking = shared event contracts + consent-gated dispatcher
analytics = concrete Firebase sinks only
```

Feature and UI code must depend on `:tracking`, never directly on Firebase or `:analytics` implementation details.

## Key Technologies

- Kotlin Multiplatform (KMP)
- Compose Multiplatform
- Koin
- Kotlinx Coroutines
- Kotlinx Serialization
- SQLDelight
- Compose Resources
- Firebase Analytics via GitLive SDK

## Source Set Rules

### Shared code
- `*/src/commonMain/kotlin/` contains most business logic, ViewModels, and shared UI.
- `resources/src/commonMain/composeResources/` contains localized strings and bundled resource files.

### Android-specific
- `composeApp/src/androidMain/` contains Android startup and app integrations.
- `*/src/androidMain/` contains Android actual implementations.

### iOS-specific
- `composeApp/src/iosMain/` contains iOS-specific KMP wiring.
- `iosApp/` contains SwiftUI app startup, Xcode project files, and iOS Firebase plist.

## Coding Conventions

### UI and Resources
- Use `stringResource(Res.string.*)` for all user-facing text.
- Import generated resources from `kmpexam.resources.generated.resources.*`.
- Do not hardcode new user-facing strings in Kotlin when they belong in resources.
- Preserve the existing Material 3 + `designsystem` visual language unless a task explicitly changes design.

### ViewModels
- ViewModels live in `commonMain` and expose UI state via `StateFlow`.
- This repo currently uses a custom `koinInjectViewModel()` helper in `composeApp/src/commonMain/kotlin/de/skabs/skgroup/App.kt` for Compose + Koin lifecycle handling.
- Do not assume `koinViewModel()` is the active pattern here.

### Dependency Injection
- The main shared DI assembly is in `composeApp/src/commonMain/kotlin/de/skabs/skgroup/di/AppModule.kt`.
- Android startup registers Koin in `composeApp/src/androidMain/kotlin/de/skabs/skgroup/EinbuergerungApp.kt`.
- iOS startup registers Koin in `composeApp/src/iosMain/kotlin/de/skabs/skgroup/KoinInit.kt`.
- If adding analytics-related behavior, keep provider-agnostic contracts in `:tracking` and register concrete sinks through `:analytics`.

## Analytics Architecture

- `:tracking` contains the public tracking API used by app and feature code.
- `:analytics` contains Firebase-backed `TrackingSink` implementations for Android and iOS.
- Consent is stored in `UserSettings.analyticsEnabled` and enforced through the tracking dispatcher.
- The app should emit events through `TrackingClient`, not by calling Firebase APIs directly.
- Screen tracking is centralized from navigation/app composition, while feature-specific user actions are tracked from ViewModels or explicit UI callbacks.

## Navigation Notes

- Navigation lives in `composeApp/src/commonMain/kotlin/de/skabs/skgroup/App.kt`.
- The app uses Compose Navigation with a mix of string routes and a typed `LearnQuestionRoute`.
- Bottom tabs are Home, Learn, Exam, Profile.
- Exam uses a single route with phase-based UI switching (`INTRO`, `IN_PROGRESS`, `RESULT`), so behavior tied to screen identity must account for exam phase, not just route string.

## Localization

All localized strings are centralized in the `:resources` module:
- `resources/src/commonMain/composeResources/values/strings.xml` for German
- `resources/src/commonMain/composeResources/values-en/strings.xml` for English
- `resources/src/commonMain/composeResources/values-tr/strings.xml` for Turkish
- `resources/src/commonMain/composeResources/values-ar/strings.xml` for Arabic
- `resources/src/commonMain/composeResources/values-ru/strings.xml` for Russian

When adding settings or new user-facing flows, update all existing locales unless the task explicitly scopes translations.

## Build Commands

```bash
# Android debug build
./gradlew :composeApp:assembleDebug

# iOS simulator framework
./gradlew :composeApp:linkDebugFrameworkIosSimulatorArm64

# All tests
./gradlew test

# Refresh Gradle dependencies
./gradlew --refresh-dependencies
```

## Practical Guidance for Changes

- Prefer fixing behavior at the shared layer when possible instead of duplicating Android/iOS implementations.
- Check `App.kt` before assuming navigation or screen ownership; multiple logical screens are composed there.
- For analytics changes, think in terms of canonical event names and meaningful parameters, and keep call sites decoupled from Firebase.
- For settings changes, inspect both `UserSettings` and `SettingsRepository` so persistence and reactive UI updates stay aligned.
- For profile/settings UI, verify current file content before editing because it changes often.
