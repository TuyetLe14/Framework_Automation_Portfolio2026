# Test Automation Framework - Portfolio

Professional test automation framework built with Selenium WebDriver, TestNG, and Java 21 LTS for web application testing.

## 📋 Table of Contents
- [Features](#features)
- [Requirements](#requirements)
- [Project Structure](#project-structure)
- [Setup & Installation](#setup--installation)
- [Running Tests](#running-tests)
- [Configuration](#configuration)
- [Test Reports](#test-reports)
- [CI/CD Integration](#cicd-integration)
- [Contributing](#contributing)

## ✨ Features

✅ **Modern Stack**
- Java 21 LTS (Long-Term Support until 2029)
- Selenium WebDriver 4.18.1
- TestNG 7.10.2 for test execution
- Maven 3.9.16+ for build automation

✅ **Advanced Capabilities**
- Page Object Model (POM) design pattern
- Screenshot capture on test failure
- Video recording for failed tests
- Automatic retry mechanism for flaky tests
- ExtentReports for HTML test reports
- SLF4J logging integration
- Excel test data support (Apache POI)
- **Performance Testing with k6** (Load, Stress, Spike testing)

✅ **Professional Practices**
- CI/CD ready (GitHub Actions) with automated test execution
- Performance testing pipeline (weekly scheduled runs)
- Externalized configuration (application.properties)
- Comprehensive logging
- Clean code architecture
- Git version control
- Production-ready documentation (README, CONTRIBUTING, CHANGELOG)

## 📦 Requirements

- **Java**: OpenJDK 21 or later
- **Maven**: 3.9.0 or later
- **ChromeDriver**: Auto-managed by WebDriverManager (if integrated)
- **Browser**: Chrome/Chromium (for running tests)
- **Git**: For version control

## 🏗️ Project Structure

```
Framework_Automation_Portfolio2026/
├── src/
│   ├── main/java/com/tuyet/
│   │   ├── base/
│   │   │   └── BaseTest.java              # Base test class with setup/teardown
│   │   ├── constants/
│   │   │   └── ConfigsData.java           # Configuration constants
│   │   ├── pages/
│   │   │   ├── BasePage.java              # Base POM class with common methods
│   │   │   ├── HomePage.java              # Home page object
│   │   │   └── AboutPage.java             # About page object
│   │   └── utils/
│   │       ├── CaptureHelpers.java        # Screenshot utility
│   │       ├── HighlightHelper.java       # Element highlight utility
│   │       ├── LinkValidator.java         # Link validation utility
│   │       ├── RetryListener.java         # Retry logic for failed tests
│   │       └── VideoRecorder.java         # Video recording utility
│   │
│   └── test/java/com/tuyet/
│       ├── base/
│       │   └── BaseTest.java              # Test base class
│       ├── listeners/
│       │   └── ExcelListener.java         # Test listener for Excel reporting
│       └── tests/
│           ├── HomeTest.java              # Home page tests
│           ├── AboutTest.java             # About page tests
│           ├── ThemeSyncTest.java         # Theme synchronization tests
│           └── OpenWeb.java               # Basic web opening tests
│
├── src/test/resources/
│   └── application.properties              # Test configuration
│
├── pom.xml                                 # Maven configuration
├── testng.xml                              # TestNG suite configuration
├── README.md                               # This file
└── Test_Reports/                           # Generated test reports
    └── {timestamp}/
        ├── Screenshots/                    # Failed test screenshots
        └── Videos/                         # Recorded videos
```

## 🔧 Setup & Installation

### 1. Clone Repository
```bash
git clone https://github.com/yourusername/Framework_Automation_Portfolio2026.git
cd Framework_Automation_Portfolio2026
```

### 2. Install Java 21
Download and install from [Eclipse Adoptium](https://adoptium.net/) or [Amazon Corretto](https://aws.amazon.com/corretto/):

```bash
# Verify installation
java -version
# Expected: openjdk version "21.x.x"
```

### 3. Install Maven
```bash
# Download from https://maven.apache.org/
# Add MAVEN_HOME to environment variables

mvn -version
# Expected: Apache Maven 3.9.x
```

### 4. Install Dependencies
```bash
mvn clean install
```

## ▶️ Running Tests

### Run All Tests
```bash
mvn clean test
```

### Run Specific Test Class
```bash
mvn clean test -Dtest=HomeTest
```

### Run Specific Test Method
```bash
mvn clean test -Dtest=HomeTest#testBasicInfo
```

### Run with TestNG XML Suite
```bash
mvn clean test -Dsuite=testng.xml
```

### Run Tests with Retry
```bash
mvn clean test -DisRetry=true
# Captures video and detailed logs for failed tests
```

### Run Tests in Parallel (Optional - requires testng-parallel.xml)
```bash
mvn clean test -Dsuite=testng-parallel.xml
```

## 🚀 Performance Testing with k6

### Overview
This framework includes **k6** for performance and load testing. Test your application's performance under different load scenarios.

### Quick Start

#### 1. Install k6
```bash
# macOS
brew install k6

# Linux (Debian/Ubuntu)
sudo apt-get install k6

# Windows (Chocolatey)
choco install k6

# Or from: https://github.com/grafana/k6/releases
```

#### 2. Run Performance Tests
```bash
# Load Test (5 concurrent users, 30 seconds)
k6 run performance/load-test.js

# Stress Test (ramp up to 50 users)
k6 run performance/stress-test.js

# Spike Test (sudden 100 user spike)
k6 run performance/spike-test.js

# Run All Tests
./performance/run-all-tests.sh          # Linux/macOS
performance\run-all-tests.bat           # Windows
```

#### 3. View Results
```bash
# Results are saved to: performance/results/
ls -la performance/results/
```

### Performance Test Types

| Test | Purpose | Scenario |
|------|---------|----------|
| **Load Test** | Verify app behavior under normal load | 5 users, 30 seconds |
| **Stress Test** | Find breaking point | Ramp to 50 users over 14 minutes |
| **Spike Test** | Test sudden traffic surge | 100 users spike for 10 seconds |

### Thresholds & Metrics
Each test includes performance thresholds:
- ✅ 95% of requests < 500ms (load) / 1000ms (stress)
- ✅ Less than 10% failed requests
- ✅ Application stability check

**Learn more**: [performance/README.md](performance/README.md)

## ⚙️ Configuration

### application.properties
Create `src/test/resources/application.properties`:

```properties
# Application URL
app.url=https://example.com

# Browser Configuration
browser=chrome
headless=false
window.maximize=true

# Timeouts (seconds)
implicit.wait=10
explicit.wait=10
page.load.timeout=60

# Screenshots & Videos
screenshot.on.failure=true
video.on.failure=true
video.record.all=false

# Logging
log.level=INFO

# Report Path
report.path=Test_Reports
```

### Environment-Specific Configuration
Create multiple property files:
- `application-dev.properties`
- `application-staging.properties`
- `application-prod.properties`

Use with:
```bash
mvn clean test -Dspring.profiles.active=dev
```

## 📊 Test Reports

### ExtentReports
After test execution, open the HTML report:
```
Test_Reports/{timestamp}/index.html
```

Features:
- Test execution timeline
- Pass/Fail/Skip statistics
- Screenshot attachments for failed tests
- Video attachments for failed tests
- Detailed log information

### Screenshots
Failed test screenshots are stored in:
```
Test_Reports/{timestamp}/Screenshots/
```

### Video Recordings
Failed test videos are stored in:
```
Test_Reports/{timestamp}/Videos/
```

## 🚀 CI/CD Integration

### GitHub Actions Workflow
Tests run automatically on:
- Push to main branch
- Pull requests

Workflow file: `.github/workflows/test-automation.yml`

View results: GitHub Actions tab in repository

### Running Locally with Same Configuration
```bash
# Simulate CI environment (headless mode)
mvn clean test -Dheadless=true
```

### Manual Test Run Badge
Add to README:
```markdown
![Test Status](https://github.com/yourusername/repo/actions/workflows/test-automation.yml/badge.svg)
```

## 🔍 Troubleshooting

### Common Issues

**Issue**: Chrome driver not found
```bash
# Solution: Download ChromeDriver matching your Chrome version
# Or use WebDriverManager (recommended - auto-manages drivers)
```

**Issue**: Tests timeout on slow network
```bash
# Solution: Increase timeouts in application.properties
implicit.wait=20
explicit.wait=20
page.load.timeout=120
```

**Issue**: "Invalid target release" error
```bash
# Solution: Ensure JAVA_HOME points to JDK 21
export JAVA_HOME=/path/to/jdk-21
```

## 📝 Writing New Tests

### 1. Create Page Object
```java
public class NewPage extends BasePage {
    private static final By BUTTON_SUBMIT = By.id("submit");
    
    public NewPage(WebDriver driver) {
        super(driver);
    }
    
    public void clickSubmit() {
        clickElement(BUTTON_SUBMIT);
    }
}
```

### 2. Create Test Class
```java
public class NewTest extends BaseTest {
    @Test
    public void testNewFeature() {
        getDriver().get(ConfigsData.URL);
        NewPage page = new NewPage(getDriver());
        page.clickSubmit();
    }
}
```

### 3. Add to testng.xml
```xml
<test name="New Tests">
    <classes>
        <class name="com.tuyet.tests.NewTest" />
    </classes>
</test>
```

## 🤝 Contributing

1. Create feature branch: `git checkout -b feature/your-feature`
2. Commit changes: `git commit -m 'Add your feature'`
3. Push to branch: `git push origin feature/your-feature`
4. Create Pull Request

### Code Style
- Follow Google Java Style Guide
- Use meaningful variable names
- Add JavaDoc for public methods
- Keep methods focused (Single Responsibility)

## 📚 Resources

- [Selenium Documentation](https://www.selenium.dev/documentation/)
- [TestNG User Guide](https://testng.org/doc/)
- [Maven Guide](https://maven.apache.org/guides/)
- [Java 21 Features](https://www.oracle.com/java/technologies/java-se-21-highlights.html)

## 📄 License

This project is licensed under the MIT License - see LICENSE file for details.

## 👨‍💻 Author

**Lê Huỳnh Anh Tuyết**  
- Portfolio: [Your Portfolio Website]
- GitHub: [Your GitHub Profile]
- Email: your-email@example.com

---

**Last Updated**: August 14, 2026  
**Java Version**: 21 LTS  
**Selenium Version**: 4.18.1  
**TestNG Version**: 7.10.2
