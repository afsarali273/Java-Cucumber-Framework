# Mainframe Integration Guide

## Overview

The Sands Automation Framework now supports mainframe automation using EHLLAPI (Emulator High-Level Language Application Programming Interface). This allows you to automate 3270 terminal emulator sessions.

## Components

### 1. Core Components

- **EHLLAPIWrapper** (`com.automation.core.mainframe.EHLLAPIWrapper`)
  - Low-level wrapper for EHLLAPI native library calls
  - Uses JNA (Java Native Access) to communicate with `pcshll32.dll`

- **MainFrameDriver** (`com.automation.core.mainframe.MainFrameDriver`)
  - High-level driver for mainframe operations
  - Manages session connection and screen interactions

### 2. Reusable Class

- **MainframeReusable** (`com.automation.reusables.MainframeReusable`)
  - Framework-integrated reusable methods
  - Includes UnifiedLogger integration
  - Thread-safe implementation

### 3. Step Definitions

- **MainframeSteps** (`com.automation.stepdefinitions.MainframeSteps`)
  - Cucumber step definitions for BDD testing
  - Ready-to-use Gherkin steps

## Configuration

### config.properties

```properties
# Framework Type
framework.type=mainframe

# Mainframe Session ID (typically A-Z)
mainframe.sessionId=A

# Mainframe Connection Timeout (seconds)
mainframe.timeout=30

# Mainframe Screen Wait Timeout (seconds)
mainframe.screen.wait=10
```

## Prerequisites

### 1. Install Terminal Emulator

You need a 3270 terminal emulator that supports EHLLAPI:
- IBM Personal Communications (PCOMM)
- Micro Focus Rumba
- Attachmate Extra!
- BlueZone

### 2. EHLLAPI DLL

Ensure `pcshll32.dll` is available in your system PATH or in the project directory.

### 3. Maven Dependencies

Already added to `pom.xml`:
```xml
<dependency>
    <groupId>net.java.dev.jna</groupId>
    <artifactId>jna</artifactId>
    <version>5.13.0</version>
</dependency>
```

## Usage

### Using MainframeReusable

```java
public class MainframeTest extends MainframeReusable {
    
    @Test
    public void testMainframeLogin() {
        // Connect to session
        connectToMainframe("A");
        
        // Enter credentials
        enterText(10, 20, "USER123");
        enterText(11, 20, "PASSWORD");
        
        // Submit
        pressEnter();
        
        // Wait for response
        waitForText("WELCOME", 5);
        
        // Verify screen content
        verifyTextOnScreen("MAIN MENU");
        
        // Disconnect
        disconnectFromMainframe();
    }
}
```

### Using Cucumber Steps

```gherkin
@Mainframe
Feature: Mainframe Login

  Scenario: Successful login
    Given user connects to mainframe session "A"
    When user enters "USER123" at row 10 column 20
    And user enters "PASSWORD" at row 11 column 20
    And user presses Enter key
    Then mainframe screen should display "WELCOME" within 5 seconds
    And mainframe screen should contain "MAIN MENU"
```

## Available Methods

### Connection Methods

- `connectToMainframe(String sessionId)` - Connect to mainframe session
- `disconnectFromMainframe()` - Disconnect from session
- `closeMainframeSession()` - Close and cleanup session

### Input Methods

- `enterText(int row, int col, String text)` - Enter text at position
- `sendKeys(String keys)` - Send raw keys
- `pressEnter()` - Press Enter key
- `tab()` - Press Tab key
- `clearScreen()` - Clear the screen

### Function Keys

- `sendFunctionKey(String functionKey)` - Send any function key
- `sendPF(int pfNumber)` - Send PF key (PF1-PF24)

### Screen Reading

- `getTextFromScreen(int row, int col, int length)` - Get text from position
- `getFullScreen()` - Get entire screen content
- `waitForScreenReady()` - Wait for screen unlock

### Verification Methods

- `verifyTextOnScreen(String expectedText)` - Verify text exists on screen
- `verifyTextAtPosition(int row, int col, int length, String expectedText)` - Verify text at specific position
- `waitForText(String expectedText, int timeoutSeconds)` - Wait for text to appear

## Cucumber Step Definitions

### Connection Steps
```gherkin
Given user connects to mainframe session "A"
When user disconnects from mainframe
```

### Input Steps
```gherkin
When user enters "TEXT" at row 10 column 20
When user presses Enter key
When user sends keys "@E"
When user presses PF3
When user clears the screen
When user presses Tab
```

### Verification Steps
```gherkin
Then mainframe screen should contain "EXPECTED TEXT"
Then text at row 10 column 20 length 15 should be "EXPECTED"
Then mainframe screen should display "TEXT" within 5 seconds
```

### Data Handling
```gherkin
When user saves text from row 15 column 5 length 20 as "accountNumber"
```

## EHLLAPI Function Codes

The framework uses these EHLLAPI function codes:

| Code | Function | Description |
|------|----------|-------------|
| 1 | Connect PS | Connect to presentation space |
| 2 | Disconnect PS | Disconnect from presentation space |
| 3 | Send Keys | Send keystrokes to host |
| 4 | Wait | Wait for host update |
| 8 | Copy PS | Copy presentation space content |
| 18 | Set Cursor | Position cursor |

## Special Keys

Use these codes with `sendKeys()`:

- `@E` - Enter
- `@C` - Clear
- `@T` - Tab
- `@1` to `@24` - PF1 to PF24
- `@A` - Attention (SysReq)
- `@R` - Reset

## Example Feature File

See `src/test/resources/features/mainframe_example.feature` for complete examples.

## Running Tests

```bash
# Run all mainframe tests
mvn test -Dcucumber.filter.tags="@Mainframe"

# Run with specific framework type
mvn test -Dframework.type=mainframe

# Run specific scenario
mvn test -Dcucumber.filter.tags="@Mainframe and @Login"
```

## Integration with Framework

### DriverManager Integration

The mainframe driver is integrated into `DriverManager`:

```java
// Initialize
DriverManager.initializeDriver();

// Get driver
MainFrameDriver driver = DriverManager.getMainframeDriver();

// Cleanup
DriverManager.quitDriver();
```

### ConfigManager Integration

Check if mainframe mode:
```java
if (ConfigManager.isMainframe()) {
    // Mainframe-specific logic
}
```

### UnifiedLogger Integration

All mainframe operations are logged to:
- ExtentReports
- CustomReporter
- Allure
- Log files

## Thread Safety

All mainframe components are thread-safe and support parallel execution:
- ThreadLocal driver instances
- Thread-safe ScenarioContext
- Synchronized logging

## Troubleshooting

### Common Issues

1. **DLL Not Found**
   - Ensure `pcshll32.dll` is in system PATH
   - Or place it in project root directory

2. **Session Not Connected**
   - Verify terminal emulator is running
   - Check session ID matches emulator configuration

3. **Screen Not Ready**
   - Increase `mainframe.screen.wait` timeout
   - Use `waitForScreenReady()` before operations

4. **Text Not Found**
   - Verify row/column coordinates (1-based indexing)
   - Check screen is fully loaded
   - Use `getFullScreen()` to debug

## Best Practices

1. Always connect before operations
2. Use `waitForText()` for dynamic screens
3. Disconnect after test completion
4. Store reusable data in ScenarioContext
5. Use descriptive variable names
6. Add appropriate waits between operations
7. Verify screen state before assertions

## Limitations

- Windows-only (EHLLAPI is Windows-specific)
- Requires compatible terminal emulator
- 3270 terminal sessions only
- Single session per thread

## Future Enhancements

- Support for 5250 (AS/400) terminals
- Screen scraping utilities
- Field detection and validation
- Macro recording and playback
- Multi-session support

## Support

For issues or questions:
1. Check terminal emulator EHLLAPI documentation
2. Verify DLL compatibility
3. Review framework logs
4. Contact framework maintainer

---

**Framework Version**: 1.0  
**Last Updated**: 2025-01-21  
**Maintained by**: Afsar Ali
