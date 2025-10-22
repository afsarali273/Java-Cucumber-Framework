Feature: Examples for newly added API steps
  This feature demonstrates the newly added Cucumber steps in APISteps.java

  Background:
    # Adjust base URL / service in your environment or via existing steps if needed
    Given user uses custom base URL "http://api.example.local"

  Scenario: JSON regex assertion
    Given user sends GET request to "/users/123"
    Then json path "data.email" should match regex "^[\\w.-]+@[\\w.-]+\\.\\w+$"

  Scenario: JSON type checks
    Given user sends GET request to "/users/123"
    Then json path "data.id" should be of type "number"
    And json path "data.name" should be of type "string"
    And json path "data.active" should be of type "boolean"

  Scenario: Response is valid JSON
    Given user sends GET request to "/health"
    Then response should be valid json

  Scenario: Response JSON equals classpath file
    Given user sends GET request to "/data/42"
    Then response json should equal file "expected/data-42.json"

  Scenario: JSON object keys contain required keys
    Given user sends GET request to "/users/123"
    Then json path "data" keys should contain "id, name, email"

  Scenario: Array sorted by field
    Given user sends GET request to "/products"
    Then json array "items" elements should be sorted by "price" in "asc" order

  Scenario: ISO8601 timestamp freshness
    Given user sends GET request to "/events/latest"
    Then json path "data.timestamp" should be ISO8601 within 5 seconds

  Scenario: Response header regex
    Given user sends GET request to "/status"
    Then response header "X-Request-Id" should match regex "^[a-f0-9\\-]{36}$"

  Scenario: Response header should not exist
    Given user sends GET request to "/info"
    Then response header "X-Debug-Info" should not exist

  Scenario: Cookie existence and value
    Given user sends GET request to "/login"
    Then cookie "SESSIONID" should exist
    And cookie "myCookie" should be "expectedValue"

  Scenario: Response body size limit
    Given user sends GET request to "/lightweight"
    Then response body size should be less than 10240 bytes

  Scenario: Unique values in an array
    Given user sends GET request to "/records"
    Then json array "records" should have unique values by "id"

  Scenario: At least one element matches
    Given user sends GET request to "/orders"
    Then at least one element in json array "orders" should have "status" equal "COMPLETED"

  Scenario: JWT not expired
    Given user sends POST request to "/auth"
    Then jwt in json path "data.token" should not be expired

  Scenario: Response encoding
    Given user sends GET request to "/static"
    Then response encoding should be ""

  Scenario: Save and reuse variables
    Given user sends POST request to "/items"
    When user saves json path "data.id" as "itemId"
    When user sends GET request to "/items/${itemId}"
    Then response status code should be 200

  Scenario: Save entire response body
    Given user sends GET request to "/dump"
    When user saves response body as "lastDump"
    Then saved variable "lastDump" should not be null

  Scenario: Validate saved variable value
    Given user sends GET request to "/users/1"
    When user saves json path "data.name" as "name"
    Then saved variable "name" should be "John Doe"

  Scenario: Set request body with DocString
    When user sets request body:
      """
      {
        "title": "New item",
        "body": "Some content"
      }
      """
    And user sends POST request to "/items"
    Then response status code should be 201

  Scenario: Set header and use with request
    When user sets header "X-Correlation-Id" as "abc-123"
    And user sends GET request to "/trace" with headers
    Then response status code should be 200

  Scenario: Query params grouping
    When user sets query param "page" as "2"
    And user sets query param "size" as "20"
    When user sends GET request to "/items" with query params
    Then response status code should be 200

  Scenario: Set bearer token from previous response and apply
    Given user sends POST request to "/auth"
    When user sets bearer token from response json "data.token" as header "Authorization"
    And user sends GET request to "/secure" with headers
    Then response status code should be 200

  Scenario: Apply headers globally
    When user sets header "Accept" as "application/json"
    And user applies headers
    When user sends GET request to "/info"
    Then response status code should be 200

  Scenario: Clear request spec and headers
    When user clears request spec and headers

  Scenario: Basic auth usage
    When user sets basic auth with username "alice" and password "s3cr3t"
    And user sends GET request to "/secure/basic"
    Then response status code should be 200

  Scenario: Set content-type and accept
    When user sets content type "application/json"
    And user sets accept header "application/json"
    And user sets request body:
      """
      {"name":"x"}
      """
    And user sends POST request to "/create"
    Then response status code should be 201

  Scenario: Multipart file upload
    When user uploads file "src/test/resources/files/photo.jpg" to "/upload" as multipart field "file"
    Then response status code should be 200

  Scenario: JSON schema validation
    Given user sends GET request to "/users/123"
    Then response should match json schema "schemas/user-schema.json"

  Scenario: Save response header and reuse
    Given user sends GET request to "/items/1"
    When user saves response header "ETag" as "etagVal"
    Then saved variable "etagVal" should not be null

  Scenario: Poll until condition met
    When user polls "/jobs/123/status" until json path "status" equals "COMPLETED" within 120 seconds interval 5 seconds
    Then response status code should be 200

  Scenario: Save response time
    Given user sends GET request to "/heavy"
    When user saves response time as "latencyMs"
    Then saved variable "latencyMs" should not be null

  Scenario: Response header contains substring
    Given user sends GET request to "/download"
    Then response header "Content-Disposition" should contain "filename=\"report\""

  Scenario: Retry last request until expected status
    Given user sends GET request to "/ready-check"
    When user retries last request up to 5 times with interval 2 seconds until status 200
    Then response status code should be 200

  Scenario: Create and send SOAP request
    When user creates SOAP envelope with action "GetInfo" and body:
      """
      <Request>
        <Id>123</Id>
      </Request>
      """
    And user sends SOAP request to "/soap" with action "GetInfo"
    Then SOAP response should not have fault

  Scenario: SOAP xpath extraction
    When user sends SOAP request to "/soap" with action "GetInfo"
    Then SOAP xpath "/Envelope/Body/Response/Name" should be "Alice"

  Scenario: Log response and clear saved variables
    Given user sends GET request to "/debug"
    When user logs response
    And user clears saved variables

  Scenario: Data-driven Access + Send
    Given Access "defaultUrl" with "{\"X-Api-Key\":\"k\"}" for "TC_001"
    When Send "GET"
    Then verify request is success with status "200"

  Scenario: Send with request body (data-driven)
    Given Access "defaultUrl" with "NA" for "TC_002"
    When Send "POST" with request body "{\"name\":\"x\"}"
    Then verify request is success with status "201"

  Scenario: Quick response body verification
    Given user sends GET request to "/users/1"
    Then verify "data.name" in response body is "John Doe"

