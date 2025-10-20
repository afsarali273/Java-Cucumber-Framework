package com.automation.stepdefinitions;

import com.automation.core.logging.LogManager;
import com.automation.reusables.APIReusable;
import io.cucumber.java.en.*;
import java.util.HashMap;
import java.util.Map;
import com.opencsv.exceptions.CsvException;
import java.io.IOException;

public class APISteps extends APIReusable {

    private Map<String, String> savedVariables = new HashMap<>();
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> queryParams = new HashMap<>();
    private Map<String, String> testData;

    @When("user sends GET request to {string}")
    public void userSendsGETRequestTo(String endpoint) {
        sendGetRequest(replaceVariables(endpoint));
    }

    @When("user creates POST request body with title {string} and body {string}")
    public void userCreatesPostRequestBody(String title, String body) {
        createJsonBody(replaceVariables(title), replaceVariables(body), 1);
    }

    @When("user sends POST request to {string}")
    public void userSendsPOSTRequestTo(String endpoint) {
        sendPostRequest(replaceVariables(endpoint), requestBody);
    }

    @Then("response status code should be {int}")
    public void responseStatusCodeShouldBe(int expectedStatusCode) {
        validateStatusCode(expectedStatusCode);
    }

    @Then("response should contain {string} field")
    public void responseShouldContainField(String fieldName) {
        validateResponseContainsField(fieldName);
    }

    @Then("response {string} should be {string}")
    public void responseFieldShouldBe(String fieldName, String expectedValue) {
        validateFieldValue(fieldName, replaceVariables(expectedValue));
    }

    @Then("response {string} should not be null")
    public void responseFieldShouldNotBeNull(String fieldName) {
        validateFieldNotNull(fieldName);
    }

    // ========== SERVICE SWITCHING ==========

    @Given("user switches to {string} API service")
    public void userSwitchesToAPIService(String serviceName) {
        switchToService(serviceName);
    }

    @Given("user uses custom base URL {string}")
    public void userUsesCustomBaseURL(String customUrl) {
        useCustomBaseUrl(replaceVariables(customUrl));
    }

    @When("user sends {string} request to {string} service endpoint {string}")
    public void userSendsRequestToServiceEndpoint(String method, String serviceName, String endpoint) {
        switchToService(serviceName);
        userSendsRequestTo(method, endpoint);
    }

    @When("user sends GET request to {string} service endpoint {string}")
    public void userSendsGETRequestToServiceEndpoint(String serviceName, String endpoint) {
        switchToService(serviceName);
        sendGetRequest(replaceVariables(endpoint));
    }

    @When("user sends POST request to {string} service endpoint {string}")
    public void userSendsPOSTRequestToServiceEndpoint(String serviceName, String endpoint) {
        switchToService(serviceName);
        sendPostRequest(replaceVariables(endpoint), requestBody);
    }

    // ========== HTTP METHODS ==========

    @When("user sends PUT request to {string}")
    public void userSendsPUTRequestTo(String endpoint) {
        sendPutRequest(replaceVariables(endpoint), requestBody);
    }

    @When("user sends DELETE request to {string}")
    public void userSendsDELETERequestTo(String endpoint) {
        sendDeleteRequest(replaceVariables(endpoint));
    }

    @When("user sends {string} request to {string}")
    public void userSendsRequestTo(String method, String endpoint) {
        String resolvedEndpoint = replaceVariables(endpoint);
        switch (method.toUpperCase()) {
            case "GET": sendGetRequest(resolvedEndpoint); break;
            case "POST": sendPostRequest(resolvedEndpoint, requestBody); break;
            case "PUT": sendPutRequest(resolvedEndpoint, requestBody); break;
            case "DELETE": sendDeleteRequest(resolvedEndpoint); break;
        }
    }

    // ========== JSON PATH VALIDATIONS ==========

    @Then("json path {string} should be {string}")
    public void jsonPathShouldBe(String jsonPath, String expectedValue) {
        validateJsonPath(jsonPath, replaceVariables(expectedValue));
    }

    @Then("json path {string} should contain {string}")
    public void jsonPathShouldContain(String jsonPath, String expectedText) {
        validateJsonPathContains(jsonPath, replaceVariables(expectedText));
    }

    @Then("json path {string} should not be null")
    public void jsonPathShouldNotBeNull(String jsonPath) {
        validateJsonPathNotNull(jsonPath);
    }

    @Then("json path {string} should be null")
    public void jsonPathShouldBeNull(String jsonPath) {
        validateJsonPathIsNull(jsonPath);
    }

    @Then("json path {string} should be greater than {int}")
    public void jsonPathShouldBeGreaterThan(String jsonPath, int expectedValue) {
        validateFieldGreaterThan(jsonPath, expectedValue);
    }

    @Then("json path {string} should be less than {int}")
    public void jsonPathShouldBeLessThan(String jsonPath, int expectedValue) {
        validateFieldLessThan(jsonPath, expectedValue);
    }

    @Then("json array {string} should have size {int}")
    public void jsonArrayShouldHaveSize(String jsonPath, int expectedSize) {
        validateArraySize(jsonPath, expectedSize);
    }

    @Then("json array {string} should contain {string}")
    public void jsonArrayShouldContain(String jsonPath, String expectedValue) {
        validateArrayContains(jsonPath, expectedValue);
    }

    // ========== RESPONSE VALIDATIONS ==========

    @Then("response time should be less than {long} ms")
    public void responseTimeShouldBeLessThan(long maxTime) {
        validateResponseTime(maxTime);
    }

    @Then("response content type should be {string}")
    public void responseContentTypeShouldBe(String contentType) {
        validateContentType(contentType);
    }

    @Then("response header {string} should be {string}")
    public void responseHeaderShouldBe(String headerName, String expectedValue) {
        validateHeader(headerName, replaceVariables(expectedValue));
    }

    @Then("response header {string} should exist")
    public void responseHeaderShouldExist(String headerName) {
        validateHeaderExists(headerName);
    }

    @Then("response body should not be empty")
    public void responseBodyShouldNotBeEmpty() {
        validateResponseBodyNotEmpty();
    }

    @Then("response body should contain {string}")
    public void responseBodyShouldContain(String expectedText) {
        validateResponseBodyContains(replaceVariables(expectedText));
    }

    @Then("response {string} should be null")
    public void responseFieldShouldBeNull(String fieldName) {
        validateJsonPathIsNull(fieldName);
    }

    // ========== SAVE & RETRIEVE VARIABLES ==========

    @When("user saves json path {string} as {string}")
    public void userSavesJsonPathAs(String jsonPath, String variableName) {
        String value = String.valueOf(getJsonPathValue(jsonPath));
        savedVariables.put(variableName, value);
        LogManager.info("Saved variable '" + variableName + "' = " + value);
    }

    @When("user saves response body as {string}")
    public void userSavesResponseBodyAs(String variableName) {
        String body = getResponseBody();
        savedVariables.put(variableName, body);
        LogManager.info("Saved response body as '" + variableName + "'");
    }

    @Then("saved variable {string} should be {string}")
    public void savedVariableShouldBe(String variableName, String expectedValue) {
        String actualValue = savedVariables.get(variableName);
        validateFieldValue(variableName, replaceVariables(expectedValue));
    }

    @Then("saved variable {string} should not be null")
    public void savedVariableShouldNotBeNull(String variableName) {
        String value = savedVariables.get(variableName);
        validateFieldNotNull(variableName);
    }

    // ========== REQUEST BODY BUILDERS ==========

    @When("user sets request body:")
    public void userSetsRequestBody(String body) {
        requestBody = replaceVariables(body);
        LogManager.info("Set request body: " + requestBody);
    }

    @When("user sets request body from file {string}")
    public void userSetsRequestBodyFromFile(String filePath) {
        // Implementation for loading from file if needed
    }

    // ========== HEADERS & QUERY PARAMS ==========

    @When("user sets header {string} as {string}")
    public void userSetsHeader(String headerName, String headerValue) {
        String resolvedValue = replaceVariables(headerValue);
        headers.put(headerName, resolvedValue);
        LogManager.info("Set header '" + headerName + "' = " + resolvedValue);
    }

    @When("user sets query param {string} as {string}")
    public void userSetsQueryParam(String paramName, String paramValue) {
        String resolvedValue = replaceVariables(paramValue);
        queryParams.put(paramName, resolvedValue);
        LogManager.info("Set query param '" + paramName + "' = " + resolvedValue);
    }

    @When("user sends GET request to {string} with query params")
    public void userSendsGETRequestWithQueryParams(String endpoint) {
        sendGetWithQueryParams(replaceVariables(endpoint), queryParams);
        queryParams.clear();
    }

    @When("user sends {string} request to {string} with headers")
    public void userSendsRequestWithHeaders(String method, String endpoint) {
        sendRequestWithHeaders(method, replaceVariables(endpoint), headers);
        headers.clear();
    }

    // ========== SOAP SUPPORT ==========

    @When("user creates SOAP envelope with action {string} and body:")
    public void userCreatesSoapEnvelope(String soapAction, String soapBody) {
        String resolvedBody = replaceVariables(soapBody);
        createSoapEnvelope(soapAction, resolvedBody);
        LogManager.info("Created SOAP envelope with action: " + soapAction);
    }

    @When("user sends SOAP request to {string} with action {string}")
    public void userSendsSoapRequest(String endpoint, String soapAction) {
        sendSoapRequest(replaceVariables(endpoint), replaceVariables(soapAction));
    }

    @Then("SOAP response should have fault")
    public void soapResponseShouldHaveFault() {
        validateSoapFault(true);
    }

    @Then("SOAP response should not have fault")
    public void soapResponseShouldNotHaveFault() {
        validateSoapFault(false);
    }

    @Then("SOAP xpath {string} should be {string}")
    public void soapXpathShouldBe(String xpath, String expectedValue) {
        String actualValue = extractSoapValue(xpath);
        validateFieldValue(xpath, replaceVariables(expectedValue));
    }

    // ========== UTILITY METHODS ==========

    @When("user logs response")
    public void userLogsResponse() {
        logResponse();
    }

    @When("user clears saved variables")
    public void userClearsSavedVariables() {
        LogManager.info("Clearing " + savedVariables.size() + " saved variables");
        savedVariables.clear();
    }

    private String replaceVariables(String text) {
        String result = text;
        for (Map.Entry<String, String> entry : savedVariables.entrySet()) {
            String placeholder = "${" + entry.getKey() + "}";
            if (result.contains(placeholder)) {
                result = result.replace(placeholder, entry.getValue());
                LogManager.info("Replaced variable: " + placeholder + " -> " + entry.getValue());
            }
        }
        return result;
    }

    @Given("Access {string} with {string} for {string}")
    public void access_with_for(String url, String headers, String tcId) throws IOException, CsvException {
        String testDataFile = "src/test/resources/testdata.csv";
        testData = com.automation.core.utils.DataDrivenUtils.getTestDataByTcId(testDataFile, tcId);
        if (testData != null) {
            url = testData.getOrDefault("url", url);
            headers = testData.getOrDefault("headers", headers);
        }
        // Set up headers if provided (from testData or argument)
        this.headers.clear();
        if (headers != null && !headers.equalsIgnoreCase("NA") && !headers.isEmpty()) {
            // If headers is a JSON string, parse and add to headers map
            try {
                org.json.JSONObject jsonHeaders = new org.json.JSONObject(headers);
                for (String key : jsonHeaders.keySet()) {
                    this.headers.put(key, jsonHeaders.getString(key));
                }
            } catch (Exception e) {
                // If not JSON, treat as single header: key:value
                if (headers.contains(":")) {
                    String[] parts = headers.split(":", 2);
                    this.headers.put(parts[0].trim(), parts[1].trim());
                }
            }
        }
        // Optionally set base URL/service if needed from testData
        if (testData != null && testData.containsKey("baseUrl")) {
            useCustomBaseUrl(testData.get("baseUrl"));
        }
        // Optionally set currentService if needed
        if (testData != null && testData.containsKey("service")) {
            switchToService(testData.get("service"));
        }
    }

    @When("Send {string}")
    public void send(String method) {
        if ("GET".equalsIgnoreCase(method)) {
            response = sendGetRequest(testData.get("url"));
        } else if ("DELETE".equalsIgnoreCase(method)) {
            response = sendDeleteRequest(testData.get("url"));
        } else if ("PUT".equalsIgnoreCase(method)) {
            response = sendPutRequest(testData.get("url"), testData.getOrDefault("payload", ""));
        } else {
            throw new UnsupportedOperationException("Method not supported: " + method);
        }
    }

    @When("Send {string} with request body {string}")
    public void send_with_body(String method, String payload) {
        if ("POST".equalsIgnoreCase(method)) {
            response = sendPostRequest(testData.get("url"), testData.getOrDefault("payload", payload));
        } else if ("PUT".equalsIgnoreCase(method)) {
            response = sendPutRequest(testData.get("url"), testData.getOrDefault("payload", payload));
        } else {
            throw new UnsupportedOperationException("Method not supported: " + method);
        }
    }

    @Then("verify request is success with status {string}")
    public void verify_request_is_success_with_status(String code) {
        if (response.getStatusCode() != Integer.parseInt(code)) {
            throw new AssertionError("Expected status code " + code + ", got " + response.getStatusCode());
        }
    }

    @Then("verify {string} in response body is {string}")
    public void verify_in_response_body_is(String field, String expected) {
        String actual = response.jsonPath().getString(field);
        if (!expected.equals(actual)) {
            throw new AssertionError("Expected " + field + " to be " + expected + ", got " + actual);
        }
    }
}
