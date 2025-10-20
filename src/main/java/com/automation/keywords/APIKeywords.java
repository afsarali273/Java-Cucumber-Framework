package com.automation.keywords;

import com.automation.core.api.APIClient;
import com.automation.core.logging.LogManager;
import io.restassured.response.Response;

import java.util.Map;

/**
 * Manual QA-friendly keywords for API automation
 * Simple methods for API testing without deep technical knowledge
 */
public class APIKeywords {
    
    private static Response lastResponse;
    
    public static void initializeAPI() {
        APIClient.initializeAPIClient();
        LogManager.info("API initialized");
    }
    
    public static void sendGETRequest(String endpoint) {
        lastResponse = APIClient.get(endpoint);
        LogManager.info("GET request sent to: " + endpoint);
    }
    
    public static void sendPOSTRequest(String endpoint, String jsonBody) {
        lastResponse = APIClient.post(endpoint, jsonBody);
        LogManager.info("POST request sent to: " + endpoint);
    }
    
    public static void sendPUTRequest(String endpoint, String jsonBody) {
        lastResponse = APIClient.put(endpoint, jsonBody);
        LogManager.info("PUT request sent to: " + endpoint);
    }
    
    public static void sendDELETERequest(String endpoint) {
        lastResponse = APIClient.delete(endpoint);
        LogManager.info("DELETE request sent to: " + endpoint);
    }
    
    public static int getResponseStatusCode() {
        int statusCode = lastResponse.getStatusCode();
        LogManager.info("Response status code: " + statusCode);
        return statusCode;
    }
    
    public static String getResponseBody() {
        String body = lastResponse.getBody().asString();
        LogManager.info("Response body retrieved");
        return body;
    }
    
    public static String getResponseField(String fieldName) {
        String value = lastResponse.jsonPath().getString(fieldName);
        LogManager.info("Field '" + fieldName + "' value: " + value);
        return value;
    }
    
    public static void addHeader(String key, String value) {
        APIClient.setHeader(key, value);
        LogManager.info("Added header: " + key + " = " + value);
    }
    
    public static void addHeaders(Map<String, String> headers) {
        APIClient.setHeaders(headers);
        LogManager.info("Added multiple headers");
    }
    
    public static boolean responseContains(String text) {
        boolean contains = lastResponse.getBody().asString().contains(text);
        LogManager.info("Response contains '" + text + "': " + contains);
        return contains;
    }

    /**
     * Sample usage for TestNG/Modular execution:
     *
     * In your test class, you can use APIKeywords for API automation:
     *
     * import com.automation.keywords.APIKeywords;
     *
     * @Test(dataProvider = "API", dataProviderClass = TestNgModularConfig.class)
     * public void apiTest() {
     *     APIKeywords.initializeAPI();
     *     APIKeywords.addHeader("Authorization", "Bearer token");
     *     APIKeywords.sendPOSTRequest("/users", "{\"name\":\"John\"}");
     *     int status = APIKeywords.getResponseStatusCode();
     *     String body = APIKeywords.getResponseBody();
     *     assert status == 201;
     *     assert APIKeywords.responseContains("John");
     * }
     *
     * The APIKeywords class provides simple, reusable methods for API testing in both Cucumber and TestNG/Modular execution modes.
     */
}
