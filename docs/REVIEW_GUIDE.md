# Build and source review guide

## Toolchain, without guessing from the Java language level

| Component | Required or checked-in value | Evidence |
| --- | --- | --- |
| Android Gradle Plugin | 8.8.0 | [Version catalog](../gradle/libs.versions.toml) |
| Gradle wrapper | 8.10.2 | [Wrapper properties](../gradle/wrapper/gradle-wrapper.properties) |
| Gradle runtime JDK | 17 minimum; use JDK 17 for the compatibility baseline | [AGP 8.8 compatibility](https://developer.android.com/build/releases/agp-8-8-0-release-notes) |
| Java source/target level | 11 | [App configuration](../app/build.gradle.kts) |
| Compile/target SDK | 35 | [App configuration](../app/build.gradle.kts) |
| SDK Build Tools | 35.0.0 baseline for AGP 8.8 | [AGP compatibility](https://developer.android.com/build/releases/agp-8-8-0-release-notes) |
| Device/emulator | API 33 or newer | `minSdk = 33` |

Java source compatibility 11 controls compiled app code. It does not mean JDK 11 can run the checked-in Android Gradle Plugin.

## Local build path

1. Open the repository root in Android Studio. The root project name is `pr20250214`; the app module is `:app`.
2. Install SDK Platform 35 and Build Tools 35.0.0. Configure the Gradle JDK as JDK 17, and let Android Studio set the machine-local SDK path. Do not commit `local.properties`.
3. Sync dependencies. The first sync needs network access to the repositories declared in [`settings.gradle.kts`](../settings.gradle.kts) and to the Gradle distribution.
4. From the repository root, use the checked-in wrapper:

```bash
./gradlew --version
./gradlew :app:assembleDebug :app:testDebugUnitTest :app:lintDebug
```

On Windows use `gradlew.bat` in place of `./gradlew`. Check that `--version` reports the intended JVM before diagnosing compilation problems.

5. For device checks, connect an API 33+ emulator/device, run the `app` configuration, and optionally run:

```bash
./gradlew :app:connectedDebugAndroidTest
```

These are the build and test commands to verify. A Gradle build, lint run, and emulator run were **not performed** in the 2026-10-03 documentation review because that review environment had no Android SDK.

## Follow the implementation

All game behavior is in [`MainActivity.java`](../app/src/main/java/com/example/pr20250214/MainActivity.java):

- **State:** `arr` stores a permutation of digits 0–9; `box` stores the corresponding ten `TextView` objects; `gtext` stores the selected digit sequence.
- **Layout:** `onCreate` makes ten rows. Each row has five positions, one occupied by a numbered view and four by blank views. The root `ll1` and result `tv1` come from [`activity_main.xml`](../app/src/main/res/layout/activity_main.xml).
- **Hiding labels:** a worker thread increments `counter` and posts a UI task through `Handler` when it reaches five. Because the first increment precedes the first sleep, this is a rough delay rather than an exact five-second timer.
- **Input:** `boxtouch.onTouch` handles the touch-down action, hides the selected view, appends the original digit from `arr`, and updates the result text.
- **Decision:** after ten selections, only `0123456789` displays `성공`; other sequences display `fail`.

Input is already active while the labels are visible; the code does not enforce a separate memorization phase before taps. `View.INVISIBLE` preserves the selected box's layout space.

## What the existing tests establish

- [`ExampleUnitTest`](../app/src/test/java/com/example/pr20250214/ExampleUnitTest.java) checks `2 + 2 == 4`.
- [`ExampleInstrumentedTest`](../app/src/androidTest/java/com/example/pr20250214/ExampleInstrumentedTest.java) checks the package name.

Neither test exercises the game. Passing them would not establish timer behavior, correct ordering, or lifecycle safety.

## Focused manual checks for a future build

| Check | Source-derived expectation or risk |
| --- | --- |
| Fresh launch | Ten unique digits, one numbered box per row |
| Tap while labels are visible | Accepted immediately; no phase gate |
| Select all digits in ascending order | Final result becomes `성공` |
| Select a different complete order | Final result becomes `fail` |
| Rotate, background, then return | Needs device verification: the worker loop has no lifecycle cancellation, and there is no saved-state/restart flow |
| Smaller screen and different density | Needs device verification: dimensions/margins use pixels, ten rows are added, and the root layout has no scrolling container |

Record the device/API level and observed result when running these checks. Screenshots or a demo should only be added after a real device/emulator run.
