package com.automation.reusables;

import com.automation.core.api.APIClient;
import com.automation.core.assertions.AssertUtils;
//import com.automation.core.logging.LogManager;
import com.automation.core.logging.UnifiedLogger;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class APIReusable {
    
    protected Response response;
    protected String requestBody;
    protected String currentService;


    protected Response sendGetRequest(String endpoint) {
        UnifiedLogger.action("GET Request", endpoint);
        response = currentService != null ? APIClient.withBaseUrl(currentService).getRequest(endpoint) : APIClient.get(endpoint);
        UnifiedLogger.info("Response Status: " + response.getStatusCode() + " | Time: " + response.getTime() + "ms");
        currentService = null;
        return response;
    }

    protected Response sendPostRequest(String endpoint, String body) {
        UnifiedLogger.action("POST Request", endpoint + " | Body: " + body);
        response = currentService != null ? APIClient.withBaseUrl(currentService).postRequest(endpoint, body) : APIClient.post(endpoint, body);
        UnifiedLogger.info("Response Status: " + response.getStatusCode() + " | Time: " + response.getTime() + "ms");
        currentService = null;
        return response;
    }

    protected Response sendPutRequest(String endpoint, String body) {
        UnifiedLogger.action("PUT Request", endpoint + " | Body: " + body);
        response = currentService != null ? APIClient.withBaseUrl(currentService).putRequest(endpoint, body) : APIClient.put(endpoint, body);
        UnifiedLogger.info("Response Status: " + response.getStatusCode() + " | Time: " + response.getTime() + "ms");
        currentService = null;
        return response;
    }

    protected Response sendDeleteRequest(String endpoint) {
        UnifiedLogger.action("DELETE Request", endpoint);
        response = currentService != null ? APIClient.withBaseUrl(currentService).deleteRequest(endpoint) : APIClient.delete(endpoint);
        UnifiedLogger.info("Response Status: " + response.getStatusCode() + " | Time: " + response.getTime() + "ms");
        currentService = null;
        return response;
    }

    protected String createJsonBody(String title, String body, int userId) {
        JSONObject json = new JSONObject();
        json.put("title", title);
        json.put("body", body);
        json.put("userId", userId);
        requestBody = json.toString();
        UnifiedLogger.info("Created JSON body: " + requestBody);
        return requestBody;
    }

    protected void validateStatusCode(int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        assertEquals(actualStatusCode, expectedStatusCode, "Status code validation");
    }

    protected void validateResponseContainsField(String fieldName) {
        String responseBody = response.getBody().asString();
        assertContains(responseBody, fieldName, "Response should contain field: " + fieldName);
    }

    protected void validateFieldValue(String fieldName, String expectedValue) {
        String actualValue = response.jsonPath().getString(fieldName);
        assertEquals(actualValue, expectedValue, "Field '" + fieldName + "' validation");
    }

    protected void validateFieldNotNull(String fieldName) {
        Object value = response.jsonPath().get(fieldName);
        assertNotNull(value, "Field '" + fieldName + "' should not be null");
    }

    protected String getFieldValue(String fieldName) {
        return response.jsonPath().getString(fieldName);
    }

    protected int getStatusCode() {
        return response.getStatusCode();
    }

    // ========== JSON PATH VALIDATIONS ==========
    
    protected void validateJsonPath(String jsonPath, Object expectedValue) {
        UnifiedLogger.action("Validate JSON Path", jsonPath + " = " + expectedValue);
        Object actualValue = response.jsonPath().get(jsonPath);
        assertEquals(actualValue, expectedValue, "JsonPath '" + jsonPath + "' validation");
    }

    protected void validateJsonPathContains(String jsonPath, String expectedText) {
        UnifiedLogger.action("Validate JSON Path Contains", jsonPath + " contains: " + expectedText);
        String actualValue = response.jsonPath().getString(jsonPath);
        assertContains(actualValue, expectedText, "JsonPath '" + jsonPath + "' contains validation");
    }

    protected void validateJsonPathNotNull(String jsonPath) {
        Object value = response.jsonPath().get(jsonPath);
        assertNotNull(value, "JsonPath '" + jsonPath + "' should not be null");
    }

    protected void validateJsonPathIsNull(String jsonPath) {
        Object value = response.jsonPath().get(jsonPath);
        assertNull(value, "JsonPath '" + jsonPath + "' should be null");
    }

    protected void validateArraySize(String jsonPath, int expectedSize) {
        UnifiedLogger.action("Validate Array Size", jsonPath + " = " + expectedSize);
        List<Object> array = response.jsonPath().getList(jsonPath);
        assertEquals(array.size(), expectedSize, "Array size validation for '" + jsonPath + "'");
    }

    protected void validateArrayContains(String jsonPath, Object expectedValue) {
        List<Object> array = response.jsonPath().getList(jsonPath);
        assertTrue(array.contains(expectedValue), "Array '" + jsonPath + "' should contain: " + expectedValue);
    }

    protected void validateFieldGreaterThan(String jsonPath, Number expectedValue) {
        Number actualValue = response.jsonPath().get(jsonPath);
        assertGreaterThan((int) actualValue.doubleValue(), (int) expectedValue.doubleValue(), "Field '" + jsonPath + "' should be greater than " + expectedValue);
    }

    protected void validateFieldLessThan(String jsonPath, Number expectedValue) {
        Number actualValue = response.jsonPath().get(jsonPath);
        assertLessThan((int) actualValue.doubleValue(), (int) expectedValue.doubleValue(), "Field '" + jsonPath + "' should be less than " + expectedValue);
    }

    // ========== RESPONSE VALIDATIONS ==========
    
    protected void validateResponseTime(long maxTimeInMs) {
        long responseTime = response.getTime();
        UnifiedLogger.action("Validate Response Time", responseTime + "ms < " + maxTimeInMs + "ms");
        assertLessThan((int) responseTime, (int) maxTimeInMs, "Response time should be less than " + maxTimeInMs + "ms");
    }

    protected void validateContentType(String expectedContentType) {
        String actualContentType = response.getContentType();
        assertContains(actualContentType, expectedContentType, "Content-Type validation");
    }

    protected void validateHeader(String headerName, String expectedValue) {
        UnifiedLogger.action("Validate Header", headerName + " = " + expectedValue);
        String actualValue = response.getHeader(headerName);
        assertEquals(actualValue, expectedValue, "Header '" + headerName + "' validation");
    }

    protected void validateHeaderExists(String headerName) {
        String headerValue = response.getHeader(headerName);
        assertNotNull(headerValue, "Header '" + headerName + "' should exist");
    }

    protected void validateResponseBodyNotEmpty() {
        String responseBody = response.getBody().asString();
        assertFalse(responseBody.isEmpty(), "Response body should not be empty");
    }

    protected void validateResponseBodyContains(String expectedText) {
        String responseBody = response.getBody().asString();
        assertContains(responseBody, expectedText, "Response body should contain: " + expectedText);
    }

    // ========== REQUEST BUILDERS ==========
    
    protected String createJsonBody(Map<String, Object> data) {
        JSONObject json = new JSONObject(data);
        requestBody = json.toString();
        UnifiedLogger.info("Created JSON body: " + requestBody);
        return requestBody;
    }

    protected String createJsonArrayBody(List<Map<String, Object>> dataList) {
        JSONArray jsonArray = new JSONArray();
        for (Map<String, Object> data : dataList) {
            jsonArray.put(new JSONObject(data));
        }
        requestBody = jsonArray.toString();
        UnifiedLogger.info("Created JSON array body: " + requestBody);
        return requestBody;
    }

    protected Response sendRequestWithHeaders(String method, String endpoint, Map<String, String> headers) {
        UnifiedLogger.action(method + " Request with Headers", endpoint + " | Headers: " + headers);
        if (currentService != null) {
            APIClient.withBaseUrl(currentService, headers);
        } else {
            APIClient.setHeaders(headers);
        }
        switch (method.toUpperCase()) {
            case "GET":
                response = currentService != null ? APIClient.withBaseUrl(currentService, headers).getRequest(endpoint) : APIClient.get(endpoint);
                break;
            case "POST":
                response = currentService != null ? APIClient.withBaseUrl(currentService, headers).postRequest(endpoint, requestBody) : APIClient.post(endpoint, requestBody);
                break;
            case "PUT":
                response = currentService != null ? APIClient.withBaseUrl(currentService, headers).putRequest(endpoint, requestBody) : APIClient.put(endpoint, requestBody);
                break;
            case "DELETE":
                response = currentService != null ? APIClient.withBaseUrl(currentService, headers).deleteRequest(endpoint) : APIClient.delete(endpoint);
                break;
        }
        UnifiedLogger.info("Response Status: " + response.getStatusCode() + " | Time: " + response.getTime() + "ms");
        currentService = null;
        return response;
    }

    protected Response sendGetWithQueryParams(String endpoint, Map<String, String> queryParams) {
        UnifiedLogger.info("Sending GET request to: " + endpoint + " with query params: " + queryParams);
        response = currentService != null ? APIClient.withBaseUrl(currentService).getRequest(endpoint, queryParams) : APIClient.get(endpoint, queryParams);
        UnifiedLogger.info("Response Status: " + response.getStatusCode() + " | Time: " + response.getTime() + "ms");
        currentService = null;
        return response;
    }

    // ========== SOAP API SUPPORT ==========
    
    protected String createSoapEnvelope(String soapAction, String soapBody) {
        StringBuilder envelope = new StringBuilder();
        envelope.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                .append("<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">")
                .append("<soap:Header/>")
                .append("<soap:Body>")
                .append(soapBody)
                .append("</soap:Body>")
                .append("</soap:Envelope>");
        requestBody = envelope.toString();
        UnifiedLogger.info("Created SOAP envelope for action: " + soapAction);
        return requestBody;
    }

    protected Response sendSoapRequest(String endpoint, String soapAction) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/xml; charset=utf-8");
        headers.put("SOAPAction", soapAction);
        UnifiedLogger.info("Sending SOAP request to: " + endpoint + " with action: " + soapAction);
        UnifiedLogger.info("SOAP Request Body: " + requestBody);
        APIClient.setHeaders(headers);
        response = APIClient.post(endpoint, requestBody);
        UnifiedLogger.info("Response Status: " + response.getStatusCode() + " | Time: " + response.getTime() + "ms");
        return response;
    }

    protected void validateSoapFault(boolean shouldHaveFault) {
        String responseBody = response.getBody().asString();
        boolean hasFault = responseBody.contains("soap:Fault") || responseBody.contains("faultcode");
        if (shouldHaveFault) {
            assertTrue(hasFault, "SOAP response should contain fault");
        } else {
            assertFalse(hasFault, "SOAP response should not contain fault");
        }
    }

    protected String extractSoapValue(String xpath) {
        return response.xmlPath().getString(xpath);
    }


    // ========== ASSERTION METHODS ==========

    protected void assertEquals(Object actual, Object expected, String message) {
        AssertUtils.assertEquals(actual, expected, message);
    }

    protected void assertNotEquals(Object actual, Object expected, String message) {
        AssertUtils.assertNotEquals(actual, expected, message);
    }

    protected void assertTrue(boolean condition, String message) {
        AssertUtils.assertTrue(condition, message);
    }

    protected void assertFalse(boolean condition, String message) {
        AssertUtils.assertFalse(condition, message);
    }

    protected void assertNull(Object object, String message) {
        AssertUtils.assertNull(object, message);
    }

    protected void assertNotNull(Object object, String message) {
        AssertUtils.assertNotNull(object, message);
    }

    protected void assertContains(String actual, String expected, String message) {
        AssertUtils.assertContains(actual, expected, message);
    }

    protected void assertGreaterThan(int actual, int expected, String message) {
        AssertUtils.assertGreaterThan(actual, expected, message);
    }

    protected void assertLessThan(int actual, int expected, String message) {
        AssertUtils.assertLessThan(actual, expected, message);
    }

    protected void assertGreaterThanOrEqual(int actual, int expected, String message) {
        AssertUtils.assertGreaterThanOrEqual(actual, expected, message);
    }

    protected void assertLessThanOrEqual(int actual, int expected, String message) {
        AssertUtils.assertLessThanOrEqual(actual, expected, message);
    }

    protected void enableSoftAssert() {
        AssertUtils.enableSoftAssert();
    }

    protected void assertAll() {
        AssertUtils.assertAll();
    }

    // ========== UTILITY METHODS ==========
    
    protected Object getJsonPathValue(String jsonPath) {
        Object value = response.jsonPath().get(jsonPath);
        UnifiedLogger.info("Extracted JSON path '" + jsonPath + "' value: " + value);
        return value;
    }

    protected List<Object> getJsonPathList(String jsonPath) {
        return response.jsonPath().getList(jsonPath);
    }

    protected String getResponseBody() {
        return response.getBody().asString();
    }

    protected long getResponseTime() {
        return response.getTime();
    }

    protected String getHeader(String headerName) {
        return response.getHeader(headerName);
    }

    protected void logResponse() {
        UnifiedLogger.info("Response Status: " + response.getStatusCode());
        UnifiedLogger.info("Response Time: " + response.getTime() + "ms");
        UnifiedLogger.info("Response Body: " + response.getBody().asString());
    }

    protected void switchToService(String serviceName) {
        currentService = serviceName;
        UnifiedLogger.info("Switched to API service: " + serviceName);
    }

    protected void useCustomBaseUrl(String customUrl) {
        APIClient.withCustomBaseUrl(customUrl);
        UnifiedLogger.info("Using custom base URL: " + customUrl);
    }
}