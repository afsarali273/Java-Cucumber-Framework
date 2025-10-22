package com.automation.core.exceptions;

/**
 * Exception thrown for API-related errors.
 */
public class APIException extends FrameworkException {
    
    private final int statusCode;
    
    public APIException(String message, int statusCode) {
        super("API Error [" + statusCode + "]: " + message);
        this.statusCode = statusCode;
    }
    
    public APIException(String message, int statusCode, Throwable cause) {
        super("API Error [" + statusCode + "]: " + message, cause);
        this.statusCode = statusCode;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
}
