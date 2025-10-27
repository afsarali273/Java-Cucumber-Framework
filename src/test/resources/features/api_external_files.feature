@API
Feature: API Testing with External JSON/XML Files

  Background:
    Given user switches to "jsonplaceholder" API service

  Scenario: Create post using inline JSON with variables
    When user saves json path "1" as "userId"
    And user sets request body:
      """
      {
        "title": "New Post",
        "body": "Post body content",
        "userId": ${userId}
      }
      """
    And user sends POST request to "/posts"
    Then response status code should be 201
    And json path "title" should be "New Post"
    And json path "userId" should be "1"

  Scenario: Create post using external JSON file with variable replacement
    # Save variables first
    When user saves json path "5" as "userId"
    And user saves json path "My Post Title" as "postTitle"
    And user saves json path "This is the post content" as "postBody"
    And user saves json path "technology" as "tag1"
    And user saves json path "automation" as "tag2"
    And user saves json path "2025-01-21T10:30:00Z" as "timestamp"
    
    # Load JSON from file (variables will be replaced)
    And user sets request body from json file "rest/createPost.json"
    And user sends POST request to "/posts"
    
    Then response status code should be 201
    And json path "title" should be "My Post Title"
    And json path "userId" should be "5"

  Scenario: Create user using external JSON file
    # Set variables
    When user saves json path "John Doe" as "userName"
    And user saves json path "john.doe@example.com" as "userEmail"
    And user saves json path "30" as "userAge"
    And user saves json path "123 Main St" as "street"
    And user saves json path "New York" as "city"
    And user saves json path "10001" as "zipCode"
    
    # Send POST request directly from file
    When user sends POST request from file "rest/createUser.json" to "/users"
    
    Then response status code should be 201

  Scenario: Update post using external JSON file with dynamic endpoint
    # Get existing post
    When user sends GET request to "/posts/1"
    Then response status code should be 200
    When user saves json path "userId" as "userId"
    And user saves json path "id" as "postId"
    
    # Set update data
    And user saves json path "Updated Title" as "postTitle"
    And user saves json path "Updated Content" as "postBody"
    And user saves json path "updated" as "tag1"
    And user saves json path "test" as "tag2"
    And user saves json path "2025-01-21T11:00:00Z" as "timestamp"
    
    # Update using file with variable in endpoint
    When user sends PUT request from file "rest/createPost.json" to "/posts/${postId}"
    
    Then response status code should be 200
    And json path "title" should be "Updated Title"

  Scenario: Load request body from different file locations
    # Test loading from testData folder (without prefix)
    When user sets request body from json file "rest/createPost.json"
    Then saved variable "postTitle" should not be null
    
    # Test loading from testData folder (with prefix)
    When user sets request body from file "testData/rest/createUser.json"
    Then saved variable "userName" should not be null
    
    # Test loading from classpath
    When user sets request body from file "rest/createPost.json"
    Then saved variable "postBody" should not be null

  Scenario: SOAP request using external XML file
    # Set SOAP variables
    When user saves json path "Bearer abc123xyz" as "authToken"
    And user saves json path "12345" as "userId"
    
    # Load and send SOAP request from file
    When user sends SOAP request from file "soap/getUserRequest.xml" to "/soap/userService"
    
    Then response status code should be 200

  Scenario: SOAP order creation with multiple variables
    # Set all order variables
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
    
    # Send SOAP request from file
    When user sets SOAP envelope from file "soap/createOrderRequest.xml"
    And user sends SOAP request to "/soap/orderService" with action "CreateOrder"
    
    Then response status code should be 200
    And SOAP response should not have fault

  Scenario: XML REST API with external file
    # Set XML data variables
    When user saves json path "Product Name" as "productName"
    And user saves json path "99.99" as "productPrice"
    And user saves json path "Electronics" as "category"
    
    # Send XML request
    When user sends POST request from file "testData/rest/createProduct.xml" to "/api/products" with content type "application/xml"
    
    Then response status code should be 201
    And response content type should be "application/xml"

  Scenario: Complete workflow with file-based requests
    # Step 1: Get user data
    When user sends GET request to "/users/1"
    Then response status code should be 200
    When user saves json path "id" as "userId"
    And user saves json path "name" as "userName"
    
    # Step 2: Create post using file
    And user saves json path "Post by ${userName}" as "postTitle"
    And user saves json path "Content from user workflow" as "postBody"
    And user saves json path "workflow" as "tag1"
    And user saves json path "test" as "tag2"
    And user saves json path "2025-01-21T12:00:00Z" as "timestamp"
    And user sends POST request from file "rest/createPost.json" to "/posts"
    
    Then response status code should be 201
    And json path "userId" should be "${userId}"
    When user saves json path "id" as "postId"
    
    # Step 3: Update post using file
    And user saves json path "Updated Post Title" as "postTitle"
    And user sends PUT request from file "rest/createPost.json" to "/posts/${postId}"
    
    Then response status code should be 200
    And json path "title" should be "Updated Post Title"
    
    # Step 4: Delete post
    When user sends DELETE request to "/posts/${postId}"
    Then response status code should be 200

  Scenario: Nested variable replacement in JSON file
    # Set nested variables
    When user saves json path "Jane Smith" as "userName"
    And user saves json path "jane.smith@test.com" as "userEmail"
    And user saves json path "25" as "userAge"
    And user saves json path "789 Elm St" as "street"
    And user saves json path "Chicago" as "city"
    And user saves json path "60601" as "zipCode"
    
    # Load file and verify variable replacement
    When user sets request body from json file "rest/createUser.json"
    And user sends POST request to "/users"
    
    Then response status code should be 201
    And response body should contain "Jane Smith"
    And response body should contain "jane.smith@test.com"
    And response body should contain "Chicago"

  Scenario: Array and complex JSON from file
    # Set array variables
    When user saves json path "Array Test Post" as "postTitle"
    And user saves json path "Testing arrays in JSON" as "postBody"
    And user saves json path "10" as "userId"
    And user saves json path "array" as "tag1"
    And user saves json path "complex" as "tag2"
    And user saves json path "2025-01-21T13:00:00Z" as "timestamp"
    
    When user sends POST request from file "rest/createPost.json" to "/posts"
    
    Then response status code should be 201
    And json array "tags" should have size 2
    And json array "tags" should contain "array"
    And json array "tags" should contain "complex"
