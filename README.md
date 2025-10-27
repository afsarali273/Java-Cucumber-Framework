# Sands Automation Framework

Enterprise-grade automation framework supporting Selenium, Playwright, Mobile, Desktop, and API testing with Cucumber BDD.

## Features

- **Multi-Framework Support**: Selenium, Playwright, Mobile (Appium), Desktop (WinAppDriver), API
- **Unified Logging**: Single logger for all reports (ExtentReports, CustomReporter, Allure, Logs)
- **BDD with Cucumber**: Behavior-driven development with Gherkin syntax
- **TestNG/Modular Support**: Traditional TestNG test execution
- **Parallel Execution**: Thread-safe parallel test execution
- **Comprehensive Configuration**: Highly configurable via properties files
- **Advanced Assertions**: Playwright native assertions + custom assertions
- **Multiple Reports**: ExtentReports, CustomReporter, Allure, Cucumber HTML
- **Thread-Safe Context**: Share data between steps with ScenarioContext
- **Windows Desktop Automation**: WinAppDriver support for desktop apps
- **Screenshot Embedding**: Screenshots embedded in reports (not links)

## Project Structure

```
SandsAutoFramework/
├── src/main/java/com/automation/
│   ├── core/
│   │   ├── api/              # API client for REST testing
│   │   ├── assertions/       # Centralized assertion utilities
│   │   ├── config/           # Configuration manager
│   │   ├── context/          # ScenarioContext for data sharing
│   │   ├── driver/           # Driver manager (Selenium/Playwright/Mobile/Desktop)
│   │   ├── hooks/            # Cucumber hooks
│   │   ├── logging/          # UnifiedLogger for all reports
│   │   └── reporting/        # ExtentReporter, CustomReporter, ScreenshotUtil
│   ├── pages/                # Page Object classes
│   └── reusables/            # Framework-specific reusable methods
├── src/test/java/com/automation/
│   ├── runners/              # TestNG Cucumber runners
│   └── stepdefinitions/      # Cucumber step definitions
├── src/test/resources/
│   └── features/             # Cucumber feature files
└── src/main/resources/       # Configuration files
```

## Configuration

### Global Configuration (config.properties)
```properties
# Framework Type: selenium | playwright | api | mobile | desktop | windows
framework.type=playwright

# Execution Type: cucumber | testng | modular
executionType=cucumber

# Browser: chrome | firefox | edge | safari | webkit
browser=chrome

# Headless Mode
headless=false

# Environment: qa | uat | staging | prod
environment=qa

# Parallel Execution
parallel.execution=true
thread.count=3

# Timeouts (seconds)
implicit.wait=10
explicit.wait=20
page.load.timeout=30

# Reporting
screenshot.on.failure=true
report.path=test-output/reports
```

### Desktop Configuration
```properties
desktop.winAppDriverUrl=http://127.0.0.1:4723
desktop.app=C:\\Windows\\System32\\calc.exe
```

### Mobile Configuration
```properties
mobile.platformName=Android
mobile.deviceName=emulator-5554
mobile.appiumServerUrl=http://127.0.0.1:4723
mobile.appPackage=com.example.android
mobile.appActivity=com.example.android.MainActivity
```

## Core Components

### 1. UnifiedLogger
Single logging call logs to all reporting systems:
```java
UnifiedLogger.info("Test started");
UnifiedLogger.action("Click", loginButton);
UnifiedLogger.pass("Login successful");
UnifiedLogger.fail("Expected element not found");
```

Logs to:
- LogManager (file logs)
- CustomReporter (HTML with embedded screenshots)
- ExtentReports (HTML with dark theme)
- Allure (interactive reports)

### 2. ScenarioContext
Thread-safe data sharing between steps:
```java
// Store data
ScenarioContext.set("productName", "iPhone 15");
ScenarioContext.set("cartCount", 1);

// Retrieve data
String product = ScenarioContext.get("productName", String.class);
Integer count = ScenarioContext.get("cartCount", Integer.class);
```

### 3. DriverManager
Supports all driver types:
- `getSeleniumDriver()` - Selenium WebDriver
- `getPlaywrightPage()` - Playwright Page
- `getAppiumDriver()` - Mobile driver
- `getWindowsDriver()` - Desktop driver

### 4. Reusable Classes
- **SeleniumReusable**: Selenium operations
- **PlaywrightReusable**: Playwright operations + native assertions
- **MobileReusable**: Mobile (Appium) operations
- **WindowsDesktopReusable**: Desktop automation operations
- **APIReusable**: API testing operations

### 5. Playwright Native Assertions
```java
// Element state
assertVisible(locator);
assertEnabled(locator);
assertChecked(locator);

// Text & content
assertContainsText(locator, "Success");
assertHasText(locator, "Welcome");

// Attributes & styles
assertHasAttribute(locator, "href", "/home");
assertHasClass(locator, "active");

// Page assertions
assertPageHasTitle("Home Page");
assertPageURLContains("/dashboard");

// Custom assertions
expect(locator).hasValue("test");
expect(page).hasURL("https://example.com");
```

## Playwright Test Generator 🎭

Generate tests by recording browser interactions:

```bash
# macOS/Linux
./codegen.sh https://example.com

# Windows
codegen.bat https://example.com

# Default demo site
./codegen.sh
```

Codegen opens a browser and records your actions, generating Playwright code automatically. See `CODEGEN_GUIDE.md` for details.

## Running Tests

```bash
# Run all tests
mvn clean test

# Run specific tags
mvn test -Dcucumber.filter.tags="@UI"
mvn test -Dcucumber.filter.tags="@Mobile"
mvn test -Dcucumber.filter.tags="@Desktop"
mvn test -Dcucumber.filter.tags="@API"

# Run with specific browser
mvn test -Dbrowser=firefox

# Generate Allure report
mvn allure:serve
```

## Writing Tests

### Feature File with Tags
```gherkin
@UI
Feature: Web Testing
  Scenario: Login test
    Given user opens website
    When user logs in
    Then dashboard is displayed

@Mobile
Feature: Mobile Testing
  Scenario: Mobile login
    Given mobile app is launched
    When user enters credentials
    Then home screen is shown

@Desktop
Feature: Desktop Testing
  Scenario: Calculator test
    Given calculator app is launched
    When user adds 5 and 3
    Then result is 8

@API
Feature: API Testing
  Scenario: Get user details
    When user sends GET request to "/users/1"
    Then response status is 200
```

### Step Definition with Context
```java
@When("user searches for {string}")
public void userSearchesFor(String product) {
    // Store in context
    ScenarioContext.set("productName", product);
    
    page.searchProduct(product);
    UnifiedLogger.action("Search", product);
}

@Then("product should be in cart")
public void productShouldBeInCart() {
    // Retrieve from context
    String product = ScenarioContext.get("productName", String.class);
    
    assertContainsText(locator(".cart"), product);
}
```

### Page Object Example
```java
public class LoginPage extends PlaywrightReusable {
    
    public void login(String username, String password) {
        type(locator("#username"), username);
        type(locator("#password"), password);
        click(locator("#login"));
        
        // Use Playwright native assertion
        assertVisible(locator(".dashboard"));
    }
}
```

## UI Example Feature Files

We provide a sample feature file with many common UI steps to serve as templates and smoke tests for the UI layer.

- Location: `src/test/resources/features/ui_examples.feature`
- Tag: `@UI` (and feature-level and scenario-level tags for grouping)

What the feature covers:
- Navigation and basic page validation
- Form interactions (type, clear, save values)
- Dropdowns, checkboxes and file upload
- Tabs / windows management
- Screenshots and element screenshots
- Saving variables and reusing them via `${var}` placeholders

Placeholders and test data
- The feature uses a placeholder `${sampleFilePath}` for file upload. You can provide this in two ways:
  1. Add a property (or env variable) and set it in your CI before running the tests. For example:

```bash
export SAMPLE_FILE_PATH=/absolute/path/to/sample-file.txt
```

  2. Or set the value programmatically in a test hook before file-upload scenario: `ScenarioContext.set("sampleFilePath", "/path/to/file")`.

Running the examples
- Run all UI scenarios with the `@UI` tag:

```bash
mvn test -Dcucumber.filter.tags="@UI"
```

- To run the specific example feature file only:

```bash
mvn test -Dcucumber.options="--features src/test/resources/features/ui_examples.feature"
```

Notes
- These examples are meant as templates; update selectors (CSS/XPath) to match your AUT. Some demo sites (like w3schools) use iframes — the example steps show how to call JS or use Playwright to handle these situations.
- Because `UISteps` delegates to `UIKeywords`, the same Gherkin steps work for both Selenium and Playwright when `framework.type` in `config.properties` is set accordingly.

If you want, I can also:
- Add a second feature file with negative tests and edge-cases.
- Generate a short quick-reference table of all implemented step phrases and parameters to add to the docs.

## Reports

After test execution, you'll have:

1. **ExtentReport**: `test-output/reports/ExtentReport_*.html`
   - Dark theme, modern UI
   - Screenshots embedded
   - Category filtering

2. **CustomReport**: `test-output/reports/CustomReport_*.html`
   - Screenshots embedded as base64
   - Pass/fail summary
   - Detailed logs

3. **Allure Report**: Run `mvn allure:serve`
   - Interactive dashboard
   - Timeline view
   - Trend analysis

4. **Cucumber Report**: `test-output/cucumber-reports/cucumber.html`

5. **Logs**: `test-output/logs/automation.log`

All reports show:
- Test name, status, duration
- Step-by-step logs
- Failure reasons with stack traces
- Embedded screenshots

## Test Data Encryption 🔒

Safely commit credentials to GitHub using Base64 encoding.

### Quick Start
```bash
# Encrypt file (macOS/Linux)
./encrypt-testdata.sh testData/credentials.json

# Encrypt file (Windows)
encrypt-testdata.bat testData\credentials.json

# Use in tests - framework auto-decodes .encrypted files
When user sets request body from file "testData/credentials.json.encrypted"
```

### Features
- ✅ Base64 encoding for GitHub safety
- ✅ Automatic decryption during test execution
- ✅ Supports JSON, XML, SOAP, query params
- ✅ Variable replacement: `${variableName}`
- ✅ Works with all file-loading steps

### Example
```gherkin
Scenario: Login with encrypted credentials
  When user sets request body from file "testData/secure/credentials.json.encrypted"
  And user sends POST request to "/auth/login"
  Then response status code should be 200
```

### Documentation
- 📄 `ENCRYPTION_GUIDE.md` - Complete guide
- 📄 `README_ENCRYPTION.md` - Quick reference
- 🌐 `docs/test-data-encryption.html` - HTML documentation

⚠️ **Note**: Base64 is encoding, not encryption. Use for GitHub safety only. For production, use AWS Secrets Manager, Azure Key Vault, etc.

## Advanced Features

### Parallel Execution
```properties
parallel.execution=true
thread.count=3
parallel.mode=methods
driver.lazy.init=true  # Recommended for cleaner parallel execution
```

### Retry Failed Tests
```properties
retry.failed.tests=true
max.retry.count=2
```

### Video Recording (Playwright)
```properties
video.recording=true
video.path=test-output/videos
```

### Cloud Testing
```properties
cloud.enabled=true
cloud.provider=browserstack
cloud.username=your_username
cloud.accessKey=your_key
```

## Thread Safety

All components are thread-safe:
- DriverManager (ThreadLocal)
- ScenarioContext (ThreadLocal)
- UnifiedLogger
- All Reusable classes
- All Reporters

## Dependencies

- Selenium 4.15.0
- Playwright 1.56.0
- Cucumber 7.14.0
- TestNG 7.8.0
- Rest Assured 5.3.2
- Allure 2.24.0
- ExtentReports 5.1.1
- Appium 10.0.0
- SLF4J + Logback

## Best Practices

1. Use UnifiedLogger for all logging
2. Store test data in ScenarioContext
3. Use Playwright native assertions for better error messages
4. Tag features appropriately (@UI, @API, @Mobile, @Desktop)
5. Use Page Object Model
6. Keep step definitions thin
7. Review all generated reports

## Change Log

### Latest Updates (2025-01-21)
- ✅ Added UnifiedLogger for all reports
- ✅ Added ExtentReports integration
- ✅ Added ScenarioContext for data sharing
- ✅ Added Windows Desktop automation support
- ✅ Added Playwright native assertions
- ✅ Enhanced config.properties with all options
- ✅ Screenshots now embedded in reports
- ✅ Failure reasons with stack traces in reports
- ✅ Updated ConfigManager and FrameworkConfig
- ✅ Added @Desktop tag support in hooks

## License

Licensed under the Apache License, Version 2.0.

## Authors

Framework maintained by Afsar Ali.

## Support

For issues or questions, refer to the inline documentation in each class.
