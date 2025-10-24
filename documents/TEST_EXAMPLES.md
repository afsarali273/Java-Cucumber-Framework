# 📝 Test Examples - All Categories

## Overview

Comprehensive test examples for all application categories using both Cucumber BDD and Modular TestNG approaches.

---

## 🌐 Web Application Testing

### Example 1: Login Test (Cucumber - Using Existing Steps)

**Feature File:** `login.feature`

```gherkin
@Web @Smoke
Feature: User Login
  As a user
  I want to login to the application
  So that I can access my account

  Background:
    Given user opens website "https://casino-credit.example.com"

  Scenario: Successful login with valid credentials
    When user enters "testuser" in "username"
    And user enters "Password123!" in "password"
    And user clicks "loginButton"
    Then user should see element "dashboard"
    And user should see text "Welcome" in "welcomeMessage"

  Scenario: Login failure with invalid credentials
    When user enters "invaliduser" in "username"
    And user enters "wrongpass" in "password"
    And user clicks "loginButton"
    Then user should see text "Invalid credentials" in "errorMessage"
    And user should see element "loginButton"

  Scenario Outline: Login with multiple users
    When user enters "<username>" in "username"
    And user enters "<password>" in "password"
    And user clicks "loginButton"
    Then user should see text "<message>" in "statusMessage"

    Examples:
      | username  | password    | message              |
      | admin     | Admin123!   | Welcome Admin        |
      | manager   | Manager123! | Welcome Manager      |
      | guest     | Guest123!   | Limited Access       |
```

**No Code Required!** All steps are pre-built in the framework.

---

### Example 2: Login Test (Cucumber - Custom Steps)

**Feature File:** `login_custom.feature`

```gherkin
@Web @Custom
Feature: Advanced Login

  Scenario: Login and verify dashboard elements
    Given user navigates to login page
    When user logs in with username "testuser" and password "Password123!"
    Then user should be on dashboard page
    And user should see their profile name
    And user should see navigation menu
```

**Step Definitions:** `LoginSteps.java`

```java
package com.automation.stepdefinitions;

import com.automation.pages.LoginPage;
import com.automation.pages.DashboardPage;
import io.cucumber.java.en.*;

public class LoginSteps {
    
    private LoginPage loginPage = new LoginPage();
    private DashboardPage dashboardPage = new DashboardPage();
    
    @Given("user navigates to login page")
    public void userNavigatesToLoginPage() {
        loginPage.navigateToLoginPage();
    }
    
    @When("user logs in with username {string} and password {string}")
    public void userLogsIn(String username, String password) {
        loginPage.login(username, password);
    }
    
    @Then("user should be on dashboard page")
    public void userShouldBeOnDashboardPage() {
        dashboardPage.verifyDashboardLoaded();
    }
    
    @Then("user should see their profile name")
    public void userShouldSeeProfileName() {
        dashboardPage.verifyProfileNameDisplayed();
    }
    
    @Then("user should see navigation menu")
    public void userShouldSeeNavigationMenu() {
        dashboardPage.verifyNavigationMenuDisplayed();
    }
}
```

**Page Object:** `LoginPage.java`

```java
package com.automation.pages;

import com.automation.reusables.SeleniumReusable;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.automation.core.driver.DriverManager;

public class LoginPage extends SeleniumReusable {
    
    @FindBy(id = "username")
    private WebElement usernameField;
    
    @FindBy(id = "password")
    private WebElement passwordField;
    
    @FindBy(id = "loginButton")
    private WebElement loginButton;
    
    @FindBy(className = "error-message")
    private WebElement errorMessage;
    
    public LoginPage() {
        PageFactory.initElements(DriverManager.getSeleniumDriver(), this);
    }
    
    public void navigateToLoginPage() {
        navigateToURL("https://casino-credit.example.com/login");
        waitForElement(loginButton);
    }
    
    public void login(String username, String password) {
        type(usernameField, username);
        type(passwordField, password);
        click(loginButton);
    }
    
    public String getErrorMessage() {
        return getText(errorMessage);
    }
}
```

---

### Example 3: Web Test (Modular TestNG)

**Test Class:** `LoginTest.java`

```java
package com.automation.tests;

import com.automation.pages.LoginPage;
import com.automation.pages.DashboardPage;
import com.automation.core.driver.DriverManager;
import com.automation.core.logging.UnifiedLogger;
import org.testng.annotations.*;

public class LoginTest {
    
    private LoginPage loginPage;
    private DashboardPage dashboardPage;
    
    @BeforeClass
    public void setup() {
        DriverManager.getSeleniumDriver();
        loginPage = new LoginPage();
        dashboardPage = new DashboardPage();
    }
    
    @Test(priority = 1)
    public void testSuccessfulLogin() {
        UnifiedLogger.info("Starting successful login test");
        
        loginPage.navigateToLoginPage();
        loginPage.login("testuser", "Password123!");
        
        dashboardPage.verifyDashboardLoaded();
        dashboardPage.verifyProfileNameDisplayed();
        
        UnifiedLogger.pass("Login successful");
    }
    
    @Test(priority = 2)
    public void testInvalidLogin() {
        UnifiedLogger.info("Starting invalid login test");
        
        loginPage.navigateToLoginPage();
        loginPage.login("invaliduser", "wrongpass");
        
        String errorMsg = loginPage.getErrorMessage();
        AssertUtils.assertEquals(errorMsg, "Invalid credentials", "Error message should match");
        
        UnifiedLogger.pass("Invalid login handled correctly");
    }
    
    @AfterClass
    public void teardown() {
        DriverManager.quitDriver();
    }
}
```

---

## 🔌 API Testing

### Example 1: API Test (Cucumber - Using Existing Steps)

**Feature File:** `user_api.feature`

```gherkin
@API @Smoke
Feature: User API Testing

  Background:
    Given API base URL is "https://api.example.com"
    And API header "Content-Type" is "application/json"
    And API header "Authorization" is "Bearer ${token}"

  Scenario: Get user details
    When user sends GET request to "/users/1"
    Then response status code should be 200
    And response should contain "id"
    And response should contain "name"
    And JSON path "$.id" should be "1"
    And JSON path "$.email" should contain "@example.com"

  Scenario: Create new user
    Given request body is:
      """
      {
        "name": "John Doe",
        "email": "john.doe@example.com",
        "role": "admin"
      }
      """
    When user sends POST request to "/users"
    Then response status code should be 201
    And JSON path "$.name" should be "John Doe"
    And JSON path "$.email" should be "john.doe@example.com"
    And user saves JSON path "$.id" as "userId"

  Scenario: Update user
    When user sends PUT request to "/users/${userId}" with body:
      """
      {
        "name": "John Updated",
        "email": "john.updated@example.com"
      }
      """
    Then response status code should be 200
    And JSON path "$.name" should be "John Updated"

  Scenario: Delete user
    When user sends DELETE request to "/users/${userId}"
    Then response status code should be 204
```

---

### Example 2: API Test (Cucumber - Custom Steps)

**Feature File:** `user_api_custom.feature`

```gherkin
@API @Custom
Feature: Advanced User API

  Scenario: Complete user lifecycle
    Given API is initialized with base URL
    When user creates a new account
    Then account should be created successfully
    When user updates account details
    Then account should be updated
    When user retrieves account information
    Then all details should match
    When user deletes the account
    Then account should be removed
```

**Step Definitions:** `UserAPISteps.java`

```java
package com.automation.stepdefinitions;

import com.automation.core.api.APIClient;
import com.automation.core.context.ScenarioContext;
import com.automation.core.assertions.AssertUtils;
import io.cucumber.java.en.*;
import io.restassured.response.Response;

public class UserAPISteps {
    
    private Response response;
    
    @Given("API is initialized with base URL")
    public void apiIsInitialized() {
        APIClient.initialize("https://api.example.com");
        APIClient.addHeader("Content-Type", "application/json");
    }
    
    @When("user creates a new account")
    public void userCreatesNewAccount() {
        String requestBody = "{ \"name\": \"Test User\", \"email\": \"test@example.com\" }";
        response = APIClient.post("/users", requestBody);
        
        String userId = response.jsonPath().getString("id");
        ScenarioContext.set("userId", userId);
    }
    
    @Then("account should be created successfully")
    public void accountShouldBeCreated() {
        AssertUtils.assertEquals(response.getStatusCode(), 201, "Status code should be 201");
        AssertUtils.assertNotNull(response.jsonPath().getString("id"), "User ID should not be null");
    }
    
    @When("user updates account details")
    public void userUpdatesAccount() {
        String userId = ScenarioContext.get("userId", String.class);
        String updateBody = "{ \"name\": \"Updated User\" }";
        response = APIClient.put("/users/" + userId, updateBody);
    }
    
    @Then("account should be updated")
    public void accountShouldBeUpdated() {
        AssertUtils.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        AssertUtils.assertEquals(response.jsonPath().getString("name"), "Updated User", "Name should be updated");
    }
}
```

---

### Example 3: API Test (Modular TestNG)

**Test Class:** `UserAPITest.java`

```java
package com.automation.tests;

import com.automation.core.api.APIClient;
import com.automation.core.assertions.AssertUtils;
import com.automation.core.logging.UnifiedLogger;
import io.restassured.response.Response;
import org.testng.annotations.*;

public class UserAPITest {
    
    private String userId;
    
    @BeforeClass
    public void setup() {
        APIClient.initialize("https://api.example.com");
        APIClient.addHeader("Content-Type", "application/json");
    }
    
    @Test(priority = 1)
    public void testCreateUser() {
        UnifiedLogger.info("Creating new user");
        
        String requestBody = "{ \"name\": \"Test User\", \"email\": \"test@example.com\" }";
        Response response = APIClient.post("/users", requestBody);
        
        AssertUtils.assertEquals(response.getStatusCode(), 201, "Status code should be 201");
        userId = response.jsonPath().getString("id");
        AssertUtils.assertNotNull(userId, "User ID should not be null");
        
        UnifiedLogger.pass("User created successfully with ID: " + userId);
    }
    
    @Test(priority = 2, dependsOnMethods = "testCreateUser")
    public void testGetUser() {
        UnifiedLogger.info("Retrieving user details");
        
        Response response = APIClient.get("/users/" + userId);
        
        AssertUtils.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        AssertUtils.assertEquals(response.jsonPath().getString("name"), "Test User", "Name should match");
        
        UnifiedLogger.pass("User details retrieved successfully");
    }
    
    @Test(priority = 3, dependsOnMethods = "testGetUser")
    public void testUpdateUser() {
        UnifiedLogger.info("Updating user details");
        
        String updateBody = "{ \"name\": \"Updated User\" }";
        Response response = APIClient.put("/users/" + userId, updateBody);
        
        AssertUtils.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        AssertUtils.assertEquals(response.jsonPath().getString("name"), "Updated User", "Name should be updated");
        
        UnifiedLogger.pass("User updated successfully");
    }
    
    @Test(priority = 4, dependsOnMethods = "testUpdateUser")
    public void testDeleteUser() {
        UnifiedLogger.info("Deleting user");
        
        Response response = APIClient.delete("/users/" + userId);
        
        AssertUtils.assertEquals(response.getStatusCode(), 204, "Status code should be 204");
        
        UnifiedLogger.pass("User deleted successfully");
    }
}
```

---

## 📱 Mobile Application Testing

### Example 1: Mobile Test (Cucumber - Using Existing Steps)

**Feature File:** `mobile_login.feature`

```gherkin
@Mobile @Android
Feature: Mobile App Login

  Background:
    Given mobile app is launched

  Scenario: Successful mobile login
    When user taps element with id "com.app:id/username"
    And user enters text "testuser"
    And user taps element with id "com.app:id/password"
    And user enters text "Password123!"
    And user hides keyboard
    And user taps element with id "com.app:id/loginBtn"
    Then user should see element with id "com.app:id/dashboard"
    And user waits for 2 seconds

  Scenario: Navigate through app
    When user taps element with text "Menu"
    And user swipes up
    And user taps element with text "Settings"
    Then user should see element with text "Profile"
```

---

### Example 2: Mobile Test (Cucumber - Custom Steps)

**Feature File:** `mobile_approval.feature`

```gherkin
@Mobile @SmartApprover
Feature: Smart Approver Mobile

  Scenario: Approve pending request
    Given user is logged into mobile app
    When user navigates to pending approvals
    And user selects first pending request
    And user reviews request details
    And user approves the request with comment "Approved via mobile"
    Then request should be marked as approved
    And user should see success message
```

**Step Definitions:** `MobileApprovalSteps.java`

```java
package com.automation.stepdefinitions;

import com.automation.pages.mobile.MobileHomePage;
import com.automation.pages.mobile.ApprovalPage;
import io.cucumber.java.en.*;

public class MobileApprovalSteps {
    
    private MobileHomePage homePage = new MobileHomePage();
    private ApprovalPage approvalPage = new ApprovalPage();
    
    @Given("user is logged into mobile app")
    public void userIsLoggedIn() {
        homePage.login("testuser", "Password123!");
    }
    
    @When("user navigates to pending approvals")
    public void userNavigatesToPendingApprovals() {
        homePage.tapMenu();
        homePage.selectPendingApprovals();
    }
    
    @When("user selects first pending request")
    public void userSelectsFirstRequest() {
        approvalPage.selectFirstRequest();
    }
    
    @When("user approves the request with comment {string}")
    public void userApprovesRequest(String comment) {
        approvalPage.approveRequest(comment);
    }
    
    @Then("request should be marked as approved")
    public void requestShouldBeApproved() {
        approvalPage.verifyRequestApproved();
    }
}
```

**Page Object:** `ApprovalPage.java`

```java
package com.automation.pages.mobile;

import com.automation.reusables.MobileReusable;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.*;
import org.openqa.selenium.WebElement;
import com.automation.core.driver.DriverManager;

public class ApprovalPage extends MobileReusable {
    
    @AndroidFindBy(id = "com.app:id/requestList")
    @iOSXCUITFindBy(id = "requestList")
    private WebElement requestList;
    
    @AndroidFindBy(id = "com.app:id/approveBtn")
    @iOSXCUITFindBy(id = "approveButton")
    private WebElement approveButton;
    
    @AndroidFindBy(id = "com.app:id/commentField")
    @iOSXCUITFindBy(id = "commentField")
    private WebElement commentField;
    
    @AndroidFindBy(id = "com.app:id/submitBtn")
    @iOSXCUITFindBy(id = "submitButton")
    private WebElement submitButton;
    
    public ApprovalPage() {
        AppiumDriver driver = DriverManager.getAppiumDriver();
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }
    
    public void selectFirstRequest() {
        tap(requestList);
        waitForElement(approveButton);
    }
    
    public void approveRequest(String comment) {
        tap(approveButton);
        type(commentField, comment);
        tap(submitButton);
    }
    
    public void verifyRequestApproved() {
        waitForTextToBePresent("Approved");
    }
}
```

---

## 🖥️ Desktop Application Testing

### Example 1: Desktop Test (Cucumber - Using Existing Steps)

**Feature File:** `calculator.feature`

```gherkin
@Desktop @Calculator
Feature: Windows Calculator

  Background:
    Given desktop application "Calculator" is launched

  Scenario: Addition operation
    When user clicks button "5"
    And user clicks button "Plus"
    And user clicks button "3"
    And user clicks button "Equals"
    Then result should display "8"

  Scenario: Complex calculation
    When user clicks button "9"
    And user clicks button "Multiply"
    And user clicks button "6"
    And user clicks button "Equals"
    Then result should display "54"
    When user clicks button "Divide"
    And user clicks button "3"
    And user clicks button "Equals"
    Then result should display "18"
```

---

### Example 2: IGPOS Desktop Test (Cucumber - Custom Steps)

**Feature File:** `igpos_transaction.feature`

```gherkin
@Desktop @IGPOS
Feature: IGPOS Transaction Processing

  Scenario: Process POS transaction
    Given IGPOS application is launched
    When cashier logs in with ID "CASH001"
    And cashier scans item "12345"
    And cashier enters quantity "2"
    And cashier adds item to cart
    Then cart should show 2 items
    When cashier proceeds to payment
    And cashier selects payment method "Credit Card"
    And cashier processes payment
    Then transaction should be completed
    And receipt should be printed
```

**Step Definitions:** `IGPOSSteps.java`

```java
package com.automation.stepdefinitions;

import com.automation.pages.desktop.IGPOSMainWindow;
import com.automation.pages.desktop.IGPOSCartWindow;
import io.cucumber.java.en.*;

public class IGPOSSteps {
    
    private IGPOSMainWindow mainWindow = new IGPOSMainWindow();
    private IGPOSCartWindow cartWindow = new IGPOSCartWindow();
    
    @Given("IGPOS application is launched")
    public void igposIsLaunched() {
        mainWindow.waitForMainWindow();
    }
    
    @When("cashier logs in with ID {string}")
    public void cashierLogsIn(String cashierId) {
        mainWindow.login(cashierId);
    }
    
    @When("cashier scans item {string}")
    public void cashierScansItem(String itemCode) {
        mainWindow.scanItem(itemCode);
    }
    
    @When("cashier adds item to cart")
    public void cashierAddsItemToCart() {
        mainWindow.addToCart();
    }
    
    @Then("cart should show {int} items")
    public void cartShouldShowItems(int count) {
        cartWindow.verifyItemCount(count);
    }
}
```

---

## 🗳️ Mainframe Testing

### Example 1: Mainframe Test (Cucumber - Using Existing Steps)

**Feature File:** `acsc_login.feature`

```gherkin
@Mainframe @ACSC
Feature: ACSC Mainframe Login

  Scenario: Successful mainframe login
    Given mainframe session "A" is connected
    When user sends text "USERID" at row 10 col 20
    And user sends text "PASSWORD" at row 11 col 20
    And user sends Enter key
    Then user should see text "MAIN MENU" on screen
    And user should see text "ACSC SYSTEM" at row 1 col 30

  Scenario: Navigate to credit inquiry
    Given user is on ACSC main menu
    When user sends text "CI" at row 5 col 10
    And user sends Enter key
    Then user should see text "CREDIT INQUIRY" on screen
    When user sends text "ACC12345" at row 8 col 15
    And user sends Enter key
    Then user should see text "ACCOUNT DETAILS" on screen
```

---

### Example 2: Mainframe Test (Cucumber - Custom Steps)

**Feature File:** `acsc_credit_workflow.feature`

```gherkin
@Mainframe @CreditWorkflow
Feature: ACSC Credit Management

  Scenario: Complete credit application workflow
    Given user is logged into ACSC mainframe
    When user navigates to credit application screen
    And user enters customer number "CUST001"
    And user enters credit limit "50000"
    And user enters approval code "MGR123"
    And user submits credit application
    Then application should be submitted successfully
    And user should see confirmation number
    When user saves confirmation number as "confirmNum"
    And user navigates to inquiry screen
    And user searches for confirmation number "${confirmNum}"
    Then application status should be "PENDING"
```

**Step Definitions:** `ACSCSteps.java`

```java
package com.automation.stepdefinitions;

import com.automation.pages.mainframe.ACSCLoginScreen;
import com.automation.pages.mainframe.ACSCCreditScreen;
import io.cucumber.java.en.*;

public class ACSCSteps {
    
    private ACSCLoginScreen loginScreen = new ACSCLoginScreen();
    private ACSCCreditScreen creditScreen = new ACSCCreditScreen();
    
    @Given("user is logged into ACSC mainframe")
    public void userIsLoggedIn() {
        loginScreen.login("USERID", "PASSWORD");
    }
    
    @When("user navigates to credit application screen")
    public void userNavigatesToCreditScreen() {
        creditScreen.navigateToCreditApplication();
    }
    
    @When("user enters customer number {string}")
    public void userEntersCustomerNumber(String custNum) {
        creditScreen.enterCustomerNumber(custNum);
    }
    
    @When("user submits credit application")
    public void userSubmitsApplication() {
        creditScreen.submitApplication();
    }
    
    @Then("application should be submitted successfully")
    public void applicationShouldBeSubmitted() {
        creditScreen.verifySubmissionSuccess();
    }
}
```

**Page Object:** `ACSCCreditScreen.java`

```java
package com.automation.pages.mainframe;

import com.automation.reusables.MainframeReusable;

public class ACSCCreditScreen extends MainframeReusable {
    
    public void navigateToCreditApplication() {
        sendText(5, 10, "CA");
        sendEnter();
        waitForText("CREDIT APPLICATION");
    }
    
    public void enterCustomerNumber(String custNum) {
        sendText(8, 15, custNum);
    }
    
    public void enterCreditLimit(String limit) {
        sendText(10, 15, limit);
    }
    
    public void submitApplication() {
        sendPFKey(8);
        waitForText("SUBMITTED");
    }
    
    public void verifySubmissionSuccess() {
        assertTextPresent("APPLICATION SUBMITTED SUCCESSFULLY");
    }
    
    public String getConfirmationNumber() {
        return getText(15, 20, 10);
    }
}
```

---

**Document continues in TEST_EXAMPLES_PART2.md due to size...**
