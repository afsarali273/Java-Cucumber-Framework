@API
Feature: Advanced API Testing with Keyword-Driven Steps

  Scenario: Get post and save data for later use
    When user sends GET request to "/posts/1"
    Then response status code should be 200
    And json path "userId" should not be null
    And json path "id" should be "1"
    And response time should be less than 2000 ms
    When user saves json path "userId" as "savedUserId"
    And user saves json path "title" as "savedTitle"
    Then saved variable "savedUserId" should not be null

  Scenario: Create post using saved variables
    When user sends GET request to "/posts/1"
    And user saves json path "userId" as "userId"
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

  Scenario: Test with custom headers
    When user sets header "Authorization" as "Bearer token123"
    And user sets header "Custom-Header" as "CustomValue"
    And user sends "GET" request to "/posts/1" with headers
    Then response status code should be 200

  Scenario: Test with query parameters
    When user sets query param "userId" as "1"
    And user sets query param "limit" as "5"
    And user sends GET request to "/posts" with query params
    Then response status code should be 200
    And json array "$" should have size 5

  Scenario: JSON Path validations
    When user sends GET request to "/posts"
    Then response status code should be 200
    And json array "$" should have size 100
    And json path "[0].userId" should not be null
    And json path "[0].id" should be greater than 0
    And response content type should be "application/json"

  Scenario: Response time and header validation
    When user sends GET request to "/posts/1"
    Then response status code should be 200
    And response time should be less than 3000 ms
    And response header "Content-Type" should exist
    And response body should not be empty
    And response body should contain "userId"

  Scenario: Complete workflow with variable chaining
    # Get user data
    When user sends GET request to "/users/1"
    Then response status code should be 200
    When user saves json path "id" as "userId"
    And user saves json path "name" as "userName"
    
    # Create post with saved user data
    And user sets request body:
      """
      {
        "title": "Post by ${userName}",
        "body": "Content",
        "userId": ${userId}
      }
      """
    And user sends POST request to "/posts"
    Then response status code should be 201
    And json path "title" should contain "${userName}"
    When user saves json path "id" as "postId"
    
    # Update the post
    And user sets request body:
      """
      {
        "title": "Updated Post",
        "body": "Updated Content",
        "userId": ${userId}
      }
      """
    And user sends PUT request to "/posts/${postId}"
    Then response status code should be 200
    And json path "title" should be "Updated Post"
    
    # Delete the post
    When user sends DELETE request to "/posts/${postId}"
    Then response status code should be 200

  Scenario: Array operations
    When user sends GET request to "/posts"
    Then response status code should be 200
    And json array "$" should have size 100
    And json path "[0].id" should not be null
    And json path "[0].userId" should be greater than 0

  Scenario: Null checks and validations
    When user sends GET request to "/posts/1"
    Then response status code should be 200
    And json path "id" should not be null
    And json path "title" should not be null
    And json path "nonExistentField" should be null
