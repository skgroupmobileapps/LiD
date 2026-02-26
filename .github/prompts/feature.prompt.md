# Feature Implementation Prompt

Use this prompt to implement a new feature in the KMPExam project with maximum code sharing across Android and iOS.

## Usage

```
/feature [FEATURE NAME]

## Description
[Describe what the feature should do]

## User Stories
- As a user, I want to [action] so that [benefit]

## Requirements
### Functional
- [Requirement 1]
- [Requirement 2]

### Non-Functional
- [Performance, accessibility, etc.]

## UI/UX
- [Screen descriptions]
- [Navigation flow]

## Data Requirements
- [What data does this feature need?]

## Platform Considerations
### Shared (Both Platforms)
- [What should be shared?]

### Android-Specific (if any)
- [Android-only requirements]

### iOS-Specific (if any)
- [iOS-only requirements]
```

## Example

```
/feature Bookmark Questions

## Description
Allow users to bookmark questions for later review. Bookmarked questions appear in a dedicated list accessible from the Learn screen.

## User Stories
- As a user, I want to bookmark difficult questions so I can review them later
- As a user, I want to see all my bookmarked questions in one place
- As a user, I want to remove bookmarks when I've mastered a question

## Requirements
### Functional
- Bookmark/unbookmark toggle on question cards
- Persist bookmarks across app sessions
- Show bookmark count on Learn screen
- Dedicated bookmarks list screen

### Non-Functional
- Instant visual feedback when bookmarking

## UI/UX
- Bookmark icon (outline/filled) on question cards
- "Bookmarks (N)" section on Learn screen
- Bookmarks list shows question preview with topic badge

## Data Requirements
- Store bookmark IDs in local preferences
- Query questions by bookmark status

## Platform Considerations
### Shared (Both Platforms)
- Bookmark data model
- BookmarkRepository (interface + implementation)
- Compose UI for bookmark toggle and list
- ViewModel logic

### Android-Specific
- None (using shared DataStore implementation)

### iOS-Specific
- None (using shared DataStore implementation)
```

## How Crossplatform Agent Processes This

### Phase 1: Analysis
- Parse requirements into shared vs platform-specific tasks
- Identify data models, repositories, UI components needed

### Phase 2: Delegation to @shared-kmp-agent
```
Tasks:
- Create Bookmark data class in core/model
- Add BookmarkRepository in domain
- Implement BookmarkRepositoryImpl in data
- Create BookmarkListScreen in feature/learn
- Add bookmark toggle to QuestionCard component
- Add strings: bookmark_add, bookmark_remove, bookmarks_title
- Wire up navigation to bookmarks screen
```

### Phase 3: Delegation to Platform Agents (if needed)
```
@android-agent Tasks:
- [Only if Android-specific APIs needed]

@ios-agent Tasks:
- [Only if iOS-specific APIs needed]
```

### Phase 4: Integration Verification
- Build both platforms
- Verify feature works identically on Android and iOS

## Tips for Writing Good Feature Prompts

1. **Be specific** about UI behavior and data flow
2. **Identify platform boundaries** upfront if you know them
3. **Include edge cases** (empty states, errors, loading)
4. **Reference existing patterns** in the codebase
5. **Keep scope focused** - one feature per prompt

## Quick Templates

### UI-Only Feature (100% Shared)
```
/feature [Name]
Platform: Shared only (Compose Multiplatform)
[Description]
```

### Feature with Platform APIs
```
/feature [Name]
Platform APIs needed:
- Android: [specific API]
- iOS: [specific API]
Use expect/actual pattern for abstraction.
[Description]
```

### Feature with Native UI
```
/feature [Name]
Note: Requires native UI implementation
- Android: [Jetpack Compose specific / View]
- iOS: [SwiftUI / UIKit]
[Description]
```
