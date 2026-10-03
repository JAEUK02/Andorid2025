# Android Java: Number Memory Game

**An Android coursework exercise using Java, dynamic layouts, touch input, and UI-thread updates.** The app displays the digits 0–9 in random positions, hides their labels after about five seconds, and checks whether the user taps them in ascending order.

The repository's existing name is `Andorid2025`. **Start here:** [`MainActivity.java`](app/src/main/java/com/example/pr20250214/MainActivity.java).

## Learning goals and contribution record

This exercise practices generating unique random values, constructing Android views in code, handling touch events, and posting UI changes through a `Handler`. The [public development commit](https://github.com/JAEUK02/Andorid2025/commit/7ecd2682e2e2d49cd1b1843587d1d6f31a61e246) records the Java app source.

## How the game works

1. Generate a shuffled set of digits from `0` through `9`.
2. Place each digit in a randomly selected position within a programmatically created row.
3. A background loop counts seconds and posts a task that clears the digit labels after approximately five seconds.
4. Touching a numbered view hides it and appends its original digit to the selection sequence.
5. After ten selections, compare the sequence with `0123456789` and display success or failure.

**Stack:** Java, Android Views, AppCompat, and Gradle. The checked-in configuration uses compile/target SDK 35, minimum SDK 33, Java source compatibility 11, and Android Gradle Plugin 8.8.0.

## Repository map

| Path | Purpose |
| --- | --- |
| `app/src/main/java/.../MainActivity.java` | Game state, dynamic views, timer, and touch handling |
| `app/src/main/res/layout/activity_main.xml` | Root layout and result text view |
| `app/build.gradle.kts` | Android SDK and Java configuration |
| `gradle/libs.versions.toml` | Dependency and plugin versions |

## Local exploration

See the [build and source review guide](docs/REVIEW_GUIDE.md) for wrapper commands, the game-state walkthrough, and a device-check checklist.

1. Clone `https://github.com/JAEUK02/Andorid2025.git` and open the root project in Android Studio.
2. Use JDK 17 to run the checked-in AGP 8.8.0 / Gradle 8.10.2 toolchain, and install Android SDK 35 with Build Tools 35.0.0. Java source compatibility 11 is separate from the Gradle runtime JDK requirement.
3. Sync the Gradle project and run the `app` module on an emulator or device with API level 33 or above.

The source was inspected for this documentation review; a build or emulator run was not performed.

## Status and limits

This is a single-activity educational prototype. The background thread loops indefinitely without lifecycle cancellation; the game has no restart or saved-state flow, and view dimensions use fixed pixel values. The checked-in tests are starter templates and do not verify the game behavior.
