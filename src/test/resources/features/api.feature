@API
Feature: API Testing with JSONPlaceholder

  Scenario: Get a post by ID
    When user sends GET request to "/posts/1"
    Then response status code should be 200
    And response should contain "userId" field
    And response should contain "title" field
    And response "userId" should not be null

  Scenario: Create a new post
    When user creates POST request body with title "Test Post" and body "This is a test post"
    And user sends POST request to "/posts"
    Then response status code should be 201
    And response "title" should be "Test Post"
    And response "body" should be "This is a test post"
