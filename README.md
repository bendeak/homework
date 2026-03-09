# Automation Framework

A clean, modular Java automation framework separating framework infrastructure from test logic.

## Stack

| Concern         | Library                        |
|-----------------|--------------------------------|
| Language        | Java 21 (LTS)                  |
| Build           | Gradle (multi-module)          |
| UI Automation   | Selenium 4 + WebDriverManager  |
| API Testing     | OkHttp3                        |
| Test Runner     | JUnit 5                        |
| Logging API     | SLF4J 2                        |
| Logging Impl    | Log4j2                         |

---

## Project Structure

```
automation-framework/
├── build.gradle              ← shared deps & config for all modules
├── settings.gradle           ← module declarations
│
├── framework/                ← FRAMEWORK MODULE (no tests here)
│   └── src/main/java/com/framework/
│       ├── config/
│       │   └── FrameworkConfig.java     ← central config (properties + system props)
│       ├── core/
│       │   ├── DriverFactory.java       ← thread-safe WebDriver (Chrome/Firefox/Edge)
│       │   ├── BasePage.java            ← base Page Object with wait helpers
│       │   ├── BaseTest.java            ← JUnit 5 base for UI tests
│       │   └── BaseApiTest.java         ← JUnit 5 base for API tests
│       └── api/
│           ├── ApiClient.java           ← OkHttp3 REST client wrapper
│           └── ApiResponse.java         ← response value object
│
└── tests/                    ← TEST MODULE (depends on framework)
    └── src/test/java/com/tests/
        ├── ui/
        │   └── SampleUiTest.java
        └── api/
            └── SampleApiTest.java
```

---

## Quick Start

### Run all tests
```bash
./gradlew test
```

### Run only API tests
```bash
./gradlew :tests:test --tests "com.tests.api.*"
```

### Run headless in CI
```bash
./gradlew :tests:test -Dheadless=true -Dbrowser=chrome
```

### Override any config via system properties
```bash
./gradlew :tests:test -Dbase.url=https://staging.example.com -Dbrowser=firefox
```

---

## Configuration

Edit `framework/src/main/resources/framework.properties`:

```properties
browser=chrome          # chrome | firefox | edge
headless=false
base.url=https://example.com
api.base.url=https://api.example.com
implicit.wait.seconds=5
explicit.wait.seconds=15
```

All values can be overridden at runtime with `-Dkey=value`.

---

## Writing Tests

### UI Test
```java
class LoginTest extends BaseTest {
    @Test
    void userCanLogin() {
        driver.get(FrameworkConfig.baseUrl() + "/login");
        // use driver or create a Page Object extending BasePage
    }
}
```

### API Test
```java
class UserApiTest extends BaseApiTest {
    @Test
    void getUserReturns200() {
        ApiResponse resp = api.get("/users/1");
        assertEquals(200, resp.getStatusCode());
    }
}
```
