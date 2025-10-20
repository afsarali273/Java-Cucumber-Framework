package com.automation.reusables;

import com.automation.core.api.APIClient;
import com.automation.core.assertions.AssertUtils;
import com.automation.core.logging.LogManager;
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
        LogManager.info("Sending GET request to: " + endpoint);
        response = currentService != null ? APIClient.withBaseUrl(currentService).getRequest(endpoint) : APIClient.get(endpoint);
        LogManager.info("Response Status: " + response.getStatusCode() + " | Time: " + response.getTime() + "ms");
        currentService = null;
        return response;
    }

    protected Response sendPostRequest(String endpoint, String body) {
        LogManager.info("Sending POST request to: " + endpoint);
        LogManager.info("Request Body: " + body);
        response = currentService != null ? APIClient.withBaseUrl(currentService).postRequest(endpoint, body) : APIClient.post(endpoint, body);
        LogManager.info("Response Status: " + response.getStatusCode() + " | Time: " + response.getTime() + "ms");
        currentService = null;
        return response;
    }

    protected Response sendPutRequest(String endpoint, String body) {
        LogManager.info("Sending PUT request to: " + endpoint);
        LogManager.info("Request Body: " + body);
        response = currentService != null ? APIClient.withBaseUrl(currentService).putRequest(endpoint, body) : APIClient.put(endpoint, body);
        LogManager.info("Response Status: " + response.getStatusCode() + " | Time: " + response.getTime() + "ms");
        currentService = null;
        return response;
    }

    protected Response sendDeleteRequest(String endpoint) {
        LogManager.info("Sending DELETE request to: " + endpoint);
        response = currentService != null ? APIClient.withBaseUrl(currentService).deleteRequest(endpoint) : APIClient.delete(endpoint);
        LogManager.info("Response Status: " + response.getStatusCode() + " | Time: " + response.getTime() + "ms");
        currentService = null;
        return response;
    }

    protected String createJsonBody(String title, String body, int userId) {
        JSONObject json = new JSONObject();
        json.put("title", title);
        json.put("body", body);
        json.put("userId", userId);
        requestBody = json.toString();
        LogManager.info("Created JSON body: " + requestBody);
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
        LogManager.info("Validating JSON path: " + jsonPath + " = " + expectedValue);
        Object actualValue = response.jsonPath().get(jsonPath);
        assertEquals(actualValue, expectedValue, "JsonPath '" + jsonPath + "' validation");
    }

    protected void validateJsonPathContains(String jsonPath, String expectedText) {
        LogManager.info("Validating JSON path '" + jsonPath + "' contains: " + expectedText);
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
        LogManager.info("Validating array size for '" + jsonPath + "' = " + expectedSize);
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
        LogManager.info("Validating response time: " + responseTime + "ms < " + maxTimeInMs + "ms");
        assertLessThan((int) responseTime, (int) maxTimeInMs, "Response time should be less than " + maxTimeInMs + "ms");
    }

    protected void validateContentType(String expectedContentType) {
        String actualContentType = response.getContentType();
        assertContains(actualContentType, expectedContentType, "Content-Type validation");
    }

    protected void validateHeader(String headerName, String expectedValue) {
        LogManager.info("Validating header '" + headerName + "' = " + expectedValue);
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
        LogManager.info("Created JSON body: " + requestBody);
        return requestBody;
    }

    protected String createJsonArrayBody(List<Map<String, Object>> dataList) {
        JSONArray jsonArray = new JSONArray();
        for (Map<String, Object> data : dataList) {
            jsonArray.put(new JSONObject(data));
        }
        requestBody = jsonArray.toString();
        LogManager.info("Created JSON array body: " + requestBody);
        return requestBody;
    }

    protected Response sendRequestWithHeaders(String method, String endpoint, Map<String, String> headers) {
        LogManager.info("Sending " + method + " request to: " + endpoint + " with headers: " + headers);
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
        LogManager.info("Response Status: " + response.getStatusCode() + " | Time: " + response.getTime() + "ms");
        currentService = null;
        return response;
    }

    protected Response sendGetWithQueryParams(String endpoint, Map<String, String> queryParams) {
        LogManager.info("Sending GET request to: " + endpoint + " with query params: " + queryParams);
        response = currentService != null ? APIClient.withBaseUrl(currentService).getRequest(endpoint, queryParams) : APIClient.get(endpoint, queryParams);
        LogManager.info("Response Status: " + response.getStatusCode() + " | Time: " + response.getTime() + "ms");
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
        LogManager.info("Created SOAP envelope for action: " + soapAction);
        return requestBody;
    }

    protected Response sendSoapRequest(String endpoint, String soapAction) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/xml; charset=utf-8");
        headers.put("SOAPAction", soapAction);
        LogManager.info("Sending SOAP request to: " + endpoint + " with action: " + soapAction);
        LogManager.info("SOAP Request Body: " + requestBody);
        APIClient.setHeaders(headers);
        response = APIClient.post(endpoint, requestBody);
        LogManager.info("Response Status: " + response.getStatusCode() + " | Time: " + response.getTime() + "ms");
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
        LogManager.info("Extracted JSON path '" + jsonPath + "' value: " + value);
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
        LogManager.info("Response Status: " + response.getStatusCode());
        LogManager.info("Response Time: " + response.getTime() + "ms");
        LogManager.info("Response Body: " + response.getBody().asString());
    }

    protected void switchToService(String serviceName) {
        currentService = serviceName;
        LogManager.info("Switched to API service: " + serviceName);
    }

    protected void useCustomBaseUrl(String customUrl) {
        APIClient.withCustomBaseUrl(customUrl);
        LogManager.info("Using custom base URL: " + customUrl);
    }
}