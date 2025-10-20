@API
Feature: Advanced API Testing Examples

  Scenario: Validate multiple posts
    When user sends GET request to "/posts"
    Then response status code should be 200

  Scenario: Update a post
    When user creates POST request body with title "Updated Title" and body "Updated Body"
    And user sends POST request to "/posts/1"
    Then response status code should be 201
    And response "title" should be "Updated Title"

  Scenario: Delete a post
    When user sends GET request to "/posts/1"
    Then response status code should be 200
