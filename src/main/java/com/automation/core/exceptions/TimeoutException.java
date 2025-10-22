package com.automation.core.exceptions;

/**
 * Exception thrown when an operation times out.
 */
public class TimeoutException extends FrameworkException {
    
    public TimeoutException(String operation, int timeoutSeconds) {
        super("Timeout after " + timeoutSeconds + " seconds: " + operation);
    }
    
    public TimeoutException(String operation, int timeoutSeconds, Throwable cause) {
        super("Timeout after " + timeoutSeconds + " seconds: " + operation, cause);
    }
}
