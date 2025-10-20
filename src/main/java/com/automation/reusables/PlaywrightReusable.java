package com.automation.reusables;

import com.automation.core.assertions.AssertUtils;
import com.automation.core.driver.DriverManager;
import com.automation.core.logging.LogManager;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;

import java.nio.file.Paths;
import java.util.List;

public class PlaywrightReusable implements WebActions<Locator> {
    protected Page page;

    public PlaywrightReusable() {
        this.page = DriverManager.getPlaywrightPage();
    }
    

    @Override
    public void navigateTo(String url) {
        page.navigate(url);
        LogManager.info("Navigated to: " + url);
    }

    @Override
    public void click(Locator locator) {
        locator.click();
        LogManager.info("Clicked on element");
    }

    @Override
    public void type(Locator locator, String text) {
        locator.fill(text);
        LogManager.info("Typed '" + text + "' into element");
    }

    @Override
    public void typeAndEnter(Locator locator, String text) {
        locator.fill(text);
        locator.press("Enter");
        LogManager.info("Pressed ENTER after typing");
    }

    @Override
    public String getText(Locator locator) {
        String text = locator.textContent();
        LogManager.info("Retrieved text: " + text);
        return text;
    }

    @Override
    public boolean isDisplayed(Locator locator) {
        return locator.isVisible();
    }

    @Override
    public void selectDropdownByText(Locator locator, String text) {
        locator.selectOption(text);
        LogManager.info("Selected dropdown option: " + text);
    }

    @Override
    public void waitForElementToBeVisible(Locator locator) {
        locator.waitFor();
    }

    @Override
    public void waitForSeconds(int seconds) {
        page.waitForTimeout(seconds * 1000);
    }

    public Locator locator(String selector) {
        return page.locator(selector);
    }

    public Locator getByRole(AriaRole role) {
        return page.getByRole(role);
    }

    public Locator getByRole(AriaRole role, Page.GetByRoleOptions options) {
        return page.getByRole(role, options);
    }

    public Locator getByText(String text) {
        return page.getByText(text);
    }

    public Locator getByTestId(String testId) {
        return page.getByTestId(testId);
    }

    public Locator getByPlaceholder(String placeholder) {
        return page.getByPlaceholder(placeholder);
    }

    public Locator getByLabel(String label) {
        return page.getByLabel(label);
    }

    public Locator getByTitle(String title) {
        return page.getByTitle(title);
    }

    public Locator getByAltText(String altText) {
        return page.getByAltText(altText);
    }

    @Override
    public void scrollToElement(Locator locator) {
        locator.scrollIntoViewIfNeeded();
        LogManager.info("Scrolled to element");
    }

    @Override
    public void doubleClick(Locator locator) {
        locator.dblclick();
        LogManager.info("Double clicked");
    }

    @Override
    public void jsClick(Locator locator) {
        locator.evaluate("element => element.click()");
        LogManager.info("JavaScript clicked");
    }

    @Override
    public void clear(Locator locator) {
        locator.fill("");
        LogManager.info("Cleared element");
    }

    @Override
    public void selectDropdownByValue(Locator locator, String value) {
        locator.selectOption(value);
        LogManager.info("Selected dropdown value: " + value);
    }

    @Override
    public void selectDropdownByIndex(Locator locator, int index) {
        locator.selectOption(String.valueOf(index));
        LogManager.info("Selected dropdown index: " + index);
    }

    @Override
    public void check(Locator locator) {
        locator.check();
        LogManager.info("Checked element");
    }

    @Override
    public void uncheck(Locator locator) {
        locator.uncheck();
        LogManager.info("Unchecked element");
    }

    @Override
    public boolean isChecked(Locator locator) {
        return locator.isChecked();
    }

    @Override
    public void mouseHover(Locator locator) {
        locator.hover();
        LogManager.info("Mouse hovered");
    }

    @Override
    public void dragAndDrop(Locator source, Locator target) {
        source.dragTo(target);
        LogManager.info("Dragged element");
    }

    @Override
    public String getAttribute(Locator locator, String attribute) {
        return locator.getAttribute(attribute);
    }

    @Override
    public String getCssValue(Locator locator, String property) {
        return locator.evaluate("element => getComputedStyle(element)." + property).toString();
    }

    @Override
    public boolean isEnabled(Locator locator) {
        return locator.isEnabled();
    }

    @Override
    public void waitForElementToBeClickable(Locator locator) {
        locator.waitFor(new Locator.WaitForOptions().setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));
    }

    @Override
    public void scrollToTop() {
        page.evaluate("window.scrollTo(0, 0)");
        LogManager.info("Scrolled to top");
    }

    @Override
    public void scrollToBottom() {
        page.evaluate("window.scrollTo(0, document.body.scrollHeight)");
        LogManager.info("Scrolled to bottom");
    }

    @Override
    public void takeScreenshot(String filePath) {
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(filePath)));
        LogManager.info("Screenshot saved: " + filePath);
    }

    @Override
    public void uploadFile(Locator locator, String filePath) {
        locator.setInputFiles(Paths.get(filePath));
        LogManager.info("File uploaded: " + filePath);
    }

    @Override
    public void navigateBack() {
        page.goBack();
        LogManager.info("Navigated back");
    }

    @Override
    public void navigateForward() {
        page.goForward();
        LogManager.info("Navigated forward");
    }

    @Override
    public void refreshPage() {
        page.reload();
        LogManager.info("Page refreshed");
    }

    @Override
    public void assertEquals(Object actual, Object expected, String message) {
        AssertUtils.assertEquals(actual, expected, message);
    }

    @Override
    public void assertNotEquals(Object actual, Object expected, String message) {
        AssertUtils.assertNotEquals(actual, expected, message);
    }

    @Override
    public void assertTrue(boolean condition, String message) {
        AssertUtils.assertTrue(condition, message);
    }

    @Override
    public void assertFalse(boolean condition, String message) {
        AssertUtils.assertFalse(condition, message);
    }

    @Override
    public void assertNull(Object object, String message) {
        AssertUtils.assertNull(object, message);
    }

    @Override
    public void assertNotNull(Object object, String message) {
        AssertUtils.assertNotNull(object, message);
    }

    @Override
    public void assertContains(String actual, String expected, String message) {
        AssertUtils.assertContains(actual, expected, message);
    }

    @Override
    public void enableSoftAssert() {
        AssertUtils.enableSoftAssert();
    }

    @Override
    public void assertAll() {
        AssertUtils.assertAll();
    }

}
