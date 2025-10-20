# Sands Automation Framework

Enterprise-grade automation framework supporting Selenium, Playwright, and API testing with Cucumber BDD.

## Features

- **Multi-Framework Support**: Switch between Selenium and Playwright via configuration
- **API Testing**: REST API testing with Rest Assured
- **BDD with Cucumber**: Behavior-driven development with Gherkin syntax
- **Parallel Execution**: TestNG-based parallel test execution
- **Centralized Configuration**: Global and environment-specific properties
- **Advanced Assertions**: Soft and hard assertions with auto-logging
- **Custom HTML Reports**: Detailed test reports with screenshots
- **Allure Integration**: Optional Allure reporting
- **Thread-Safe**: All components are thread-safe for parallel execution
- **Reusable Components**: Framework-specific reusable classes

## Project Structure

```
SandsAutoFramework/
├── src/main/java/com/automation/
│   ├── core/
│   │   ├── api/              # API client for REST testing
│   │   ├── assertions/       # Centralized assertion utilities
│   │   ├── config/           # Configuration manager
│   │   ├── driver/           # Driver manager (Selenium/Playwright)
│   │   ├── hooks/            # Cucumber hooks
│   │   ├── logging/          # Logging utilities
│   │   └── reporting/        # Custom reporter and screenshot utilities
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
framework.type=selenium    # selenium or playwright
browser=chrome            # chrome, firefox, webkit
headless=false
environment=qa            # qa or prod
executionType=cucumber    # cucumber or testng
```

### Environment Configuration (qa.properties, prod.properties)
```properties
app.url=https://www.flipkart.com
api.base.url=https://jsonplaceholder.typicode.com
```

## Core Components

### 1. ConfigManager
Centralized configuration management with support for global and environment-specific properties.

### 2. DriverManager
Thread-safe driver management for both Selenium and Playwright with automatic initialization.

### 3. APIClient
REST API client built on Rest Assured with support for GET, POST, PUT, DELETE operations.

### 4. AssertUtils
Centralized assertion utility supporting:
- Hard and soft assertions
- Auto-logging to console, custom report, and Allure
- Multiple assertion types: equals, contains, greater than, less than, null checks

### 5. CustomReporter
HTML report generator with:
- Test case name, status, duration
- Pass/fail summary
- Detailed logs
- Screenshot links

### 6. Reusable Classes
- **SeleniumReusable**: Common Selenium operations
- **PlaywrightReusable**: Common Playwright operations
- **MobileReusable**: Common Mobile (Appium) operations

## Usage

### Running Tests

```bash
# Run all tests
mvn clean test

# Run specific feature
mvn test -Dcucumber.filter.tags="@UI"

# Run with specific browser
mvn test -Dbrowser=firefox

# Generate Allure report
mvn allure:serve
```

### Switching Frameworks

Edit `config.properties`:
```properties
framework.type=playwright  # Change to playwright
```

### Writing Tests

#### Page Object Example (Selenium)
```java
public class LoginPage extends SeleniumReusable {
    private By usernameField = By.id("username");
    
    public void enterUsername(String username) {
        type(usernameField, username);
    }
}
```

#### Step Definition Example
```java
@When("user searches for {string}")
public void userSearchesFor(String product) {
    page.searchProduct(product);
}
```

#### Feature File Example
```gherkin
@UI
Feature: Product Search
  Scenario: Search for iPhone
    Given user opens website
    When user searches for "iPhone"
    Then results should be displayed
```

### Using Assertions

```java
// Hard assertion
AssertUtils.assertEquals(actual, expected, "Values should match");

// Soft assertions
AssertUtils.enableSoftAssert();
AssertUtils.assertEquals(value1, expected1, "Check 1");
AssertUtils.assertEquals(value2, expected2, "Check 2");
AssertUtils.assertAll(); // Verify all soft assertions
```

### API Testing

```java
Response response = APIClient.get("/posts/1");
AssertUtils.assertEquals(response.getStatusCode(), 200, "Status code check");
```

### YAML Utility

```java
String host = YamlUtils.getStringByPath("config.yaml", "database.host");
```

## Packaging as JAR

To package the core framework as a reusable JAR:

```bash
mvn clean package
```

The JAR can be included in other projects as a dependency.

## Reports

- **Custom HTML Report**: `test-output/reports/CustomReport_*.html`
- **Cucumber Report**: `test-output/cucumber-reports/cucumber.html`
- **Allure Report**: Run `mvn allure:serve`
- **Logs**: `test-output/logs/automation.log`

## Dependencies

- Selenium 4.15.0
- Playwright 1.40.0
- Cucumber 7.14.0
- TestNG 7.8.0
- Rest Assured 5.3.2
- Allure 2.24.0
- SLF4J + Logback

## Best Practices

1. Use Page Object Model for UI tests
2. Keep step definitions thin, logic in page objects
3. Use soft assertions for multiple validations
4. Tag features appropriately (@UI, @API)
5. Use meaningful assertion messages
6. Capture screenshots on failure
7. Review custom HTML reports after execution

## Thread Safety

All core components are thread-safe using ThreadLocal:
- DriverManager
- APIClient
- AssertUtils
- CustomReporter
- LogManager

## Support

For issues or questions, refer to the inline documentation in each class.

# SandsAutoFramework Documentation

## Last Updated: 2025-10-20

---

## Overview
SandsAutoFramework is a unified automation framework supporting both Cucumber (BDD) and TestNG/Modular execution types. It enables robust automation for Web, Mobile (Appium), and API testing, with flexible configuration via properties and YAML files.

---

## Key Enhancements (2025-10-20)
- **Dual Execution Support:**
  - `executionType` property allows switching between Cucumber and TestNG/Modular execution.
  - ModularTestConfig and ModularTestLifecycle enable standardized lifecycle management for TestNG tests.
- **Mobile Automation:**
  - Appium driver support added to DriverManager and ConfigManager.
  - Mobile configuration properties in `config.properties`.
  - MobileReusable class updated for AppiumBy and W3C Actions API.
- **YAML Configuration:**
  - YamlUtils utility for reading YAML files as Map, POJO, or by key path.
- **Framework Type Detection:**
  - FrameworkConfig and ConfigManager support: selenium, playwright, api, mobile, modular, testng.
  - Helper methods: isSelenium(), isPlaywright(), isAPI(), isMobile(), isModular(), isTestNG().
- **Cucumber Hooks:**
  - Tag-based driver initialization for @UI, @Mobile, @API.
- **TestNG Modular Support:**
  - ModularTestConfig provides DataProviders and lifecycle hooks for browser, mobile, and API tests.
- **API Automation:**
  - APIKeywords class for reusable API test steps in both Cucumber and TestNG.

---

## Configuration
- `config.properties` now supports mobile and executionType properties.
- YAML files can be used for advanced configuration and test data.

---

## Usage Examples
### Cucumber
```gherkin
Feature: Mobile App Login
  @Mobile
  Scenario: User logs in on mobile
    Given the app is launched
    When the user enters valid credentials
    Then the user should see the home screen
```

### TestNG Modular
```java
@Test(dataProvider = "ChromeBrowser", dataProviderClass = ModularTestConfig.class)
public void webTest(String instance) {
    WebDriver driver = DriverManager.getSeleniumDriver();
    driver.get("https://example.com");
}

@Test(dataProvider = "MobileAndroid", dataProviderClass = ModularTestConfig.class)
public void mobileTest(String instance) {
    AppiumDriver driver = DriverManager.getAppiumDriver();
    // ...mobile test steps...
}

@Test(dataProvider = "API", dataProviderClass = ModularTestConfig.class)
public void apiTest(String instance) {
    // Use APIKeywords for API tests
}
```

### YAML Utility
```java
String host = YamlUtils.getStringByPath("config.yaml", "database.host");
```

---

## Extensibility
- Easily add new drivers, execution modes, and configuration sources.
- Modular design for maintainability and scalability.

---

## References
- **ConfigManager**: Loads configuration from properties/YAML.
- **DriverManager**: Manages driver lifecycle for all supported types.
- **ModularTestConfig & ModularTestLifecycle**: TestNG modular execution.
- **MobileReusable**: Mobile automation utilities.
- **YamlUtils**: YAML file reading utilities.
- **APIKeywords**: Reusable API automation steps.

---

## License
Licensed under the Apache License, Version 2.0.

---

## Authors & Contributors
- Framework maintained by Afsar Ali.

---

## Change Log
- See above for 2025-10-20 enhancements.
