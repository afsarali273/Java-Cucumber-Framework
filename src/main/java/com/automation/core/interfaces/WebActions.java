package com.automation.core.interfaces;

public interface WebActions<T> {
    
    // Navigation
    void navigateTo(String url);
    void navigateBack();
    void navigateForward();
    void refreshPage();
    
    // Click Actions
    void click(T locator);
    void doubleClick(T locator);
    void jsClick(T locator);
    
    // Input Actions
    void type(T locator, String text);
    void typeAndEnter(T locator, String text);
    void clear(T locator);
    
    // Dropdown Actions
    void selectDropdownByText(T locator, String text);
    void selectDropdownByValue(T locator, String value);
    void selectDropdownByIndex(T locator, int index);
    
    // Checkbox/Radio Actions
    void check(T locator);
    void uncheck(T locator);
    boolean isChecked(T locator);
    
    // Mouse Actions
    void mouseHover(T locator);
    void dragAndDrop(T source, T target);
    
    // Get Actions
    String getText(T locator);
    String getAttribute(T locator, String attribute);
    String getCssValue(T locator, String property);
    
    // Visibility Actions
    boolean isDisplayed(T locator);
    boolean isEnabled(T locator);
    
    // Wait Actions
    void waitForElementToBeVisible(T locator);
    void waitForElementToBeClickable(T locator);
    void waitForSeconds(int seconds);
    
    // Scroll Actions
    void scrollToElement(T locator);
    void scrollToTop();
    void scrollToBottom();
    
    // Screenshot Actions
    void takeScreenshot(String filePath);
    
    // File Upload
    void uploadFile(T locator, String filePath);
    
    // Assertions
    void assertEquals(Object actual, Object expected, String message);
    void assertNotEquals(Object actual, Object expected, String message);
    void assertTrue(boolean condition, String message);
    void assertFalse(boolean condition, String message);
    void assertNull(Object object, String message);
    void assertNotNull(Object object, String message);
    void assertContains(String actual, String expected, String message);
    
    // Soft Assertions
    void enableSoftAssert();
    void assertAll();
}
