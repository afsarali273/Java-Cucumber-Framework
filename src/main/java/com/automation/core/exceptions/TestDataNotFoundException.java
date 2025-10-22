package com.automation.core.exceptions;

/**
 * Exception thrown when test data is not found.
 */
public class TestDataNotFoundException extends FrameworkException {
    
    public TestDataNotFoundException(String dataIdentifier) {
        super("Test data not found: " + dataIdentifier);
    }
    
    public TestDataNotFoundException(String dataIdentifier, Throwable cause) {
        super("Test data not found: " + dataIdentifier, cause);
    }
}
