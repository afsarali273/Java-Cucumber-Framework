# API Testing with External JSON/XML Files

## Overview

The framework supports loading request bodies from external JSON and XML files with automatic variable replacement. This enables:

- ✅ Reusable request templates
- ✅ Separation of test data from test logic
- ✅ Dynamic variable substitution using `${variableName}` syntax
- ✅ Support for both REST (JSON/XML) and SOAP requests
- ✅ Nested variable replacement in complex JSON structures

## File Locations

The framework searches for files in the following order:

1. **Absolute path**: `/absolute/path/to/file.json`
2. **testData folder**: `src/test/resources/testData/`
3. **Classpath resources**: Any resource on the classpath

### Recommended Structure

```
src/test/resources/testData/
├── rest/
│   ├── createUser.json
│   ├── updateUser.json
│   ├── createPost.json
│   └── createProduct.xml
└── soap/
    ├── getUserRequest.xml
    ├── createOrderRequest.xml
    └── updateCustomerRequest.xml
```

## Step Definitions

### 1. Load Request Body from File

```gherkin
When user sets request body from file "testData/rest/createUser.json"
When user sets request body from file "/absolute/path/to/request.json"
```

### 2. Load JSON File (Auto-searches testData folder)

```gherkin
When user sets request body from json file "rest/createUser.json"
When user sets request body from json file "createPost.json"
```

### 3. Load XML File

```gherkin
When user sets request body from xml file "testData/rest/createProduct.xml"
```

### 4. Load SOAP Envelope

```gherkin
When user sets SOAP envelope from file "testData/soap/getUserRequest.xml"
```

### 5. Send Request Directly from File

```gherkin
# POST request
When user sends POST request from file "rest/createUser.json" to "/users"

# PUT request
When user sends PUT request from file "rest/updateUser.json" to "/users/${userId}"

# SOAP request
When user sends SOAP request from file "soap/getUserRequest.xml" to "/soap/userService"
```

### 6. Send with Custom Content Type

```gherkin
When user sends POST request from file "rest/data.xml" to "/api/xml" with content type "application/xml"
```

## Variable Replacement

### How It Works

1. Save variables to ScenarioContext
2. Use `${variableName}` syntax in JSON/XML files
3. Framework automatically replaces variables when loading files

### Example

**Step 1: Save Variables**
```gherkin
When user saves json path "John Doe" as "userName"
And user saves json path "john@example.com" as "userEmail"
And user saves json path "30" as "userAge"
```

**Step 2: JSON File (createUser.json)**
```json
{
  "name": "${userName}",
  "email": "${userEmail}",
  "age": ${userAge},
  "active": true
}
```

**Step 3: Load and Send**
```gherkin
When user sends POST request from file "rest/createUser.json" to "/users"
```

**Result: Variables Replaced**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "age": 30,
  "active": true
}
```

## Complete Examples

### Example 1: REST API with JSON File

**Feature File:**
```gherkin
Scenario: Create user from JSON file
  # Set variables
  When user saves json path "Jane Smith" as "userName"
  And user saves json path "jane@test.com" as "userEmail"
  And user saves json path "25" as "userAge"
  And user saves json path "123 Main St" as "street"
  And user saves json path "New York" as "city"
  And user saves json path "10001" as "zipCode"
  
  # Send request from file
  When user sends POST request from file "rest/createUser.json" to "/users"
  
  Then response status code should be 201
  And json path "name" should be "Jane Smith"
```

**JSON File (testData/rest/createUser.json):**
```json
{
  "name": "${userName}",
  "email": "${userEmail}",
  "age": ${userAge},
  "active": true,
  "address": {
    "street": "${street}",
    "city": "${city}",
    "zipCode": "${zipCode}"
  },
  "roles": ["user", "admin"]
}
```

### Example 2: SOAP Request with XML File

**Feature File:**
```gherkin
Scenario: Get user via SOAP
  # Set variables
  When user saves json path "Bearer abc123" as "authToken"
  And user saves json path "12345" as "userId"
  
  # Send SOAP request from file
  When user sends SOAP request from file "soap/getUserRequest.xml" to "/soap/userService"
  
  Then response status code should be 200
  And SOAP response should not have fault
```

**XML File (testData/soap/getUserRequest.xml):**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:usr="http://example.com/user">
   <soapenv:Header>
      <usr:AuthToken>${authToken}</usr:AuthToken>
   </soapenv:Header>
   <soapenv:Body>
      <usr:GetUserRequest>
         <usr:UserId>${userId}</usr:UserId>
         <usr:IncludeDetails>true</usr:IncludeDetails>
      </usr:GetUserRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

### Example 3: Complete Workflow with Files

```gherkin
Scenario: Complete user and post workflow
  # Step 1: Get user data
  When user sends GET request to "/users/1"
  Then response status code should be 200
  When user saves json path "id" as "userId"
  And user saves json path "name" as "userName"
  
  # Step 2: Create post using file
  And user saves json path "Post by ${userName}" as "postTitle"
  And user saves json path "Content here" as "postBody"
  And user saves json path "test" as "tag1"
  And user saves json path "demo" as "tag2"
  And user saves json path "2025-01-21T10:00:00Z" as "timestamp"
  And user sends POST request from file "rest/createPost.json" to "/posts"
  
  Then response status code should be 201
  When user saves json path "id" as "postId"
  
  # Step 3: Update post using file
  And user saves json path "Updated Title" as "postTitle"
  And user sends PUT request from file "rest/createPost.json" to "/posts/${postId}"
  
  Then response status code should be 200
  And json path "title" should be "Updated Title"
  
  # Step 4: Delete post
  When user sends DELETE request to "/posts/${postId}"
  Then response status code should be 200
```

### Example 4: Complex SOAP Order with Multiple Variables

**Feature File:**
```gherkin
Scenario: Create order via SOAP
  # Set order variables
  When user saves json path "CUST-001" as "customerId"
  And user saves json path "2025-01-21" as "orderDate"
  And user saves json path "PROD-101" as "productId1"
  And user saves json path "2" as "quantity1"
  And user saves json path "29.99" as "price1"
  And user saves json path "PROD-202" as "productId2"
  And user saves json path "1" as "quantity2"
  And user saves json path "49.99" as "price2"
  And user saves json path "109.97" as "totalAmount"
  And user saves json path "456 Oak Ave" as "shippingStreet"
  And user saves json path "Los Angeles" as "shippingCity"
  And user saves json path "90001" as "shippingZip"
  
  # Send SOAP request
  When user sends SOAP request from file "soap/createOrderRequest.xml" to "/soap/orderService"
  
  Then response status code should be 200
  And SOAP response should not have fault
```

**XML File (testData/soap/createOrderRequest.xml):**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:ord="http://example.com/order">
   <soapenv:Body>
      <ord:CreateOrderRequest>
         <ord:CustomerId>${customerId}</ord:CustomerId>
         <ord:OrderDate>${orderDate}</ord:OrderDate>
         <ord:Items>
            <ord:Item>
               <ord:ProductId>${productId1}</ord:ProductId>
               <ord:Quantity>${quantity1}</ord:Quantity>
               <ord:Price>${price1}</ord:Price>
            </ord:Item>
            <ord:Item>
               <ord:ProductId>${productId2}</ord:ProductId>
               <ord:Quantity>${quantity2}</ord:Quantity>
               <ord:Price>${price2}</ord:Price>
            </ord:Item>
         </ord:Items>
         <ord:TotalAmount>${totalAmount}</ord:TotalAmount>
         <ord:ShippingAddress>
            <ord:Street>${shippingStreet}</ord:Street>
            <ord:City>${shippingCity}</ord:City>
            <ord:ZipCode>${shippingZip}</ord:ZipCode>
         </ord:ShippingAddress>
      </ord:CreateOrderRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

## Advanced Features

### 1. Nested Variable Replacement

Variables work in nested JSON structures:

```json
{
  "user": {
    "name": "${userName}",
    "contact": {
      "email": "${userEmail}",
      "phone": "${userPhone}"
    }
  }
}
```

### 2. Arrays with Variables

```json
{
  "tags": ["${tag1}", "${tag2}", "${tag3}"],
  "items": [
    {
      "id": "${itemId1}",
      "name": "${itemName1}"
    },
    {
      "id": "${itemId2}",
      "name": "${itemName2}"
    }
  ]
}
```

### 3. Dynamic Endpoints

Use variables in endpoint URLs:

```gherkin
When user sends PUT request from file "updateUser.json" to "/users/${userId}"
When user sends DELETE request to "/posts/${postId}/comments/${commentId}"
```

### 4. Inline JSON with Variables

You can still use inline JSON with variable replacement:

```gherkin
When user sets request body:
  """
  {
    "title": "${postTitle}",
    "userId": ${userId}
  }
  """
```

## Best Practices

### 1. Organize Files by Domain

```
testData/
├── users/
│   ├── createUser.json
│   ├── updateUser.json
│   └── deleteUser.json
├── posts/
│   ├── createPost.json
│   └── updatePost.json
└── orders/
    ├── createOrder.json
    └── cancelOrder.json
```

### 2. Use Descriptive File Names

✅ Good:
- `createUserWithAddress.json`
- `updatePostWithTags.json`
- `soapGetCustomerDetails.xml`

❌ Bad:
- `request1.json`
- `test.xml`
- `data.json`

### 3. Document Variables in Files

Add comments (for XML) or use descriptive keys:

```json
{
  "_comment": "Variables: userName, userEmail, userAge, street, city, zipCode",
  "name": "${userName}",
  "email": "${userEmail}"
}
```

### 4. Validate Variable Replacement

Always verify variables were replaced:

```gherkin
When user sends POST request from file "createUser.json" to "/users"
Then response status code should be 201
And response body should contain "${userName}"
And json path "email" should be "${userEmail}"
```

### 5. Reuse Templates

Create generic templates and reuse with different variables:

```gherkin
# Test 1
When user saves json path "User One" as "userName"
And user sends POST request from file "createUser.json" to "/users"

# Test 2
When user saves json path "User Two" as "userName"
And user sends POST request from file "createUser.json" to "/users"
```

## Troubleshooting

### Issue: File Not Found

**Error:** `Failed to load file: createUser.json`

**Solutions:**
1. Check file exists in `src/test/resources/testData/`
2. Use correct path: `rest/createUser.json` or `testData/rest/createUser.json`
3. Verify file extension: `.json` or `.xml`

### Issue: Variables Not Replaced

**Error:** Response contains `${userName}` instead of actual value

**Solutions:**
1. Ensure variable is saved before loading file:
   ```gherkin
   When user saves json path "John" as "userName"  # Must come first
   And user sends POST request from file "createUser.json" to "/users"
   ```
2. Check variable name matches exactly (case-sensitive)
3. Verify variable exists in ScenarioContext

### Issue: Invalid JSON After Replacement

**Error:** `JsonSyntaxException` or `Invalid JSON`

**Solutions:**
1. For string values, use quotes: `"name": "${userName}"`
2. For numbers, no quotes: `"age": ${userAge}`
3. For booleans, no quotes: `"active": ${isActive}`

### Issue: SOAP Request Fails

**Error:** SOAP fault or 500 error

**Solutions:**
1. Verify XML structure is valid
2. Check namespace declarations
3. Ensure all required SOAP elements are present
4. Validate variable replacement in XML

## Summary

### Key Benefits

✅ **Reusability** - One template, multiple tests
✅ **Maintainability** - Update template once, affects all tests
✅ **Readability** - Cleaner feature files
✅ **Flexibility** - Support for REST and SOAP
✅ **Dynamic** - Variable replacement at runtime

### Supported Formats

- ✅ JSON (REST APIs)
- ✅ XML (REST APIs)
- ✅ SOAP Envelopes
- ✅ Plain text

### Variable Replacement

- ✅ Simple values: `${variableName}`
- ✅ Nested objects: `${user.name}`
- ✅ Arrays: `["${tag1}", "${tag2}"]`
- ✅ Numbers: `${userId}` (no quotes)
- ✅ Strings: `"${userName}"` (with quotes)
- ✅ Dynamic endpoints: `/users/${userId}`

---

**Last Updated:** 2025-01-21  
**Author:** Afsar Ali  
**Framework Version:** 1.0
