# Playwright Design Patterns - Quick Reference

## ✅ Recommended Pattern: Locator Methods

### Why Use Locator Methods?

1. **Fresh Locators**: Each call creates a new locator, preventing stale element issues
2. **Better Readability**: Cleaner code with method calls
3. **Dynamic Pages**: Works perfectly with SPAs and dynamic content
4. **Maintainability**: Easy to update selectors in one place

### Pattern Comparison

#### ❌ Not Recommended: String Selectors
```java
public class LoginPage extends PlaywrightReusable {
    // String variables
    private String usernameField = "#username";
    private String passwordField = "#password";
    private String loginButton = "#loginBtn";
    
    public void login(String user, String pass) {
        type(locator(usernameField), user);      // Manual locator creation
        type(locator(passwordField), pass);      // Manual locator creation
        click(locator(loginButton));             // Manual locator creation
    }
}
```

**Issues:**
- Requires `locator()` call every time
- Can lead to stale elements on dynamic pages
- More verbose

#### ⚠️ Better: Locator Methods with CSS
```java
public class LoginPage extends PlaywrightReusable {
    // Locator methods with CSS
    private Locator usernameField() { return locator("#username"); }
    private Locator passwordField() { return locator("#password"); }
    private Locator loginButton() { return locator("#loginBtn"); }
    
    public void login(String user, String pass) {
        type(usernameField(), user);
        type(passwordField(), pass);
        click(loginButton());
    }
}
```

#### ✅ Best: Built-in Locator Strategies
```java
public class LoginPage extends PlaywrightReusable {
    // Use Playwright's built-in locators
    private Locator usernameField() { return getByLabel("Username"); }
    private Locator passwordField() { return getByLabel("Password"); }
    private Locator loginButton() { return getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login")); }
    
    public void login(String user, String pass) {
        type(usernameField(), user);
        type(passwordField(), pass);
        click(loginButton());
    }
}
```

**Benefits:**
- More resilient to UI changes
- Better accessibility compliance
- Self-documenting code
- Follows Playwright best practices
- Works better with dynamic content

## 🎯 Complete Example: E-commerce Page

```java
package com.automation.pages;

import com.automation.core.config.ConfigManager;
import com.automation.core.driver.DriverManager;
import com.automation.reusables.PlaywrightReusable;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class FlipkartPage extends PlaywrightReusable {
    
    // ✅ Best Practice: Use Playwright's built-in locator strategies
    private Locator searchBox() { return getByPlaceholder("Search for Products, Brands and More"); }
    private Locator searchButton() { return getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")); }
    private Locator firstProduct() { return getByRole(AriaRole.LINK).first(); }
    private Locator addToCartButton() { return getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add to cart")); }
    private Locator cartIcon() { return getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Cart")); }
    
    // Fallback to CSS when semantic locators aren't available
    private Locator closeLoginPopup() { return locator("button._2KpZ6l._2doB4z"); }

    public void openFlipkart() {
        navigateTo(ConfigManager.getInstance().getAppUrl());
        
        // Handle optional popup
        try {
            if (isDisplayed(closeLoginPopup())) {
                click(closeLoginPopup());
            }
        } catch (Exception e) {
            // Popup not displayed
        }
    }

    public void searchProduct(String productName) {
        type(searchBox(), productName);
        click(searchButton());
        waitForSeconds(2);
    }

    public void clickFirstProduct() {
        scrollToElement(firstProduct());
        
        // ✅ Handle new page/tab opening
        Page newPage = page.context().waitForPage(() -> {
            click(firstProduct());
        });
        
        // Switch to new page
        page = newPage;
        DriverManager.setPlaywrightPage(newPage);
        waitForSeconds(2);
    }

    public void addToCart() {
        waitForSeconds(2);
        try {
            if (isDisplayed(addToCartButton())) {
                click(addToCartButton());
            }
        } catch (Exception e) {
            // Button might not be visible
        }
    }

    public boolean isProductInCart() {
        try {
            return isDisplayed(cartIcon());
        } catch (Exception e) {
            return false;
        }
    }
}
```

## 🔄 Handling New Windows/Tabs

### Pattern: Wait for New Page
```java
public void clickLinkAndSwitchToNewPage() {
    // Playwright waits for new page and captures it
    Page newPage = page.context().waitForPage(() -> {
        click(linkThatOpensNewTab());
    });
    
    // Update current page reference
    page = newPage;
    DriverManager.setPlaywrightPage(newPage);
    
    // Continue on new page
    assertVisible(newPageElement());
}
```

### Pattern: Multiple Pages
```java
public void handleMultiplePages() {
    // Get all pages
    List<Page> pages = page.context().pages();
    
    // Switch to specific page
    page = pages.get(1);
    DriverManager.setPlaywrightPage(page);
    
    // Or switch by URL
    for (Page p : pages) {
        if (p.url().contains("checkout")) {
            page = p;
            DriverManager.setPlaywrightPage(p);
            break;
        }
    }
}
```

## 🎨 Advanced Locator Patterns

### Chaining Locators
```java
private Locator productCard() { return locator(".product-card"); }
private Locator productTitle() { return productCard().locator(".title"); }
private Locator productPrice() { return productCard().locator(".price"); }
private Locator addToCartBtn() { return productCard().locator("button"); }
```

### Dynamic Locators
```java
private Locator productByName(String name) {
    return locator("div.product:has-text('" + name + "')");
}

private Locator buttonByText(String text) {
    return locator("button:has-text('" + text + "')");
}

public void selectProduct(String productName) {
    click(productByName(productName));
}
```

### First/Last/Nth Elements
```java
private Locator firstProduct() { return locator(".product").first(); }
private Locator lastProduct() { return locator(".product").last(); }
private Locator thirdProduct() { return locator(".product").nth(2); }
```

### Filter Locators
```java
private Locator visibleProducts() {
    return locator(".product").filter(new Locator.FilterOptions().setHasText("In Stock"));
}

private Locator activeButtons() {
    return locator("button").filter(new Locator.FilterOptions().setHas(locator(".active")));
}
```

## 🛡️ Error Handling Patterns

### Optional Elements
```java
public void closePopupIfPresent() {
    try {
        if (isDisplayed(popup())) {
            click(closeButton());
        }
    } catch (Exception e) {
        // Popup not present, continue
    }
}
```

### Conditional Actions
```java
public void addToCartIfAvailable() {
    if (addToCartButton().isVisible()) {
        click(addToCartButton());
    } else {
        System.out.println("Product not available");
    }
}
```

### Retry Pattern
```java
public void clickWithRetry(Locator element, int maxRetries) {
    for (int i = 0; i < maxRetries; i++) {
        try {
            click(element);
            return;
        } catch (Exception e) {
            if (i == maxRetries - 1) throw e;
            waitForSeconds(1);
        }
    }
}
```

## 📊 Pattern Comparison Table

| Aspect | String Selectors | Locator Methods |
|--------|-----------------|-----------------|
| **Syntax** | `private String btn = "#id";` | `private Locator btn() { return locator("#id"); }` |
| **Usage** | `click(locator(btn));` | `click(btn());` |
| **Freshness** | Manual | Automatic |
| **Stale Elements** | Possible | Prevented |
| **Dynamic Pages** | Issues | Works Great |
| **Readability** | Good | Excellent |
| **Maintenance** | Moderate | Easy |
| **Performance** | Same | Same |
| **Recommendation** | ❌ Avoid | ✅ Use This |

## 🎯 Best Practices

1. ✅ **Always use Locator methods** for production code
2. ✅ **Use descriptive method names** (e.g., `submitButton()` not `btn1()`)
3. ✅ **Handle new pages properly** with `waitForPage()`
4. ✅ **Use try-catch for optional elements** (popups, banners)
5. ✅ **Chain locators** for complex element hierarchies
6. ✅ **Use dynamic locators** for parameterized elements
7. ✅ **Leverage Playwright's auto-wait** instead of explicit waits
8. ✅ **Use native assertions** (`assertVisible`, `assertHasText`)

## 🚫 Anti-Patterns to Avoid

1. ❌ Storing locators as strings
2. ❌ Using Thread.sleep() instead of Playwright waits
3. ❌ Not handling new pages/tabs properly
4. ❌ Ignoring stale element exceptions
5. ❌ Using XPath when CSS selectors work
6. ❌ Not using Page Object Model
7. ❌ Hardcoding waits without need

## 📚 Quick Reference

### Basic Locator Methods
```java
private Locator element() { return locator("#id"); }
private Locator byClass() { return locator(".class"); }
private Locator byText() { return locator("text=Login"); }
private Locator byRole() { return locator("role=button[name='Submit']"); }
```

### Actions
```java
click(element());
type(element(), "text");
fill(element(), "text");
check(element());
uncheck(element());
selectOption(element(), "value");
```

### Assertions
```java
assertVisible(element());
assertHidden(element());
assertEnabled(element());
assertDisabled(element());
assertHasText(element(), "text");
assertContainsText(element(), "text");
```

### Waits
```java
waitForSelector("#id");
waitForLoadState("networkidle");
element().waitFor();
```

---

**Remember:** Locator methods = Fresh locators = No stale elements = Happy testing! 🎉
