package com.automation.reusables;

import com.automation.core.assertions.AssertUtils;
import com.automation.core.driver.DriverManager;
import com.automation.core.interfaces.WebActions;
import com.automation.core.logging.UnifiedLogger;
import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.assertions.PageAssertions;
import com.microsoft.playwright.options.AriaRole;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class PlaywrightReusable implements WebActions<Locator> {
    protected Page page;

    public PlaywrightReusable() {
        this.page = DriverManager.getPlaywrightPage();
    }
    

    @Override
    public void navigateTo(String url) {
        page.navigate(url);
        UnifiedLogger.info("Navigated to: " + url);
    }

    @Override
    public void click(Locator locator) {
        locator.click();
        UnifiedLogger.action("Click", locator.toString());
    }

    @Override
    public void type(Locator locator, String text) {
        locator.fill(text);
        UnifiedLogger.action("Type", locator + " | Text: " + text);
    }

    @Override
    public void typeAndEnter(Locator locator, String text) {
        locator.fill(text);
        locator.press("Enter");
        UnifiedLogger.action("Type and Enter", locator.toString());
    }

    @Override
    public String getText(Locator locator) {
        String text = locator.textContent();
        UnifiedLogger.action("Get Text", locator + " | Text: " + text);
        return text;
    }

    @Override
    public boolean isDisplayed(Locator locator) {
        return locator.isVisible();
    }

    @Override
    public void selectDropdownByText(Locator locator, String text) {
        locator.selectOption(text);
        UnifiedLogger.action("Select Dropdown", locator + " | Text: " + text);
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
        UnifiedLogger.action("Scroll to Element", locator.toString());
    }

    @Override
    public void doubleClick(Locator locator) {
        locator.dblclick();
        UnifiedLogger.action("Double Click", locator.toString());
    }

    @Override
    public void jsClick(Locator locator) {
        locator.evaluate("element => element.click()");
        UnifiedLogger.action("JS Click", locator.toString());
    }

    @Override
    public void clear(Locator locator) {
        locator.fill("");
        UnifiedLogger.action("Clear", locator.toString());
    }

    @Override
    public void selectDropdownByValue(Locator locator, String value) {
        locator.selectOption(value);
        UnifiedLogger.action("Select Dropdown by Value", locator + " | Value: " + value);
    }

    @Override
    public void selectDropdownByIndex(Locator locator, int index) {
        locator.selectOption(String.valueOf(index));
        UnifiedLogger.action("Select Dropdown by Index", locator + " | Index: " + index);
    }

    @Override
    public void check(Locator locator) {
        locator.check();
        UnifiedLogger.action("Check", locator.toString());
    }

    @Override
    public void uncheck(Locator locator) {
        locator.uncheck();
        UnifiedLogger.action("Uncheck", locator.toString());
    }

    @Override
    public boolean isChecked(Locator locator) {
        return locator.isChecked();
    }

    @Override
    public void mouseHover(Locator locator) {
        locator.hover();
        UnifiedLogger.action("Mouse Hover", locator.toString());
    }

    @Override
    public void dragAndDrop(Locator source, Locator target) {
        source.dragTo(target);
        UnifiedLogger.action("Drag and Drop", "From: " + source + " To: " + target);
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
        UnifiedLogger.info("Scrolled to top");
    }

    @Override
    public void scrollToBottom() {
        page.evaluate("window.scrollTo(0, document.body.scrollHeight)");
        UnifiedLogger.info("Scrolled to bottom");
    }

    @Override
    public void takeScreenshot(String filePath) {
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(filePath)));
        UnifiedLogger.info("Screenshot saved: " + filePath);
    }

    @Override
    public void uploadFile(Locator locator, String filePath) {
        locator.setInputFiles(Paths.get(filePath));
        UnifiedLogger.action("Upload File", locator + " | File: " + filePath);
    }

    @Override
    public void navigateBack() {
        page.goBack();
        UnifiedLogger.info("Navigated back");
    }

    @Override
    public void navigateForward() {
        page.goForward();
        UnifiedLogger.info("Navigated forward");
    }

    @Override
    public void refreshPage() {
        page.reload();
        UnifiedLogger.info("Page refreshed");
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

    // ============================================================================
    // PLAYWRIGHT NATIVE ASSERTIONS
    // ============================================================================

    /**
     * Assert element is visible.
     */
    public void assertVisible(Locator locator) {
        assertThat(locator).isVisible();
        UnifiedLogger.pass("Element is visible: " + locator);
    }

    /**
     * Assert element is hidden.
     */
    public void assertHidden(Locator locator) {
        assertThat(locator).isHidden();
        UnifiedLogger.pass("Element is hidden: " + locator);
    }

    /**
     * Assert element is enabled.
     */
    public void assertEnabled(Locator locator) {
        assertThat(locator).isEnabled();
        UnifiedLogger.pass("Element is enabled: " + locator);
    }

    /**
     * Assert element is disabled.
     */
    public void assertDisabled(Locator locator) {
        assertThat(locator).isDisabled();
        UnifiedLogger.pass("Element is disabled: " + locator);
    }

    /**
     * Assert checkbox is checked.
     */
    public void assertChecked(Locator locator) {
        assertThat(locator).isChecked();
        UnifiedLogger.pass("Element is checked: " + locator);
    }

    /**
     * Assert element is editable.
     */
    public void assertEditable(Locator locator) {
        assertThat(locator).isEditable();
        UnifiedLogger.pass("Element is editable: " + locator);
    }

    /**
     * Assert element is focused.
     */
    public void assertFocused(Locator locator) {
        assertThat(locator).isFocused();
        UnifiedLogger.pass("Element is focused: " + locator);
    }

    /**
     * Assert element is attached to DOM.
     */
    public void assertAttached(Locator locator) {
        assertThat(locator).isAttached();
        UnifiedLogger.pass("Element is attached: " + locator);
    }

    /**
     * Assert element is in viewport.
     */
    public void assertInViewport(Locator locator) {
        assertThat(locator).isInViewport();
        UnifiedLogger.pass("Element is in viewport: " + locator);
    }

    /**
     * Assert element contains text.
     */
    public void assertContainsText(Locator locator, String text) {
        assertThat(locator).containsText(text);
        UnifiedLogger.pass("Element contains text '" + text + "': " + locator);
    }

    /**
     * Assert element has exact text.
     */
    public void assertHasText(Locator locator, String text) {
        assertThat(locator).hasText(text);
        UnifiedLogger.pass("Element has text '" + text + "': " + locator);
    }

    /**
     * Assert element has attribute.
     */
    public void assertHasAttribute(Locator locator, String name, String value) {
        assertThat(locator).hasAttribute(name, value);
        UnifiedLogger.pass("Element has attribute '" + name + "'='" + value + "': " + locator);
    }

    /**
     * Assert element has CSS class.
     */
    public void assertHasClass(Locator locator, String className) {
        assertThat(locator).hasClass(className);
        UnifiedLogger.pass("Element has class '" + className + "': " + locator);
    }

    /**
     * Assert element contains CSS class.
     */
    public void assertContainsClass(Locator locator, String className) {
        assertThat(locator).hasClass(java.util.regex.Pattern.compile(".*" + className + ".*"));
        UnifiedLogger.pass("Element contains class '" + className + "': " + locator);
    }

    /**
     * Assert element has CSS property.
     */
    public void assertHasCSS(Locator locator, String name, String value) {
        assertThat(locator).hasCSS(name, value);
        UnifiedLogger.pass("Element has CSS '" + name + "'='" + value + "': " + locator);
    }

    /**
     * Assert element has ID.
     */
    public void assertHasId(Locator locator, String id) {
        assertThat(locator).hasId(id);
        UnifiedLogger.pass("Element has ID '" + id + "': " + locator);
    }

    /**
     * Assert input has value.
     */
    public void assertHasValue(Locator locator, String value) {
        assertThat(locator).hasValue(value);
        UnifiedLogger.pass("Element has value '" + value + "': " + locator);
    }

    /**
     * Assert element count.
     */
    public void assertHasCount(Locator locator, int count) {
        assertThat(locator).hasCount(count);
        UnifiedLogger.pass("Element count is " + count + ": " + locator);
    }

    /**
     * Assert page has title.
     */
    public void assertPageHasTitle(String title) {
        assertThat(page).hasTitle(title);
        UnifiedLogger.pass("Page has title: " + title);
    }

    /**
     * Assert page has URL.
     */
    public void assertPageHasURL(String url) {
        assertThat(page).hasURL(url);
        UnifiedLogger.pass("Page has URL: " + url);
    }

    /**
     * Assert page URL contains.
     */
    public void assertPageURLContains(String urlPart) {
        assertThat(page).hasURL(java.util.regex.Pattern.compile(".*" + urlPart + ".*"));
        UnifiedLogger.pass("Page URL contains: " + urlPart);
    }

    /**
     * Assert element has accessible name.
     */
    public void assertHasAccessibleName(Locator locator, String name) {
        assertThat(locator).hasAccessibleName(name);
        UnifiedLogger.pass("Element has accessible name '" + name + "': " + locator);
    }

    /**
     * Assert element has role.
     */
    public void assertHasRole(Locator locator, AriaRole role) {
        assertThat(locator).hasRole(role);
        UnifiedLogger.pass("Element has role '" + role + "': " + locator);
    }

    /**
     * Get Playwright LocatorAssertions for custom assertions.
     */
    public LocatorAssertions expect(Locator locator) {
        return assertThat(locator);
    }

    /**
     * Get Playwright PageAssertions for custom assertions.
     */
    public PageAssertions expect(Page page) {
        return assertThat(page);
    }

}
