# Usage Examples

## 1. Switching Between Selenium and Playwright

Edit `src/main/resources/config.properties`:

```properties
# For Selenium
framework.type=selenium
browser=chrome

# For Playwright
framework.type=playwright
browser=chromium
```

## 2. Using Manual QA-Friendly Keywords

### UI Keywords Example

```java
import com.automation.keywords.UIKeywords;
import com.automation.core.assertions.AssertUtils;

public class SimpleUITest {
    public void testLogin() {
        UIKeywords.openBrowser();
        UIKeywords.navigateToURL("https://example.com");
        UIKeywords.enterText("#username", "testuser");
        UIKeywords.enterText("#password", "password123");
        UIKeywords.clickElement("#loginButton");
        
        boolean isVisible = UIKeywords.isElementVisible("#dashboard");
        AssertUtils.assertTrue(isVisible, "Dashboard should be visible");
        
        UIKeywords.closeBrowser();
    }
}
```

### API Keywords Example

```java
import com.automation.keywords.APIKeywords;
import com.automation.core.assertions.AssertUtils;

public class SimpleAPITest {
    public void testGetUser() {
        APIKeywords.initializeAPI();
        APIKeywords.sendGETRequest("/users/1");
        
        int statusCode = APIKeywords.getResponseStatusCode();
        AssertUtils.assertEquals(statusCode, 200, "Status code should be 200");
        
        String name = APIKeywords.getResponseField("name");
        AssertUtils.assertNotNull(name, "Name should not be null");
    }
}
```

## 3. Using Page Objects

### Selenium Page Object

```java
public class LoginPage extends SeleniumReusable {
    private By usernameField = By.id("username");
    private By passwordField = By.id("password");
    private By loginButton = By.id("login");
    
    public void login(String username, String password) {
        type(usernameField, username);
        type(passwordField, password);
        click(loginButton);
    }
}
```

### Playwright Page Object

```java
public class LoginPage extends PlaywrightReusable {
    private String usernameField = "#username";
    private String passwordField = "#password";
    private String loginButton = "#login";
    
    public void login(String username, String password) {
        type(usernameField, username);
        type(passwordField, password);
        click(loginButton);
    }
}
```

## 4. Using Soft Assertions

```java
import com.automation.core.assertions.AssertUtils;

public void validateMultipleFields() {
    AssertUtils.enableSoftAssert();
    
    AssertUtils.assertEquals(actualName, expectedName, "Name validation");
    AssertUtils.assertEquals(actualEmail, expectedEmail, "Email validation");
    AssertUtils.assertEquals(actualPhone, expectedPhone, "Phone validation");
    
    AssertUtils.assertAll(); // This will fail if any assertion failed
}
```

## 5. Writing Cucumber Step Definitions

```java
@Given("user is on login page")
public void userIsOnLoginPage() {
    loginPage.navigateTo(ConfigManager.getInstance().getAppUrl());
}

@When("user enters username {string} and password {string}")
public void userEntersCredentials(String username, String password) {
    loginPage.login(username, password);
}

@Then("user should see dashboard")
public void userShouldSeeDashboard() {
    AssertUtils.assertTrue(dashboardPage.isDashboardVisible(), 
        "Dashboard should be visible");
}
```

## 6. API Testing with Custom Headers

```java
Map<String, String> headers = new HashMap<>();
headers.put("Authorization", "Bearer token123");
headers.put("Content-Type", "application/json");

APIClient.setHeaders(headers);
Response response = APIClient.get("/protected/resource");
```

## 7. Running Tests

```bash
# Run all tests
mvn clean test

# Run only UI tests
mvn test -Dcucumber.filter.tags="@UI"

# Run only API tests
mvn test -Dcucumber.filter.tags="@API"

# Run with specific browser
mvn test -Dbrowser=firefox

# Run in headless mode
mvn test -Dheadless=true

# Run with specific environment
mvn test -Denvironment=prod

# Generate Allure report
mvn allure:serve
```

## 8. Parallel Execution

Configured in `testng.xml`:

```xml
<suite name="Test Suite" parallel="methods" thread-count="3">
```

Or via Maven:

```bash
mvn test -DthreadCount=5
```

## 9. Custom Reporting

Reports are automatically generated after test execution:

- **Custom HTML Report**: `test-output/reports/CustomReport_*.html`
- **Cucumber Report**: `test-output/cucumber-reports/cucumber.html`
- **Logs**: `test-output/logs/automation.log`

## 10. Screenshot Capture

Screenshots are automatically captured on failure. To capture manually:

```java
import com.automation.core.reporting.ScreenshotUtil;

String screenshotPath = ScreenshotUtil.captureScreenshot("test_name");
```

## 11. Using Framework as JAR

After building the JAR:

```bash
mvn clean package
```

Include in another project's `pom.xml`:

```xml
<dependency>
    <groupId>com.automation</groupId>
    <artifactId>SandsAutoFramework</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

## 12. Environment-Specific Configuration

Create environment files:
- `qa.properties`
- `prod.properties`
- `dev.properties`

Switch environment in `config.properties`:

```properties
environment=qa
```

Or via command line:

```bash
mvn test -Denvironment=prod
```
