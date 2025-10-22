package com.automation.core.exceptions;

/**
 * Exception thrown when driver initialization fails.
 */
public class DriverInitializationException extends FrameworkException {
    
    public DriverInitializationException(String driverType) {
        super("Failed to initialize " + driverType + " driver");
    }
    
    public DriverInitializationException(String driverType, Throwable cause) {
        super("Failed to initialize " + driverType + " driver", cause);
    }
}
