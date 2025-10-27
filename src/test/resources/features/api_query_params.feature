@API
Feature: API Query Parameters - Multiple Approaches

  Background:
    Given user switches to "jsonplaceholder" API service

  Scenario: Query parameters using individual steps
    When user sets query param "userId" as "1"
    And user sets query param "limit" as "5"
    And user sends GET request to "/posts" with query params
    Then response status code should be 200

  Scenario: Query parameters using DataTable
    When user sets query parameters:
      | page   | 1      |
      | limit  | 10     |
      | sort   | title  |
      | order  | asc    |
    And user sends GET request to "/posts" with query params
    Then response status code should be 200

  Scenario: Query parameters using inline format
    When user sets query parameters "page=1&limit=10&userId=1"
    And user sends GET request to "/posts" with query params
    Then response status code should be 200

  Scenario: Query parameters from properties file
    When user sets query parameters from file "testData/queryParams/searchParams.properties"
    And user sends GET request to "/posts" with query params
    Then response status code should be 200

  Scenario: Query parameters from JSON file with variables
    When user saves json path "admin" as "userRole"
    And user saves json path "Engineering" as "departmentName"
    And user sets query parameters from json file "testData/queryParams/userFilters.json"
    And user sends GET request to "/users" with query params
    Then response status code should be 200

  Scenario: Send GET with query parameters in one step (DataTable)
    When user sends GET request to "/posts" with query parameters:
      | userId | 1  |
      | _limit | 5  |
    Then response status code should be 200
    And json array "$" should have size 5

  Scenario: Send POST with query parameters
    When user sets request body:
      """
      {
        "title": "Test Post",
        "body": "Content",
        "userId": 1
      }
      """
    And user sends POST request to "/posts" with query parameters:
      | notify | true  |
      | async  | false |
    Then response status code should be 201

  Scenario: Query parameters with variable replacement
    When user sends GET request to "/posts/1"
    And user saves json path "userId" as "userId"
    And user sets query param "userId" as "${userId}"
    And user sets query param "limit" as "10"
    And user sends GET request to "/posts" with query params
    Then response status code should be 200

  Scenario: Clear and reuse query parameters
    When user sets query parameters:
      | page  | 1  |
      | limit | 5  |
    And user sends GET request to "/posts" with query params
    Then response status code should be 200
    
    When user clears query parameters
    And user sets query parameters:
      | page  | 2  |
      | limit | 10 |
    And user sends GET request to "/posts" with query params
    Then response status code should be 200

  Scenario: Send GET with query parameters from file in one step
    When user sends GET request to "/posts" with query parameters from file "testData/queryParams/searchParams.properties"
    Then response status code should be 200

  Scenario: Complex query parameters with multiple sources
    # Set some params individually
    When user sets query param "page" as "1"
    
    # Add params from DataTable
    And user sets query parameters:
      | limit | 10    |
      | sort  | title |
    
    # Add params from inline format
    And user sets query parameters "order=asc&filter=active"
    
    # Send request
    And user sends GET request to "/posts" with query params
    Then response status code should be 200

  Scenario: Query parameters with special characters
    When user saves json path "test@example.com" as "email"
    And user sets query param "email" as "${email}"
    And user sets query param "name" as "John Doe"
    And user sends GET request to "/users" with query params
    Then response status code should be 200
