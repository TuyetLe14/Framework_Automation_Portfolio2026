# Contributing to Test Automation Framework

Thank you for your interest in contributing! This guide outlines how to contribute to this project.

## Code Style Guidelines

### Java Code Standards
- **Indentation**: 4 spaces (enforced by `.editorconfig`)
- **Naming Conventions**:
  - Classes: PascalCase (e.g., `HomePage`)
  - Methods: camelCase (e.g., `getPageTitle()`)
  - Constants: UPPER_CASE (e.g., `HEADING_NAME`)
  - Locators: Prefix with element type (e.g., `BUTTON_THEME_SWITCH`, `INPUT_EMAIL`)

### Page Object Model (POM) Standards
- **File Structure**:
  1. Locators (private static final By fields)
  2. Constructor
  3. Getters (retrieve page data)
  4. Actions (user interactions, return `this` for fluent API)
  5. Validations (check page state)
  6. Helper methods

- **Example Method Organization**:
```java
// Locators
private static final By BUTTON_LOGIN = By.xpath("//button[text()='Login']");

// Getter
public String getPageTitle() {
    return driver.getTitle();
}

// Action (fluent API - return this)
public HomePage clickLoginButton() {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    wait.until(ExpectedConditions.elementToBeClickable(BUTTON_LOGIN)).click();
    return this;
}

// Validation
public boolean isLoginFormVisible() {
    return driver.findElement(BUTTON_LOGIN).isDisplayed();
}
```

### Test Class Standards
- Extend `BaseTest` for WebDriver initialization
- Use TestNG annotations: `@Test`, `@BeforeMethod`, `@AfterMethod`
- Method naming: `test<Feature><Scenario>` (e.g., `testThemeSwitching()`)
- One assertion focus per test
- Use `@Listeners({RetryListener.class})` for retry capability
- Load configuration via `ConfigsData` class

## Configuration

All test configuration is externalized in `src/test/resources/application.properties`:

```properties
# Application Configuration
app.url=https://portfolio-2026.tgdd-ld9941.workers.dev/
app.environment=dev

# Browser Configuration
browser.name=chrome
browser.headless=false
browser.window.maximize=true

# Timeout Configuration
timeout.implicit=10
timeout.explicit=10
timeout.pageLoad=60
```

**DO NOT** hardcode configuration values in source code.

## Running Tests

### Single Test File
```bash
mvn test -Dtest=HomeTest
```

### Specific Test Method
```bash
mvn test -Dtest=HomeTest#testThemeSwitching
```

### Parallel Execution
```bash
mvn test -Dsuites=testng-parallel.xml
```

### With Custom Properties
```bash
mvn test -Dbrowser.name=firefox -Dtimeout.implicit=15
```

## Debugging

1. **Enable Debug Logging**:
   - Edit `src/test/resources/application.properties`
   - Set `log.level=DEBUG`

2. **View Screenshots/Videos**:
   - Screenshots: `Test_Reports/Screenshots/`
   - Videos: `Test_Reports/Videos/`
   - Reports: `Test_Reports/*.html`

3. **Use RetryListener**:
   - Automatically retries failed tests (configurable via `retry.count`)
   - Videos recorded only on retry (first failure)

## Git Workflow

1. Create a feature branch: `git checkout -b feature/descriptive-name`
2. Make changes following code standards above
3. Commit with descriptive messages:
   ```bash
   git commit -m "feat: add Page Object for UserProfile page"
   git commit -m "test: add test cases for UserProfile login flow"
   git commit -m "fix: handle timeout in waitForElement method"
   ```
4. Push to branch: `git push origin feature/descriptive-name`
5. Create a Pull Request with description of changes

## Commit Message Format

Follow conventional commits format:

```
<type>(<scope>): <subject>

<body>

<footer>
```

**Types**: feat, fix, docs, style, refactor, test, chore  
**Scope**: page, test, util, config, ci  
**Subject**: imperative, 50 chars max, lowercase, no period

**Examples**:
- `feat(pages): add AboutPage locators and methods`
- `test(tests): add email validation test cases`
- `fix(utils): handle stale element reference exception`

## Pull Request Guidelines

1. **Title**: Follow commit message format
2. **Description**: Explain what changed and why
3. **Tests**: Include relevant test screenshots/videos
4. **Checklist**:
   - [ ] Code follows style guidelines
   - [ ] Tests compile with `mvn clean compile`
   - [ ] Tests pass with `mvn clean test`
   - [ ] No hardcoded values
   - [ ] Configuration uses `ConfigsData`
   - [ ] JavaDoc added for public methods

## Documentation

- Update `README.md` for significant changes
- Add JavaDoc comments to public methods and classes
- Document new configuration properties in `application.properties`

## Resources

- [Selenium WebDriver Documentation](https://www.selenium.dev/documentation/)
- [TestNG Documentation](https://testng.org/doc/)
- [Page Object Model Pattern](https://www.selenium.dev/documentation/test_practices/encouraged/page_object_models/)
- [Java Code Conventions](https://www.oracle.com/java/technologies/javase/codeconventions-contents.html)

Thank you for contributing! 🎉
