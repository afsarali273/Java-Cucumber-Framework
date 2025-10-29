# ExtentReports Integration Guide

## Overview
ExtentReports has been integrated into the Sands Automation Framework through the UnifiedLogger, providing beautiful HTML reports with detailed test execution information.

## Features
- ✅ Automatic integration with Cucumber scenarios
- ✅ Thread-safe for parallel execution
- ✅ Dark theme with modern UI
- ✅ Screenshot attachment on failure
- ✅ Category and author assignment
- ✅ Synchronized with all other reporting systems (CustomReporter, Allure, LogManager)

## Report Location
After test execution, the ExtentReport will be generated at:
```
test-output/reports/ExtentReport_<timestamp>.html
```

## Automatic Logging
All logs from reusable classes automatically appear in ExtentReports:

```java
// In your test or page object
UnifiedLogger.info("User navigated to login page");
UnifiedLogger.action("Click", loginButton);
UnifiedLogger.pass("Login successful");
UnifiedLogger.fail("Expected element not found");
UnifiedLogger.warn("Slow response detected");
```

## Cucumber Integration
ExtentReports automatically:
- Creates a test for each scenario
- Assigns categories based on tags (@UI, @API, @Mobile)
- Attaches screenshots on failure
- Logs all steps and actions
- Generates final report after suite completion

## Manual Usage (TestNG/Modular Tests)
For non-Cucumber tests, manually manage the lifecycle:

```java
@BeforeClass
public void setup() {
    ExtentReporter.initReports();
    ExtentReporter.startTest("My Test Name");
    ExtentReporter.assignCategory("Regression", "Smoke");
    ExtentReporter.assignAuthor("Afsar Ali");
}

@Test
public void myTest() {
    UnifiedLogger.info("Test step 1");
    UnifiedLogger.action("Click", "Login Button");
    UnifiedLogger.pass("Test passed");
}

@AfterClass
public void teardown() {
    ExtentReporter.endTest();
    ExtentReporter.flushReports();
}
```

## Report Features
- **Test Summary**: Pass/Fail counts, execution time
- **Test Details**: Step-by-step logs with timestamps
- **Screenshots**: Attached on failure
- **Categories**: Filter tests by category (UI, API, Mobile)
- **Authors**: Track test ownership
- **System Info**: Environment, browser, framework details
- **Dark Theme**: Modern, easy-to-read interface

## Configuration
ExtentReports uses the same configuration as other reports:
```properties
# config.properties
report.path=test-output/reports
screenshot.on.failure=true
```

## Benefits
1. **Single Logging Call**: One UnifiedLogger call logs to all systems
2. **Consistent Format**: All reports show the same information
3. **Thread-Safe**: Works with parallel execution
4. **Zero Overhead**: No additional code needed in tests
5. **Beautiful UI**: Professional-looking reports for stakeholders

## Viewing Reports
Simply open the generated HTML file in any browser:
```bash
open test-output/reports/ExtentReport_<timestamp>.html
```

## All Available Reports
After test execution, you'll have:
1. **ExtentReport**: `test-output/reports/ExtentReport_*.html`
2. **CustomReport**: `test-output/reports/CustomReport_*.html`
3. **Cucumber Report**: `test-output/cucumber-reports/cucumber.html`
4. **Allure Report**: Run `mvn allure:serve`
5. **Logs**: `test-output/logs/automation.log`

All reports contain the same information, logged through UnifiedLogger!
