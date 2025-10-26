# ⏱️ Wait Steps Reference - Complete Guide

## Overview

Comprehensive reference for all 30+ wait-related step definitions available in the Sands Automation Framework.

---

## 📋 Wait Categories

1. [Basic Waits](#basic-waits)
2. [Element State Waits](#element-state-waits)
3. [Text Waits](#text-waits)
4. [Attribute Waits](#attribute-waits)
5. [Page State Waits](#page-state-waits)
6. [Element Count Waits](#element-count-waits)
7. [Alert & Frame Waits](#alert--frame-waits)
8. [Timing Waits](#timing-waits)

---

## 🔹 Basic Waits

### Wait for Visible
```gherkin
When wait for visible "#loginButton" for 10 seconds
When wait for visible ".dashboard" for 15 seconds
```
**Use Case:** Wait until element becomes visible on the page

---

### Wait for Clickable
```gherkin
When wait for clickable "#submitBtn" for 15 seconds
When wait for clickable "button[type='submit']" for 10 seconds
```
**Use Case:** Wait until element is both visible and enabled for clicking

---

### Wait for Invisible
```gherkin
When wait for invisible ".loading-spinner" for 20 seconds
When wait for invisible "#overlay" for 30 seconds
```
**Use Case:** Wait until element disappears from the page (e.g., loading spinners)

---

## 🔹 Element State Waits

### Wait for Element Present
```gherkin
When wait for element "#dashboard" to be present
When wait for element "#dashboard" to be present for 15 seconds
```
**Use Case:** Wait until element exists in DOM (may not be visible)

---

### Wait for Element Enabled
```gherkin
When wait for element "#loginForm" to be enabled
When wait for element "#loginForm" to be enabled for 10 seconds
```
**Use Case:** Wait until element becomes enabled/interactive

---

### Wait for Element to Disappear
```gherkin
When wait for element "#loadingSpinner" to disappear
When wait for element "#loadingSpinner" to disappear for 20 seconds
```
**Use Case:** Wait until element is removed or hidden

---

## 🔹 Text Waits

### Wait for Text in Element
```gherkin
When wait for text "Welcome" in ".message" for 10 seconds
When wait for text "Success" in "#status" for 15 seconds
```
**Use Case:** Wait until specific text appears in an element

---

### Wait for Text to Appear
```gherkin
When wait for text "Success" to appear
When wait for text "Success" to appear for 15 seconds
```
**Use Case:** Wait until text appears anywhere on the page

---

### Wait for Text to Disappear
```gherkin
When wait for text "Loading..." to disappear
When wait for text "Loading..." to disappear for 20 seconds
```
**Use Case:** Wait until text is removed from the page

---

## 🔹 Attribute Waits

### Wait for Attribute Contains
```gherkin
When wait for attribute "class" of "#button" to contain "active"
When wait for attribute "class" of "#button" to contain "active" for 10 seconds
```
**Use Case:** Wait until element attribute contains specific value

**Examples:**
```gherkin
# Wait for button to become active
When wait for attribute "class" of "#submitBtn" to contain "enabled"

# Wait for input to be validated
When wait for attribute "class" of "#email" to contain "valid"

# Wait for element to be selected
When wait for attribute "aria-selected" of ".tab" to contain "true"
```

---

## 🔹 Page State Waits

### Wait for Page Load
```gherkin
When wait for page load
When wait for page load for 30 seconds
```
**Use Case:** Wait until page is fully loaded (document.readyState = complete)

---

### Wait for URL Contains
```gherkin
When wait for URL to contain "dashboard"
When wait for URL to contain "dashboard" for 15 seconds
```
**Use Case:** Wait until URL changes to expected value

**Examples:**
```gherkin
# After login, wait for redirect
When wait for URL to contain "/home"

# After form submission
When wait for URL to contain "success"

# After navigation
When wait for URL to contain "/profile"
```

---

### Wait for Title
```gherkin
When wait for title to be "Dashboard - MyApp"
When wait for title to be "Dashboard - MyApp" for 10 seconds
```
**Use Case:** Wait until page title matches exactly

---

### Wait for Title Contains
```gherkin
When wait for title to contain "Dashboard"
When wait for title to contain "Dashboard" for 10 seconds
```
**Use Case:** Wait until page title contains specific text

---

## 🔹 Element Count Waits

### Wait for Element Count
```gherkin
When wait for element count of ".item" to be 5
When wait for element count of ".item" to be 5 for 15 seconds
```
**Use Case:** Wait until specific number of elements are present

**Examples:**
```gherkin
# Wait for all items to load
When wait for element count of ".product" to be 10

# Wait for table rows
When wait for element count of "tr" to be 20

# Wait for search results
When wait for element count of ".result" to be 5
```

---

## 🔹 Alert & Frame Waits

### Wait for Alert
```gherkin
When wait for alert to be present
When wait for alert to be present for 10 seconds
```
**Use Case:** Wait until JavaScript alert appears

---

### Wait for Frame Available
```gherkin
When wait for frame "#myFrame" to be available
When wait for frame "#myFrame" to be available for 10 seconds
```
**Use Case:** Wait until iframe is loaded and ready

---

## 🔹 Timing Waits

### Pause
```gherkin
When pause for 3 seconds
When pause for 5 seconds
```
**Use Case:** Hard wait for specific duration (use sparingly)

---

### Wait Milliseconds
```gherkin
When wait for 500 milliseconds
When wait for 1000 milliseconds
```
**Use Case:** Precise timing for animations or transitions

---

## 💡 Best Practices

### ✅ DO:
- Use explicit waits instead of hard waits (pause)
- Wait for specific conditions (visible, clickable, text)
- Use appropriate timeouts based on application behavior
- Wait for page load after navigation
- Wait for elements to disappear (loading indicators)

### ❌ DON'T:
- Overuse `pause` - prefer condition-based waits
- Use very long timeouts (>30 seconds) without reason
- Wait for elements that are already present
- Chain multiple waits unnecessarily

---

## 🎯 Common Patterns

### Pattern 1: Wait for Page Load After Navigation
```gherkin
Given navigate to "https://example.com"
When wait for page load
Then element "#header" should be visible
```

---

### Pattern 2: Wait for Loading to Complete
```gherkin
When click element "#loadData"
And wait for element ".loading-spinner" to disappear for 20 seconds
Then element ".data-table" should be visible
```

---

### Pattern 3: Wait for Dynamic Content
```gherkin
When type "search term" into "#searchBox"
And wait for element count of ".result" to be 10 for 15 seconds
Then element ".result" should be visible
```

---

### Pattern 4: Wait for URL Change
```gherkin
When click element "#loginBtn"
And wait for URL to contain "dashboard" for 10 seconds
Then page title should contain "Dashboard"
```

---

### Pattern 5: Wait for Text Update
```gherkin
When click element "#refreshBtn"
And wait for text "Updated" to appear for 10 seconds
Then element ".status" should contain text "Updated"
```

---

### Pattern 6: Wait for Element State Change
```gherkin
When type "user@example.com" into "#email"
And wait for attribute "class" of "#email" to contain "valid" for 5 seconds
Then element "#submitBtn" should be enabled
```

---

## 🔄 Framework Support

All wait steps work with both **Selenium** and **Playwright** frameworks:

| Wait Type | Selenium | Playwright | Notes |
|-----------|----------|------------|-------|
| Visible | ✅ WebDriverWait | ✅ waitForSelector | Both supported |
| Clickable | ✅ elementToBeClickable | ✅ waitForSelector | Both supported |
| Invisible | ✅ invisibilityOf | ✅ waitForSelector(hidden) | Both supported |
| Page Load | ✅ document.readyState | ✅ waitForLoadState | Both supported |
| Text Present | ✅ textToBePresentIn | ✅ Custom polling | Both supported |
| URL Contains | ✅ urlContains | ✅ waitForURL | Both supported |
| Title | ✅ titleIs | ✅ Custom polling | Both supported |
| Alert | ✅ alertIsPresent | ✅ Dialog events | Both supported |

---

## 📊 Performance Impact

| Wait Type | Performance | Reliability | Use Case |
|-----------|-------------|-------------|----------|
| **Condition-based** | ⚡ Fast | ⭐⭐⭐⭐⭐ High | Preferred |
| **Polling** | ⚡ Fast | ⭐⭐⭐⭐ Good | Dynamic content |
| **Hard wait (pause)** | 🐌 Slow | ⭐⭐⭐ Moderate | Last resort |

---

## 🧪 Testing Examples

### Example 1: Login Flow
```gherkin
Feature: Login with Waits

  Scenario: Successful login with proper waits
    Given navigate to "https://example.com"
    When wait for page load
    And wait for element "#loginForm" to be present
    And type "testuser" into "#username"
    And type "password123" into "#password"
    And wait for element "#loginBtn" to be enabled
    And click element "#loginBtn"
    And wait for URL to contain "dashboard" for 10 seconds
    Then element ".welcome-message" should be visible
```

---

### Example 2: Search with Dynamic Results
```gherkin
Feature: Search with Dynamic Content

  Scenario: Search and wait for results
    Given navigate to "https://example.com/search"
    When type "automation" into "#searchBox"
    And wait for element ".loading" to disappear for 15 seconds
    And wait for element count of ".result" to be 10
    Then element ".result" should be visible
```

---

### Example 3: Form Validation
```gherkin
Feature: Form Validation

  Scenario: Wait for field validation
    Given navigate to "https://example.com/register"
    When type "invalid-email" into "#email"
    And wait for attribute "class" of "#email" to contain "error" for 5 seconds
    Then element ".error-message" should contain text "Invalid email"
```

---

## 🔍 Troubleshooting

### Issue: Element not found
**Solution:** Increase timeout or verify locator
```gherkin
# Before
When wait for visible "#button" for 5 seconds

# After
When wait for visible "#button" for 15 seconds
```

---

### Issue: Flaky tests due to timing
**Solution:** Use condition-based waits instead of pause
```gherkin
# ❌ Bad
When pause for 5 seconds
And click element "#button"

# ✅ Good
When wait for clickable "#button" for 10 seconds
And click element "#button"
```

---

### Issue: Page not fully loaded
**Solution:** Add page load wait after navigation
```gherkin
Given navigate to "https://example.com"
When wait for page load
And wait for element "#content" to be present
```

---

## 📞 Support

For questions or issues with wait steps:
- 📧 Email: automation-support@company.com
- 💬 Slack: #automation-framework
- 📖 Docs: `/docs/web-automation.html`

---

**Document Version:** 1.0  
**Last Updated:** January 2025  
**Total Wait Steps:** 30+
