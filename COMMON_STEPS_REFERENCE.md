# Common Step Definitions Reference

This document provides a comprehensive reference for all reusable step definitions available in the framework JAR.

## Table of Contents
- [UI Steps](#ui-steps)
- [API Steps](#api-steps)
- [Shared Steps](#shared-steps)

---

## UI Steps

### Navigation & Browser

| Step | Example | Description |
|------|---------|-------------|
| `Given open browser` | `Given open browser` | Opens browser (Selenium/Playwright) |
| `Given navigate to {string}` | `Given navigate to "https://example.com"` | Navigate to URL |
| `When close browser` | `When close browser` | Closes browser |
| `When refresh page` | `When refresh page` | Refreshes current page |
| `When navigate back` | `When navigate back` | Browser back button |
| `When navigate forward` | `When navigate forward` | Browser forward button |

### Element Interactions

| Step | Example | Description |
|------|---------|-------------|
| `When click element {string}` | `When click element "#submitBtn"` | Click element by locator |
| `When click text {string}` | `When click text "Login"` | Click by visible text |
| `When type {string} into {string}` | `When type "test@email.com" into "#email"` | Type text into field |
| `When type {string} into {string} and press enter` | `When type "search term" into "#search" and press enter` | Type and press Enter |
| `When clear {string}` | `When clear "#username"` | Clear input field |
| `When select {string} from dropdown {string}` | `When select "USA" from dropdown "#country"` | Select dropdown by text |
| `When select option by index {int} from dropdown {string}` | `When select option by index 2 from dropdown "#country"` | Select by index |
| `When select option by value {string} from dropdown {string}` | `When select option by value "usa" from dropdown "#country"` | Select by value |
| `When upload file {string} to {string}` | `When upload file "/path/file.pdf" to "#fileInput"` | Upload file |
| `When check {string}` | `When check "#terms"` | Check checkbox |
| `When uncheck {string}` | `When uncheck "#newsletter"` | Uncheck checkbox |
| `When hover over {string}` | `When hover over ".menu-item"` | Mouse hover |
| `When double click {string}` | `When double click "#item"` | Double click element |
| `When right click element {string}` | `When right click element "#contextMenu"` | Right click element |
| `When drag {string} to {string}` | `When drag "#source" to "#target"` | Drag and drop |

### Waiting

| Step | Example | Description |
|------|---------|-------------|
| `When wait for visible {string} for {int} seconds` | `When wait for visible "#loader" for 10 seconds` | Wait for element visible |
| `When wait for clickable {string} for {int} seconds` | `When wait for clickable "#btn" for 5 seconds` | Wait for clickable |
| `When wait for invisible {string} for {int} seconds` | `When wait for invisible "#spinner" for 10 seconds` | Wait for invisible |
| `When wait for text {string} in {string} for {int} seconds` | `When wait for text "Success" in "#msg" for 5 seconds` | Wait for text |
| `When pause for {int} seconds` | `When pause for 3 seconds` | Generic wait |

### Assertions - Visibility & State

| Step | Example | Description |
|------|---------|-------------|
| `Then element {string} should be visible` | `Then element "#welcome" should be visible` | Assert visible |
| `Then element {string} should not be visible` | `Then element "#error" should not be visible` | Assert not visible |
| `Then element {string} should exist` | `Then element "#submitBtn" should exist` | Assert exists in DOM |
| `Then element {string} should not exist` | `Then element "#errorMsg" should not exist` | Assert not in DOM |
| `Then element {string} should be enabled` | `Then element "#btn" should be enabled` | Assert enabled |
| `Then element {string} should be disabled` | `Then element "#btn" should be disabled` | Assert disabled |

### Assertions - Text & Content

| Step | Example | Description |
|------|---------|-------------|
| `Then element {string} should contain text {string}` | `Then element "#msg" should contain text "Success"` | Assert contains text |
| `Then element {string} text should be {string}` | `Then element "#title" text should be "Welcome"` | Assert exact text |
| `Then element {string} text should match regex {string}` | `Then element "#price" text should match regex "\\$\\d+\\.\\d{2}"` | Assert regex match |
| `Then page should contain text {string}` | `Then page should contain text "Dashboard"` | Assert text on page |

### Assertions - Attributes & CSS

| Step | Example | Description |
|------|---------|-------------|
| `Then element {string} attribute {string} should be {string}` | `Then element "#link" attribute "href" should be "/home"` | Assert attribute value |
| `Then element {string} css {string} should be {string}` | `Then element "#btn" css "color" should be "red"` | Assert CSS property |

### Assertions - Checkboxes

| Step | Example | Description |
|------|---------|-------------|
| `Then checkbox {string} should be checked` | `Then checkbox "#terms" should be checked` | Assert checked |
| `Then checkbox {string} should not be checked` | `Then checkbox "#newsletter" should not be checked` | Assert unchecked |

### Assertions - Dropdowns

| Step | Example | Description |
|------|---------|-------------|
| `Then selected option in dropdown {string} should be {string}` | `Then selected option in dropdown "#country" should be "USA"` | Assert selected option |

### Assertions - Element Count

| Step | Example | Description |
|------|---------|-------------|
| `Then element count of {string} should be {int}` | `Then element count of ".product" should be 5` | Assert exact count |
| `Then element count of {string} should be greater than {int}` | `Then element count of ".item" should be greater than 0` | Assert count > value |

### Page State

| Step | Example | Description |
|------|---------|-------------|
| `Then page title should be {string}` | `Then page title should be "Home Page"` | Assert page title |
| `Then page title should contain {string}` | `Then page title should contain "Dashboard"` | Assert title contains |
| `Then current url should be {string}` | `Then current url should be "https://example.com/home"` | Assert exact URL |
| `Then current url should contain {string}` | `Then current url should contain "/dashboard"` | Assert URL contains |

### Variables - Save & Retrieve

| Step | Example | Description |
|------|---------|-------------|
| `When save text of {string} as {string}` | `When save text of "#username" as "savedUser"` | Save element text |
| `When save attribute {string} of {string} as {string}` | `When save attribute "href" of "#link" as "linkUrl"` | Save attribute |
| `When save current url as {string}` | `When save current url as "homeUrl"` | Save current URL |
| `When save page title as {string}` | `When save page title as "pageTitle"` | Save page title |
| `When clear saved variables` | `When clear saved variables` | Clear all variables |

### Screenshots

| Step | Example | Description |
|------|---------|-------------|
| `When take screenshot {string}` | `When take screenshot "test-output/screenshot.png"` | Full page screenshot |
| `When take element screenshot {string} save as {string}` | `When take element screenshot "#chart" save as "chart.png"` | Element screenshot |

### Tabs & Windows

| Step | Example | Description |
|------|---------|-------------|
| `When open new tab` | `When open new tab` | Open new browser tab |
| `When switch to latest tab` | `When switch to latest tab` | Switch to last tab |
| `When switch to tab {int}` | `When switch to tab 0` | Switch to tab by index |
| `When close current tab` | `When close current tab` | Close current tab |

### Alerts & Dialogs

| Step | Example | Description |
|------|---------|-------------|
| `When accept alert` | `When accept alert` | Accept alert/confirm |
| `When dismiss alert` | `When dismiss alert` | Dismiss alert/confirm |
| `Then alert text should be {string}` | `Then alert text should be "Are you sure?"` | Assert alert text |

### Frames

| Step | Example | Description |
|------|---------|-------------|
| `When switch to frame {string}` | `When switch to frame "iframe1"` | Switch to iframe |
| `When switch to default content` | `When switch to default content` | Switch to main content |

### Scroll Actions

| Step | Example | Description |
|------|---------|-------------|
| `When scroll to element {string}` | `When scroll to element "#footer"` | Scroll to element |
| `When scroll to top` | `When scroll to top` | Scroll to page top |
| `When scroll to bottom` | `When scroll to bottom` | Scroll to page bottom |

### Keyboard Actions

| Step | Example | Description |
|------|---------|-------------|
| `When press key {string} on element {string}` | `When press key "Escape" on element "#modal"` | Press specific key |
| `When press enter on element {string}` | `When press enter on element "#search"` | Press Enter key |

### Advanced

| Step | Example | Description |
|------|---------|-------------|
| `When execute js {string}` | `When execute js "window.scrollTo(0,500)"` | Execute JavaScript |
| `Then element {string} rect should contain {string}` | `Then element "#box" rect should contain "width"` | Assert element rect |

---

## API Steps

### Basic HTTP Operations

| Step | Example | Description |
|------|---------|-------------|
| `When user sends GET request to {string}` | `When user sends GET request to "/users/1"` | Send GET request |
| `When user sends POST request to {string}` | `When user sends POST request to "/users"` | Send POST request |
| `When user sends PUT request to {string}` | `When user sends PUT request to "/users/1"` | Send PUT request |
| `When user sends DELETE request to {string}` | `When user sends DELETE request to "/users/1"` | Send DELETE request |
| `When user sends {string} request to {string}` | `When user sends "PATCH" request to "/users/1"` | Send any HTTP method |

### Service Switching

| Step | Example | Description |
|------|---------|-------------|
| `Given user switches to {string} API service` | `Given user switches to "userService" API service` | Switch to service |
| `Given user uses custom base URL {string}` | `Given user uses custom base URL "https://api.test.com"` | Set custom base URL |
| `When user sends GET request to {string} service endpoint {string}` | `When user sends GET request to "userService" service endpoint "/users"` | GET with service |

### Request Building

| Step | Example | Description |
|------|---------|-------------|
| `When user sets request body:` | `When user sets request body:`<br>`"""`<br>`{"name":"John"}`<br>`"""` | Set JSON body |
| `When user sets request body from file {string}` | `When user sets request body from file "data/request.json"` | Load body from file |
| `When user sets header {string} as {string}` | `When user sets header "Authorization" as "Bearer token123"` | Set request header |
| `When user sets query param {string} as {string}` | `When user sets query param "page" as "1"` | Set query parameter |
| `When user sets content type {string}` | `When user sets content type "application/json"` | Set Content-Type |
| `When user sets accept header {string}` | `When user sets accept header "application/json"` | Set Accept header |

### Authentication

| Step | Example | Description |
|------|---------|-------------|
| `When user sets basic auth with username {string} and password {string}` | `When user sets basic auth with username "admin" and password "pass123"` | Basic auth |
| `When user sets bearer token from response json {string} as header {string}` | `When user sets bearer token from response json "token" as header "Authorization"` | Bearer token from response |

### Response Validations - Status & Headers

| Step | Example | Description |
|------|---------|-------------|
| `Then response status code should be {int}` | `Then response status code should be 200` | Assert status code |
| `Then response content type should be {string}` | `Then response content type should be "application/json"` | Assert content type |
| `Then response header {string} should be {string}` | `Then response header "Server" should be "nginx"` | Assert header value |
| `Then response header {string} should exist` | `Then response header "X-Request-Id" should exist` | Assert header exists |
| `Then response header {string} should not exist` | `Then response header "X-Debug" should not exist` | Assert header absent |
| `Then response header {string} should contain {string}` | `Then response header "Content-Type" should contain "json"` | Assert header contains |
| `Then response header {string} should match regex {string}` | `Then response header "Date" should match regex "\\d{4}"` | Assert header regex |

### Response Validations - Body

| Step | Example | Description |
|------|---------|-------------|
| `Then response should contain {string} field` | `Then response should contain "id" field` | Assert field exists |
| `Then response {string} should be {string}` | `Then response "name" should be "John"` | Assert field value |
| `Then response {string} should not be null` | `Then response "id" should not be null` | Assert not null |
| `Then response {string} should be null` | `Then response "deletedAt" should be null` | Assert null |
| `Then response body should not be empty` | `Then response body should not be empty` | Assert body not empty |
| `Then response body should contain {string}` | `Then response body should contain "success"` | Assert body contains |
| `Then response should be valid json` | `Then response should be valid json` | Validate JSON syntax |

### JSON Path Validations

| Step | Example | Description |
|------|---------|-------------|
| `Then json path {string} should be {string}` | `Then json path "user.name" should be "John"` | Assert JSON path value |
| `Then json path {string} should contain {string}` | `Then json path "message" should contain "success"` | Assert contains text |
| `Then json path {string} should not be null` | `Then json path "data.id" should not be null` | Assert not null |
| `Then json path {string} should be null` | `Then json path "error" should be null` | Assert null |
| `Then json path {string} should be greater than {int}` | `Then json path "count" should be greater than 0` | Assert > value |
| `Then json path {string} should be less than {int}` | `Then json path "age" should be less than 100` | Assert < value |
| `Then json path {string} should match regex {string}` | `Then json path "email" should match regex ".*@.*\\.com"` | Assert regex match |
| `Then json path {string} should be of type {string}` | `Then json path "age" should be of type "number"` | Assert data type |

### JSON Array Validations

| Step | Example | Description |
|------|---------|-------------|
| `Then json array {string} should have size {int}` | `Then json array "users" should have size 5` | Assert array size |
| `Then json array {string} should contain {string}` | `Then json array "tags" should contain "urgent"` | Assert array contains |
| `Then json array {string} elements should be sorted by {string} in {string} order` | `Then json array "users" elements should be sorted by "age" in "asc" order` | Assert sorted |
| `Then json array {string} should have unique values by {string}` | `Then json array "users" should have unique values by "email"` | Assert unique values |
| `Then at least one element in json array {string} should have {string} equal {string}` | `Then at least one element in json array "users" should have "role" equal "admin"` | Assert element match |

### Advanced JSON Validations

| Step | Example | Description |
|------|---------|-------------|
| `Then response json should equal file {string}` | `Then response json should equal file "expected/user.json"` | Compare with file |
| `Then json path {string} keys should contain {string}` | `Then json path "user" keys should contain "id,name,email"` | Assert object keys |
| `Then json path {string} should be ISO8601 within {int} seconds` | `Then json path "createdAt" should be ISO8601 within 60 seconds` | Validate timestamp |
| `Then response should match json schema {string}` | `Then response should match json schema "schemas/user-schema.json"` | JSON schema validation |

### Response Time & Size

| Step | Example | Description |
|------|---------|-------------|
| `Then response time should be less than {long} ms` | `Then response time should be less than 2000 ms` | Assert response time |
| `Then response body size should be less than {int} bytes` | `Then response body size should be less than 5000 bytes` | Assert body size |

### Cookies

| Step | Example | Description |
|------|---------|-------------|
| `Then cookie {string} should exist` | `Then cookie "sessionId" should exist` | Assert cookie exists |
| `Then cookie {string} should be {string}` | `Then cookie "theme" should be "dark"` | Assert cookie value |

### JWT Tokens

| Step | Example | Description |
|------|---------|-------------|
| `Then jwt in json path {string} should not be expired` | `Then jwt in json path "token" should not be expired` | Validate JWT expiry |

### Variables - Save & Retrieve

| Step | Example | Description |
|------|---------|-------------|
| `When user saves json path {string} as {string}` | `When user saves json path "id" as "userId"` | Save JSON value |
| `When user saves response body as {string}` | `When user saves response body as "fullResponse"` | Save entire body |
| `When user saves response header {string} as {string}` | `When user saves response header "X-Request-Id" as "requestId"` | Save header value |
| `When user saves response time as {string}` | `When user saves response time as "responseTime"` | Save response time |
| `When user clears saved variables` | `When user clears saved variables` | Clear all variables |

### File Upload

| Step | Example | Description |
|------|---------|-------------|
| `When user uploads file {string} to {string} as multipart field {string}` | `When user uploads file "test.pdf" to "/upload" as multipart field "file"` | Upload file |

### Polling & Retry

| Step | Example | Description |
|------|---------|-------------|
| `When user polls {string} until json path {string} equals {string} within {int} seconds interval {int} seconds` | `When user polls "/status" until json path "state" equals "completed" within 30 seconds interval 5 seconds` | Poll endpoint |
| `When user retries last request up to {int} times with interval {int} seconds until status {int}` | `When user retries last request up to 3 times with interval 2 seconds until status 200` | Retry request |

### SOAP Support

| Step | Example | Description |
|------|---------|-------------|
| `When user creates SOAP envelope with action {string} and body:` | `When user creates SOAP envelope with action "GetUser" and body:`<br>`"""<userId>1</userId>"""` | Create SOAP envelope |
| `When user sends SOAP request to {string} with action {string}` | `When user sends SOAP request to "/soap" with action "GetUser"` | Send SOAP request |
| `Then SOAP response should have fault` | `Then SOAP response should have fault` | Assert SOAP fault |
| `Then SOAP response should not have fault` | `Then SOAP response should not have fault` | Assert no fault |
| `Then SOAP xpath {string} should be {string}` | `Then SOAP xpath "//userId" should be "1"` | Assert SOAP XPath |

### Utilities

| Step | Example | Description |
|------|---------|-------------|
| `When user logs response` | `When user logs response` | Log full response |
| `When user applies headers` | `When user applies headers` | Apply set headers |
| `When user clears request spec and headers` | `When user clears request spec and headers` | Clear request state |

---

## Shared Steps

These steps work across both UI and API tests.

| Step | Example | Description |
|------|---------|-------------|
| `Then saved variable {string} should be {string}` | `Then saved variable "userId" should be "123"` | Assert saved variable |
| `Then saved variable {string} should not be null` | `Then saved variable "token" should not be null` | Assert variable not null |

---

## Variable Substitution

All steps support variable substitution using `${variableName}` syntax:

```gherkin
# Save a value
When user saves json path "id" as "userId"

# Use it later
When user sends GET request to "/users/${userId}"
Then element "#user-${userId}" should be visible
```

## Tips

1. **Chain Steps**: Combine steps for complex workflows
2. **Use Variables**: Save and reuse data across steps
3. **Tag Scenarios**: Use `@UI`, `@API` tags for organization
4. **Parallel Execution**: All steps are thread-safe
5. **Custom Steps**: Add project-specific steps alongside framework steps

## Example Feature Files

### UI Example
```gherkin
@UI
Feature: Login Test
  Scenario: Successful login
    Given open browser
    And navigate to "https://example.com/login"
    When type "user@test.com" into "#email"
    And type "password123" into "#password"
    And click element "#loginBtn"
    Then element "#dashboard" should be visible
    And page title should contain "Dashboard"
```

### API Example
```gherkin
@API
Feature: User API Test
  Scenario: Create and verify user
    Given user switches to "userService" API service
    When user sets request body:
      """
      {"name":"John","email":"john@test.com"}
      """
    And user sends POST request to "/users"
    Then response status code should be 201
    And json path "name" should be "John"
    When user saves json path "id" as "userId"
    And user sends GET request to "/users/${userId}"
    Then response status code should be 200
```

---

**Framework Version**: 1.0.0  
**Last Updated**: 2025-01-21
