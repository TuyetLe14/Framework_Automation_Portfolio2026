# 🚀 Framework Automation Portfolio 2026

A Java-based web UI test automation framework built with **Selenium WebDriver, TestNG, Maven, and Java 21**.

This project is designed as a QA Automation portfolio project to demonstrate practical experience in UI automation, framework design, reusable utilities, test reporting, parallel execution, CI/CD, and performance testing.

---

## 📌 Project Overview

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

## 🛠️ Tech Stack

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

# ✨ Main Features

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

# 📁 Project Structure

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

# 🧩 Framework Architecture

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

# 🔁 Retry Mechanism

The framework includes retry support for failed tests.

Conceptually:

```text
Test
 │
 ├── PASS ─────────────► Continue
 │
 └── FAIL
      │
      ▼
    Retry
      │
      ├── PASS ────────► Continue
      │
      └── FAIL ────────► Report Failure
```

Retry is intended to help diagnose intermittent failures and should not be used to hide genuine application defects.

---

# 📸 Screenshot & Failure Evidence

The framework includes screenshot support to provide visual evidence when investigating test failures.

Additional diagnostic information can include:

- Screenshot
- Video recording
- Logs
- Test reports

This helps reduce the time required to reproduce and investigate failures.

---

# 🎥 Video Recording

The project includes video-recording support through the Monte Media library.

Video evidence can be used together with screenshots and logs to understand the sequence of actions that occurred during test execution.

The recorder creates timestamped video files in the configured test-report directory.

---

# 📊 Reporting

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

# ⚡ Parallel Test Execution

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

---

# ⚙️ Configuration

Test configuration is maintained in:

```text
src/test/resources/application.properties
```

Example configuration:

```properties
app.url=https://example.com
browser=chrome
headless=false
window.maximize=true
implicit.wait=10
explicit.wait=10
```

Update environment-specific values before running the tests.

Sensitive information such as passwords, tokens, API keys, and credentials should not be committed to the repository.

---

# 🧪 Running UI Tests

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

# 📈 Performance Testing with k6

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

The workflow also contains quality-check and notification steps.

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

# 📦 Test Reports & Artifacts

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

# 🧠 Test Strategy

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

# 🔐 Security & Configuration Guidelines

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

# 🎯 Current Project Scope

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
- ✅ GitHub Actions CI/CD
- ✅ k6 Load Testing
- ✅ k6 Stress Testing
- ✅ k6 Spike Testing

---

# 🔮 Future Improvements

The following items can be added in future iterations:

- [ ] API automation with REST Assured
- [ ] Database testing
- [ ] Cross-browser execution
- [ ] Selenium Grid
- [ ] Docker-based execution
- [ ] SonarQube quality gates
- [ ] Slack/Teams notifications
- [ ] More advanced performance dashboards
- [ ] Expanded test-data management

These are future enhancements and are not presented as currently implemented features.

---

# 📚 What This Project Demonstrates

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
