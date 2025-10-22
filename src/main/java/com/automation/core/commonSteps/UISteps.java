package com.automation.stepdefinitions;

import com.automation.core.config.ConfigManager;
import com.automation.core.context.ScenarioContext;
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

    @When("wait for visible {string} for {int} seconds")
    public void waitForVisible(String locator, int seconds) {
        UIKeywords.waitForVisible(replaceVariables(locator), seconds);
    }

    @When("wait for clickable {string} for {int} seconds")
    public void waitForClickable(String locator, int seconds) {
        UIKeywords.waitForClickable(replaceVariables(locator), seconds);
    }

    @When("wait for invisible {string} for {int} seconds")
    public void waitForInvisible(String locator, int seconds) {
        UIKeywords.waitForInvisible(replaceVariables(locator), seconds);
    }

    @When("wait for text {string} in {string} for {int} seconds")
    public void waitForText(String locator, String text, int seconds) {
        UIKeywords.waitForText(replaceVariables(locator), replaceVariables(text), seconds);
    }

    @When("pause for {int} seconds")
    public void pause(int seconds) {
        UIKeywords.waitForElement(replaceVariables("body"), seconds); // generic wait
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
