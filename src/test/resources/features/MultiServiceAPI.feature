@API
Feature: Multi-Service API Testing

  Scenario: Test multiple API services in single scenario
    # Test Auth Service
    Given user switches to "auth" API service
    When user creates POST request body with title "login" and body "credentials"
    And user sends POST request to "/login"
    Then response status code should be 200
    And user saves json path "token" as "authToken"

    # Test User Service
    Given user switches to "user" API service
    When user sets header "Authorization" as "Bearer ${authToken}"
    And user sends GET request to "/profile"
    Then response status code should be 200
    And response "name" should not be null

    # Test Payment Service
    Given user switches to "payment" API service
    When user sends GET request to "/balance"
    Then response status code should be 200
    And json path "balance" should be greater than 0

  Scenario: Test service-specific endpoints
    When user sends GET request to "auth" service endpoint "/validate"
    Then response status code should be 200

    When user sends GET request to "payment" service endpoint "/transactions"
    Then response status code should be 200

  Scenario: Test with custom base URL
    Given user uses custom base URL "https://external-api.example.com"
    When user sends GET request to "/data"
    Then response status code should be 200

  Scenario: Test default service (backward compatible)
    When user sends GET request to "/posts/1"
    Then response status code should be 200
    And response "id" should be "1"

  Scenario: Complex multi-service workflow
    # Authenticate
    Given user switches to "auth" API service
    When user sets request body:
      """
      {
        "username": "testuser",
        "password": "testpass"
      }
      """
    And user sends POST request to "/authenticate"
    Then response status code should be 200
    And user saves json path "accessToken" as "token"
    And user saves json path "userId" as "userId"

    # Get user profile
    Given user switches to "user" API service
    When user sets header "Authorization" as "Bearer ${token}"
    And user sends GET request to "/users/${userId}"
    Then response status code should be 200
    And response "email" should not be null
    And user saves json path "email" as "userEmail"

    # Create order
    Given user switches to "order" API service
    When user sets header "Authorization" as "Bearer ${token}"
    And user sets request body:
      """
      {
        "userId": "${userId}",
        "email": "${userEmail}",
        "items": ["item1", "item2"]
      }
      """
    And user sends POST request to "/orders"
    Then response status code should be 201
    And response "orderId" should not be null
    And user saves json path "orderId" as "orderId"

    # Process payment
    Given user switches to "payment" API service
    When user sets header "Authorization" as "Bearer ${token}"
    And user sets request body:
      """
      {
        "orderId": "${orderId}",
        "amount": 100.50
      }
      """
    And user sends POST request to "/process"
    Then response status code should be 200
    And response "status" should be "success"
