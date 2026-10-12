# Framework Automation Portfolio 2026

![CI](https://github.com/TuyetLe14/Framework_Automation_Portfolio2026/actions/workflows/test-automation.yml/badge.svg)

A Java-based web UI test automation framework built with **Selenium WebDriver, TestNG, Maven, and Java 21**.

This project is designed as a QA Automation portfolio project to demonstrate practical experience in UI automation, framework design, reusable utilities, test reporting, parallel execution, CI/CD, and performance testing.

---

## Project Overview

The framework follows a layered automation design:

```text
TestNG Tests
     │
     ▼
  BaseTest
     │
     ▼
 Page Objects
     │
     ▼
Selenium WebDriver
     │
     ▼
Web Application
     │
     ├── Screenshot
     ├── Video Recording
     ├── Retry
     └── Logging
             │
             ▼
       Test Reporting
```

The project also contains a separate **k6 performance-testing suite** and GitHub Actions workflows for automated execution.

---

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 21 | Programming language |
| Selenium WebDriver | Web UI automation |
| TestNG | Test execution and test suites |
| Maven | Build and dependency management |
| ExtentReports | Test reporting |
| SLF4J | Logging |
| Apache POI | Excel/test-data handling |
| Monte Media | Video recording |
| GitHub Actions | CI/CD |
| k6 | Performance testing |

---

# Main Features

## Web UI Automation

- Selenium WebDriver
- TestNG
- Page Object Model (POM)
- Reusable `BasePage`
- Reusable `BaseTest`
- Browser configuration
- Explicit wait support
- TestNG XML suites
- Parallel test execution

## Failure Diagnostics

- Screenshot capture
- Video recording
- Retry mechanism
- Element highlighting
- Link validation
- Logging
- ExtentReports

## Test Data & Configuration

- Externalized test configuration
- `application.properties`
- Apache POI for Excel-based test data
- Configurable browser and execution settings

## CI/CD

- GitHub Actions
- Maven compilation
- Automated test execution
- Test report artifacts
- Test result publishing
- Scheduled test execution
- Pull-request test execution

## Performance Testing

- k6 Load Test
- k6 Stress Test
- k6 Spike Test
- JSON performance result artifacts
- Scheduled/manual performance workflow

---

# Project Structure

```text
Framework_Automation_Portfolio2026/
│
├── .github/
│   └── workflows/
│       ├── test-automation.yml
│       └── performance-test.yml
│
├── performance/
│   ├── load-test.js
│   ├── stress-test.js
│   ├── spike-test.js
│   ├── run-all-tests.sh
│   └── run-all-tests.bat
│
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── tuyet/
│   │               ├── base/
│   │               ├── constants/
│   │               ├── pages/
│   │               └── utils/
│   │
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── tuyet/
│       │           ├── base/
│       │           ├── listeners/
│       │           └── tests/
│       │
│       └── resources/
│           └── application.properties
│
├── pom.xml
├── testng.xml
├── testng-parallel.xml
└── README.md
```

---

# Framework Architecture

## Base Layer

The base layer contains common framework and test lifecycle functionality.

Responsibilities include:

- WebDriver setup
- Test setup and teardown
- Common test configuration
- Shared test behavior

## Page Object Layer

Page Objects separate UI locators and page interactions from test scenarios.

Example:

```text
pages/
├── BasePage.java
├── HomePage.java
└── AboutPage.java
```

This approach helps:

- Reduce duplicated Selenium code
- Improve maintainability
- Keep tests readable
- Centralize page-specific actions

## Utility Layer

Reusable utilities are located under:

```text
src/main/java/com/tuyet/utils/
```

The project contains utilities for:

- Screenshot capture
- Element highlighting
- Link validation
- Retry handling
- Video recording

## Test Layer

Test classes are separated from framework implementation:

```text
src/test/java/com/tuyet/
├── base/
├── listeners/
└── tests/
```

This keeps test scenarios separate from reusable framework components.

---

# Retry Mechanism
Failed tests are retried **only when the failure looks like a flaky or environment issue**. Assertion failures are never retried, so real defects are not hidden.

| Rule | Value |
| ---- | ----- |
| Max retries | 1 |
| Retried exceptions | `StaleElementReferenceException`, `TimeoutException`, `NoSuchElementException`, `WebDriverException` |
| Not retried | `AssertionError` (genuine functional failure) |
| Logging | Each retry is logged as a warning with the test name and exception type |

```
Test
 │
 ├── PASS ─────────────► Continue
 │
 └── FAIL
      │
      ├── Assertion failure ─────►Report failure (no retry)
      │
      └── Flaky exception
            │
            ▼
          Retry once
            │
            ├── PASS ──────► Continue
            └── FAIL ──────► Report failure
```
Implementation: `src/main/java/com/tuyet/utils/RetryListener.java`

---

# Screenshot & Failure Evidence

The framework includes screenshot support to provide visual evidence when investigating test failures.

Additional diagnostic information can include:

- Screenshot
- Video recording
- Logs
- Test reports

This helps reduce the time required to reproduce and investigate failures.

---

# Video Recording

The project includes video-recording support through the Monte Media library.

Video evidence can be used together with screenshots and logs to understand the sequence of actions that occurred during test execution.

The recorder creates timestamped video files in the configured test-report directory.

---

# Reporting

The framework uses **ExtentReports** for test reporting.

Test execution can produce:

- Test status
- Execution information
- Failure information
- Screenshots
- Logs
- Video evidence

Maven Surefire reports are also generated during test execution and can be consumed by CI/CD tooling.

---

# Parallel Test Execution

The project includes:

```text
testng.xml
testng-parallel.xml
```

The parallel suite is configured for concurrent test execution.

Example:

```xml
<suite
    name="Parallel Test Suite"
    parallel="methods"
    thread-count="3">
```

Run the parallel suite with:

```bash
mvn clean test -DsuiteXmlFile=testng-parallel.xml
```

Parallel execution can reduce total execution time when tests are independent and properly isolated.

**WebDriver management implemented with ThreadLocal to ensure thread-safety during parallel execution
---

# Cross-Browser Execution
The browser is selected at runtime through the `browser` system property, handled in `DriverManager`. If the property is not set, Chrome is used.

| Browser | Command |
| ------- | ------- |
| Chrome (default) | `mvn clean test` |
| Firefox | `mvn clean test -Dbrowser=firefox` |
| Edge | `mvn clean test -Dbrowser=edge` |
| Headless | `mvn clean test -Dbrowser=firefox -Dheadless=true` |

Notes:

- Supported values: `chrome`, `firefox`, `edge`. Any other value fails fast with a clear error message.
- WebDriver is stored in a `ThreadLocal`, so cross-browser runs work together with parallel execution.
- Selenium Manager downloads the matching browser driver automatically.
- The target browser must be installed on the machine running the tests.

Implementation: `src/test/java/com/tuyet/base/DriverManager.java`

CI currently runs Chrome only. Firefox and Edge are supported through -Dbrowser for local runs.

# API Testing

API tests use **REST Assured** with TestNG against the public JSONPlaceholder API. They run without a browser and as a separate CI job.

| Test | Verifies |
| ---- | -------- |
| GET single post | Status 200 and response fields |
| GET all posts | Status 200 and list size |
| Filter by userId (data-driven) | Query parameter and response content |
| GET comments of a post | Nested resource and email format |
| POST create | Status 201 and echoed data |
| PUT / PATCH update | Status 200 and updated fields |
| DELETE | Status 200 |
| GET non-existing id (data-driven) | Status 404 |
| Response time | Under the configured timeout |

Run API tests:

```
mvn clean test -DsuiteXmlFile=testng-api.xml
```

Configuration: `api.baseurl` and `api.timeout` in `application.properties`. Override the base URL with `-Dapi.baseUri=<url>`.

# Configuration

Test configuration is maintained in:

```text
src/test/resources/application.properties
```

Example configuration:

```properties
app.url=YOUR_APPLICATION_URL
window.maximize=true
implicit.wait=10
explicit.wait=10
```

Update environment-specific values before running the tests.

Sensitive information such as passwords, tokens, API keys, and credentials should not be committed to the repository.

---

# Running UI Tests

## Prerequisites

Install:

- Java 21
- Maven
- Git
- Google Chrome or a compatible Chromium-based browser

Verify Java:

```bash
java -version
```

Verify Maven:

```bash
mvn -version
```

---

## Clone the Repository

```bash
git clone https://github.com/TuyetLe14/Framework_Automation_Portfolio2026.git
```

```bash
cd Framework_Automation_Portfolio2026
```

---

## Compile

```bash
mvn clean compile
```

---

## Run All Tests

```bash
mvn clean test
```

---

## Run the TestNG Suite

```bash
mvn clean test -DsuiteXmlFile=testng.xml
```

---

## Run Parallel Tests

```bash
mvn clean test -DsuiteXmlFile=testng-parallel.xml
```

---

# Performance Testing with k6

Performance tests are located in:

```text
performance/
├── load-test.js
├── stress-test.js
└── spike-test.js
```

## Load Test

```bash
k6 run performance/load-test.js
```

The load test evaluates application behavior under the configured normal workload.

## Stress Test

```bash
k6 run performance/stress-test.js
```

The stress test increases the workload to evaluate system behavior under higher pressure.

## Spike Test

```bash
k6 run performance/spike-test.js
```

The spike test evaluates application behavior during a rapid increase in traffic.

---

## Run All Performance Tests

### Linux / macOS

```bash
./performance/run-all-tests.sh
```

### Windows

```bat
performance\run-all-tests.bat
```

---

## Export k6 Results

Example:

```bash
k6 run performance/load-test.js --out json=load-test-results.json
```

The generated JSON file can be uploaded as a GitHub Actions artifact for later analysis.

---

# 🔄 CI/CD with GitHub Actions

The project contains two workflows:

```text
.github/workflows/
├── test-automation.yml
└── performance-test.yml
```

## Test Automation Workflow

The test workflow supports:

- Push to `main`
- Push to `develop`
- Pull requests targeting `main`
- Pull requests targeting `develop`
- Scheduled daily execution
- Tests run on Chrome (headless) in CI.
- API tests run as a separate job
- Database tests run as a separate job
  
Pipeline:

```text
GitHub Event
     │
     ▼
Checkout Code
     │
     ▼
Setup Java 21
     │
     ▼
Maven Compile
     │
     ▼
Run Tests
     │
     ▼
Upload Test Reports
     │
     ▼
Publish Test Results
```

### API Test Job

API tests run as a separate job (`api-test`) in the same workflow. The job runs in parallel with the UI tests, does not open a browser, and publishes its own test report and artifact.

# Database Testing

The portfolio site has no database, so these SQL tests run against an **embedded H2 database** with a sample e-commerce schema (customers, products, orders, order items). The database is rebuilt before every test, so tests stay independent and need no external service.

| Area | Verifies |
| ---- | -------- |
| Data quality rules (data-driven) | No duplicate emails, no orphan rows, order total equals sum of items, no negative stock |
| Constraints (data-driven) | UNIQUE, NOT NULL, FOREIGN KEY and CHECK violations are rejected with the right SQLSTATE |
| Business queries | LEFT JOIN, SUM aggregation, GROUP BY with ORDER BY |
| Security | Parameterized queries treat input as data (SQL injection attempt) |
| Transactions | Rollback undoes uncommitted changes |

Run database tests:

```
mvn clean test -DsuiteXmlFile=testng-db.xml
```

## Performance Workflow

The performance workflow supports scheduled and manual execution.

Pipeline:

```text
Load Test
    │
    ▼
Stress Test
    │
    ▼
Spike Test
    │
    ▼
Upload Results
```

Performance results are stored as GitHub Actions artifacts.

---

# Test Reports & Artifacts

The project can produce different types of test evidence:

| Output | Purpose |
|---|---|
| Screenshots | Visual failure evidence |
| Video | Execution evidence |
| Logs | Debugging information |
| ExtentReports | Human-readable test report |
| Surefire XML | CI test-result publishing |
| k6 JSON | Performance-test results |

When running in GitHub Actions, generated results can be uploaded as workflow artifacts.

---

# Test Strategy

The current project focuses on web UI automation and performance testing.

### Functional Testing

Automated browser-based scenarios validate application behavior.

### Regression Testing

Reusable automated scenarios can be executed repeatedly to check existing functionality.

### Failure Diagnostics

Failed tests can provide additional evidence through:

- Screenshots
- Video
- Logs
- Reports

### Parallel Execution

Independent tests can be executed concurrently through TestNG.

### Performance Testing

k6 is used for:

- Load testing
- Stress testing
- Spike testing

---

# Security & Configuration Guidelines

Do not commit sensitive data into source control.

Avoid storing:

```text
Passwords
API Keys
Access Tokens
Private Credentials
```

For CI/CD, sensitive values should be stored using GitHub Secrets or another appropriate secret-management solution.

---

# Current Project Scope

The current project demonstrates:

- ✅ Java 21
- ✅ Selenium WebDriver
- ✅ TestNG
- ✅ Maven
- ✅ Page Object Model
- ✅ Base test/page architecture
- ✅ Screenshot capture
- ✅ Video recording
- ✅ Retry mechanism
- ✅ Element highlighting
- ✅ Link validation
- ✅ Logging
- ✅ ExtentReports
- ✅ Excel/test-data support
- ✅ Parallel TestNG execution
- ✅ Cross-browser execution (Chrome, Firefox, Edge)
- ✅ GitHub Actions CI/CD
- ✅ k6 Load Testing
- ✅ k6 Stress Testing
- ✅ k6 Spike Testing
- ✅ API testing with REST Assured
- ✅ Database testing with SQL (H2)
  
---

# 🔮 Future Improvements

The following items can be added in future iterations:

- [ ] Selenium Grid
- [ ] Docker-based execution
- [ ] SonarQube quality gates
- [ ] Slack/Teams notifications
- [ ] More advanced performance dashboards
- [ ] Expanded test-data management

These are future enhancements and are not presented as currently implemented features.

---

# What This Project Demonstrates

This project demonstrates an end-to-end QA Automation workflow:

```text
Framework Design
       ↓
Test Development
       ↓
UI Automation
       ↓
Failure Diagnostics
       ↓
Test Reporting
       ↓
Parallel Execution
       ↓
CI/CD
       ↓
Performance Testing
```

The focus is not only on writing Selenium test cases, but also on building reusable framework components that make automated tests easier to maintain, execute, debug, and integrate into CI/CD.

---

# 👩‍💻 Author

**Tuyet Le**

QA Automation Portfolio Project — 2026

GitHub:

https://github.com/TuyetLe14

---

# 📄 License

This project is created for educational and portfolio purposes.
