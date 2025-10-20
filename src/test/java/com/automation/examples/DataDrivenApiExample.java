package com.automation.examples;

import com.automation.core.utils.DataDrivenUtils;
import com.automation.reusables.APIReusable;
import io.restassured.response.Response;
import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.util.Map;

/**
 * Example usage of DataDrivenUtils for API data-driven testing.
 */
public class DataDrivenApiExample extends APIReusable {
    public void runTestFromCsv(String filePath, String tcId) throws IOException, CsvException {
        // Fetch test data row by tc_id from CSV
        Map<String, String> testData = DataDrivenUtils.getTestDataByTcId(filePath, tcId);
        if (testData == null) {
            throw new RuntimeException("Test data not found for tc_id: " + tcId);
        }
        String url = testData.get("url");
        String method = testData.get("method");
        String payload = testData.getOrDefault("payload", null);
        String code = testData.get("code");

        Response response;
        if ("GET".equalsIgnoreCase(method)) {
            response = sendGetRequest(url);
        } else if ("POST".equalsIgnoreCase(method)) {
            response = sendPostRequest(url, payload);
        } else {
            throw new UnsupportedOperationException("Method not supported: " + method);
        }

        // Example assertion
        if (response.getStatusCode() != Integer.parseInt(code)) {
            throw new AssertionError("Expected status code " + code + ", got " + response.getStatusCode());
        }
        System.out.println("Test for tc_id " + tcId + " passed with status code " + code);
    }

    public void runTestFromExcel(String filePath, String tcId) throws IOException, CsvException {
        // Fetch test data row by tc_id from Excel
        Map<String, String> testData = DataDrivenUtils.getTestDataByTcId(filePath, tcId);
        if (testData == null) {
            throw new RuntimeException("Test data not found for tc_id: " + tcId);
        }
        String url = testData.get("url");
        String method = testData.get("method");
        String payload = testData.getOrDefault("payload", null);
        String code = testData.get("code");

        Response response;
        if ("GET".equalsIgnoreCase(method)) {
            response = sendGetRequest(url);
        } else if ("POST".equalsIgnoreCase(method)) {
            response = sendPostRequest(url, payload);
        } else {
            throw new UnsupportedOperationException("Method not supported: " + method);
        }

        // Example assertion
        if (response.getStatusCode() != Integer.parseInt(code)) {
            throw new AssertionError("Expected status code " + code + ", got " + response.getStatusCode());
        }
        System.out.println("Test for tc_id " + tcId + " passed with status code " + code);
    }
}

