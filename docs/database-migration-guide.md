# Database Migration Guide

This document describes how to increment the SQLDelight database version, write a
migration script, and keep existing user data intact when the question catalogue changes.

---

## Overview

The app uses **SQLDelight 2.x** with the `AppDatabase` schema.  
The current schema version is derived automatically from the number of `.sqm` migration
files present in `data/src/commonMain/sqldelight/de/skgroup/einburgerungstest/data/local/`.

| `.sqm` file count | Resulting schema version |
|-------------------|--------------------------|
| 0                 | 1 *(initial release)*    |
| 1 (`1.sqm`)       | 2                        |
| 2 (`1.sqm`, `2.sqm`) | 3                     |
| …                 | …                        |

The `AndroidSqliteDriver` and `NativeSqliteDriver` automatically detect that an
existing database is at an older version and run the migration files in order before
the app starts.

---

## When to Create a New Migration

Create a migration whenever **any** of the following change:

- Question text, answer text, or `correctAnswerIndex` in `questions_de.json`
- A column is added, removed, or renamed in `AppDatabase.sq`
- A new table is introduced or an existing table is dropped
- A bulk data re-seed is required for existing users

---

## Step-by-Step: Incrementing the Database Version

### 1. Identify the current version

Check how many `.sqm` files exist:

```bash
ls data/src/commonMain/sqldelight/de/skgroup/einburgerungstest/data/local/*.sqm
```

If you see `1.sqm`, the current version is **2**. Your next migration file is `2.sqm`.

### 2. Update the question catalogue JSON (if needed)

Replace or update  
`resources/src/commonMain/composeResources/files/questions_de.json`  
with the new catalogue. Update `catalogDate` in the JSON header so it reflects the
source revision date (e.g. `"07.05.2025"`).

### 3. Write the migration SQL file

Create `{N}.sqm` (where N is the **current** schema version) in:

```
data/src/commonMain/sqldelight/de/skgroup/einburgerungstest/data/local/
```

#### Example — question catalogue update only (no schema change)

```sql
-- Migration: schema version N → N+1
-- [Describe what changed, e.g. "Catalogue updated to Stand 07.05.2025".]
--
-- Clears QuestionEntity so QuestionSeeder.seedIfNeeded() re-seeds the updated
-- catalogue on next app start.
--
-- UserAnswerEntity, BookmarkEntity, ExamHistoryEntity and SettingsEntity are
-- intentionally preserved. All 460 original question IDs are stable, so FK
-- references are restored once the seeder re-inserts them.

DELETE FROM QuestionEntity;
```

#### Example — adding a new column to QuestionEntity

```sql
-- Migration: schema version N → N+1
-- Adds a 'difficulty' column to QuestionEntity.

ALTER TABLE QuestionEntity ADD COLUMN difficulty TEXT NOT NULL DEFAULT 'MEDIUM';
```

#### Example — adding a new table

```sql
-- Migration: schema version N → N+1
-- Adds a FavouriteTopicEntity table.

CREATE TABLE FavouriteTopicEntity (
    topic TEXT NOT NULL PRIMARY KEY,
    addedAt INTEGER NOT NULL
);
```

### 4. Update `AppDatabase.sq` to match the new schema

If you changed a table (added a column, new table, etc.), update the `CREATE TABLE`
statement in `AppDatabase.sq` to reflect the **final** state of the schema.  
SQLDelight validates that your `.sq` schema matches the result of applying all migrations.

> **No schema change needed** when the migration only manipulates data
> (e.g. `DELETE FROM QuestionEntity;`).

### 5. Add any new queries required by the migration or updated code

If the migration introduces new columns or tables, add the corresponding
SQLDelight queries to `AppDatabase.sq`.

### 6. Run the tests

```bash
# Parser unit tests (common, runs as Android JVM unit tests)
./gradlew :data:testDebugUnitTest

# Full Android build to validate generated schema
./gradlew :composeApp:assembleDebug
```

All tests in `DatabaseMigrationTest` must pass. In particular, verify that:

- `migration_clearsAllQuestions` — questions are gone after migration
- `migration_preservesBookmarks` — bookmark rows survive
- `migration_preservesUserAnswers` — user answers survive
- `migration_preservesExamHistory` — exam history survives
- `migration_preservesSettings` — settings survive
- `seedIfNeeded_reseedsAfterMigrationClearsQuestions` — seeder detects count == 0

### 7. Rebuild to regenerate the SQLDelight interface

```bash
./gradlew :data:generateSqlDelightInterface
```

Verify that `AppDatabase.Schema.version` in the generated code equals the new version.

### 8. Commit

Include the following in the commit:

- `{N}.sqm` — the new migration file
- `AppDatabase.sq` — updated schema / queries (if changed)
- `questions_de.json` — updated catalogue (if changed)
- `QuestionSeeder.kt` — if seeder logic was updated
- `DatabaseMigrationTest.kt` / `QuestionSeederParserTest.kt` — updated tests

---

## How Existing User Data Is Preserved

| Table                | After a catalogue-only migration | Notes |
|----------------------|----------------------------------|-------|
| `QuestionEntity`     | **Cleared**, then re-seeded      | All 460 question IDs are stable across catalogue revisions |
| `BookmarkEntity`     | Preserved                        | Dangling FK is harmless (SQLite FKs off by default); reference restored by seeder |
| `UserAnswerEntity`   | Preserved                        | Same as above |
| `ExamHistoryEntity`  | Preserved                        | No FK to QuestionEntity |
| `SettingsEntity`     | Preserved                        | No FK to QuestionEntity |

> **Important:** If a question ID is ever *removed* from the catalogue (not currently
> the case — catalogue is always 460 questions), bookmarks and answers for that ID
> will remain as orphaned rows. Add a `DELETE FROM BookmarkEntity WHERE questionId NOT IN (...)` 
> statement to the migration SQL if cleanup is desired.

---

## Re-Seeding Flow (Automatic)

```
App start
  └─ createDriver()          ← platform driver detects DB is at old version
       └─ Schema.migrate()   ← runs {N}.sqm  →  DELETE FROM QuestionEntity
  └─ QuestionSeeder.seedIfNeeded()
       └─ getQuestionCount() == 0   ← true after migration
       └─ seedQuestions()    ← parses questions_de.json, inserts all 460 rows
```

No code changes to `QuestionSeeder` are required for a catalogue-only update.

---

## Forcing a Re-Seed Without a Schema Migration

For debug/tooling purposes, `QuestionSeeder.reSeed()` clears and re-seeds in-process:

```kotlin
questionSeeder.reSeed()
```

Do **not** rely on this in production; use the SQL migration approach so the platform
driver handles the version upgrade atomically before any app code runs.

---

## Files Reference

| File | Purpose |
|------|---------|
| `data/src/commonMain/sqldelight/.../AppDatabase.sq` | Schema definition + all queries |
| `data/src/commonMain/sqldelight/.../{N}.sqm` | Migration from version N to N+1 |
| `resources/.../composeResources/files/questions_de.json` | Live question catalogue |
| `docs/leben_in_deutschland_460_cleanQ_optionB_with_ids.json` | Reference catalogue snapshot |
| `docs/catalogue_change_log.md` | Human-readable diff between catalogue revisions |
| `data/src/commonTest/.../QuestionSeederParserTest.kt` | JSON parser unit tests |
| `data/src/androidUnitTest/.../DatabaseMigrationTest.kt` | Migration integration tests |
