# Playwright Test Generator (Codegen) Guide

## Quick Start

### macOS/Linux
```bash
./codegen.sh https://example.com
```

### Windows
```cmd
codegen.bat https://example.com
```

### Default (no URL)
```bash
./codegen.sh
# Opens demo.playwright.dev/todomvc
```

## What is Codegen?

Playwright's test generator records your browser interactions and generates test code automatically.

## Features

- 🎭 **Record Actions** - Click, type, navigate automatically recorded
- 📝 **Generate Code** - Creates Playwright test code in real-time
- 🎯 **Smart Selectors** - Generates optimal locators
- 🔍 **Inspector** - Explore page elements
- 📸 **Screenshots** - Capture page state

## Usage Examples

### Example 1: Record Login Flow
```bash
./codegen.sh https://example.com/login
```

**Actions in Browser:**
1. Type username
2. Type password
3. Click login button

**Generated Code:**
```java
page.locator("#username").fill("admin");
page.locator("#password").fill("password123");
page.locator("#login-btn").click();
```

### Example 2: Record E-commerce Flow
```bash
./codegen.sh https://example.com/shop
```

**Actions:**
1. Search for product
2. Add to cart
3. Checkout

**Generated Code:**
```java
page.locator("#search").fill("laptop");
page.locator("button:has-text('Search')").click();
page.locator(".product-card >> nth=0").click();
page.locator("button:has-text('Add to Cart')").click();
```

## Converting to Framework Format

### Generated Playwright Code
```java
page.navigate("https://example.com");
page.locator("#username").fill("admin");
page.locator("#password").fill("password123");
page.locator("#login-btn").click();
```

### Convert to Framework Reusable
```java
public class LoginPage extends PlaywrightReusable {
    
    public void login(String username, String password) {
        navigateTo("https://example.com");
        type(locator("#username"), username);
        type(locator("#password"), password);
        click(locator("#login-btn"));
        UnifiedLogger.pass("Login successful");
    }
}
```

### Convert to Cucumber Steps
```gherkin
Given user navigates to "https://example.com"
When user types "admin" in "#username"
And user types "password123" in "#password"
And user clicks "#login-btn"
Then user should see dashboard
```

## Advanced Options

### Record with Device Emulation
```bash
mvn exec:java -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="codegen --device='iPhone 12' https://example.com"
```

### Record with Specific Browser
```bash
mvn exec:java -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="codegen --browser=firefox https://example.com"
```

### Record with Viewport Size
```bash
mvn exec:java -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="codegen --viewport-size=1280,720 https://example.com"
```

### Save Output to File
```bash
mvn exec:java -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="codegen --target=java --output=test.java https://example.com"
```

## Codegen Window

When you run codegen, two windows open:

### 1. Browser Window
- Interact with the application
- All actions are recorded
- Red dot indicates recording

### 2. Inspector Window
- Shows generated code in real-time
- Copy code snippets
- Explore element selectors
- Pause/resume recording

## Tips & Best Practices

### 1. Use Stable Selectors
Generated selectors might be fragile. Prefer:
- ✅ `#id` - ID selectors
- ✅ `[data-testid="login"]` - Test IDs
- ✅ `text=Login` - Text content
- ❌ `.css-xyz-123` - Generated CSS classes

### 2. Clean Up Generated Code
```java
// Generated (verbose)
page.locator("div.container > div.row > div.col > button.btn.btn-primary").click();

// Cleaned (better)
page.locator("button:has-text('Submit')").click();
```

### 3. Add Assertions
Codegen doesn't generate assertions. Add them:
```java
// Generated
page.locator("#username").fill("admin");

// Add assertion
page.locator("#username").fill("admin");
assertVisible(locator(".dashboard")); // Add this
```

### 4. Extract to Methods
```java
// Generated inline code
page.locator("#username").fill("admin");
page.locator("#password").fill("pass");
page.locator("#login").click();

// Extract to method
public void login(String user, String pass) {
    type(locator("#username"), user);
    type(locator("#password"), pass);
    click(locator("#login"));
}
```

## Common Commands

```bash
# Basic codegen
./codegen.sh https://example.com

# With device emulation
./codegen.sh --device="iPhone 12" https://example.com

# With specific browser
./codegen.sh --browser=firefox https://example.com

# Save to file
./codegen.sh --output=test.java https://example.com

# With authentication
./codegen.sh --save-storage=auth.json https://example.com/login
./codegen.sh --load-storage=auth.json https://example.com/dashboard
```

## Workflow

### Step 1: Generate Test
```bash
./codegen.sh https://example.com
```

### Step 2: Record Actions
- Interact with the application
- Copy generated code from Inspector

### Step 3: Create Page Object
```java
public class HomePage extends PlaywrightReusable {
    
    public void searchProduct(String product) {
        type(locator("#search"), product);
        click(locator("button:has-text('Search')"));
    }
}
```

### Step 4: Create Step Definition
```java
@When("user searches for {string}")
public void userSearchesFor(String product) {
    homePage.searchProduct(product);
    UnifiedLogger.action("Search", product);
}
```

### Step 5: Create Feature File
```gherkin
Scenario: Search for product
  Given user is on home page
  When user searches for "laptop"
  Then search results should be displayed
```

## Troubleshooting

### Issue: Codegen doesn't start
```bash
# Ensure Playwright browsers are installed
mvn exec:java -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
```

### Issue: Generated selectors are fragile
**Solution**: Use data-testid attributes in your application
```html
<button data-testid="login-btn">Login</button>
```
```java
page.locator("[data-testid='login-btn']").click();
```

### Issue: Code is too verbose
**Solution**: Simplify using text or role selectors
```java
// Instead of
page.locator("div > div > button.btn-primary").click();

// Use
page.locator("button:has-text('Submit')").click();
```

## Integration with Framework

### 1. Record Test
```bash
./codegen.sh https://example.com
```

### 2. Convert to Reusable
```java
public class ExamplePage extends PlaywrightReusable {
    public void performAction() {
        // Paste and adapt generated code
        click(locator("#button"));
        UnifiedLogger.action("Click", "button");
    }
}
```

### 3. Use in Tests
```gherkin
When user performs action on example page
```

## Additional Resources

- [Playwright Codegen Docs](https://playwright.dev/java/docs/codegen)
- [Playwright Selectors](https://playwright.dev/java/docs/selectors)
- [Best Practices](https://playwright.dev/java/docs/best-practices)

## Summary

| Command | Description |
|---------|-------------|
| `./codegen.sh` | Start with default URL |
| `./codegen.sh <url>` | Start with specific URL |
| `./codegen.sh --device="iPhone 12" <url>` | Mobile emulation |
| `./codegen.sh --browser=firefox <url>` | Specific browser |
| `./codegen.sh --output=test.java <url>` | Save to file |

---

**Created**: 2025-01-21  
**Framework**: Sands Automation Framework  
**Author**: Afsar Ali
