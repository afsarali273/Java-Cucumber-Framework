package com.automation.stepdefinitions;

import com.automation.core.logging.LogManager;
import com.automation.reusables.APIReusable;
import io.cucumber.java.en.*;
import java.util.HashMap;
import java.util.Map;
import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.io.File;
import io.restassured.module.jsv.JsonSchemaValidator;
import com.automation.core.api.APIClient;

import java.util.regex.Pattern;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.nio.charset.StandardCharsets;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.time.Instant;
import java.time.Duration;
import java.util.List;
import java.util.Comparator;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.automation.core.context.ScenarioContext;

/**
 * Comprehensive API step definitions for REST/SOAP API testing.
 * 
 * Built on RestAssured with full support for variable substitution using ${variableName}.
 * All responses and extracted values can be saved to ScenarioContext for reuse.
 * 
 * Categories:
 * - HTTP Methods: GET, POST, PUT, DELETE with custom endpoints
 * - Service Switching: Multi-service support, custom base URLs
 * - Request Building: Set body from string/file, headers, query params, auth
 * - JSON Path Validations: Extract and validate JSON fields, arrays, nested objects
 * - Response Validations: Status codes, headers, content-type, response time, body
 * - Advanced Assertions: Regex matching, type checking, array sorting, unique values
 * - Variables: Save/retrieve response data, headers, body to ScenarioContext
 * - Authentication: Basic auth, bearer tokens, custom headers
 * - File Upload: Multipart file upload support
 * - JSON Schema: Validate response against JSON schema files
 * - Polling: Poll endpoints until condition met
 * - SOAP: SOAP envelope creation, fault validation, XPath assertions
 * - Data-Driven: CSV-based test data support
 * - Cookies: Cookie validation and extraction
 * - JWT: JWT token expiry validation
 * 
 * Example usage:
 *   Given user switches to "userService" API service
 *   When user sends GET request to "/users/1"
 *   Then response status code should be 200
 *   And json path "name" should be "John Doe"
 *   When user saves json path "id" as "userId"
 *   Then saved variable "userId" should not be null
 */
public class APISteps extends APIReusable {

    // saved variables are stored in ScenarioContext so they are shared across step classes
    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, String> queryParams = new HashMap<>();
    private Map<String, String> testData;

    // ========== BASIC HTTP OPERATIONS ==========

    /**
     * Example: When user sends GET request to "/users/1"
     * Example: When user sends GET request to "/users/${userId}"
     */
    @When("user sends GET request to {string}")
    public void userSendsGETRequestTo(String endpoint) {
        sendGetRequest(replaceVariables(endpoint));
    }

    /**
     * Example: When user creates POST request body with title "Test" and body "Content"
     */
    @When("user creates POST request body with title {string} and body {string}")
    public void userCreatesPostRequestBody(String title, String body) {
        createJsonBody(replaceVariables(title), replaceVariables(body), 1);
    }

    /**
     * Example: When user sends POST request to "/users"
     */
    @When("user sends POST request to {string}")
    public void userSendsPOSTRequestTo(String endpoint) {
        sendPostRequest(replaceVariables(endpoint), requestBody);
    }

    /**
     * Example: Then response status code should be 200
     */
    @Then("response status code should be {int}")
    public void responseStatusCodeShouldBe(int expectedStatusCode) {
        validateStatusCode(expectedStatusCode);
    }

    /**
     * Example: Then response should contain "id" field
     */
    @Then("response should contain {string} field")
    public void responseShouldContainField(String fieldName) {
        validateResponseContainsField(fieldName);
    }

    /**
     * Example: Then response "name" should be "John Doe"
     * Example: Then response "email" should be "${expectedEmail}"
     */
    @Then("response {string} should be {string}")
    public void responseFieldShouldBe(String fieldName, String expectedValue) {
        validateFieldValue(fieldName, replaceVariables(expectedValue));
    }

    /**
     * Example: Then response "id" should not be null
     */
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

    // ======= NEW: Additional assertions =======

    @Then("json path {string} should match regex {string}")
    public void jsonPathShouldMatchRegex(String jsonPath, String regex) {
        Object val = getJsonPathValue(jsonPath);
        String s = val == null ? null : val.toString();
        boolean matches = s != null && Pattern.compile(regex).matcher(s).matches();
        assertTrue(matches, "JsonPath '" + jsonPath + "' should match regex '" + regex + "' but was '" + s + "'");
    }

    @Then("json path {string} should be of type {string}")
    public void jsonPathShouldBeOfType(String jsonPath, String type) {
        Object v = getJsonPathValue(jsonPath);
        String actualType = (v == null) ? "null" : v.getClass().getSimpleName().toLowerCase();
        boolean ok = false;
        switch (type.toLowerCase()) {
            case "string": ok = (v instanceof String); break;
            case "number": ok = (v instanceof Number); break;
            case "boolean": ok = (v instanceof Boolean); break;
            case "object": ok = (v instanceof java.util.Map); break;
            case "array": ok = (v instanceof java.util.List); break;
            case "null": ok = (v == null); break;
        }
        assertTrue(ok, "JsonPath '" + jsonPath + "' expected type '" + type + "' but was '" + actualType + "'");
    }

    @Then("response should be valid json")
    public void responseShouldBeValidJson() {
        String body = getResponseBody();
        try {
            JsonElement e = JsonParser.parseString(body);
            assertNotNull(e, "Response should be valid JSON");
        } catch (JsonSyntaxException ex) {
            throw new AssertionError("Response is not valid JSON: " + ex.getMessage());
        }
    }

    @Then("response json should equal file {string}")
    public void responseJsonShouldEqualFile(String classpathFile) throws IOException {
        String expected = readClasspathResourceAsString(classpathFile);
        String actual = getResponseBody();
        JsonElement e1 = JsonParser.parseString(expected);
        JsonElement e2 = JsonParser.parseString(actual);
        assertEquals(e2, e1, "Response JSON should match expected JSON from " + classpathFile);
    }

    @Then("json path {string} keys should contain {string}")
    public void jsonPathKeysShouldContain(String jsonPath, String keysCommaSeparated) {
        Object v = getJsonPathValue(jsonPath);
        if (!(v instanceof java.util.Map)) {
            throw new AssertionError("JsonPath '" + jsonPath + "' is not a JSON object");
        }
        @SuppressWarnings("unchecked")
        java.util.Map<String, Object> map = (java.util.Map<String, Object>) v;
        String[] keys = keysCommaSeparated.split(",");
        for (String k : keys) {
            String trimmed = k.trim();
            assertTrue(map.containsKey(trimmed), "Expected key '" + trimmed + "' in object at '" + jsonPath + "'");
        }
    }

    @Then("json array {string} elements should be sorted by {string} in {string} order")
    public void jsonArrayShouldBeSortedBy(String arrayPath, String sortByJsonPath, String order) {
        java.util.List<java.util.Map<String, Object>> list = response.jsonPath().getList(arrayPath);
        if (list == null) throw new AssertionError("Array not found at: " + arrayPath);
        // Use Object list and a safe comparator to avoid generic type conflicts
        List<Object> values = new ArrayList<>();
        for (Map<String, Object> item : list) {
            Object val = item.get(sortByJsonPath);
            if (val == null) values.add(null);
            else if (val instanceof Number) values.add(((Number) val).doubleValue());
            else values.add(val);
        }

        List<Object> sorted = new ArrayList<>(values);

        Comparator<Object> safeComparator = (o1, o2) -> {
            if (o1 == o2) return 0;
            if (o1 == null) return -1;
            if (o2 == null) return 1;

            // Both non-null: handle numbers specially
            if (o1 instanceof Number && o2 instanceof Number) {
                double d1 = ((Number) o1).doubleValue();
                double d2 = ((Number) o2).doubleValue();
                return Double.compare(d1, d2);
            }

            // If both are Comparable and of same class, try direct comparison
            if (o1 instanceof Comparable && o2 instanceof Comparable && o1.getClass().isInstance(o2)) {
                try {
                    @SuppressWarnings({"unchecked", "rawtypes"})
                    int res = ((Comparable) o1).compareTo(o2);
                    return res;
                } catch (ClassCastException ignore) {
                    // fall through to string compare
                }
            }

            // Fallback to string comparison
            return o1.toString().compareTo(o2.toString());
        };

        if ("asc".equalsIgnoreCase(order)) {
            sorted.sort(Comparator.nullsFirst(safeComparator));
        } else {
            sorted.sort(Comparator.nullsFirst(safeComparator).reversed());
        }

        assertEquals(values, sorted, "Array at '" + arrayPath + "' is not sorted by '" + sortByJsonPath + "' in order " + order);
    }

    @Then("json path {string} should be ISO8601 within {int} seconds")
    public void jsonPathShouldBeIso8601Within(String jsonPath, int seconds) {
        Object v = getJsonPathValue(jsonPath);
        if (v == null) throw new AssertionError("Value at '" + jsonPath + "' is null");
        String s = v.toString();
        try {
            OffsetDateTime odt = OffsetDateTime.parse(s);
            Instant then = odt.toInstant();
            Instant now = Instant.now();
            long diff = Math.abs(Duration.between(now, then).getSeconds());
            assertTrue(diff <= seconds, "Timestamp at '" + jsonPath + "' differs from now by " + diff + " seconds, allowed " + seconds);
        } catch (DateTimeParseException ex) {
            throw new AssertionError("Value at '" + jsonPath + "' is not a valid ISO8601 timestamp: " + s);
        }
    }

    @Then("response header {string} should match regex {string}")
    public void responseHeaderShouldMatchRegex(String headerName, String regex) {
        String value = getHeader(headerName);
        boolean ok = value != null && Pattern.compile(regex).matcher(value).matches();
        assertTrue(ok, "Header '" + headerName + "' expected to match regex '" + regex + "' but was '" + value + "'");
    }

    // ======= MORE API ASSERTIONS =======

    @Then("response header {string} should not exist")
    public void responseHeaderShouldNotExist(String headerName) {
        String value = getHeader(headerName);
        assertNull(value, "Header '" + headerName + "' should not be present but was '" + value + "'");
    }

    @Then("cookie {string} should exist")
    public void cookieShouldExist(String cookieName) {
        String val = response.getCookie(cookieName);
        assertNotNull(val, "Cookie '" + cookieName + "' should exist");
    }

    @Then("cookie {string} should be {string}")
    public void cookieShouldBe(String cookieName, String expected) {
        String val = response.getCookie(cookieName);
        assertEquals(val, replaceVariables(expected), "Cookie '" + cookieName + "' validation");
    }

    @Then("response body size should be less than {int} bytes")
    public void responseBodySizeShouldBeLessThan(int maxBytes) {
        byte[] bytes = getResponseBody().getBytes(java.nio.charset.StandardCharsets.UTF_8);
        assertTrue(bytes.length < maxBytes, "Response body size '" + bytes.length + "' should be less than " + maxBytes + " bytes");
    }

    @Then("json array {string} should have unique values by {string}")
    public void jsonArrayShouldHaveUniqueValuesBy(String arrayPath, String field) {
        List<Map<String, Object>> list = response.jsonPath().getList(arrayPath);
        if (list == null) throw new AssertionError("Array not found at: " + arrayPath);
        java.util.Set<Object> seen = new java.util.HashSet<>();
        for (Map<String, Object> item : list) {
            Object v = item.get(field);
            if (seen.contains(v)) {
                throw new AssertionError("Duplicate value '" + v + "' found for field '" + field + "' in array '" + arrayPath + "'");
            }
            seen.add(v);
        }
    }

    @Then("at least one element in json array {string} should have {string} equal {string}")
    public void atLeastOneElementShouldMatch(String arrayPath, String field, String expected) {
        List<Map<String, Object>> list = response.jsonPath().getList(arrayPath);
        if (list == null) throw new AssertionError("Array not found at: " + arrayPath);
        boolean found = false;
        String exp = replaceVariables(expected);
        for (Map<String, Object> item : list) {
            Object v = item.get(field);
            if (v != null && exp.equals(v.toString())) { found = true; break; }
        }
        assertTrue(found, "No element in '" + arrayPath + "' had '" + field + "' equal to '" + exp + "'");
    }

    @Then("jwt in json path {string} should not be expired")
    public void jwtInJsonPathShouldNotBeExpired(String jsonPath) {
        Object tok = getJsonPathValue(jsonPath);
        if (tok == null) throw new AssertionError("JWT token not found at: " + jsonPath);
        String token = tok.toString();
        String[] parts = token.split("\\.");
        if (parts.length < 2) throw new AssertionError("Invalid JWT token format");
        try {
            byte[] decoded = java.util.Base64.getUrlDecoder().decode(parts[1]);
            String payload = new String(decoded, java.nio.charset.StandardCharsets.UTF_8);
            JsonElement el = JsonParser.parseString(payload);
            if (!el.getAsJsonObject().has("exp")) throw new AssertionError("JWT payload has no 'exp' claim");
            long exp = el.getAsJsonObject().get("exp").getAsLong();
            long now = Instant.now().getEpochSecond();
            assertTrue(exp > now, "JWT token at '" + jsonPath + "' is expired or exp <= now");
        } catch (IllegalArgumentException | JsonSyntaxException e) {
            throw new AssertionError("Failed to decode/parse JWT payload: " + e.getMessage());
        }
    }

    @Then("response encoding should be {string}")
    public void responseEncodingShouldBe(String expected) {
        String enc = response.getHeader("Content-Encoding");
        assertEquals(enc == null ? "" : enc, replaceVariables(expected), "Response Content-Encoding validation");
    }

    // ======= existing steps continue below =======

    /**
     * Deprecated helper method - kept for compatibility but not exposed as a Cucumber step
     */
    @Deprecated
    public void jsonPathShouldBeDuplicate(String jsonPath, String expectedValue) {
        // placeholder duplicate of earlier step to keep older codepaths working if directly called
        validateJsonPath(jsonPath, replaceVariables(expectedValue));
    }

    // ========== SAVE & RETRIEVE VARIABLES ==========

    @When("user saves json path {string} as {string}")
    public void userSavesJsonPathAs(String jsonPath, String variableName) {
        String value = String.valueOf(getJsonPathValue(jsonPath));
        ScenarioContext.set(variableName, value);
        LogManager.info("Saved variable '" + variableName + "' = " + value);
    }

    @When("user saves response body as {string}")
    public void userSavesResponseBodyAs(String variableName) {
        String body = getResponseBody();
        ScenarioContext.set(variableName, body);
        LogManager.info("Saved response body as '" + variableName + "'");
    }

    // NOTE: saved-variable assertions are implemented in SharedSteps to avoid duplicates across step classes

    // ========== REQUEST BODY BUILDERS ==========

    @When("user sets request body:")
    public void userSetsRequestBody(String body) {
        requestBody = replaceVariables(body);
        LogManager.info("Set request body: " + requestBody);
    }

    /**
     * Load request body from external file (supports JSON/XML/text).
     * Searches in: 1) Absolute path, 2) testData folder, 3) Classpath resources
     * Supports variable replacement using ${variableName} syntax.
     * 
     * Examples:
     *   When user sets request body from file "testData/createUser.json"
     *   When user sets request body from file "testData/soapRequest.xml"
     *   When user sets request body from file "/absolute/path/to/request.json"
     */
    @When("user sets request body from file {string}")
    public void userSetsRequestBodyFromFile(String filePath) {
        String resolved = replaceVariables(filePath);
        String content = loadFileContent(resolved);
        requestBody = replaceVariables(content);
        LogManager.info("Loaded and processed request body from: " + resolved);
    }

    /**
     * Load request body from JSON file in testData folder with variable replacement.
     * Automatically looks in src/test/resources/testData/ folder.
     * 
     * Examples:
     *   When user sets request body from json file "createUser.json"
     *   When user sets request body from json file "posts/createPost.json"
     */
    @When("user sets request body from json file {string}")
    public void userSetsRequestBodyFromJsonFile(String fileName) {
        String filePath = "testData/" + replaceVariables(fileName);
        String content = loadFileContent(filePath);
        requestBody = replaceVariables(content);
        LogManager.info("Loaded JSON request body from testData: " + fileName);
    }

    /**
     * Load request body from XML file with variable replacement (for SOAP/REST XML).
     * 
     * Examples:
     *   When user sets request body from xml file "testData/soapRequest.xml"
     *   When user sets request body from xml file "testData/restRequest.xml"
     */
    @When("user sets request body from xml file {string}")
    public void userSetsRequestBodyFromXmlFile(String filePath) {
        String resolved = replaceVariables(filePath);
        String content = loadFileContent(resolved);
        requestBody = replaceVariables(content);
        LogManager.info("Loaded XML request body from: " + resolved);
    }

    /**
     * Load SOAP envelope from XML file with variable replacement.
     * 
     * Examples:
     *   When user sets SOAP envelope from file "testData/soap/getUser.xml"
     *   When user sets SOAP envelope from file "testData/soap/createOrder.xml"
     */
    @When("user sets SOAP envelope from file {string}")
    public void userSetsSoapEnvelopeFromFile(String filePath) {
        String resolved = replaceVariables(filePath);
        String content = loadFileContent(resolved);
        requestBody = replaceVariables(content);
        LogManager.info("Loaded SOAP envelope from: " + resolved);
    }

    /**
     * Load SOAP envelope from file and send to endpoint.
     * 
     * Examples:
     *   When user sends SOAP request from file "testData/soap/getUser.xml" to "/soap/userService"
     */
    @When("user sends SOAP request from file {string} to {string}")
    public void userSendsSoapRequestFromFile(String filePath, String endpoint) {
        userSetsSoapEnvelopeFromFile(filePath);
        sendSoapRequest(replaceVariables(endpoint), "");
    }

    /**
     * Load request body from JSON file and send POST request.
     * 
     * Examples:
     *   When user sends POST request from file "testData/createUser.json" to "/users"
     *   When user sends POST request from file "createPost.json" to "/posts"
     */
    @When("user sends POST request from file {string} to {string}")
    public void userSendsPostRequestFromFile(String filePath, String endpoint) {
        userSetsRequestBodyFromJsonFile(filePath);
        sendPostRequest(replaceVariables(endpoint), requestBody);
    }

    /**
     * Load request body from JSON file and send PUT request.
     * 
     * Examples:
     *   When user sends PUT request from file "testData/updateUser.json" to "/users/1"
     *   When user sends PUT request from file "updatePost.json" to "/posts/${postId}"
     */
    @When("user sends PUT request from file {string} to {string}")
    public void userSendsPutRequestFromFile(String filePath, String endpoint) {
        userSetsRequestBodyFromJsonFile(filePath);
        sendPutRequest(replaceVariables(endpoint), requestBody);
    }

    /**
     * Load request body from file with custom content type.
     * 
     * Examples:
     *   When user sends POST request from file "testData/data.xml" to "/api/xml" with content type "application/xml"
     */
    @When("user sends POST request from file {string} to {string} with content type {string}")
    public void userSendsPostRequestFromFileWithContentType(String filePath, String endpoint, String contentType) {
        String resolved = replaceVariables(filePath);
        String content = loadFileContent(resolved);
        requestBody = replaceVariables(content);
        userSetsContentType(contentType);
        sendPostRequest(replaceVariables(endpoint), requestBody);
    }

    /**
     * Helper method to load file content from multiple locations.
     * Search order: 1) Absolute path, 2) testData folder, 3) Classpath
     */
    private String loadFileContent(String filePath) {
        try {
            // Try absolute path first
            File f = new File(filePath);
            if (f.exists() && f.isFile()) {
                return Files.readString(Paths.get(f.getAbsolutePath()), StandardCharsets.UTF_8);
            }
            
            // Try testData folder
            String testDataPath = "src/test/resources/testData/" + filePath;
            f = new File(testDataPath);
            if (f.exists() && f.isFile()) {
                return Files.readString(Paths.get(f.getAbsolutePath()), StandardCharsets.UTF_8);
            }
            
            // Try classpath
            return readClasspathResourceAsString(filePath);
        } catch (IOException e) {
            throw new AssertionError("Failed to load file: " + filePath + ", error: " + e.getMessage());
        }
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

    /**
     * Set query parameters using Cucumber DataTable.
     * 
     * Example:
     *   When user sets query parameters:
     *     | page   | 1      |
     *     | limit  | 10     |
     *     | sort   | name   |
     *     | filter | active |
     */
    @When("user sets query parameters:")
    public void userSetsQueryParameters(io.cucumber.datatable.DataTable dataTable) {
        java.util.List<java.util.List<String>> rows = dataTable.asLists(String.class);
        for (java.util.List<String> row : rows) {
            if (row.size() >= 2) {
                String key = replaceVariables(row.get(0));
                String value = replaceVariables(row.get(1));
                queryParams.put(key, value);
                LogManager.info("Set query param '" + key + "' = " + value);
            }
        }
    }

    /**
     * Set query parameters from external properties file.
     * File format: key=value (one per line)
     * 
     * Example:
     *   When user sets query parameters from file "testData/queryParams/searchParams.properties"
     */
    @When("user sets query parameters from file {string}")
    public void userSetsQueryParametersFromFile(String filePath) {
        String resolved = replaceVariables(filePath);
        try {
            String content = loadFileContent(resolved);
            String[] lines = content.split("\n");
            for (String line : lines) {
                line = line.trim();
                if (!line.isEmpty() && !line.startsWith("#") && line.contains("=")) {
                    String[] parts = line.split("=", 2);
                    String key = replaceVariables(parts[0].trim());
                    String value = replaceVariables(parts[1].trim());
                    queryParams.put(key, value);
                    LogManager.info("Set query param from file: '" + key + "' = " + value);
                }
            }
        } catch (Exception e) {
            throw new AssertionError("Failed to load query parameters from file: " + resolved + ", error: " + e.getMessage());
        }
    }

    /**
     * Set query parameters from JSON file.
     * 
     * Example:
     *   When user sets query parameters from json file "testData/queryParams/searchParams.json"
     */
    @When("user sets query parameters from json file {string}")
    public void userSetsQueryParametersFromJsonFile(String filePath) {
        String resolved = replaceVariables(filePath);
        try {
            String content = loadFileContent(resolved);
            content = replaceVariables(content);
            JsonElement element = JsonParser.parseString(content);
            if (element.isJsonObject()) {
                com.google.gson.JsonObject jsonObject = element.getAsJsonObject();
                for (String key : jsonObject.keySet()) {
                    String value = jsonObject.get(key).getAsString();
                    queryParams.put(key, value);
                    LogManager.info("Set query param from JSON: '" + key + "' = " + value);
                }
            }
        } catch (Exception e) {
            throw new AssertionError("Failed to load query parameters from JSON file: " + resolved + ", error: " + e.getMessage());
        }
    }

    /**
     * Set query parameters using inline format: key1=value1&key2=value2
     * 
     * Example:
     *   When user sets query parameters "page=1&limit=10&sort=name&filter=active"
     */
    @When("user sets query parameters {string}")
    public void userSetsQueryParametersInline(String queryString) {
        String resolved = replaceVariables(queryString);
        String[] pairs = resolved.split("&");
        for (String pair : pairs) {
            if (pair.contains("=")) {
                String[] kv = pair.split("=", 2);
                String key = kv[0].trim();
                String value = kv.length > 1 ? kv[1].trim() : "";
                queryParams.put(key, value);
                LogManager.info("Set query param: '" + key + "' = " + value);
            }
        }
    }

    /**
     * Send GET request with query parameters from DataTable.
     * 
     * Example:
     *   When user sends GET request to "/users" with query parameters:
     *     | page  | 1    |
     *     | limit | 10   |
     */
    @When("user sends GET request to {string} with query parameters:")
    public void userSendsGETRequestWithQueryParametersDataTable(String endpoint, io.cucumber.datatable.DataTable dataTable) {
        userSetsQueryParameters(dataTable);
        sendGetWithQueryParams(replaceVariables(endpoint), queryParams);
        queryParams.clear();
    }

    /**
     * Send POST request with query parameters.
     * 
     * Example:
     *   When user sends POST request to "/users" with query parameters:
     *     | notify | true |
     *     | async  | false|
     */
    @When("user sends POST request to {string} with query parameters:")
    public void userSendsPOSTRequestWithQueryParameters(String endpoint, io.cucumber.datatable.DataTable dataTable) {
        userSetsQueryParameters(dataTable);
        sendPostWithQueryParams(replaceVariables(endpoint), requestBody, queryParams);
        queryParams.clear();
    }

    /**
     * Clear all query parameters.
     * 
     * Example:
     *   When user clears query parameters
     */
    @When("user clears query parameters")
    public void userClearsQueryParameters() {
        queryParams.clear();
        LogManager.info("Cleared all query parameters");
    }

    /**
     * Send request with query parameters from file.
     * 
     * Example:
     *   When user sends GET request to "/users" with query parameters from file "testData/queryParams/userSearch.properties"
     */
    @When("user sends GET request to {string} with query parameters from file {string}")
    public void userSendsGETRequestWithQueryParamsFromFile(String endpoint, String filePath) {
        userSetsQueryParametersFromFile(filePath);
        sendGetWithQueryParams(replaceVariables(endpoint), queryParams);
        queryParams.clear();
    }

    @When("user sends {string} request to {string} with headers")
    public void userSendsRequestWithHeaders(String method, String endpoint) {
        sendRequestWithHeaders(method, replaceVariables(endpoint), headers);
        headers.clear();
    }

    // ------------------ Additional Common API Steps ------------------

    @When("user sets bearer token from response json {string} as header {string}")
    public void userSetsBearerTokenFromResponse(String jsonPath, String headerName) {
        Object tokenObj = getJsonPathValue(jsonPath);
        String token = tokenObj == null ? null : tokenObj.toString();
        if (token != null) {
            headers.put(headerName, "Bearer " + token);
            LogManager.info("Set header '" + headerName + "' with bearer token from json path: " + jsonPath);
        } else {
            LogManager.info("Token at json path '" + jsonPath + "' was null; header not set");
        }
    }

    @When("user applies headers")
    public void userAppliesHeaders() {
        if (!headers.isEmpty()) {
            APIClient.setHeaders(headers);
            LogManager.info("Applied headers: " + headers);
            headers.clear();
        }
    }

    @When("user clears request spec and headers")
    public void userClearsRequestSpecAndHeaders() {
        APIClient.clearRequestSpec();
        headers.clear();
        LogManager.info("Cleared request spec and local headers map");
    }

    @When("user sets basic auth with username {string} and password {string}")
    public void userSetsBasicAuth(String username, String password) {
        APIClient.getRequestSpec().auth().preemptive().basic(replaceVariables(username), replaceVariables(password));
        LogManager.info("Set basic auth for user: " + username);
    }

    @When("user sets content type {string}")
    public void userSetsContentType(String contentType) {
        APIClient.setHeader("Content-Type", replaceVariables(contentType));
        LogManager.info("Set Content-Type: " + contentType);
    }

    @When("user sets accept header {string}")
    public void userSetsAcceptHeader(String accept) {
        APIClient.setHeader("Accept", replaceVariables(accept));
        LogManager.info("Set Accept: " + accept);
    }

    @When("user uploads file {string} to {string} as multipart field {string}")
    public void userUploadsFileMultipart(String filePath, String endpoint, String fieldName) {
        File f = new File(replaceVariables(filePath));
        if (!f.exists()) {
            throw new AssertionError("File not found: " + f.getAbsolutePath());
        }
        response = APIClient.getRequestSpec().multiPart(fieldName, f).post(replaceVariables(endpoint));
        LogManager.info("Uploaded file '" + f.getAbsolutePath() + "' to endpoint: " + endpoint + " as field: " + fieldName);
    }

    @Then("response should match json schema {string}")
    public void responseShouldMatchJsonSchema(String schemaPath) {
        // schemaPath should be a classpath resource like "schemas/response-schema.json"
        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath(replaceVariables(schemaPath)));
        LogManager.info("Validated response against JSON schema: " + schemaPath);
    }

    @When("user saves response header {string} as {string}")
    public void userSavesResponseHeader(String headerName, String variableName) {
        String val = getHeader(headerName);
        ScenarioContext.set(variableName, val);
        LogManager.info("Saved response header '" + headerName + "' as variable '" + variableName + "' = " + val);
    }

    @When("user polls {string} until json path {string} equals {string} within {int} seconds interval {int} seconds")
    public void userPollsUntilJsonPathEquals(String endpoint, String jsonPath, String expectedValue, int timeoutSeconds, int intervalSeconds) throws InterruptedException {
        long end = System.currentTimeMillis() + timeoutSeconds * 1000L;
        boolean matched = false;
        String resolvedEndpoint = replaceVariables(endpoint);
        String expected = replaceVariables(expectedValue);
        while (System.currentTimeMillis() < end) {
            sendGetRequest(resolvedEndpoint);
            Object val = getJsonPathValue(jsonPath);
            if (val != null && expected.equals(val.toString())) {
                matched = true;
                break;
            }
            Thread.sleep(Math.max(500, intervalSeconds * 1000L));
        }
        if (!matched) {
            throw new AssertionError("Condition not met within timeout: json path '" + jsonPath + "' did not equal '" + expected + "'");
        }
        LogManager.info("Polling succeeded: json path '" + jsonPath + "' equals '" + expected + "'");
    }

    @When("user saves response time as {string}")
    public void userSavesResponseTime(String variableName) {
        String t = String.valueOf(getResponseTime());
        ScenarioContext.set(variableName, t);
        LogManager.info("Saved response time as '" + variableName + "' = " + t + "ms");
    }

    @Then("response header {string} should contain {string}")
    public void responseHeaderShouldContain(String headerName, String expectedText) {
        String actual = getHeader(headerName);
        assertContains(actual, replaceVariables(expectedText), "Header contains validation for '" + headerName + "'");
    }

    @When("user retries last request up to {int} times with interval {int} seconds until status {int}")
    public void userRetriesLastRequest(int maxAttempts, int intervalSeconds, int expectedStatus) throws InterruptedException {
        int attempts = 0;
        boolean success = false;
        while (attempts < maxAttempts) {
            attempts++;
            if (response != null && response.getStatusCode() == expectedStatus) {
                success = true; break;
            }
            Thread.sleep(Math.max(500, intervalSeconds * 1000L));
        }
        if (!success) throw new AssertionError("Failed to reach status " + expectedStatus + " after " + maxAttempts + " attempts");
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
        validateFieldValue(xpath, replaceVariables(expectedValue));
    }

    // ========== UTILITY METHODS ==========

    @When("user logs response")
    public void userLogsResponse() {
        logResponse();
    }

    @When("user clears saved variables")
    public void userClearsSavedVariables() {
        LogManager.info("Clearing scenario context saved variables");
        ScenarioContext.clear();
    }

    private String replaceVariables(String text) {
        String result = text;
        for (Map.Entry<String, Object> entry : ScenarioContext.getAll().entrySet()) {
            String placeholder = "${" + entry.getKey() + "}";
            if (result != null && result.contains(placeholder)) {
                result = result.replace(placeholder, String.valueOf(entry.getValue()));
                LogManager.info("Replaced variable: " + placeholder + " -> " + entry.getValue());
            }
        }
        return result;
    }

    private String readClasspathResourceAsString(String path) throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream(path);
        if (is == null) throw new IOException("Resource not found on classpath: " + path);
        try (InputStreamReader r = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            StringBuilder sb = new StringBuilder();
            char[] buf = new char[4096];
            int len;
            while ((len = r.read(buf)) != -1) sb.append(buf, 0, len);
            return sb.toString();
        }
    }

    @Given("Access {string} with {string} for {string}")
    public void access_with_for(String url, String headers, String tcId) throws IOException, CsvException {
        String testDataFile = "src/test/resources/testdata.csv";
        Map<String, String> td = com.automation.core.utils.DataDrivenUtils.getTestDataByTcId(testDataFile, tcId);
        if (td == null) td = new HashMap<>();
        String resolvedUrl = td.getOrDefault("url", url);
        String resolvedHeaders = td.getOrDefault("headers", headers);
        // ensure testData map is set and contains url/headers for later Send steps
        this.testData = td;
        this.testData.put("url", resolvedUrl);
        this.testData.put("headers", resolvedHeaders);
        // Set up headers if provided (from testData or argument)
        this.headers.clear();
        if (resolvedHeaders != null && !resolvedHeaders.equalsIgnoreCase("NA") && !resolvedHeaders.isEmpty()) {
            // If headers is a JSON string, parse and add to headers map
            try {
                org.json.JSONObject jsonHeaders = new org.json.JSONObject(resolvedHeaders);
                for (String key : jsonHeaders.keySet()) {
                    this.headers.put(key, jsonHeaders.getString(key));
                }
            } catch (Exception e) {
                // If not JSON, treat as single header: key:value
                if (resolvedHeaders.contains(":")) {
                    String[] parts = resolvedHeaders.split(":", 2);
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
