package com.automation.core.api;

import com.automation.core.logging.LogManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class SOAPClient {
    
    public static Response sendSOAPRequest(String endpoint, String soapEnvelope) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/xml; charset=utf-8");
        headers.put("SOAPAction", "");
        
        LogManager.info("Sending SOAP request to: " + endpoint);
        Response response = RestAssured.given()
                .headers(headers)
                .body(soapEnvelope)
                .post(endpoint);
        
        LogManager.info("SOAP Response Status: " + response.getStatusCode());
        return response;
    }
    
    public static String createSOAPEnvelope(String body) {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soap:Body>" + body + "</soap:Body>" +
                "</soap:Envelope>";
    }
}
