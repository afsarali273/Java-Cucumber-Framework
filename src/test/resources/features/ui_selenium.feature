@UI @Selenium
Feature: UI Testing with Selenium

  Scenario: Verify Flipkart search functionality
    Given user opens Flipkart website
    When user searches for "Samsung Galaxy"
    Then product should be added to cart successfully
