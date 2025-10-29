# Multi Base URI Support - Usage Guide

## Overview
APIClient now supports multiple base URIs for different API services, allowing you to test multiple APIs in a single test suite.

## Configuration

### Environment Properties (qa.properties / prod.properties)

```properties
# Default API Base URL (backward compatible)
api.base.url=https://jsonplaceholder.typicode.com

# Named Service Base URLs
api.base.url.auth=https://auth-api-qa.example.com
api.base.url.payment=https://payment-api-qa.example.com
api.base.url.user=https://user-api-qa.example.com
api.base.url.order=https://order-api-qa.example.com
```

## Usage Examples

### 1. Default Base URL (Backward Compatible)
```java
// Uses api.base.url from properties
Response response = APIClient.get("/posts/1");
AssertUtils.assertEquals(response.getStatusCode(), 200, "Status check");
```

### 2. Named Service Base URL
```java
// Switch to auth service
APIClient.withBaseUrl("auth").postRequest("/login", loginBody);

// Switch to payment service
Response paymentResponse = APIClient.withBaseUrl("payment").getRequest("/transactions");

// Switch to user service
APIClient.withBaseUrl("user").putRequest("/profile", profileData);

// Switch to order service
APIClient.withBaseUrl("order").deleteRequest("/orders/123");
```

### 3. Named Service with Custom Headers
```java
Map<String, String> headers = new HashMap<>();
headers.put("Authorization", "Bearer token123");
headers.put("X-Request-ID", "req-001");

APIClient.withBaseUrl("auth", headers).postRequest("/validate", tokenData);
```

### 4. Custom Base URL (Ad-hoc)
```java
// Use a completely custom URL not in properties
APIClient.withCustomBaseUrl("https://external-api.com").getRequest("/data");
```

### 5. Multiple Services in Same Test
```java
@Test
public void testMultipleServices() {
    // Test auth service
    Response loginResp = APIClient.withBaseUrl("auth").postRequest("/login", credentials);
    String token = loginResp.jsonPath().getString("token");
    
    // Test user service with token
    Map<String, String> headers = new HashMap<>();
    headers.put("Authorization", "Bearer " + token);
    Response userResp = APIClient.withBaseUrl("user", headers).getRequest("/profile");
    
    // Test payment service
    Response paymentResp = APIClient.withBaseUrl("payment").getRequest("/balance");
    
    // Assertions
    AssertUtils.assertEquals(loginResp.getStatusCode(), 200, "Login successful");
    AssertUtils.assertEquals(userResp.getStatusCode(), 200, "User profile retrieved");
    AssertUtils.assertEquals(paymentResp.getStatusCode(), 200, "Payment balance retrieved");
}
```

### 6. Static Methods (Original Approach)
```java
// Still works - uses default base URL
Response response = APIClient.get("/posts");
APIClient.post("/posts", postData);
APIClient.put("/posts/1", updateData);
APIClient.delete("/posts/1");
```

## Step Definition Example

```java
public class APIStepDefinitions {
    private Response response;
    
    @When("user authenticates with {string} service")
    public void authenticateWithService(String service) {
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("username", "testuser");
        credentials.put("password", "testpass");
        
        response = APIClient.withBaseUrl(service).postRequest("/login", credentials);
    }
    
    @When("user calls {string} service endpoint {string}")
    public void callServiceEndpoint(String service, String endpoint) {
        response = APIClient.withBaseUrl(service).getRequest(endpoint);
    }
    
    @Then("response status should be {int}")
    public void verifyResponseStatus(int expectedStatus) {
        AssertUtils.assertEquals(response.getStatusCode(), expectedStatus, "Status code validation");
    }
}
```

## Feature File Example

```gherkin
@API
Feature: Multi-Service API Testing

  Scenario: Test multiple API services
    When user authenticates with "auth" service
    Then response status should be 200
    
    When user calls "user" service endpoint "/profile"
    Then response status should be 200
    
    When user calls "payment" service endpoint "/transactions"
    Then response status should be 200
```

## Best Practices

1. **Use Named Services**: Define all your API services in properties files for consistency
2. **Service Naming**: Use descriptive service names (auth, payment, user, order)
3. **Environment Specific**: Configure different URLs per environment (qa.properties, prod.properties)
4. **Thread Safety**: All methods are thread-safe for parallel execution
5. **Logging**: All requests are automatically logged with full URLs

## Migration from Old Code

Old code continues to work without changes:
```java
// Old code - still works
Response response = APIClient.get("/posts/1");
```

New code with multiple services:
```java
// New code - multiple services
Response authResp = APIClient.withBaseUrl("auth").getRequest("/login");
Response userResp = APIClient.withBaseUrl("user").getRequest("/profile");
```

## Error Handling

If a service name is not configured:
```java
// Throws RuntimeException with clear message
APIClient.withBaseUrl("nonexistent").getRequest("/test");
// Error: API base URL not found for service: nonexistent. Expected property: api.base.url.nonexistent
```
