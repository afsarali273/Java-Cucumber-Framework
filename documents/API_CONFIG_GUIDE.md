# API Configuration Guide

## Overview
ConfigManager now includes dedicated API configuration support with APIConfig DTO and global static variables for API testing.

## Configuration Files

### config.properties (Global API Settings)
```properties
# API Configuration
api.content.type=application/json
api.log.request=true
api.log.response=true
```

### qa.properties / prod.properties (Environment-Specific)
```properties
# Default API Base URL
api.base.url=https://jsonplaceholder.typicode.com

# Named Service Base URLs
api.base.url.auth=https://auth-api-qa.example.com
api.base.url.payment=https://payment-api-qa.example.com
api.base.url.user=https://user-api-qa.example.com

# API Timeouts and Retry
api.timeout=30
max.retry.count=2
```

## Global API Variables

```java
ConfigManager.API_BASE_URL          // Default API base URL
ConfigManager.API_TIMEOUT           // API timeout in seconds
ConfigManager.API_CONTENT_TYPE      // Default content type
ConfigManager.API_LOG_REQUEST       // Enable request logging
ConfigManager.API_LOG_RESPONSE      // Enable response logging
ConfigManager.MAX_RETRY_COUNT       // Max retry attempts
```

## APIConfig DTO

```java
APIConfig apiConfig = ConfigManager.getInstance().getAPIConfig();

String baseUrl = apiConfig.getBaseUrl();
int timeout = apiConfig.getTimeout();
int maxRetry = apiConfig.getMaxRetryCount();
String contentType = apiConfig.getContentType();
boolean logRequest = apiConfig.isLogRequest();
boolean logResponse = apiConfig.isLogResponse();
```

## Usage Examples

### Example 1: Using Global Variables
```java
public class APIHelper {
    public void setupClient() {
        RestAssured.baseURI = ConfigManager.API_BASE_URL;
        RestAssured.requestSpecification = given()
            .contentType(ConfigManager.API_CONTENT_TYPE)
            .timeout(ConfigManager.API_TIMEOUT, TimeUnit.SECONDS);
        
        if (ConfigManager.API_LOG_REQUEST) {
            RestAssured.filters(new RequestLoggingFilter());
        }
        
        if (ConfigManager.API_LOG_RESPONSE) {
            RestAssured.filters(new ResponseLoggingFilter());
        }
    }
}
```

### Example 2: Using APIConfig DTO
```java
public class APIClient {
    private APIConfig config;
    
    public APIClient() {
        this.config = ConfigManager.getInstance().getAPIConfig();
        initialize();
    }
    
    private void initialize() {
        RestAssured.baseURI = config.getBaseUrl();
        
        RequestSpecification spec = given()
            .contentType(config.getContentType())
            .timeout(config.getTimeout(), TimeUnit.SECONDS);
        
        if (config.isLogRequest()) {
            spec.filter(new RequestLoggingFilter());
        }
        
        if (config.isLogResponse()) {
            spec.filter(new ResponseLoggingFilter());
        }
    }
}
```

### Example 3: Retry Logic with Config
```java
public class APIReusable {
    public Response sendWithRetry(String endpoint) {
        int maxRetries = ConfigManager.MAX_RETRY_COUNT;
        
        for (int i = 0; i < maxRetries; i++) {
            try {
                return given()
                    .baseUri(ConfigManager.API_BASE_URL)
                    .contentType(ConfigManager.API_CONTENT_TYPE)
                    .get(endpoint);
            } catch (Exception e) {
                if (i == maxRetries - 1) throw e;
                LogManager.warn("Retry attempt " + (i + 1) + " of " + maxRetries);
            }
        }
        return null;
    }
}
```

### Example 4: Conditional Logging
```java
public class APILogger {
    public void logRequest(Request request) {
        if (ConfigManager.API_LOG_REQUEST) {
            LogManager.info("Request: " + request.getMethod() + " " + request.getURI());
            LogManager.info("Headers: " + request.getHeaders());
            LogManager.info("Body: " + request.getBody());
        }
    }
    
    public void logResponse(Response response) {
        if (ConfigManager.API_LOG_RESPONSE) {
            LogManager.info("Status: " + response.getStatusCode());
            LogManager.info("Body: " + response.getBody().asString());
        }
    }
}
```

### Example 5: Multi-Service Configuration
```java
public class ServiceClient {
    public Response callAuthService(String endpoint) {
        String authUrl = ConfigManager.getInstance().getApiBaseUrl("auth");
        
        return given()
            .baseUri(authUrl)
            .contentType(ConfigManager.API_CONTENT_TYPE)
            .timeout(ConfigManager.API_TIMEOUT, TimeUnit.SECONDS)
            .get(endpoint);
    }
    
    public Response callPaymentService(String endpoint) {
        String paymentUrl = ConfigManager.getInstance().getApiBaseUrl("payment");
        
        return given()
            .baseUri(paymentUrl)
            .contentType(ConfigManager.API_CONTENT_TYPE)
            .timeout(ConfigManager.API_TIMEOUT, TimeUnit.SECONDS)
            .get(endpoint);
    }
}
```

### Example 6: Environment-Specific API Testing
```java
public class APITestBase {
    protected APIConfig apiConfig;
    
    @BeforeClass
    public void setup() {
        apiConfig = ConfigManager.getInstance().getAPIConfig();
        
        LogManager.info("API Test Configuration:");
        LogManager.info("Environment: " + ConfigManager.ENVIRONMENT);
        LogManager.info("Base URL: " + apiConfig.getBaseUrl());
        LogManager.info("Timeout: " + apiConfig.getTimeout() + "s");
        LogManager.info("Max Retry: " + apiConfig.getMaxRetryCount());
        
        RestAssured.baseURI = apiConfig.getBaseUrl();
    }
}
```

## Complete Configuration Example

### config.properties
```properties
# Framework
framework.type=selenium
browser=chrome
headless=false
environment=qa

# API Global Settings
api.content.type=application/json
api.log.request=true
api.log.response=true
```

### qa.properties
```properties
# Application URL
app.url=https://www.flipkart.com

# API Configuration
api.base.url=https://jsonplaceholder.typicode.com
api.base.url.auth=https://auth-api-qa.example.com
api.base.url.payment=https://payment-api-qa.example.com
api.base.url.user=https://user-api-qa.example.com
api.timeout=30

# Retry Configuration
max.retry.count=3
wait.timeout=15
```

### prod.properties
```properties
# Application URL
app.url=https://www.flipkart.com

# API Configuration
api.base.url=https://jsonplaceholder.typicode.com
api.base.url.auth=https://auth-api-prod.example.com
api.base.url.payment=https://payment-api-prod.example.com
api.base.url.user=https://user-api-prod.example.com
api.timeout=60

# Retry Configuration
max.retry.count=5
wait.timeout=20
```

## API Configuration Properties

| Property | Type | Default | Description |
|----------|------|---------|-------------|
| `api.base.url` | String | - | Default API base URL |
| `api.base.url.<service>` | String | - | Service-specific base URL |
| `api.timeout` | int | 30 | API timeout in seconds |
| `api.content.type` | String | application/json | Default content type |
| `api.log.request` | boolean | true | Enable request logging |
| `api.log.response` | boolean | true | Enable response logging |
| `max.retry.count` | int | 2 | Max retry attempts |

## Global Variables Reference

| Variable | Type | Description |
|----------|------|-------------|
| `API_BASE_URL` | String | Default API base URL |
| `API_TIMEOUT` | int | API timeout in seconds |
| `API_CONTENT_TYPE` | String | Default content type |
| `API_LOG_REQUEST` | boolean | Request logging enabled |
| `API_LOG_RESPONSE` | boolean | Response logging enabled |
| `MAX_RETRY_COUNT` | int | Max retry attempts |

## APIConfig DTO Methods

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getBaseUrl()` | String | Get base URL |
| `getTimeout()` | int | Get timeout |
| `getMaxRetryCount()` | int | Get max retry count |
| `getContentType()` | String | Get content type |
| `isLogRequest()` | boolean | Check if request logging enabled |
| `isLogResponse()` | boolean | Check if response logging enabled |
| `toString()` | String | Get formatted config string |

## Best Practices

1. **Use Global Variables for Simple Access**
   ```java
   RestAssured.baseURI = ConfigManager.API_BASE_URL;
   ```

2. **Use DTO for Passing Config**
   ```java
   public void initClient(APIConfig config) {
       // Use config
   }
   ```

3. **Environment-Specific URLs**
   ```properties
   # qa.properties
   api.base.url.auth=https://auth-api-qa.example.com
   
   # prod.properties
   api.base.url.auth=https://auth-api-prod.example.com
   ```

4. **Conditional Logging**
   ```java
   if (ConfigManager.API_LOG_REQUEST) {
       logRequest(request);
   }
   ```

5. **Service-Specific Configuration**
   ```java
   String authUrl = ConfigManager.getInstance().getApiBaseUrl("auth");
   String paymentUrl = ConfigManager.getInstance().getApiBaseUrl("payment");
   ```

## Summary

✅ **APIConfig DTO** - Dedicated API configuration object  
✅ **Global Variables** - Easy access to API settings  
✅ **Multi-Service Support** - Named service URLs  
✅ **Environment-Specific** - Different configs per environment  
✅ **Logging Control** - Enable/disable request/response logging  
✅ **Retry Configuration** - Configurable retry logic  
✅ **Backward Compatible** - All existing code works  

Your API testing framework now has comprehensive configuration support! 🚀
