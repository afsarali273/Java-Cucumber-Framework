@API
Feature: API Examples - Common reusable API steps
  This feature contains example scenarios that demonstrate common API step usages
  implemented in `APISteps`. These are templates — update endpoints/paths/selectors
  and schema files to match your API under test.

  Background:
    # APIClient will be initialized by hooks when @API tag is present

  @auth
  Scenario: Obtain bearer token and call protected endpoint
    # Example flow: authenticate, extract token, call protected resource
    When user sends POST request to "/auth/login"
    Then response status code should be 200
    # Assume response JSON contains token at data.token
    When user sets bearer token from response json "data.token" as header "Authorization"
    When user applies headers
    When user sends GET request to "/users/me"
    Then response status code should be 200
    Then json path "data.username" should not be null

  @schema
  Scenario: Validate response against JSON schema
    When user sends GET request to "/users/1"
    Then response status code should be 200
    Then response should match json schema "schemas/user-response.json"

  @upload
  Scenario: Upload a file using multipart (requires a real file path)
    # Provide SAMPLE_FILE_PATH env var or set ScenarioContext in a hook
    When user uploads file "${sampleFilePath}" to "/files/upload" as multipart field "file"
    Then response status code should be 201

  @polling
  Scenario: Poll a job status endpoint until completion
    When user polls "/jobs/123/status" until json path "status" equals "COMPLETED" within 120 seconds interval 5 seconds
    Then json path "result.count" should be greater than 0

  @headers
  Scenario: Set custom headers, apply them and validate echo
    When user sets header "X-Test-Run" as "run-${random}"
    When user applies headers
    When user sends GET request to "/headers/echo"
    Then response header "X-Test-Run" should exist
    When user saves response header "X-Test-Run" as "echoedRunId"
    Then saved variable "echoedRunId" should not be null

  @basic-auth
  Scenario: Basic auth example and response time capture
    When user sets basic auth with username "demoUser" and password "password"
    When user sends GET request to "/auth/basic"
    Then response status code should be 200
    When user saves response time as "basicAuthTimeMs"

  @soap
  Scenario: SOAP request example
    When user creates SOAP envelope with action "GetInfo" and body:
      """
      <GetInfoRequest><Id>123</Id></GetInfoRequest>
      """
    When user sends SOAP request to "/soap" with action "GetInfo"
    Then SOAP xpath "Envelope.Body.GetInfoResponse.Code" should be "200"

  @cleanup
  Scenario: Clear request spec and headers
    When user clears request spec and headers
    When user clears saved variables

