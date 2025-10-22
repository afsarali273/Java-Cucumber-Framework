package com.automation.core.exceptions;

/**
 * Exception thrown when there's a configuration error.
 */
public class ConfigurationException extends FrameworkException {
    
    public ConfigurationException(String message) {
        super("Configuration Error: " + message);
    }
    
    public ConfigurationException(String message, Throwable cause) {
        super("Configuration Error: " + message, cause);
    }
}
