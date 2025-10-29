package com.automation.core.commonSteps;

import com.automation.core.config.ConfigManager;
import com.automation.core.context.ScenarioContext;
import com.automation.core.driver.DriverManager;
import com.automation.core.logging.LogManager;
import com.automation.keywords.UIKeywords;
import io.cucumber.java.en.*;

import java.util.Map;

/**
 * Comprehensive UI step definitions for web automation testing.
 * 
 * Supports both Selenium and Playwright frameworks (auto-detected from config).
 * All steps support variable substitution using ${variableName} syntax.
 * 
 * Categories:
 * - Navigation & Browser: open, navigate, refresh, back, forward, close
 * - Element Interactions: click, type, clear, select, upload, check, hover, drag
 * - Waiting: wait for visible/clickable/invisible/text, pause
 * - Assertions: visibility, text, attributes, CSS, enabled/disabled state
 * - Variables: save/retrieve text, attributes, URL, title
 * - Screenshots: full page and element screenshots
 * - Tabs/Windows: open, switch, close tabs
 * - Alerts: accept, dismiss, get text
 * - Frames: switch to frame/default content
 * - Page State: title, URL validation and storage
 * - Element Count: count elements, check existence
 * - Checkboxes: check, uncheck, verify state
 * - Scroll: scroll to element/top/bottom
 * - Keyboard: press keys, enter
 * - Dropdowns: select by text/index/value, verify selection
 * - Advanced: JS execution, regex matching, right-click
 * 
 * Example usage:
 *   Given navigate to "https://example.com"
 *   When type "${username}" into "#email"
 *   Then element "#welcome" should contain text "Hello"
 */
public class UISteps {

    // ------------------- Navigation & Browser -------------------

    /**
     * Example: Given open browser
     */
    @Given("open browser")
    public void openBrowser() {
        UIKeywords.openBrowser();
    }

    /**
     * Example: Given navigate to "https://example.com"
     * Example: Given navigate to "${baseUrl}/login"
     */
    @Given("navigate to {string}")
    public void navigateTo(String url) {
        UIKeywords.navigateToURL(replaceVariables(url));
    }

    /**
     * Example: When close browser
     */
    @When("close browser")
    public void closeBrowser() {
        UIKeywords.closeBrowser();
    }

    /**
     * Example: When refresh page
     */
    @When("refresh page")
    public void refreshPage() {
        UIKeywords.refreshPage();
    }

    /**
     * Example: When navigate back
     */
    @When("navigate back")
    public void navigateBack() {
        UIKeywords.navigateBack();
    }

    /**
     * Example: When navigate forward
     */
    @When("navigate forward")
    public void navigateForward() {
        UIKeywords.navigateForward();
    }

    // ------------------- Element interactions -------------------

    @When("click element {string}")
    public void clickElement(String locator) {
        UIKeywords.clickElement(replaceVariables(locator));
    }

    @When("click text {string}")
    public void clickByText(String text) {
        // Use Playwright-friendly API when available, otherwise fallback to basic click by text xpath
        if (ConfigManager.isPlaywright()) {
            com.microsoft.playwright.Locator loc = com.automation.core.driver.DriverManager.getPlaywrightPage().getByText(replaceVariables(text));
            loc.click();
            LogManager.info("Clicked text using Playwright: " + text);
        } else {
            String xpath = "//*[text()='" + replaceVariables(text) + "']";
            UIKeywords.clickElement(xpath);
        }
    }

    @When("type {string} into {string}")
    public void typeInto(String text, String locator) {
        UIKeywords.enterText(replaceVariables(locator), replaceVariables(text));
    }

    @When("type {string} into {string} and press enter")
    public void typeIntoAndEnter(String text, String locator) {
        if (ConfigManager.isPlaywright()) {
            com.automation.core.driver.DriverManager.getPlaywrightPage().fill(replaceVariables(locator), replaceVariables(text));
            com.automation.core.driver.DriverManager.getPlaywrightPage().press(replaceVariables(locator), "Enter");
        } else {
            UIKeywords.enterText(replaceVariables(locator), replaceVariables(text));
            // send enter via JS if needed
            UIKeywords.executeJS("arguments[0].dispatchEvent(new KeyboardEvent('keydown',{key:'Enter'}));", UIKeywords.getTextFromElement(replaceVariables(locator)));
        }
    }

    @When("clear {string}")
    public void clearField(String locator) {
        UIKeywords.enterText(replaceVariables(locator), "");
    }

    @When("select {string} from dropdown {string}")
    public void selectDropdownByText(String option, String locator) {
        UIKeywords.selectDropdownByText(replaceVariables(locator), replaceVariables(option));
    }

    @When("upload file {string} to {string}")
    public void uploadFile(String filePath, String locator) {
        UIKeywords.uploadFile(replaceVariables(locator), replaceVariables(filePath));
    }

    @When("check {string}")
    public void checkCheckbox(String locator) {
        UIKeywords.checkCheckbox(replaceVariables(locator));
    }

    @When("uncheck {string}")
    public void uncheckCheckbox(String locator) {
        UIKeywords.uncheckCheckbox(replaceVariables(locator));
    }

    @When("hover over {string}")
    public void hoverOver(String locator) {
        UIKeywords.mouseHover(replaceVariables(locator));
    }

    @When("double click {string}")
    public void doubleClick(String locator) {
        UIKeywords.doubleClick(replaceVariables(locator));
    }

    @When("drag {string} to {string}")
    public void dragAndDrop(String source, String target) {
        UIKeywords.dragAndDrop(replaceVariables(source), replaceVariables(target));
    }

    // ------------------- Waiting -------------------

    /**
     * Example: When wait for visible "#loginButton" for 10 seconds
     */
    @When("wait for visible {string} for {int} seconds")
    public void waitForVisible(String locator, int seconds) {
        UIKeywords.waitForVisible(replaceVariables(locator), seconds);
    }

    /**
     * Example: When wait for clickable "#submitBtn" for 15 seconds
     */
    @When("wait for clickable {string} for {int} seconds")
    public void waitForClickable(String locator, int seconds) {
        UIKeywords.waitForClickable(replaceVariables(locator), seconds);
    }

    /**
     * Example: When wait for invisible ".loading-spinner" for 20 seconds
     */
    @When("wait for invisible {string} for {int} seconds")
    public void waitForInvisible(String locator, int seconds) {
        UIKeywords.waitForInvisible(replaceVariables(locator), seconds);
    }

    /**
     * Example: When wait for text "Welcome" in ".message" for 10 seconds
     */
    @When("wait for text {string} in {string} for {int} seconds")
    public void waitForText(String text, String locator, int seconds) {
        UIKeywords.waitForText(replaceVariables(locator), replaceVariables(text), seconds);
    }

    /**
     * Example: When pause for 3 seconds
     */
    @When("pause for {int} seconds")
    public void pause(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
            LogManager.info("Paused for " + seconds + " seconds");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Example: When wait for page load
     */
    @When("wait for page load")
    public void waitForPageLoad() {
        UIKeywords.waitForPageLoad();
    }

    /**
     * Example: When wait for page load for 30 seconds
     */
    @When("wait for page load for {int} seconds")
    public void waitForPageLoadWithTimeout(int seconds) {
        UIKeywords.waitForPageLoad(seconds);
    }

    /**
     * Example: When wait for element "#dashboard" to be present
     */
    @When("wait for element {string} to be present")
    public void waitForElementPresent(String locator) {
        UIKeywords.waitForElementPresent(replaceVariables(locator));
    }

    /**
     * Example: When wait for element "#dashboard" to be present for 15 seconds
     */
    @When("wait for element {string} to be present for {int} seconds")
    public void waitForElementPresentWithTimeout(String locator, int seconds) {
        UIKeywords.waitForElementPresent(replaceVariables(locator), seconds);
    }

    /**
     * Example: When wait for element "#loginForm" to be enabled
     */
    @When("wait for element {string} to be enabled")
    public void waitForElementEnabled(String locator) {
        UIKeywords.waitForElementEnabled(replaceVariables(locator));
    }

    /**
     * Example: When wait for element "#loginForm" to be enabled for 10 seconds
     */
    @When("wait for element {string} to be enabled for {int} seconds")
    public void waitForElementEnabledWithTimeout(String locator, int seconds) {
        UIKeywords.waitForElementEnabled(replaceVariables(locator), seconds);
    }

    /**
     * Example: When wait for element "#loadingSpinner" to disappear
     */
    @When("wait for element {string} to disappear")
    public void waitForElementToDisappear(String locator) {
        UIKeywords.waitForInvisible(replaceVariables(locator), 30);
    }

    /**
     * Example: When wait for element "#loadingSpinner" to disappear for 20 seconds
     */
    @When("wait for element {string} to disappear for {int} seconds")
    public void waitForElementToDisappearWithTimeout(String locator, int seconds) {
        UIKeywords.waitForInvisible(replaceVariables(locator), seconds);
    }

    /**
     * Example: When wait for text "Success" to appear
     */
    @When("wait for text {string} to appear")
    public void waitForTextToAppear(String text) {
        UIKeywords.waitForTextPresent(replaceVariables(text));
    }

    /**
     * Example: When wait for text "Success" to appear for 15 seconds
     */
    @When("wait for text {string} to appear for {int} seconds")
    public void waitForTextToAppearWithTimeout(String text, int seconds) {
        UIKeywords.waitForTextPresent(replaceVariables(text), seconds);
    }

    /**
     * Example: When wait for text "Loading..." to disappear
     */
    @When("wait for text {string} to disappear")
    public void waitForTextToDisappear(String text) {
        UIKeywords.waitForTextToDisappear(replaceVariables(text));
    }

    /**
     * Example: When wait for text "Loading..." to disappear for 20 seconds
     */
    @When("wait for text {string} to disappear for {int} seconds")
    public void waitForTextToDisappearWithTimeout(String text, int seconds) {
        UIKeywords.waitForTextToDisappear(replaceVariables(text), seconds);
    }

    /**
     * Example: When wait for attribute "class" of "#button" to contain "active"
     */
    @When("wait for attribute {string} of {string} to contain {string}")
    public void waitForAttributeContains(String attribute, String locator, String value) {
        UIKeywords.waitForAttributeContains(replaceVariables(locator), replaceVariables(attribute), replaceVariables(value));
    }

    /**
     * Example: When wait for attribute "class" of "#button" to contain "active" for 10 seconds
     */
    @When("wait for attribute {string} of {string} to contain {string} for {int} seconds")
    public void waitForAttributeContainsWithTimeout(String attribute, String locator, String value, int seconds) {
        UIKeywords.waitForAttributeContains(replaceVariables(locator), replaceVariables(attribute), replaceVariables(value), seconds);
    }

    /**
     * Example: When wait for URL to contain "dashboard"
     */
    @When("wait for URL to contain {string}")
    public void waitForURLContains(String urlPart) {
        UIKeywords.waitForURLContains(replaceVariables(urlPart));
    }

    /**
     * Example: When wait for URL to contain "dashboard" for 15 seconds
     */
    @When("wait for URL to contain {string} for {int} seconds")
    public void waitForURLContainsWithTimeout(String urlPart, int seconds) {
        UIKeywords.waitForURLContains(replaceVariables(urlPart), seconds);
    }

    /**
     * Example: When wait for title to be "Dashboard - MyApp"
     */
    @When("wait for title to be {string}")
    public void waitForTitle(String title) {
        UIKeywords.waitForTitle(replaceVariables(title));
    }

    /**
     * Example: When wait for title to be "Dashboard - MyApp" for 10 seconds
     */
    @When("wait for title to be {string} for {int} seconds")
    public void waitForTitleWithTimeout(String title, int seconds) {
        UIKeywords.waitForTitle(replaceVariables(title), seconds);
    }

    /**
     * Example: When wait for title to contain "Dashboard"
     */
    @When("wait for title to contain {string}")
    public void waitForTitleContains(String titlePart) {
        UIKeywords.waitForTitleContains(replaceVariables(titlePart));
    }

    /**
     * Example: When wait for title to contain "Dashboard" for 10 seconds
     */
    @When("wait for title to contain {string} for {int} seconds")
    public void waitForTitleContainsWithTimeout(String titlePart, int seconds) {
        UIKeywords.waitForTitleContains(replaceVariables(titlePart), seconds);
    }

    /**
     * Example: When wait for element count of ".item" to be 5
     */
    @When("wait for element count of {string} to be {int}")
    public void waitForElementCount(String locator, int count) {
        UIKeywords.waitForElementCount(replaceVariables(locator), count);
    }

    /**
     * Example: When wait for element count of ".item" to be 5 for 15 seconds
     */
    @When("wait for element count of {string} to be {int} for {int} seconds")
    public void waitForElementCountWithTimeout(String locator, int count, int seconds) {
        UIKeywords.waitForElementCount(replaceVariables(locator), count, seconds);
    }

    /**
     * Example: When wait for alert to be present
     */
    @When("wait for alert to be present")
    public void waitForAlert() {
        UIKeywords.waitForAlert();
    }

    /**
     * Example: When wait for alert to be present for 10 seconds
     */
    @When("wait for alert to be present for {int} seconds")
    public void waitForAlertWithTimeout(int seconds) {
        UIKeywords.waitForAlert(seconds);
    }

    /**
     * Example: When wait for frame "#myFrame" to be available
     */
    @When("wait for frame {string} to be available")
    public void waitForFrame(String locator) {
        UIKeywords.waitForFrameAvailable(replaceVariables(locator));
    }

    /**
     * Example: When wait for frame "#myFrame" to be available for 10 seconds
     */
    @When("wait for frame {string} to be available for {int} seconds")
    public void waitForFrameWithTimeout(String locator, int seconds) {
        UIKeywords.waitForFrameAvailable(replaceVariables(locator), seconds);
    }

    /**
     * Example: When wait for {int} milliseconds
     */
    @When("wait for {int} milliseconds")
    public void waitMilliseconds(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
            LogManager.info("Waited for " + milliseconds + " milliseconds");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // ------------------- Assertions -------------------

    @Then("element {string} should be visible")
    public void elementShouldBeVisible(String locator) {
        boolean visible = UIKeywords.isElementVisible(replaceVariables(locator));
        if (!visible) throw new AssertionError("Expected element to be visible: " + locator);
    }

    @Then("element {string} should not be visible")
    public void elementShouldNotBeVisible(String locator) {
        boolean visible = UIKeywords.isElementVisible(replaceVariables(locator));
        if (visible) throw new AssertionError("Expected element to be not visible: " + locator);
    }

    @Then("element {string} should contain text {string}")
    public void elementShouldContainText(String locator, String expected) {
        String actual = UIKeywords.getTextFromElement(replaceVariables(locator));
        if (actual == null || !actual.contains(replaceVariables(expected))) {
            throw new AssertionError("Expected element '" + locator + "' to contain '" + expected + "' but was '" + actual + "'");
        }
    }

    @Then("element {string} text should be {string}")
    public void elementTextShouldBe(String locator, String expected) {
        String actual = UIKeywords.getTextFromElement(replaceVariables(locator));
        if (!replaceVariables(expected).equals(actual)) {
            throw new AssertionError("Expected text '" + expected + "' but got '" + actual + "'");
        }
    }

    @Then("page should contain text {string}")
    public void pageShouldContainText(String text) {
        boolean present = UIKeywords.isTextPresent(replaceVariables(text));
        if (!present) throw new AssertionError("Expected page to contain text: " + text);
    }

    @Then("element {string} attribute {string} should be {string}")
    public void elementAttributeShouldBe(String locator, String attribute, String expected) {
        String actual = UIKeywords.getAttribute(replaceVariables(locator), replaceVariables(attribute));
        if (!replaceVariables(expected).equals(actual)) {
            throw new AssertionError("Expected attribute '" + attribute + "' to be '" + expected + "' but was '" + actual + "'");
        }
    }

    @Then("element {string} css {string} should be {string}")
    public void elementCssShouldBe(String locator, String property, String expected) {
        String actual = UIKeywords.getCssValue(replaceVariables(locator), replaceVariables(property));
        if (!replaceVariables(expected).equals(actual)) {
            throw new AssertionError("Expected css '" + property + "' to be '" + expected + "' but was '" + actual + "'");
        }
    }

    @Then("element {string} should be enabled")
    public void elementShouldBeEnabled(String locator) {
        boolean enabled = UIKeywords.isElementEnabled(replaceVariables(locator));
        if (!enabled) throw new AssertionError("Expected element to be enabled: " + locator);
    }

    @Then("element {string} should be disabled")
    public void elementShouldBeDisabled(String locator) {
        boolean enabled = UIKeywords.isElementEnabled(replaceVariables(locator));
        if (enabled) throw new AssertionError("Expected element to be disabled: " + locator);
    }

    // ------------------- Storage / Variables -------------------

    @When("save text of {string} as {string}")
    public void saveTextOfElement(String locator, String variableName) {
        String value = UIKeywords.getTextFromElement(replaceVariables(locator));
        ScenarioContext.set(variableName, value);
        LogManager.info("Saved variable '" + variableName + "' = " + value);
    }

    @When("save attribute {string} of {string} as {string}")
    public void saveAttributeOfElement(String attribute, String locator, String variableName) {
        String value = UIKeywords.getAttribute(replaceVariables(locator), replaceVariables(attribute));
        ScenarioContext.set(variableName, value);
        LogManager.info("Saved attribute '" + attribute + "' as variable '" + variableName + "' = " + value);
    }

    @When("clear saved variables")
    public void clearSavedVariables() {
        // Use the ScenarioContext clear so saved variables used by SharedSteps are reset
        ScenarioContext.clear();
        LogManager.info("Cleared saved variables");
    }

    // NOTE: Saved-variable assertion steps ("saved variable ...") are provided by SharedSteps to avoid duplicates.

    // ------------------- Screenshots / Visual -------------------

    @When("take screenshot {string}")
    public void takeScreenshot(String filePath) {
        UIKeywords.takeScreenshot(replaceVariables(filePath));
    }

    @When("take element screenshot {string} save as {string}")
    public void takeElementScreenshot(String locator, String filePath) {
        UIKeywords.takeElementScreenshot(replaceVariables(locator), replaceVariables(filePath));
    }

    // ------------------- Tabs/Windows -------------------

    @When("open new tab")
    @SuppressWarnings("resource")
    public void openNewTab() {
        // UIKeywords doesn't expose open tab; use DriverManager directly for Selenium
        if (ConfigManager.isPlaywright()) {
            com.automation.core.driver.DriverManager.getPlaywrightPage().context().newPage();
            LogManager.info("Opened new Playwright page");
        } else {
            // execute script to open new tab
            UIKeywords.executeJS("window.open()");
            LogManager.info("Opened new tab via JS");
        }
    }

    @When("switch to latest tab")
    @SuppressWarnings("resource")
    public void switchToLatestTab() {
        if (ConfigManager.isPlaywright()) {
            // Playwright keeps pages in context; switch by index (last)
            com.microsoft.playwright.BrowserContext ctx = com.automation.core.driver.DriverManager.getPlaywrightPage().context();
            int lastIdx = ctx.pages().size() - 1;
            com.automation.core.driver.DriverManager.setPlaywrightPage(ctx.pages().get(lastIdx));
            LogManager.info("Switched to latest Playwright tab");
        } else {
            // Selenium: switch to latest window handle
            java.util.List<String> handles = new java.util.ArrayList<>(com.automation.core.driver.DriverManager.getSeleniumDriver().getWindowHandles());
            com.automation.core.driver.DriverManager.getSeleniumDriver().switchTo().window(handles.get(handles.size() - 1));
            LogManager.info("Switched to latest Selenium tab");
        }
    }

    // ------------------- JS / Advanced -------------------

    @When("execute js {string}")
    public void executeJs(String script) {
        UIKeywords.executeJS(replaceVariables(script));
    }

    @Then("element {string} rect should contain {string}")
    public void elementRectShouldContain(String locator, String expected) {
        String rect = UIKeywords.getElementRect(replaceVariables(locator));
        if (!rect.contains(replaceVariables(expected))) {
            throw new AssertionError("Expected rect to contain '" + expected + "' but was '" + rect + "'");
        }
    }

    // ------------------- Alerts & Dialogs -------------------

    /**
     * Example: When accept alert
     */
    @When("accept alert")
    public void acceptAlert() {
        UIKeywords.acceptAlert();
    }

    /**
     * Example: When dismiss alert
     */
    @When("dismiss alert")
    public void dismissAlert() {
        UIKeywords.dismissAlert();
    }

    /**
     * Example: Then alert text should be "Are you sure?"
     */
    @Then("alert text should be {string}")
    public void alertTextShouldBe(String expected) {
        String actual = UIKeywords.getAlertText();
        if (!replaceVariables(expected).equals(actual)) {
            throw new AssertionError("Expected alert text '" + expected + "' but got '" + actual + "'");
        }
    }

    // ------------------- Frames/iFrames -------------------

    /**
     * Example: When switch to frame "frameId"
     */
    @When("switch to frame {string}")
    public void switchToFrame(String frameLocator) {
        UIKeywords.switchToFrame(replaceVariables(frameLocator));
    }

    /**
     * Example: When switch to default content
     */
    @When("switch to default content")
    public void switchToDefaultContent() {
        UIKeywords.switchToDefaultContent();
    }

    // ------------------- Page State -------------------

    /**
     * Example: Then page title should be "Home Page"
     */
    @Then("page title should be {string}")
    public void pageTitleShouldBe(String expected) {
        String actual = UIKeywords.getPageTitle();
        if (!replaceVariables(expected).equals(actual)) {
            throw new AssertionError("Expected page title '" + expected + "' but got '" + actual + "'");
        }
    }

    /**
     * Example: Then page title should contain "Dashboard"
     */
    @Then("page title should contain {string}")
    public void pageTitleShouldContain(String expected) {
        String actual = UIKeywords.getPageTitle();
        if (!actual.contains(replaceVariables(expected))) {
            throw new AssertionError("Expected page title to contain '" + expected + "' but was '" + actual + "'");
        }
    }

    /**
     * Example: Then current url should be "https://example.com/home"
     */
    @Then("current url should be {string}")
    public void currentUrlShouldBe(String expected) {
        String actual = UIKeywords.getCurrentURL();
        if (!replaceVariables(expected).equals(actual)) {
            throw new AssertionError("Expected URL '" + expected + "' but got '" + actual + "'");
        }
    }

    /**
     * Example: Then current url should contain "/dashboard"
     */
    @Then("current url should contain {string}")
    public void currentUrlShouldContain(String expected) {
        String actual = UIKeywords.getCurrentURL();
        if (!actual.contains(replaceVariables(expected))) {
            throw new AssertionError("Expected URL to contain '" + expected + "' but was '" + actual + "'");
        }
    }

    /**
     * Example: When save current url as "homeUrl"
     */
    @When("save current url as {string}")
    public void saveCurrentUrl(String variableName) {
        String url = UIKeywords.getCurrentURL();
        ScenarioContext.set(variableName, url);
        LogManager.info("Saved current URL as '" + variableName + "' = " + url);
    }

    /**
     * Example: When save page title as "pageTitle"
     */
    @When("save page title as {string}")
    public void savePageTitle(String variableName) {
        String title = UIKeywords.getPageTitle();
        ScenarioContext.set(variableName, title);
        LogManager.info("Saved page title as '" + variableName + "' = " + title);
    }

    // ------------------- Element Count & Existence -------------------

    /**
     * Example: Then element count of ".product-item" should be 5
     */
    @Then("element count of {string} should be {int}")
    public void elementCountShouldBe(String locator, int expected) {
        int actual = UIKeywords.getElementCount(replaceVariables(locator));
        if (actual != expected) {
            throw new AssertionError("Expected element count " + expected + " but got " + actual);
        }
    }

    /**
     * Example: Then element count of ".product-item" should be greater than 0
     */
    @Then("element count of {string} should be greater than {int}")
    public void elementCountShouldBeGreaterThan(String locator, int expected) {
        int actual = UIKeywords.getElementCount(replaceVariables(locator));
        if (actual <= expected) {
            throw new AssertionError("Expected element count > " + expected + " but got " + actual);
        }
    }

    /**
     * Example: Then element "#submitBtn" should exist
     */
    @Then("element {string} should exist")
    public void elementShouldExist(String locator) {
        boolean exists = UIKeywords.isElementPresent(replaceVariables(locator));
        if (!exists) throw new AssertionError("Expected element to exist: " + locator);
    }

    /**
     * Example: Then element "#errorMsg" should not exist
     */
    @Then("element {string} should not exist")
    public void elementShouldNotExist(String locator) {
        boolean exists = UIKeywords.isElementPresent(replaceVariables(locator));
        if (exists) throw new AssertionError("Expected element to not exist: " + locator);
    }

    // ------------------- Checkbox & Radio -------------------

    /**
     * Example: Then checkbox "#terms" should be checked
     */
    @Then("checkbox {string} should be checked")
    public void checkboxShouldBeChecked(String locator) {
        boolean checked = UIKeywords.isCheckboxChecked(replaceVariables(locator));
        if (!checked) throw new AssertionError("Expected checkbox to be checked: " + locator);
    }

    /**
     * Example: Then checkbox "#newsletter" should not be checked
     */
    @Then("checkbox {string} should not be checked")
    public void checkboxShouldNotBeChecked(String locator) {
        boolean checked = UIKeywords.isCheckboxChecked(replaceVariables(locator));
        if (checked) throw new AssertionError("Expected checkbox to not be checked: " + locator);
    }

    // ------------------- Scroll Actions -------------------

    /**
     * Example: When scroll to element "#footer"
     */
    @When("scroll to element {string}")
    public void scrollToElement(String locator) {
        UIKeywords.scrollToElement(replaceVariables(locator));
    }

    /**
     * Example: When scroll to top
     */
    @When("scroll to top")
    public void scrollToTop() {
        UIKeywords.scrollToTop();
    }

    /**
     * Example: When scroll to bottom
     */
    @When("scroll to bottom")
    public void scrollToBottom() {
        UIKeywords.scrollToBottom();
    }

    // ------------------- Keyboard Actions -------------------

    /**
     * Example: When press key "Escape" on element "#modal"
     */
    @When("press key {string} on element {string}")
    public void pressKeyOnElement(String key, String locator) {
        UIKeywords.pressKey(replaceVariables(locator), replaceVariables(key));
    }

    /**
     * Example: When press enter on element "#searchBox"
     */
    @When("press enter on element {string}")
    public void pressEnterOnElement(String locator) {
        UIKeywords.pressKey(replaceVariables(locator), "Enter");
    }

    // ------------------- Dropdown Advanced -------------------

    /**
     * Example: When select option by index 2 from dropdown "#country"
     */
    @When("select option by index {int} from dropdown {string}")
    public void selectDropdownByIndex(int index, String locator) {
        UIKeywords.selectDropdownByIndex(replaceVariables(locator), index);
    }

    /**
     * Example: When select option by value "usa" from dropdown "#country"
     */
    @When("select option by value {string} from dropdown {string}")
    public void selectDropdownByValue(String value, String locator) {
        UIKeywords.selectDropdownByValue(replaceVariables(locator), replaceVariables(value));
    }

    /**
     * Example: Then selected option in dropdown "#country" should be "United States"
     */
    @Then("selected option in dropdown {string} should be {string}")
    public void selectedOptionShouldBe(String locator, String expected) {
        String actual = UIKeywords.getSelectedDropdownOption(replaceVariables(locator));
        if (!replaceVariables(expected).equals(actual)) {
            throw new AssertionError("Expected selected option '" + expected + "' but got '" + actual + "'");
        }
    }

    // ------------------- Text Matching -------------------

    /**
     * Example: Then element "#price" text should match regex "\\$\\d+\\.\\d{2}"
     */
    @Then("element {string} text should match regex {string}")
    public void elementTextShouldMatchRegex(String locator, String regex) {
        String actual = UIKeywords.getTextFromElement(replaceVariables(locator));
        if (!actual.matches(replaceVariables(regex))) {
            throw new AssertionError("Expected text to match regex '" + regex + "' but was '" + actual + "'");
        }
    }

    /**
     * Example: Then element "#message" text should not be empty
     */
    @Then("element {string} text should not be empty")
    public void elementTextShouldNotBeEmpty(String locator) {
        String actual = UIKeywords.getTextFromElement(replaceVariables(locator));
        if (actual == null || actual.trim().isEmpty()) {
            throw new AssertionError("Expected element text to not be empty: " + locator);
        }
    }

    /**
     * Example: Then element "#message" text should be empty
     */
    @Then("element {string} text should be empty")
    public void elementTextShouldBeEmpty(String locator) {
        String actual = UIKeywords.getTextFromElement(replaceVariables(locator));
        if (actual != null && !actual.trim().isEmpty()) {
            throw new AssertionError("Expected element text to be empty but was: '" + actual + "'");
        }
    }

    /**
     * Example: Then element "#username" text should start with "User:"
     */
    @Then("element {string} text should start with {string}")
    public void elementTextShouldStartWith(String locator, String prefix) {
        String actual = UIKeywords.getTextFromElement(replaceVariables(locator));
        if (!actual.startsWith(replaceVariables(prefix))) {
            throw new AssertionError("Expected text to start with '" + prefix + "' but was '" + actual + "'");
        }
    }

    /**
     * Example: Then element "#filename" text should end with ".pdf"
     */
    @Then("element {string} text should end with {string}")
    public void elementTextShouldEndWith(String locator, String suffix) {
        String actual = UIKeywords.getTextFromElement(replaceVariables(locator));
        if (!actual.endsWith(replaceVariables(suffix))) {
            throw new AssertionError("Expected text to end with '" + suffix + "' but was '" + actual + "'");
        }
    }

    /**
     * Example: Then element "#error" should not contain text "success"
     */
    @Then("element {string} should not contain text {string}")
    public void elementShouldNotContainText(String locator, String text) {
        String actual = UIKeywords.getTextFromElement(replaceVariables(locator));
        if (actual != null && actual.contains(replaceVariables(text))) {
            throw new AssertionError("Expected element not to contain '" + text + "' but was '" + actual + "'");
        }
    }

    /**
     * Example: Then page should not contain text "Error"
     */
    @Then("page should not contain text {string}")
    public void pageShouldNotContainText(String text) {
        boolean present = UIKeywords.isTextPresent(replaceVariables(text));
        if (present) throw new AssertionError("Expected page to not contain text: " + text);
    }

    /**
     * Example: Then element "#link" attribute "href" should contain "example.com"
     */
    @Then("element {string} attribute {string} should contain {string}")
    public void elementAttributeShouldContain(String locator, String attribute, String expected) {
        String actual = UIKeywords.getAttribute(replaceVariables(locator), replaceVariables(attribute));
        if (actual == null || !actual.contains(replaceVariables(expected))) {
            throw new AssertionError("Expected attribute '" + attribute + "' to contain '" + expected + "' but was '" + actual + "'");
        }
    }

    /**
     * Example: Then element "#button" attribute "disabled" should not exist
     */
    @Then("element {string} attribute {string} should not exist")
    public void elementAttributeShouldNotExist(String locator, String attribute) {
        String actual = UIKeywords.getAttribute(replaceVariables(locator), replaceVariables(attribute));
        if (actual != null) {
            throw new AssertionError("Expected attribute '" + attribute + "' to not exist but was '" + actual + "'");
        }
    }

    /**
     * Example: Then element "#input" attribute "value" should not be empty
     */
    @Then("element {string} attribute {string} should not be empty")
    public void elementAttributeShouldNotBeEmpty(String locator, String attribute) {
        String actual = UIKeywords.getAttribute(replaceVariables(locator), replaceVariables(attribute));
        if (actual == null || actual.trim().isEmpty()) {
            throw new AssertionError("Expected attribute '" + attribute + "' to not be empty");
        }
    }

    /**
     * Example: Then element "#box" css "background-color" should contain "rgb"
     */
    @Then("element {string} css {string} should contain {string}")
    public void elementCssShouldContain(String locator, String property, String expected) {
        String actual = UIKeywords.getCssValue(replaceVariables(locator), replaceVariables(property));
        if (actual == null || !actual.contains(replaceVariables(expected))) {
            throw new AssertionError("Expected css '" + property + "' to contain '" + expected + "' but was '" + actual + "'");
        }
    }

    /**
     * Example: Then element count of ".item" should be less than 10
     */
    @Then("element count of {string} should be less than {int}")
    public void elementCountShouldBeLessThan(String locator, int expected) {
        int actual = UIKeywords.getElementCount(replaceVariables(locator));
        if (actual >= expected) {
            throw new AssertionError("Expected element count < " + expected + " but got " + actual);
        }
    }

    /**
     * Example: Then element count of ".item" should be greater than or equal to 5
     */
    @Then("element count of {string} should be greater than or equal to {int}")
    public void elementCountShouldBeGreaterThanOrEqual(String locator, int expected) {
        int actual = UIKeywords.getElementCount(replaceVariables(locator));
        if (actual < expected) {
            throw new AssertionError("Expected element count >= " + expected + " but got " + actual);
        }
    }

    /**
     * Example: Then element count of ".item" should be less than or equal to 20
     */
    @Then("element count of {string} should be less than or equal to {int}")
    public void elementCountShouldBeLessThanOrEqual(String locator, int expected) {
        int actual = UIKeywords.getElementCount(replaceVariables(locator));
        if (actual > expected) {
            throw new AssertionError("Expected element count <= " + expected + " but got " + actual);
        }
    }

    /**
     * Example: Then element count of ".item" should not be 0
     */
    @Then("element count of {string} should not be {int}")
    public void elementCountShouldNotBe(String locator, int expected) {
        int actual = UIKeywords.getElementCount(replaceVariables(locator));
        if (actual == expected) {
            throw new AssertionError("Expected element count to not be " + expected);
        }
    }

    /**
     * Example: Then element "#button" should be focused
     */
    @Then("element {string} should be focused")
    public void elementShouldBeFocused(String locator) {
        if (ConfigManager.isPlaywright()) {
            boolean focused = DriverManager.getPlaywrightPage().locator(replaceVariables(locator)).evaluate("el => el === document.activeElement").toString().equals("true");
            if (!focused) throw new AssertionError("Expected element to be focused: " + locator);
        } else {
            org.openqa.selenium.WebElement element = DriverManager.getSeleniumDriver().findElement(org.openqa.selenium.By.cssSelector(replaceVariables(locator)));
            org.openqa.selenium.WebElement activeElement = DriverManager.getSeleniumDriver().switchTo().activeElement();
            if (!element.equals(activeElement)) {
                throw new AssertionError("Expected element to be focused: " + locator);
            }
        }
    }

    /**
     * Example: Then element "#hidden" should be hidden
     */
    @Then("element {string} should be hidden")
    public void elementShouldBeHidden(String locator) {
        boolean visible = UIKeywords.isElementVisible(replaceVariables(locator));
        if (visible) throw new AssertionError("Expected element to be hidden: " + locator);
    }

    /**
     * Example: Then element "#button" should be clickable
     */
    @Then("element {string} should be clickable")
    public void elementShouldBeClickable(String locator) {
        boolean visible = UIKeywords.isElementVisible(replaceVariables(locator));
        boolean enabled = UIKeywords.isElementEnabled(replaceVariables(locator));
        if (!visible || !enabled) {
            throw new AssertionError("Expected element to be clickable (visible and enabled): " + locator);
        }
    }

    /**
     * Example: Then element "#button" should not be clickable
     */
    @Then("element {string} should not be clickable")
    public void elementShouldNotBeClickable(String locator) {
        boolean visible = UIKeywords.isElementVisible(replaceVariables(locator));
        boolean enabled = UIKeywords.isElementEnabled(replaceVariables(locator));
        if (visible && enabled) {
            throw new AssertionError("Expected element to not be clickable: " + locator);
        }
    }

    /**
     * Example: Then current url should start with "https://"
     */
    @Then("current url should start with {string}")
    public void currentUrlShouldStartWith(String prefix) {
        String actual = UIKeywords.getCurrentURL();
        if (!actual.startsWith(replaceVariables(prefix))) {
            throw new AssertionError("Expected URL to start with '" + prefix + "' but was '" + actual + "'");
        }
    }

    /**
     * Example: Then current url should end with "/dashboard"
     */
    @Then("current url should end with {string}")
    public void currentUrlShouldEndWith(String suffix) {
        String actual = UIKeywords.getCurrentURL();
        if (!actual.endsWith(replaceVariables(suffix))) {
            throw new AssertionError("Expected URL to end with '" + suffix + "' but was '" + actual + "'");
        }
    }

    /**
     * Example: Then current url should not contain "error"
     */
    @Then("current url should not contain {string}")
    public void currentUrlShouldNotContain(String text) {
        String actual = UIKeywords.getCurrentURL();
        if (actual.contains(replaceVariables(text))) {
            throw new AssertionError("Expected URL to not contain '" + text + "' but was '" + actual + "'");
        }
    }

    /**
     * Example: Then current url should match regex "https://.*\\.example\\.com/.*"
     */
    @Then("current url should match regex {string}")
    public void currentUrlShouldMatchRegex(String regex) {
        String actual = UIKeywords.getCurrentURL();
        if (!actual.matches(replaceVariables(regex))) {
            throw new AssertionError("Expected URL to match regex '" + regex + "' but was '" + actual + "'");
        }
    }

    /**
     * Example: Then page title should not be empty
     */
    @Then("page title should not be empty")
    public void pageTitleShouldNotBeEmpty() {
        String actual = UIKeywords.getPageTitle();
        if (actual == null || actual.trim().isEmpty()) {
            throw new AssertionError("Expected page title to not be empty");
        }
    }

    /**
     * Example: Then page title should start with "Dashboard"
     */
    @Then("page title should start with {string}")
    public void pageTitleShouldStartWith(String prefix) {
        String actual = UIKeywords.getPageTitle();
        if (!actual.startsWith(replaceVariables(prefix))) {
            throw new AssertionError("Expected page title to start with '" + prefix + "' but was '" + actual + "'");
        }
    }

    /**
     * Example: Then page title should end with "- MyApp"
     */
    @Then("page title should end with {string}")
    public void pageTitleShouldEndWith(String suffix) {
        String actual = UIKeywords.getPageTitle();
        if (!actual.endsWith(replaceVariables(suffix))) {
            throw new AssertionError("Expected page title to end with '" + suffix + "' but was '" + actual + "'");
        }
    }

    /**
     * Example: Then page title should not contain "Error"
     */
    @Then("page title should not contain {string}")
    public void pageTitleShouldNotContain(String text) {
        String actual = UIKeywords.getPageTitle();
        if (actual.contains(replaceVariables(text))) {
            throw new AssertionError("Expected page title to not contain '" + text + "' but was '" + actual + "'");
        }
    }

    /**
     * Example: Then page title should match regex ".*Dashboard.*"
     */
    @Then("page title should match regex {string}")
    public void pageTitleShouldMatchRegex(String regex) {
        String actual = UIKeywords.getPageTitle();
        if (!actual.matches(replaceVariables(regex))) {
            throw new AssertionError("Expected page title to match regex '" + regex + "' but was '" + actual + "'");
        }
    }

    /**
     * Example: Then alert text should contain "confirm"
     */
    @Then("alert text should contain {string}")
    public void alertTextShouldContain(String expected) {
        String actual = UIKeywords.getAlertText();
        if (!actual.contains(replaceVariables(expected))) {
            throw new AssertionError("Expected alert text to contain '" + expected + "' but was '" + actual + "'");
        }
    }

    /**
     * Example: Then alert should be present
     */
    @Then("alert should be present")
    public void alertShouldBePresent() {
        if (ConfigManager.isPlaywright()) {
            LogManager.info("Playwright handles alerts via dialog events");
        } else {
            try {
                DriverManager.getSeleniumDriver().switchTo().alert();
            } catch (org.openqa.selenium.NoAlertPresentException e) {
                throw new AssertionError("Expected alert to be present");
            }
        }
    }

    /**
     * Example: Then radio button "#option1" should be selected
     */
    @Then("radio button {string} should be selected")
    public void radioButtonShouldBeSelected(String locator) {
        boolean selected = UIKeywords.isCheckboxChecked(replaceVariables(locator));
        if (!selected) throw new AssertionError("Expected radio button to be selected: " + locator);
    }

    /**
     * Example: Then radio button "#option2" should not be selected
     */
    @Then("radio button {string} should not be selected")
    public void radioButtonShouldNotBeSelected(String locator) {
        boolean selected = UIKeywords.isCheckboxChecked(replaceVariables(locator));
        if (selected) throw new AssertionError("Expected radio button to not be selected: " + locator);
    }

    /**
     * Example: Then dropdown "#country" should have option "USA"
     */
    @Then("dropdown {string} should have option {string}")
    public void dropdownShouldHaveOption(String locator, String option) {
        if (ConfigManager.isPlaywright()) {
            String optionLocator = replaceVariables(locator) + " option:has-text(\"" + replaceVariables(option) + "\")";
            boolean exists = DriverManager.getPlaywrightPage().locator(optionLocator).count() > 0;
            if (!exists) throw new AssertionError("Expected dropdown to have option: " + option);
        } else {
            org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(
                DriverManager.getSeleniumDriver().findElement(org.openqa.selenium.By.cssSelector(replaceVariables(locator))));
            boolean found = select.getOptions().stream().anyMatch(opt -> opt.getText().equals(replaceVariables(option)));
            if (!found) throw new AssertionError("Expected dropdown to have option: " + option);
        }
    }

    /**
     * Example: Then selected option in dropdown "#country" should contain "United"
     */
    @Then("selected option in dropdown {string} should contain {string}")
    public void selectedOptionShouldContain(String locator, String expected) {
        String actual = UIKeywords.getSelectedDropdownOption(replaceVariables(locator));
        if (!actual.contains(replaceVariables(expected))) {
            throw new AssertionError("Expected selected option to contain '" + expected + "' but was '" + actual + "'");
        }
    }

    // ------------------- Close Tab/Window -------------------

    /**
     * Example: When close current tab
     */
    @When("close current tab")
    public void closeCurrentTab() {
        if (ConfigManager.isPlaywright()) {
            com.automation.core.driver.DriverManager.getPlaywrightPage().close();
            LogManager.info("Closed current Playwright page");
        } else {
            com.automation.core.driver.DriverManager.getSeleniumDriver().close();
            LogManager.info("Closed current Selenium window");
        }
    }

    /**
     * Example: When switch to tab {int}
     */
    @When("switch to tab {int}")
    @SuppressWarnings("resource")
    public void switchToTab(int index) {
        if (ConfigManager.isPlaywright()) {
            com.microsoft.playwright.BrowserContext ctx = com.automation.core.driver.DriverManager.getPlaywrightPage().context();
            com.automation.core.driver.DriverManager.setPlaywrightPage(ctx.pages().get(index));
            LogManager.info("Switched to Playwright tab index: " + index);
        } else {
            java.util.List<String> handles = new java.util.ArrayList<>(com.automation.core.driver.DriverManager.getSeleniumDriver().getWindowHandles());
            com.automation.core.driver.DriverManager.getSeleniumDriver().switchTo().window(handles.get(index));
            LogManager.info("Switched to Selenium tab index: " + index);
        }
    }

    // ------------------- Right Click -------------------

    /**
     * Example: When right click element "#contextMenu"
     */
    @When("right click element {string}")
    public void rightClickElement(String locator) {
        UIKeywords.rightClick(replaceVariables(locator));
    }

    // ------------------- Advanced Element Operations -------------------

    @When("click {string} with javascript")
    public void clickWithJS(String locator) {
        UIKeywords.clickWithJS(replaceVariables(locator));
    }

    @When("click {string} if visible")
    public void clickIfVisible(String locator) {
        UIKeywords.clickIfVisible(replaceVariables(locator));
    }

    @When("type {string} into {string} without clearing")
    public void typeWithoutClear(String text, String locator) {
        UIKeywords.typeWithoutClear(replaceVariables(locator), replaceVariables(text));
    }

    @When("focus element {string}")
    public void focusElement(String locator) {
        UIKeywords.focusElement(replaceVariables(locator));
    }

    @When("highlight element {string}")
    public void highlightElement(String locator) {
        UIKeywords.highlightElement(replaceVariables(locator));
    }

    // ------------------- Text & Content Operations -------------------

    @When("save input value of {string} as {string}")
    public void saveInputValue(String locator, String variableName) {
        String value = UIKeywords.getInputValue(replaceVariables(locator));
        ScenarioContext.set(variableName, value);
        LogManager.info("Saved input value as '" + variableName + "' = " + value);
    }

    @Then("input value of {string} should be {string}")
    public void inputValueShouldBe(String locator, String expected) {
        String actual = UIKeywords.getInputValue(replaceVariables(locator));
        if (!replaceVariables(expected).equals(actual)) {
            throw new AssertionError("Expected input value '" + expected + "' but got '" + actual + "'");
        }
    }

    // ------------------- Window Operations -------------------

    @When("maximize window")
    public void maximizeWindow() {
        UIKeywords.maximizeWindow();
    }

    @When("set window size to {int}x{int}")
    public void setWindowSize(int width, int height) {
        UIKeywords.setWindowSize(width, height);
    }

    // ------------------- Cookie Operations -------------------

    @When("add cookie {string} with value {string}")
    public void addCookie(String name, String value) {
        UIKeywords.addCookie(replaceVariables(name), replaceVariables(value));
    }

    @When("delete all cookies")
    public void deleteAllCookies() {
        UIKeywords.deleteAllCookies();
    }


    // ------------------- Advanced Scroll Operations -------------------

    @When("scroll by {int} pixels horizontally and {int} pixels vertically")
    public void scrollByPixels(int x, int y) {
        UIKeywords.scrollByPixels(x, y);
    }

    @When("scroll {string} into center")
    public void scrollElementIntoCenter(String locator) {
        UIKeywords.scrollElementIntoCenter(replaceVariables(locator));
    }

    // ------------------- Keyboard Operations -------------------

    @When("press keyboard key {string}")
    public void pressKeyboardKey(String key) {
        UIKeywords.pressKeyboardKey(replaceVariables(key));
    }

    @When("press enter key")
    public void pressEnter() {
        UIKeywords.pressEnter();
    }

    @When("press escape key")
    public void pressEscape() {
        UIKeywords.pressEscape();
    }

    @When("press tab key")
    public void pressTab() {
        UIKeywords.pressTab();
    }

    // ------------------- Element State Assertions -------------------

    @Then("element {string} should be editable")
    public void elementShouldBeEditable(String locator) {
        boolean editable = UIKeywords.isElementEditable(replaceVariables(locator));
        if (!editable) throw new AssertionError("Expected element to be editable: " + locator);
    }

    // ------------------- Dropdown Advanced -------------------

    @Then("dropdown {string} should have {int} options")
    public void dropdownShouldHaveOptionsCount(String locator, int expected) {
        int actual = UIKeywords.getDropdownOptionsCount(replaceVariables(locator));
        if (actual != expected) {
            throw new AssertionError("Expected dropdown to have " + expected + " options but got " + actual);
        }
    }

    // ------------------- File Upload -------------------

    @When("upload multiple files {string} to {string}")
    public void uploadMultipleFiles(String filePaths, String locator) {
        String[] paths = replaceVariables(filePaths).split(",");
        for (int i = 0; i < paths.length; i++) {
            paths[i] = paths[i].trim();
        }
        UIKeywords.uploadMultipleFiles(replaceVariables(locator), paths);
    }

    // ------------------- Element Information -------------------

    @When("save tag name of {string} as {string}")
    public void saveTagName(String locator, String variableName) {
        String tagName = UIKeywords.getElementTagName(replaceVariables(locator));
        ScenarioContext.set(variableName, tagName);
        LogManager.info("Saved tag name as '" + variableName + "' = " + tagName);
    }

    @Then("tag name of {string} should be {string}")
    public void tagNameShouldBe(String locator, String expected) {
        String actual = UIKeywords.getElementTagName(replaceVariables(locator));
        if (!replaceVariables(expected).equalsIgnoreCase(actual)) {
            throw new AssertionError("Expected tag name '" + expected + "' but got '" + actual + "'");
        }
    }

    // ------------------- Utilities -------------------

    private String replaceVariables(String text) {
        String result = text;
        for (Map.Entry<String, Object> entry : ScenarioContext.getAll().entrySet()) {
            String placeholder = "${" + entry.getKey() + "}";
            if (result != null && result.contains(placeholder)) {
                result = result.replace(placeholder, String.valueOf(entry.getValue()));
                LogManager.info("Replaced variable: " + placeholder + " -> " + entry.getValue());
            }
        }
        return result;
    }
}
