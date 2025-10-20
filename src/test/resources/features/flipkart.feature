@UI
Feature: Flipkart Product Search and Add to Cart

  Scenario: Search for a product and add to cart
    Given user opens Flipkart website
    When user searches for "iPhone 15"
    And user clicks on first product
    And user adds product to cart
    Then product should be added to cart successfully
