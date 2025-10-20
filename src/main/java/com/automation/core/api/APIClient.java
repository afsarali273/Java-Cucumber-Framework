package com.automation.core.api;

import com.automation.core.config.ConfigManager;
import com.automation.core.logging.LogManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

public class APIClient {
    private static final ThreadLocal<RequestSpecification> requestSpec = new ThreadLocal<>();
    private static final ThreadLocal<String> currentBaseUrl = new ThreadLocal<>();

    public static void initializeAPIClient() {
        String baseUrl = ConfigManager.getInstance().getApiBaseUrl();
        RestAssured.baseURI = baseUrl;
        requestSpec.set(RestAssured.given().header("Content-Type", "application/json"));
        currentBaseUrl.set(baseUrl);
        LogManager.info("API Client initialized with base URL: " + baseUrl);
    }

    public static RequestSpecification getRequestSpec() {
        if (requestSpec.get() == null) {
            initializeAPIClient();
        }
        return requestSpec.get();
    }

    public static APIClient withBaseUrl(String serviceName) {
        String baseUrl = ConfigManager.getInstance().getApiBaseUrl(serviceName);
        RestAssured.baseURI = baseUrl;
        requestSpec.set(RestAssured.given().header("Content-Type", "application/json"));
        currentBaseUrl.set(baseUrl);
        LogManager.info("API Client switched to service: " + serviceName + " with base URL: " + baseUrl);
        return new APIClient();
    }

    public static APIClient withBaseUrl(String serviceName, Map<String, String> headers) {
        String baseUrl = ConfigManager.getInstance().getApiBaseUrl(serviceName);
        RestAssured.baseURI = baseUrl;
        RequestSpecification spec = RestAssured.given().header("Content-Type", "application/json");
        if (headers != null) {
            spec.headers(headers);
        }
        requestSpec.set(spec);
        currentBaseUrl.set(baseUrl);
        LogManager.info("API Client switched to service: " + serviceName + " with base URL: " + baseUrl);
        return new APIClient();
    }

    public static APIClient withCustomBaseUrl(String customUrl) {
        RestAssured.baseURI = customUrl;
        requestSpec.set(RestAssured.given().header("Content-Type", "application/json"));
        currentBaseUrl.set(customUrl);
        LogManager.info("API Client using custom base URL: " + customUrl);
        return new APIClient();
    }

    public static Response get(String endpoint) {
        LogManager.info("GET Request: " + getFullUrl(endpoint));
        Response response = getRequestSpec().get(endpoint);
        LogManager.info("Response Status: " + response.getStatusCode());
        return response;
    }

    public Response getRequest(String endpoint) {
        return get(endpoint);
    }

    public static Response get(String endpoint, Map<String, String> queryParams) {
        LogManager.info("GET Request: " + getFullUrl(endpoint) + " with params: " + queryParams);
        Response response = getRequestSpec().queryParams(queryParams).get(endpoint);
        LogManager.info("Response Status: " + response.getStatusCode());
        return response;
    }

    public Response getRequest(String endpoint, Map<String, String> queryParams) {
        return get(endpoint, queryParams);
    }

    public static Response post(String endpoint, Object body) {
        LogManager.info("POST Request: " + getFullUrl(endpoint));
        Response response = getRequestSpec().body(body).post(endpoint);
        LogManager.info("Response Status: " + response.getStatusCode());
        return response;
    }

    public Response postRequest(String endpoint, Object body) {
        return post(endpoint, body);
    }

    public static Response put(String endpoint, Object body) {
        LogManager.info("PUT Request: " + getFullUrl(endpoint));
        Response response = getRequestSpec().body(body).put(endpoint);
        LogManager.info("Response Status: " + response.getStatusCode());
        return response;
    }

    public Response putRequest(String endpoint, Object body) {
        return put(endpoint, body);
    }

    public static Response delete(String endpoint) {
        LogManager.info("DELETE Request: " + getFullUrl(endpoint));
        Response response = getRequestSpec().delete(endpoint);
        LogManager.info("Response Status: " + response.getStatusCode());
        return response;
    }

    public Response deleteRequest(String endpoint) {
        return delete(endpoint);
    }

    public static void setHeader(String key, String value) {
        requestSpec.set(getRequestSpec().header(key, value));
    }

    public static void setHeaders(Map<String, String> headers) {
        requestSpec.set(getRequestSpec().headers(headers));
    }

    public static void clearRequestSpec() {
        requestSpec.remove();
        currentBaseUrl.remove();
    }

    private static String getFullUrl(String endpoint) {
        String baseUrl = currentBaseUrl.get();
        if (baseUrl == null) {
            baseUrl = RestAssured.baseURI;
        }
        return baseUrl + endpoint;
    }
}
