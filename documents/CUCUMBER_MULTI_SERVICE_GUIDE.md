# Cucumber Multi-Service API Testing Guide

## Overview
APIReusable and APISteps now support testing multiple API services with different base URLs in a single test scenario.

## What Changed?

### APIReusable.java
- Added `currentService` field to track active service
- Updated all HTTP methods to use service-specific base URLs
- Added `switchToService(String serviceName)` method
- Added `useCustomBaseUrl(String customUrl)` method

### APISteps.java
- Added new step definitions for service switching
- All existing steps work with service context

## New Cucumber Steps

### Service Switching
```gherkin
Given user switches to "auth" API service
Given user switches to "payment" API service
Given user uses custom base URL "https://external-api.com"
```

### Service-Specific Requests
```gherkin
When user sends GET request to "auth" service endpoint "/login"
When user sends POST request to "user" service endpoint "/profile"
When user sends "PUT" request to "payment" service endpoint "/balance"
```

## Usage Examples

### Example 1: Simple Service Switch
```gherkin
Scenario: Test multiple services
  Given user switches to "auth" API service
  When user sends POST request to "/login"
  Then response status code should be 200
  
  Given user switches to "user" API service
  When user sends GET request to "/profile"
  Then response status code should be 200
```

### Example 2: Service-Specific Endpoint (One-liner)
```gherkin
Scenario: Quick service test
  When user sends GET request to "auth" service endpoint "/validate"
  Then response status code should be 200
  
  When user sends GET request to "payment" service endpoint "/balance"
  Then response status code should be 200
```

### Example 3: Custom Base URL
```gherkin
Scenario: Test external API
  Given user uses custom base URL "https://api.github.com"
  When user sends GET request to "/users/octocat"
  Then response status code should be 200
```

### Example 4: Multi-Service Workflow with Variables
```gherkin
Scenario: Complete user journey
  # Login to auth service
  Given user switches to "auth" API service
  When user creates POST request body with title "login" and body "credentials"
  And user sends POST request to "/authenticate"
  Then response status code should be 200
  And user saves json path "token" as "authToken"
  
  # Get user data
  Given user switches to "user" API service
  When user sets header "Authorization" as "Bearer ${authToken}"
  And user sends GET request to "/profile"
  Then response status code should be 200
  And user saves json path "userId" as "userId"
  
  # Check payment balance
  Given user switches to "payment" API service
  When user sets header "Authorization" as "Bearer ${authToken}"
  And user sends GET request to "/balance/${userId}"
  Then response status code should be 200
  And json path "balance" should be greater than 0
```

### Example 5: Backward Compatible (Default Service)
```gherkin
Scenario: Use default API service
  # No service switch - uses api.base.url from properties
  When user sends GET request to "/posts/1"
  Then response status code should be 200
  And response "id" should be "1"
```

## Configuration Required

### qa.properties
```properties
# Default API (backward compatible)
api.base.url=https://jsonplaceholder.typicode.com

# Named Services
api.base.url.auth=https://auth-api-qa.example.com
api.base.url.user=https://user-api-qa.example.com
api.base.url.payment=https://payment-api-qa.example.com
api.base.url.order=https://order-api-qa.example.com
```

### prod.properties
```properties
# Default API
api.base.url=https://jsonplaceholder.typicode.com

# Named Services
api.base.url.auth=https://auth-api-prod.example.com
api.base.url.user=https://user-api-prod.example.com
api.base.url.payment=https://payment-api-prod.example.com
api.base.url.order=https://order-api-prod.example.com
```

## All Available Steps

### Service Management
| Step | Description |
|------|-------------|
| `Given user switches to "{service}" API service` | Switch to named service |
| `Given user uses custom base URL "{url}"` | Use ad-hoc custom URL |

### Service-Specific Requests
| Step | Description |
|------|-------------|
| `When user sends GET request to "{service}" service endpoint "{endpoint}"` | GET to specific service |
| `When user sends POST request to "{service}" service endpoint "{endpoint}"` | POST to specific service |
| `When user sends "{method}" request to "{service}" service endpoint "{endpoint}"` | Any method to specific service |

### Standard Requests (Use Current Service Context)
| Step | Description |
|------|-------------|
| `When user sends GET request to "{endpoint}"` | GET request |
| `When user sends POST request to "{endpoint}"` | POST request |
| `When user sends PUT request to "{endpoint}"` | PUT request |
| `When user sends DELETE request to "{endpoint}"` | DELETE request |
| `When user sends "{method}" request to "{endpoint}"` | Generic HTTP method |

All other existing steps (validations, headers, query params, etc.) work as before!

## Migration Guide

### Old Code (Still Works)
```gherkin
Scenario: Old style test
  When user sends GET request to "/posts/1"
  Then response status code should be 200
```

### New Code (Multi-Service)
```gherkin
Scenario: New style test
  Given user switches to "auth" API service
  When user sends POST request to "/login"
  Then response status code should be 200
  
  Given user switches to "user" API service
  When user sends GET request to "/profile"
  Then response status code should be 200
```

## Best Practices

1. **Switch Once**: Switch to a service once, then make multiple requests
   ```gherkin
   Given user switches to "auth" API service
   When user sends POST request to "/login"
   And user sends GET request to "/validate"
   And user sends POST request to "/refresh"
   ```

2. **Use Service-Specific Steps for One-Off Requests**:
   ```gherkin
   When user sends GET request to "auth" service endpoint "/health"
   When user sends GET request to "payment" service endpoint "/health"
   ```

3. **Save and Reuse Tokens**:
   ```gherkin
   Given user switches to "auth" API service
   When user sends POST request to "/login"
   And user saves json path "token" as "authToken"
   
   Given user switches to "user" API service
   When user sets header "Authorization" as "Bearer ${authToken}"
   And user sends GET request to "/profile"
   ```

4. **Use Descriptive Service Names**: Match your microservices architecture
   - auth, user, payment, order, inventory, notification, etc.

## Thread Safety

All service switching is thread-safe for parallel test execution. Each thread maintains its own service context.

## Error Handling

If a service is not configured in properties:
```
RuntimeException: API base URL not found for service: invalidService. 
Expected property: api.base.url.invalidService
```

Make sure all services are defined in your environment properties files!
