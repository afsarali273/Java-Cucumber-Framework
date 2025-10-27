@API @Encryption
Feature: API Testing with Encrypted Test Data

  Background:
    Given user switches to "userService" API service
    And user sets header "Content-Type" as "application/json"

  Scenario: Login with encrypted credentials
    When user sets request body from file "testData/secure/credentials.json.encrypted"
    And user sends POST request to "/auth/login"
    Then response status code should be 200
    And response "token" should not be null
    When user saves json path "token" as "authToken"

  Scenario: API call with encrypted keys and variables
    # Set variables for replacement
    Given user saves "sk_live_xyz123" as "apiKey"
    And user saves "production" as "env"
    And user saves "webhook_abc" as "webhookId"
    # Use encrypted file with variable replacement
    When user sets request body from file "testData/secure/api-keys.json.encrypted"
    And user sends POST request to "/api/authenticate"
    Then response status code should be 200
    And response "environment" should be "production"

  Scenario: SOAP payment with encrypted card details
    # Set variables
    Given user saves "Bearer token123" as "authToken"
    And user saves "100.00" as "amount"
    # Send SOAP request with encrypted file
    When user sends SOAP request to "/soap/payment" with body from file "testData/soap/secure/payment.xml.encrypted"
    Then response status code should be 200
    And SOAP response should not have fault

  Scenario: Create user from encrypted JSON file
    When user sends POST request to "/users" with body from file "testData/secure/credentials.json.encrypted"
    Then response status code should be 201
    And response "username" should be "admin@example.com"
    And response "id" should not be null

  Scenario: Update with encrypted data and dynamic endpoint
    # Get user ID first
    When user sends GET request to "/users/1"
    Then response status code should be 200
    When user saves json path "id" as "userId"
    # Update using encrypted file
    And user sends PUT request to "/users/${userId}" with body from file "testData/secure/credentials.json.encrypted"
    Then response status code should be 200
