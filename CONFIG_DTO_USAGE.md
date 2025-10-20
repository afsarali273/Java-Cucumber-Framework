# Configuration DTO Usage Guide

## Overview
ConfigManager now supports a DTO pattern for framework configuration with global static final variables for easy access throughout the framework.

## What's New?

### 1. FrameworkConfig DTO
A dedicated DTO class that encapsulates all framework-level configurations:
- Framework type (Selenium/Playwright)
- Browser
- Headless mode
- Environment
- Timeouts and retry counts

### 2. Global Static Final Variables
Easy access to common configurations without getInstance():
```java
ConfigManager.ENVIRONMENT
ConfigManager.FRAMEWORK_TYPE
ConfigManager.BROWSER
ConfigManager.IS_HEADLESS
ConfigManager.WAIT_TIMEOUT
ConfigManager.MAX_RETRY_COUNT
ConfigManager.API_TIMEOUT
```

### 3. Utility Methods
```java
ConfigManager.getEnv()
ConfigManager.isSelenium()
ConfigManager.isPlaywright()
```

## Usage Examples

### Old Way (Still Works)
```java
// Using getInstance()
String env = ConfigManager.getInstance().getEnvironment();
String browser = ConfigManager.getInstance().getBrowser();
boolean headless = ConfigManager.getInstance().isHeadless();
int timeout = ConfigManager.getInstance().getWaitTimeout();
```

### New Way - Global Static Variables
```java
// Direct access - no getInstance() needed
String env = ConfigManager.ENVIRONMENT;
String browser = ConfigManager.BROWSER;
boolean headless = ConfigManager.IS_HEADLESS;
int timeout = ConfigManager.WAIT_TIMEOUT;

// Utility methods
if (ConfigManager.isSelenium()) {
    // Selenium-specific code
}

if (ConfigManager.isPlaywright()) {
    // Playwright-specific code
}
```

### New Way - DTO Pattern
```java
// Get entire config as DTO
FrameworkConfig config = ConfigManager.getInstance().getConfig();

// Access properties
String frameworkType = config.getFrameworkType();
String browser = config.getBrowser();
boolean headless = config.isHeadless();
String environment = config.getEnvironment();
int waitTimeout = config.getWaitTimeout();
int maxRetry = config.getMaxRetryCount();
int apiTimeout = config.getApiTimeout();

// Utility methods
if (config.isSelenium()) {
    // Selenium logic
}

if (config.isPlaywright()) {
    // Playwright logic
}

// Print entire config
System.out.println(config.toString());
```

## Use Cases

### Use Case 1: Conditional Logic Based on Framework
```java
public class DriverFactory {
    public static void initializeDriver() {
        if (ConfigManager.isSelenium()) {
            // Initialize Selenium WebDriver
            WebDriver driver = new ChromeDriver();
        } else if (ConfigManager.isPlaywright()) {
            // Initialize Playwright
            Browser browser = playwright.chromium().launch();
        }
    }
}
```

### Use Case 2: Environment-Specific Logic
```java
public class TestBase {
    @BeforeClass
    public void setup() {
        String env = ConfigManager.ENVIRONMENT;
        
        if ("prod".equals(env)) {
            // Production-specific setup
            enableStrictValidation();
        } else {
            // QA/Dev setup
            enableDebugMode();
        }
    }
}
```

### Use Case 3: Dynamic Waits
```java
public class BasePage {
    protected WebDriverWait wait;
    
    public BasePage() {
        // Use global timeout
        wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigManager.WAIT_TIMEOUT));
    }
}
```

### Use Case 4: Retry Logic
```java
public class APIHelper {
    public Response sendRequestWithRetry(String endpoint) {
        int maxRetries = ConfigManager.MAX_RETRY_COUNT;
        
        for (int i = 0; i < maxRetries; i++) {
            try {
                return APIClient.get(endpoint);
            } catch (Exception e) {
                if (i == maxRetries - 1) throw e;
            }
        }
        return null;
    }
}
```

### Use Case 5: Logging Configuration
```java
public class LogManager {
    static {
        FrameworkConfig config = ConfigManager.getInstance().getConfig();
        logger.info("Framework initialized with config: " + config.toString());
        logger.info("Environment: " + ConfigManager.ENVIRONMENT);
        logger.info("Browser: " + ConfigManager.BROWSER);
        logger.info("Headless: " + ConfigManager.IS_HEADLESS);
    }
}
```

### Use Case 6: Pass Config to Methods
```java
public class ReportGenerator {
    public void generateReport(FrameworkConfig config) {
        report.addInfo("Environment", config.getEnvironment());
        report.addInfo("Browser", config.getBrowser());
        report.addInfo("Framework", config.getFrameworkType());
        report.addInfo("Headless Mode", String.valueOf(config.isHeadless()));
    }
    
    // Usage
    public void createReport() {
        FrameworkConfig config = ConfigManager.getInstance().getConfig();
        generateReport(config);
    }
}
```

## Benefits

### 1. Cleaner Code
```java
// Before
String env = ConfigManager.getInstance().getEnvironment();

// After
String env = ConfigManager.ENVIRONMENT;
```

### 2. Type Safety
```java
// DTO ensures all config values are properly typed
FrameworkConfig config = ConfigManager.getInstance().getConfig();
int timeout = config.getWaitTimeout(); // Always int, never null
```

### 3. Immutability
```java
// FrameworkConfig is immutable - values can't be changed after initialization
FrameworkConfig config = ConfigManager.getInstance().getConfig();
// No setters available - config is read-only
```

### 4. Easy Testing
```java
@Test
public void testWithConfig() {
    FrameworkConfig config = ConfigManager.getInstance().getConfig();
    
    // Easy to verify configuration
    assertEquals("qa", config.getEnvironment());
    assertEquals("chrome", config.getBrowser());
    assertFalse(config.isHeadless());
}
```

### 5. Single Source of Truth
```java
// All framework config in one place
FrameworkConfig config = ConfigManager.getInstance().getConfig();
System.out.println(config); // Prints all config values
```

## Global Variables Reference

| Variable | Type | Description | Example |
|----------|------|-------------|---------|
| `ENVIRONMENT` | String | Current environment | "qa", "prod" |
| `FRAMEWORK_TYPE` | String | Framework type | "selenium", "playwright" |
| `BROWSER` | String | Browser name | "chrome", "firefox" |
| `IS_HEADLESS` | boolean | Headless mode | true, false |
| `WAIT_TIMEOUT` | int | Wait timeout in seconds | 10, 15, 30 |
| `MAX_RETRY_COUNT` | int | Max retry attempts | 2, 3, 5 |
| `API_TIMEOUT` | int | API timeout in seconds | 30, 60 |

## Static Utility Methods

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getEnv()` | String | Get current environment |
| `isSelenium()` | boolean | Check if framework is Selenium |
| `isPlaywright()` | boolean | Check if framework is Playwright |

## DTO Methods

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getFrameworkType()` | String | Get framework type |
| `getBrowser()` | String | Get browser name |
| `isHeadless()` | boolean | Check headless mode |
| `getEnvironment()` | String | Get environment |
| `getWaitTimeout()` | int | Get wait timeout |
| `getMaxRetryCount()` | int | Get max retry count |
| `getApiTimeout()` | int | Get API timeout |
| `isSelenium()` | boolean | Check if Selenium |
| `isPlaywright()` | boolean | Check if Playwright |
| `toString()` | String | Get formatted config string |

## Backward Compatibility

All existing methods still work:
```java
// Old methods - still supported
ConfigManager.getInstance().getEnvironment();
ConfigManager.getInstance().getBrowser();
ConfigManager.getInstance().isHeadless();
ConfigManager.getInstance().getWaitTimeout();
ConfigManager.getInstance().getProperty("custom.key");
ConfigManager.getInstance().getEnvProperty("app.url");
```

## Best Practices

1. **Use Global Variables for Common Config**
   ```java
   // Good - simple and clean
   if (ConfigManager.IS_HEADLESS) {
       options.addArguments("--headless");
   }
   ```

2. **Use DTO for Passing Config**
   ```java
   // Good - pass entire config to methods
   public void initialize(FrameworkConfig config) {
       // Use config
   }
   ```

3. **Use getInstance() for Dynamic Properties**
   ```java
   // Good - for properties not in DTO
   String appUrl = ConfigManager.getInstance().getAppUrl();
   String apiUrl = ConfigManager.getInstance().getApiBaseUrl("auth");
   ```

4. **Use Utility Methods for Readability**
   ```java
   // Good - readable and clear
   if (ConfigManager.isSelenium()) {
       initializeSeleniumDriver();
   }
   ```

## Migration Guide

### Step 1: Replace getInstance() Calls (Optional)
```java
// Before
String env = ConfigManager.getInstance().getEnvironment();
String browser = ConfigManager.getInstance().getBrowser();

// After
String env = ConfigManager.ENVIRONMENT;
String browser = ConfigManager.BROWSER;
```

### Step 2: Use DTO for Config Objects (Optional)
```java
// Before
String env = ConfigManager.getInstance().getEnvironment();
String browser = ConfigManager.getInstance().getBrowser();
boolean headless = ConfigManager.getInstance().isHeadless();

// After
FrameworkConfig config = ConfigManager.getInstance().getConfig();
String env = config.getEnvironment();
String browser = config.getBrowser();
boolean headless = config.isHeadless();
```

### Step 3: Use Utility Methods (Optional)
```java
// Before
if ("selenium".equals(ConfigManager.getInstance().getFrameworkType())) {
    // Selenium code
}

// After
if (ConfigManager.isSelenium()) {
    // Selenium code
}
```

## Summary

✅ **DTO Pattern** - Clean, type-safe configuration object  
✅ **Global Variables** - Easy access without getInstance()  
✅ **Utility Methods** - Convenient helper methods  
✅ **Backward Compatible** - All existing code works  
✅ **Immutable** - Configuration can't be modified  
✅ **Thread-Safe** - Static final variables initialized once  

Use the approach that best fits your needs - all three patterns are supported!
