# 📝 Test Examples Part 2 - E2E & Advanced Scenarios

## 🔄 End-to-End Testing

### Example 1: Complete E2E Workflow (Web + API)

**Feature File:** `e2e_casino_credit.feature`

```gherkin
@E2E @CasinoCredit
Feature: Casino Credit End-to-End Workflow

  Scenario: Complete credit application and approval process
    # Step 1: Create customer via API
    Given API base URL is "https://api.casino.example.com"
    And API header "Authorization" is "Bearer ${apiToken}"
    When user sends POST request to "/customers" with body:
      """
      {
        "firstName": "John",
        "lastName": "Doe",
        "email": "john.doe@example.com",
        "phone": "555-1234"
      }
      """
    Then response status code should be 201
    And user saves JSON path "$.customerId" as "customerId"
    
    # Step 2: Submit credit application via Web UI
    Given user opens website "https://casino-credit.example.com"
    When user logs in as "credit_officer"
    And user navigates to "New Application"
    And user enters "${customerId}" in "customerIdField"
    And user enters "50000" in "creditLimitField"
    And user selects "Standard" from "creditTypeDropdown"
    And user clicks "submitButton"
    Then user should see text "Application Submitted" in "statusMessage"
    And user saves text from "applicationNumber" as "appNumber"
    
    # Step 3: Verify application in database via API
    When user sends GET request to "/applications/${appNumber}"
    Then response status code should be 200
    And JSON path "$.status" should be "PENDING"
    And JSON path "$.customerId" should be "${customerId}"
    
    # Step 4: Approve via Smart Approver
    When user opens website "https://smart-approver.example.com"
    And user logs in as "manager"
    And user searches for application "${appNumber}"
    And user clicks "approveButton"
    And user enters "Approved for standard credit" in "commentsField"
    And user clicks "confirmButton"
    Then user should see text "Application Approved" in "statusMessage"
    
    # Step 5: Verify final status via API
    When user sends GET request to "/applications/${appNumber}"
    Then response status code should be 200
    And JSON path "$.status" should be "APPROVED"
    And JSON path "$.approvedBy" should not be empty
```

---

### Example 2: E2E with Mainframe Integration

**Feature File:** `e2e_igpos_mainframe.feature`

```gherkin
@E2E @IGPOS @Mainframe
Feature: IGPOS to Mainframe Integration

  Scenario: Process transaction and verify in mainframe
    # Step 1: Process transaction in IGPOS
    Given IGPOS application is launched
    When cashier logs in with ID "CASH001"
    And cashier processes sale for item "ITEM123" quantity "2" price "25.00"
    And cashier completes transaction with payment "CASH"
    Then transaction should be completed
    And user saves transaction number as "transNum"
    
    # Step 2: Verify in ACSC Mainframe
    Given mainframe session "A" is connected
    When user logs into ACSC mainframe
    And user navigates to transaction inquiry
    And user searches for transaction "${transNum}"
    Then transaction should be found in mainframe
    And transaction amount should be "50.00"
    And transaction status should be "POSTED"
```

---

## 🎯 Data-Driven Testing

### Example 1: Data-Driven with Scenario Outline

**Feature File:** `data_driven_login.feature`

```gherkin
@DataDriven @Login
Feature: Data-Driven Login Tests

  Scenario Outline: Login with multiple user types
    Given user opens website "https://example.com"
    When user enters "<username>" in "username"
    And user enters "<password>" in "password"
    And user clicks "loginButton"
    Then user should see text "<expectedMessage>" in "statusMessage"
    And user should see element "<expectedElement>"

    Examples:
      | username      | password    | expectedMessage | expectedElement |
      | admin         | Admin123!   | Welcome Admin   | adminDashboard  |
      | manager       | Manager123! | Welcome Manager | managerDashboard|
      | employee      | Emp123!     | Welcome User    | userDashboard   |
      | readonly      | Read123!    | Limited Access  | readonlyView    |
      | invaliduser   | wrong       | Invalid Login   | errorMessage    |
```

---

### Example 2: Data-Driven API Testing

**Feature File:** `data_driven_api.feature`

```gherkin
@DataDriven @API
Feature: Data-Driven API Tests

  Scenario Outline: Create users with different roles
    Given API base URL is "https://api.example.com"
    When user sends POST request to "/users" with body:
      """
      {
        "name": "<name>",
        "email": "<email>",
        "role": "<role>",
        "department": "<department>"
      }
      """
    Then response status code should be <statusCode>
    And JSON path "$.role" should be "<role>"
    And JSON path "$.permissions" should contain "<expectedPermission>"

    Examples:
      | name          | email                | role      | department | statusCode | expectedPermission |
      | Admin User    | admin@example.com    | admin     | IT         | 201        | full_access        |
      | Manager User  | manager@example.com  | manager   | Operations | 201        | approve_requests   |
      | Employee User | employee@example.com | employee  | Sales      | 201        | view_only          |
      | Guest User    | guest@example.com    | guest     | None       | 201        | limited_access     |
```

---

## 🔀 Complex Scenarios

### Example 1: Multi-Window Handling

**Feature File:** `multi_window.feature`

```gherkin
@Web @MultiWindow
Feature: Multi-Window Operations

  Scenario: Handle multiple browser windows
    Given user opens website "https://example.com"
    When user clicks "openNewWindow"
    And user switches to new window
    Then user should see text "New Window" in "pageTitle"
    When user enters "test data" in "inputField"
    And user clicks "saveButton"
    And user switches to main window
    Then user should see text "Data Saved" in "statusMessage"
```

**Step Definitions:** `MultiWindowSteps.java`

```java
@When("user switches to new window")
public void userSwitchesToNewWindow() {
    Set<String> windows = driver.getWindowHandles();
    for (String window : windows) {
        if (!window.equals(mainWindow)) {
            driver.switchTo().window(window);
            break;
        }
    }
}

@When("user switches to main window")
public void userSwitchesToMainWindow() {
    driver.switchTo().window(mainWindow);
}
```

---

### Example 2: File Upload/Download

**Feature File:** `file_operations.feature`

```gherkin
@Web @FileOperations
Feature: File Upload and Download

  Scenario: Upload document
    Given user opens website "https://example.com/upload"
    When user uploads file "C:\\TestData\\sample.pdf" to "fileInput"
    And user clicks "uploadButton"
    Then user should see text "File uploaded successfully" in "statusMessage"
    And user should see "sample.pdf" in "uploadedFilesList"

  Scenario: Download report
    Given user opens website "https://example.com/reports"
    When user clicks "downloadReport"
    And user waits for 3 seconds
    Then file "report.xlsx" should be downloaded
```

---

### Example 3: Dynamic Table Handling

**Feature File:** `table_operations.feature`

```gherkin
@Web @Tables
Feature: Dynamic Table Operations

  Scenario: Search and verify table data
    Given user opens website "https://example.com/users"
    When user enters "John" in "searchBox"
    And user clicks "searchButton"
    Then table should contain row with:
      | Name      | Email              | Role    |
      | John Doe  | john@example.com   | Manager |
    And table row count should be greater than 0

  Scenario: Sort table by column
    Given user opens website "https://example.com/users"
    When user clicks table header "Name"
    Then table should be sorted by "Name" in "ascending" order
    When user clicks table header "Name"
    Then table should be sorted by "Name" in "descending" order
```

---

## 🔐 Security Testing

### Example 1: Authentication Tests

**Feature File:** `security_auth.feature`

```gherkin
@Security @Authentication
Feature: Security Authentication Tests

  Scenario: SQL Injection attempt
    Given user opens website "https://example.com"
    When user enters "admin' OR '1'='1" in "username"
    And user enters "password" in "password"
    And user clicks "loginButton"
    Then user should see text "Invalid credentials" in "errorMessage"
    And user should not be logged in

  Scenario: XSS attempt
    Given user opens website "https://example.com/profile"
    When user enters "<script>alert('XSS')</script>" in "nameField"
    And user clicks "saveButton"
    Then user should see text "Invalid input" in "errorMessage"
    And script should not be executed

  Scenario: Session timeout
    Given user is logged into application
    When user waits for 30 minutes
    And user tries to access protected page
    Then user should be redirected to login page
    And user should see text "Session expired" in "message"
```

---

### Example 2: API Security Tests

**Feature File:** `api_security.feature`

```gherkin
@Security @API
Feature: API Security Tests

  Scenario: Unauthorized access attempt
    Given API base URL is "https://api.example.com"
    When user sends GET request to "/admin/users"
    Then response status code should be 401
    And response should contain "Unauthorized"

  Scenario: Invalid token
    Given API base URL is "https://api.example.com"
    And API header "Authorization" is "Bearer invalid_token"
    When user sends GET request to "/users"
    Then response status code should be 401
    And response should contain "Invalid token"

  Scenario: Rate limiting
    Given API base URL is "https://api.example.com"
    When user sends 100 GET requests to "/users"
    Then at least one response status code should be 429
    And response should contain "Rate limit exceeded"
```

---

## ⚡ Performance Testing

### Example 1: Response Time Validation

**Feature File:** `performance.feature`

```gherkin
@Performance @API
Feature: API Performance Tests

  Scenario: Validate API response time
    Given API base URL is "https://api.example.com"
    When user sends GET request to "/users"
    Then response status code should be 200
    And response time should be less than 2000 milliseconds

  Scenario: Bulk data processing
    Given API base URL is "https://api.example.com"
    When user sends POST request to "/bulk-import" with 1000 records
    Then response status code should be 202
    And response time should be less than 5000 milliseconds
```

---

## 🧪 Negative Testing

### Example 1: Validation Tests

**Feature File:** `negative_validation.feature`

```gherkin
@Negative @Validation
Feature: Negative Validation Tests

  Scenario Outline: Invalid input validation
    Given user opens website "https://example.com/register"
    When user enters "<email>" in "emailField"
    And user clicks "submitButton"
    Then user should see text "<errorMessage>" in "emailError"

    Examples:
      | email           | errorMessage              |
      |                 | Email is required         |
      | invalid         | Invalid email format      |
      | test@           | Invalid email format      |
      | @example.com    | Invalid email format      |
      | test..@test.com | Invalid email format      |

  Scenario: Boundary value testing
    Given user opens website "https://example.com/credit"
    When user enters "0" in "creditLimitField"
    And user clicks "submitButton"
    Then user should see text "Minimum credit limit is 1000" in "errorMessage"
    
    When user enters "1000000" in "creditLimitField"
    And user clicks "submitButton"
    Then user should see text "Maximum credit limit is 500000" in "errorMessage"
```

---

## 🔄 Retry and Recovery

### Example 1: Retry Failed Operations

**Feature File:** `retry_operations.feature`

```gherkin
@Retry @Resilience
Feature: Retry Failed Operations

  Scenario: Retry failed API call
    Given API base URL is "https://api.example.com"
    When user sends GET request to "/unstable-endpoint" with retry 3 times
    Then response status code should be 200
    And response should be received within 3 attempts

  Scenario: Retry failed transaction
    Given IGPOS application is launched
    When cashier processes transaction
    And payment gateway fails
    Then system should retry payment 2 times
    And transaction should eventually succeed
```

---

## 📊 Reporting Examples

### Example 1: Custom Reporting

**Test with Custom Reporting:**

```java
@Test
public void testWithCustomReporting() {
    UnifiedLogger.info("Starting test with custom reporting");
    
    // Step 1
    UnifiedLogger.action("Navigate", "Login Page");
    loginPage.navigateToLoginPage();
    
    // Step 2
    UnifiedLogger.action("Enter", "Username: testuser");
    loginPage.enterUsername("testuser");
    
    // Step 3
    UnifiedLogger.action("Enter", "Password");
    loginPage.enterPassword("Password123!");
    
    // Step 4
    UnifiedLogger.action("Click", "Login Button");
    loginPage.clickLogin();
    
    // Verification
    if (dashboardPage.isDashboardDisplayed()) {
        UnifiedLogger.pass("Login successful - Dashboard displayed");
    } else {
        UnifiedLogger.fail("Login failed - Dashboard not displayed");
        ScreenshotUtil.captureScreenshot("login_failure");
    }
}
```

---

## 🎯 Best Practices Examples

### Example 1: Using ScenarioContext

**Feature File:**

```gherkin
@BestPractice @Context
Feature: Using ScenarioContext

  Scenario: Share data between steps
    Given user creates new customer via API
    And user saves customer ID as "customerId"
    When user opens customer portal
    And user searches for customer "${customerId}"
    Then customer details should be displayed
    And customer name should match API response
```

**Step Definitions:**

```java
@Given("user creates new customer via API")
public void createCustomer() {
    Response response = APIClient.post("/customers", customerData);
    String customerId = response.jsonPath().getString("id");
    String customerName = response.jsonPath().getString("name");
    
    ScenarioContext.set("customerId", customerId);
    ScenarioContext.set("customerName", customerName);
}

@When("user searches for customer {string}")
public void searchCustomer(String customerIdVar) {
    String customerId = ScenarioContext.get("customerId", String.class);
    searchPage.searchById(customerId);
}

@Then("customer name should match API response")
public void verifyCustomerName() {
    String expectedName = ScenarioContext.get("customerName", String.class);
    String actualName = detailsPage.getCustomerName();
    AssertUtils.assertEquals(actualName, expectedName, "Customer name should match");
}
```

---

### Example 2: Soft Assertions

**Test with Soft Assertions:**

```java
@Test
public void testWithSoftAssertions() {
    UnifiedLogger.info("Starting test with soft assertions");
    
    // Enable soft assertions
    AssertUtils.enableSoftAssert();
    
    // Multiple validations
    AssertUtils.assertEquals(page.getTitle(), "Expected Title", "Title check");
    AssertUtils.assertTrue(page.isLogoDisplayed(), "Logo check");
    AssertUtils.assertEquals(page.getHeaderText(), "Welcome", "Header check");
    AssertUtils.assertTrue(page.isMenuDisplayed(), "Menu check");
    AssertUtils.assertEquals(page.getFooterText(), "Copyright 2025", "Footer check");
    
    // Assert all at once
    AssertUtils.assertAll();
    
    UnifiedLogger.pass("All validations passed");
}
```

---

## 🔧 Utility Usage Examples

### Example 1: Date/Time Utilities

```java
@Test
public void testWithDateUtilities() {
    // Get current date in specific format
    String currentDate = DateTimeUtil.getCurrentDate("MM/dd/yyyy");
    
    // Get future date
    String futureDate = DateTimeUtil.getFutureDate(30, "MM/dd/yyyy");
    
    // Get past date
    String pastDate = DateTimeUtil.getPastDate(7, "MM/dd/yyyy");
    
    // Use in test
    page.enterStartDate(currentDate);
    page.enterEndDate(futureDate);
}
```

---

### Example 2: Excel Data Reader

```java
@Test(dataProvider = "excelData")
public void testWithExcelData(String username, String password, String expectedResult) {
    loginPage.login(username, password);
    
    String actualResult = dashboardPage.getWelcomeMessage();
    AssertUtils.assertEquals(actualResult, expectedResult, "Welcome message should match");
}

@DataProvider(name = "excelData")
public Object[][] getExcelData() {
    return ExcelUtil.getTestData("TestData.xlsx", "LoginTests");
}
```

---

## 📝 Summary

### Available Pre-Built Steps by Category

| Category | Step Count | Example |
|----------|-----------|---------|
| **Web UI** | 80+ | "user clicks {string}", "user enters {string} in {string}" |
| **API** | 150+ | "user sends GET request to {string}" |
| **Mobile** | 60+ | "user taps element with id {string}" |
| **Desktop** | 50+ | "user clicks button {string}" |
| **Mainframe** | 80+ | "user sends text {string} at row {int} col {int}" |
| **Common** | 40+ | "user waits {int} seconds" |

**Total: 460+ Pre-Built Steps**

### When to Use What

| Approach | When to Use | Skill Level | Time to Write |
|----------|-------------|-------------|---------------|
| **Cucumber with Existing Steps** | 80% of scenarios | Entry Level | 15-30 mins |
| **Cucumber with Custom Steps** | Complex workflows | Mid Level | 1-2 hours |
| **Modular TestNG** | API tests, utilities | Mid-Senior | 1-3 hours |
| **Framework Extension** | New capabilities | Senior | 4-8 hours |

---

**Document Version:** 1.0  
**Last Updated:** January 2025  
**For More Examples:** See `/src/test/resources/features/` folder
