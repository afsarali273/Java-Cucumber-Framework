@API
Feature: Data-driven API testing using external test data

  Scenario Outline: Verify if employee is getting details successfully using GET API
    Given Access "<url>" with "<headers>" for "<tc_id>"
    When Send "<method>"
    Then verify request is success with status "<code>"
    And verify "status" in response body is "success"

    Examples:
      | tc_id            | url                        | headers | method | code |
      | DummyGetEmployee | /api/v1/employee/1         | NA      | GET    | 200  |

  Scenario Outline: Verify if employee is getting added successfully using POST API
    Given Access "<url>" with "<headers>" for "<tc_id>"
    When Send "<method>" with request body "<payload>"
    Then verify request is success with status "<code>"
    And verify "status" in response body is "success"
    And verify "data.name" in response body is "John Doe"

    Examples:
      | tc_id           | url              | headers | method | payload              | code |
      | DummyAddEmployee| /api/v1/create   | NA      | POST   | DummyEmployeePayload | 200  |
