# Selenium Automation Framework – Homework

A multi-module Gradle test automation framework built with Java 21, Selenium 4, and JUnit 5,
covering UI automation, rich text editor interaction, iframe/tab handling, and REST API testing.

---

## Tech Stack

| Layer | Library |
|---|---|
| Language | Java 21 |
| Build | Gradle 8.12 (multi-module) |
| UI Automation | Selenium 4.29.0 |
| Driver Management | WebDriverManager 5.9.2 |
| Test Runner | JUnit 5.11.4 |
| HTTP Client | OkHttp3 4.12.0 |
| JSON Parsing | Jackson 2.18.2 |
| Logging | SLF4J 2.0.16 + Log4j2 2.24.3 |

---

## Project Structure

```
homework/
├── build.gradle                    # Root build — shared config and version catalog
├── settings.gradle
├── framework/                      # Reusable framework module
│   └── src/main/java/com/framework/
│       ├── api/                    # ApiClient, ApiResponse
│       ├── config/                 # FrameworkConfig (reads framework.properties)
│       ├── core/                   # BasePage, BaseTest, BaseApiTest, DriverFactory, WaitHelper
│       └── utils/                  # CredentialReader (reads testdata.json)
│   └── src/main/resources/
│       ├── framework.properties    # Browser and wait configuration
│       └── log4j2.xml              # Production logging config
└── tests/                          # Test module
    └── src/test/java/com/tests/
        ├── api/                    # Case 5 – REST API
        └── ui/
            ├── guru99/             # Case 4 – iFrame and tab handling
            ├── onlinehtmleditor/   # Case 3 – Rich text editor
            └── saucedemo/          # Cases 1 & 2 – Purchase process and login validation
    └── src/test/resources/
        ├── testdata.json           # Credentials, checkout info, and item names
        └── log4j2-test.xml         # Test logging config (console + rolling file per run)
```

---

## Test Cases

| Case | Class | Description |
|---|---|---|
| Case 1 | `PurchaseProcessTest` | Login, add items, complete checkout |
| Case 2 | `Case2Test` | Login validation errors and footer content |
| Case 3 | `RichTextEditorTest` | Bold and underline formatting in CKEditor 5 |
| Case 4 | `Case4Test` | iFrame click, new tab verification, hover menu navigation |
| Case 5 | `UsersApiTest` | GET /users, parse response, verify email format |

Cases 2 and 4 also have focused single-concern counterparts:
`LoginValidationTest`, `FooterTest`, `IFrameAndTabTest`, `HoverMenuNavigationTest`.

---

## Configuration

All settings are in `framework/src/main/resources/framework.properties`.
Any property can be overridden at runtime with a JVM system property, e.g. `-Dbrowser=firefox`.

| Property | Default | Description |
|---|---|---|
| `browser` | `chrome` | `chrome`, `firefox`, or `edge` |
| `headless` | `false` | Run browser without UI |
| `chrome.for.testing` | `false` | Use Selenium Manager to download Chrome for Testing |
| `chrome.version` | `145` | Chrome/ChromeDriver version |
| `chrome.binary` | _(empty)_ | Path to a custom Chrome or Chromium binary |
| `firefox.version` | `stable` | Firefox version for WebDriverManager |
| `edge.version` | `stable` | Edge version for WebDriverManager |
| `explicit.wait.seconds` | `20` | Timeout for all explicit waits |

---

## Running Tests

Run all tests:
```bash
./gradlew :tests:test
```

Run a single test class:
```bash
./gradlew :tests:test --tests "com.tests.ui.saucedemo.PurchaseProcessTest"
```

Override browser at runtime:
```bash
./gradlew :tests:test -Dbrowser=firefox
```

Run headless:
```bash
./gradlew :tests:test -Dheadless=true
```

---

## Test Logs

Each test run writes a timestamped log file to:
```
tests/logs/test-runs/run-<yyyy-MM-dd_HH-mm-ss>.log
```

Console output during the run is also at `INFO` level for `com.framework` and `com.tests`.

---

## Known Spec Deviations

| Spec | Actual |
|---|---|
| Case 2: footer contains "2024" | Footer now shows "2026" — test asserts the current year |
| Case 4: new tab title "Selenium Live Project: FREE Real Time Project for Practice" | Title is now "Selenium Live Project for Practice" |
| Case 4: "wide red Join Now button" | Button no longer exists — test asserts the red Submit button in the ConvertBox signup widget, which is the closest current equivalent |

---

## Local Setup — macOS

### Prerequisites

- [ ] **Java 21** — install via [Homebrew](https://brew.sh): `brew install openjdk@21`
  - Add to PATH: `export PATH="/opt/homebrew/opt/openjdk@21/bin:$PATH"`
  - Verify: `java -version`
- [ ] **Chrome or Chromium** installed
  - Default config points to Chromium at `/Applications/Chromium.app/Contents/MacOS/Chromium`
  - If using standard Chrome, update `chrome.binary` in `framework.properties` to:
    `/Applications/Google Chrome.app/Contents/MacOS/Google Chrome`
  - Or leave `chrome.binary` empty and set `chrome.for.testing=true` to let Selenium Manager
    download a matching browser automatically
- [ ] **No manual ChromeDriver needed** — WebDriverManager handles it automatically

### Steps

```bash
# 1. Clone the repository
git clone <repo-url>
cd homework

# 2. Verify Java
java -version   # should show 21.x

# 3. Run all tests
./gradlew :tests:test

# 4. View HTML report
open tests/build/reports/tests/test/index.html
```

---

## Local Setup — Windows

### Prerequisites

- [ ] **Java 21** — download from [Adoptium](https://adoptium.net) and run the installer
  - Tick "Set JAVA_HOME" and "Add to PATH" during installation
  - Verify in a new terminal: `java -version`
- [ ] **Chrome** installed (standard Google Chrome is fine)
- [ ] Update `framework/src/main/resources/framework.properties`:
  - Set `chrome.binary` to your Chrome path, e.g.:
    `chrome.binary=C:/Program Files/Google/Chrome/Application/chrome.exe`
  - Or leave `chrome.binary` empty and set `chrome.for.testing=true` to let Selenium Manager
    download a matching browser automatically
  - **Note:** use forward slashes (`/`) in the path, not backslashes
- [ ] **No manual ChromeDriver needed** — WebDriverManager handles it automatically

### Steps

```bat
REM 1. Clone the repository
git clone <repo-url>
cd homework

REM 2. Verify Java
java -version

REM 3. Run all tests
gradlew.bat :tests:test

REM 4. View HTML report — open this file in a browser
tests\build\reports\tests\test\index.html
```

### Notes for Windows

- If you see a firewall prompt when Gradle or WebDriverManager downloads a driver, allow the connection.
- If `gradlew.bat` is blocked by PowerShell execution policy, run:
  `Set-ExecutionPolicy -Scope CurrentUser RemoteSigned`
- Test log files are written to `tests\logs\test-runs\`.
