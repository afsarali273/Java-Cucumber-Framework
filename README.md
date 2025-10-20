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
