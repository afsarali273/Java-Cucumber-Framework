# Framework Architecture

## Overview

SandsAutoFramework is an enterprise-grade automation framework designed for scalability, maintainability, and ease of use. It supports multiple testing types (UI, API) and multiple frameworks (Selenium, Playwright).

## Architecture Layers

```
┌─────────────────────────────────────────────────────────────┐
│                    Test Layer                                │
│  (Feature Files, Step Definitions, Test Runners)            │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                  Page Object Layer                           │
│  (Page Objects extending Reusable Classes)                   │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                  Reusable Layer                              │
│  (SeleniumReusable, PlaywrightReusable, Keywords)           │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                    Core Layer                                │
│  (DriverManager, APIClient, ConfigManager, Assertions)       │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                  Utility Layer                               │
│  (Logging, Reporting, Screenshots, Utils)                    │
└─────────────────────────────────────────────────────────────┘
```

## Core Components

### 1. Configuration Management

**ConfigManager** - Singleton pattern for centralized configuration
- Loads global configuration from `config.properties`
- Loads environment-specific configuration (qa.properties, prod.properties)
- Thread-safe property access
- Support for different data types (String, int, boolean)

### 2. Driver Management

**DriverManager** - Thread-safe driver lifecycle management
- Supports Selenium WebDriver (Chrome, Firefox)
- Supports Playwright (Chromium, Firefox, WebKit)
- ThreadLocal storage for parallel execution
- Automatic driver initialization and cleanup
- Configurable timeouts and browser options

### 3. API Client

**APIClient** - REST API testing framework
- Built on Rest Assured
- Thread-safe request specifications
- Support for GET, POST, PUT, DELETE
- Custom header management
- Automatic logging of requests/responses

**SOAPClient** - SOAP API testing support
- SOAP envelope creation
- SOAP request execution

### 4. Assertion Framework

**AssertUtils** - Centralized assertion management
- Hard assertions (fail immediately)
- Soft assertions (collect all failures)
- Auto-logging to console, custom report, and Allure
- Multiple assertion types:
  - assertEquals, assertNotEquals
  - assertTrue, assertFalse
  - assertNull, assertNotNull
  - assertContains
  - assertGreaterThan, assertLessThan

### 5. Reporting System

**CustomReporter** - HTML report generation
- Thread-safe test result collection
- Pass/fail summary with statistics
- Detailed test logs
- Screenshot attachments
- Duration tracking

**ScreenshotUtil** - Screenshot capture
- Framework-agnostic (works with Selenium and Playwright)
- Automatic naming with timestamps
- Allure integration
- Custom report integration

### 6. Logging

**LogManager** - Centralized logging
- SLF4J + Logback implementation
- Thread-safe logging
- Console and file appenders
- Automatic caller class detection

### 7. Hooks

**CucumberHooks** - Test lifecycle management
- BeforeAll: Suite initialization
- Before: Scenario setup (driver/API initialization)
- After: Scenario cleanup (screenshot on failure, driver quit)
- AfterAll: Report generation, cleanup

## Design Patterns

### 1. Singleton Pattern
- ConfigManager
- DriverManager (for Playwright instance)

### 2. Page Object Model (POM)
- Separation of page structure from test logic
- Reusable page methods
- Framework-specific implementations

### 3. Factory Pattern
- Driver creation based on configuration
- Framework selection (Selenium vs Playwright)

### 4. ThreadLocal Pattern
- Thread-safe driver instances
- Thread-safe API clients
- Thread-safe assertions
- Thread-safe reporters

### 5. Strategy Pattern
- Framework-agnostic keywords
- Conditional execution based on framework type

## Thread Safety

All core components use ThreadLocal for parallel execution:

```java
private static final ThreadLocal<WebDriver> seleniumDriver = new ThreadLocal<>();
private static final ThreadLocal<Page> playwrightPage = new ThreadLocal<>();
private static final ThreadLocal<SoftAssert> softAssert = new ThreadLocal<>();
private static final ThreadLocal<RequestSpecification> requestSpec = new ThreadLocal<>();
```

## Reusable Components

### SeleniumReusable
Base class for Selenium page objects with common methods:
- click, type, getText
- waitForElement, scrollToElement
- selectDropdown, jsClick

### PlaywrightReusable
Base class for Playwright page objects with common methods:
- click, type, getText
- waitForSelector, scrollToElement
- clickByText, clickByRole

### UIKeywords
Manual QA-friendly methods:
- openBrowser, closeBrowser
- navigateToURL, clickElement
- enterText, getTextFromElement
- Framework-agnostic implementation

### APIKeywords
Manual QA-friendly API methods:
- initializeAPI
- sendGETRequest, sendPOSTRequest
- getResponseStatusCode, getResponseBody
- addHeader, responseContains

## Configuration Flow

```
config.properties (Global)
         ↓
environment=qa
         ↓
qa.properties (Environment-specific)
         ↓
ConfigManager merges both
         ↓
Available to all components
```

## Test Execution Flow

```
TestNG Runner
    ↓
Cucumber Feature Files
    ↓
@BeforeAll Hook (Suite initialization)
    ↓
@Before Hook (Scenario setup)
    ↓
Step Definitions
    ↓
Page Objects
    ↓
Reusable Methods
    ↓
Core Components (Driver/API)
    ↓
@After Hook (Cleanup, Screenshot)
    ↓
@AfterAll Hook (Report generation)
```

## Parallel Execution

Configured at multiple levels:

1. **TestNG Suite Level** (testng.xml):
```xml
<suite parallel="methods" thread-count="3">
```

2. **Maven Surefire Plugin**:
```xml
<parallel>methods</parallel>
<threadCount>3</threadCount>
```

3. **Cucumber DataProvider**:
```java
@DataProvider(parallel = true)
public Object[][] scenarios()
```

## Extensibility

### Adding New Framework Support
1. Implement driver initialization in DriverManager
2. Create new Reusable class (e.g., CypressReusable)
3. Update UIKeywords to support new framework
4. Add configuration in config.properties

### Adding New Assertion Types
1. Add method to AssertUtils
2. Implement logging to console, report, and Allure
3. Support both hard and soft assertion modes

### Adding New Reporters
1. Implement reporter class
2. Integrate with CucumberHooks
3. Add configuration properties

## Best Practices

1. **Page Objects**: Keep page objects thin, focused on element interactions
2. **Step Definitions**: Keep step definitions thin, delegate to page objects
3. **Assertions**: Use meaningful messages for all assertions
4. **Logging**: Log all important actions and validations
5. **Configuration**: Use properties files for all configurable values
6. **Thread Safety**: Always use ThreadLocal for parallel execution
7. **Cleanup**: Always cleanup resources in @After hooks
8. **Screenshots**: Capture on failure for debugging

## JAR Packaging

The framework can be packaged as a reusable JAR:

```bash
mvn clean package
```

Include in other projects:
```xml
<dependency>
    <groupId>com.automation</groupId>
    <artifactId>SandsAutoFramework</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

## Dependencies

- **Selenium**: WebDriver automation
- **Playwright**: Modern browser automation
- **Cucumber**: BDD framework
- **TestNG**: Test execution and parallel support
- **Rest Assured**: API testing
- **Allure**: Advanced reporting
- **SLF4J + Logback**: Logging
- **Gson**: JSON processing
