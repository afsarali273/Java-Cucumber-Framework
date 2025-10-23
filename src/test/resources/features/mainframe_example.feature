@Mainframe
Feature: Mainframe Testing

  Scenario: Login to mainframe application
    Given user connects to mainframe session "A"
    When user enters "USER123" at row 10 column 20
    And user enters "PASSWORD" at row 11 column 20
    And user presses Enter key
    Then mainframe screen should display "WELCOME" within 5 seconds
    And mainframe screen should contain "MAIN MENU"

  Scenario: Navigate and retrieve data
    Given user connects to mainframe session "A"
    When user enters "MENU01" at row 5 column 10
    And user presses Enter key
    And user saves text from row 15 column 5 length 20 as "accountNumber"
    Then text at row 15 column 5 length 20 should be "${accountNumber}"
    When user disconnects from mainframe

  Scenario: Use function keys
    Given user connects to mainframe session "A"
    When user presses PF3
    Then mainframe screen should contain "PREVIOUS SCREEN"
    When user presses PF12
    Then mainframe screen should contain "EXIT"
