package com.automation.core.exceptions;

/**
 * Exception thrown when an element is not found on the page.
 */
public class ElementNotFoundException extends FrameworkException {
    
    public ElementNotFoundException(String locator) {
        super("Element not found: " + locator);
    }
    
    public ElementNotFoundException(String locator, Throwable cause) {
        super("Element not found: " + locator, cause);
    }
}
