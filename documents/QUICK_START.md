# Quick Start Guide

## Prerequisites

- Java 11 or higher
- Maven 3.6+
- Chrome/Firefox browser installed

## Step 1: Build the Project

```bash
cd SandsAutoFramework
mvn clean install
```

## Step 2: Configure Framework

Edit `src/main/resources/config.properties`:

```properties
# Choose framework: selenium or playwright
framework.type=selenium

# Choose browser: chrome, firefox, webkit
browser=chrome

# Headless mode
headless=false

# Environment: qa or prod
environment=qa
```

## Step 3: Run Sample Tests

### Run All Tests
```bash
mvn clean test
```

### Run Only UI Tests
```bash
mvn test -Dcucumber.filter.tags="@UI"
```

### Run Only API Tests
```bash
mvn test -Dcucumber.filter.tags="@API"
```

### Run with Specific Browser
```bash
mvn test -Dbrowser=firefox
```

### Run in Headless Mode
```bash
mvn test -Dheadless=true
```

## Step 4: View Reports

After test execution:

1. **Custom HTML Report**:
   - Open: `test-output/reports/CustomReport_*.html`
   - Contains: Pass/fail summary, test details, screenshots

2. **Cucumber HTML Report**:
   - Open: `test-output/cucumber-reports/cucumber.html`

3. **Logs**:
   - View: `test-output/logs/automation.log`

4. **Allure Report** (Optional):
   ```bash
   mvn allure:serve
   ```

## Step 5: Switch to Playwright

1. Edit `config.properties`:
   ```properties
   framework.type=playwright
   browser=chromium
   ```

2. Run tests:
   ```bash
   mvn clean test
   ```

The same tests will now run with Playwright!

## Step 6: Create Your First Test

### 1. Create Feature File

`src/test/resources/features/login.feature`:
```gherkin
@UI
Feature: Login Functionality

  Scenario: Successful login
    Given user opens login page
    When user enters username "testuser" and password "password123"
    And user clicks login button
    Then user should see dashboard
```

### 2. Create Page Object

`src/main/java/com/automation/pages/LoginPage.java`:
```java
package com.automation.pages;

import com.automation.reusables.SeleniumReusable;
import org.openqa.selenium.By;

public class LoginPage extends SeleniumReusable {
    private By usernameField = By.id("username");
    private By passwordField = By.id("password");
    private By loginButton = By.id("loginBtn");
    
    public void openLoginPage() {
        navigateTo("https://yourapp.com/login");
    }
    
    public void login(String username, String password) {
        type(usernameField, username);
        type(passwordField, password);
        click(loginButton);
    }
}
```

### 3. Create Step Definitions

`src/test/java/com/automation/stepdefinitions/LoginSteps.java`:
```java
package com.automation.stepdefinitions;

import com.automation.pages.LoginPage;
import com.automation.core.assertions.AssertUtils;
import io.cucumber.java.en.*;

public class LoginSteps {
    private LoginPage loginPage = new LoginPage();
    
    @Given("user opens login page")
    public void userOpensLoginPage() {
        loginPage.openLoginPage();
    }
    
    @When("user enters username {string} and password {string}")
    public void userEntersCredentials(String username, String password) {
        loginPage.login(username, password);
    }
    
    @When("user clicks login button")
    public void userClicksLoginButton() {
        // Already handled in login method
    }
    
    @Then("user should see dashboard")
    public void userShouldSeeDashboard() {
        // Add your validation
        AssertUtils.assertTrue(true, "Dashboard should be visible");
    }
}
```

### 4. Run Your Test
```bash
mvn test -Dcucumber.filter.tags="@UI"
```

## Step 7: Create Your First API Test

### 1. Create Feature File

`src/test/resources/features/user_api.feature`:
```gherkin
@API
Feature: User API Testing

  Scenario: Get user details
    Given API client is initialized
    When user sends GET request to "/users/1"
    Then response status code should be 200
    And response "name" should not be null
```

### 2. Run API Test
```bash
mvn test -Dcucumber.filter.tags="@API"
```

## Common Commands

```bash
# Clean and build
mvn clean install

# Run tests
mvn test

# Run specific feature
mvn test -Dcucumber.features="src/test/resources/features/login.feature"

# Run with tags
mvn test -Dcucumber.filter.tags="@UI and @Smoke"

# Skip tests
mvn clean install -DskipTests

# Package as JAR
mvn clean package

# Generate Allure report
mvn allure:serve

# Clean test output
rm -rf test-output/
```

## Troubleshooting

### Issue: Driver not found
**Solution**: Ensure Chrome/Firefox is installed. Selenium Manager will auto-download drivers.

### Issue: Tests fail in parallel
**Solution**: Check thread count in `testng.xml`. Reduce if needed:
```xml
<suite parallel="methods" thread-count="2">
```

### Issue: Screenshots not captured
**Solution**: Check `config.properties`:
```properties
screenshot.on.failure=true
```

### Issue: Playwright not working
**Solution**: Install Playwright browsers:
```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
```

## Tips

1. **Use Soft Assertions** for multiple validations:
   ```java
   AssertUtils.enableSoftAssert();
   AssertUtils.assertEquals(value1, expected1, "Check 1");
   AssertUtils.assertEquals(value2, expected2, "Check 2");
   AssertUtils.assertAll();
   ```

2. **Log Important Actions**:
   ```java
   LogManager.info("Performing important action");
   ```

3. **Use Keywords for Simple Tests**:
   ```java
   UIKeywords.openBrowser();
   UIKeywords.navigateToURL("https://example.com");
   UIKeywords.clickElement("#button");
   ```

4. **Environment-Specific Config**:
   - Create `dev.properties`, `staging.properties`
   - Switch: `mvn test -Denvironment=staging`

## Next Steps

1. ✅ Run sample tests
2. ✅ Review generated reports
3. ✅ Create your page objects
4. ✅ Write your feature files
5. ✅ Add step definitions
6. ✅ Configure environments
7. ✅ Run in CI/CD pipeline

## Support

- Check `README.md` for detailed documentation
- Review `USAGE_EXAMPLES.md` for more examples
- See `FRAMEWORK_ARCHITECTURE.md` for architecture details

---

**Happy Testing! 🚀**
