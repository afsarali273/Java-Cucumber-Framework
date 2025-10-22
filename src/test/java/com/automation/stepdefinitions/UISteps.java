package com.automation.stepdefinitions;

import com.automation.core.config.ConfigManager;
import com.automation.core.context.ScenarioContext;
import com.automation.core.logging.LogManager;
import com.automation.keywords.UIKeywords;
import io.cucumber.java.en.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Generic UI step definitions to speed up writing UI tests.
 * Uses UIKeywords which delegates to Selenium or Playwright based on framework config.
 */
public class UISteps {

    private final Map<String, String> savedVariables = new HashMap<>();

    // ------------------- Navigation & Browser -------------------

    @Given("open browser")
    public void openBrowser() {
        UIKeywords.openBrowser();
    }

    @Given("navigate to {string}")
    public void navigateTo(String url) {
        UIKeywords.navigateToURL(replaceVariables(url));
    }

    @When("close browser")
    public void closeBrowser() {
        UIKeywords.closeBrowser();
    }

    @When("refresh page")
    public void refreshPage() {
        UIKeywords.refreshPage();
    }

    @When("navigate back")
    public void navigateBack() {
        UIKeywords.navigateBack();
    }

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
        savedVariables.put(variableName, value);
        ScenarioContext.set(variableName, value);
        LogManager.info("Saved variable '" + variableName + "' = " + value);
    }

    @When("save attribute {string} of {string} as {string}")
    public void saveAttributeOfElement(String attribute, String locator, String variableName) {
        String value = UIKeywords.getAttribute(replaceVariables(locator), replaceVariables(attribute));
        savedVariables.put(variableName, value);
        ScenarioContext.set(variableName, value);
        LogManager.info("Saved attribute '" + attribute + "' as variable '" + variableName + "' = " + value);
    }

    @Then("saved variable {string} should be {string}")
    public void savedVariableShouldBe(String variableName, String expected) {
        String actual = savedVariables.get(variableName);
        if (!replaceVariables(expected).equals(actual)) {
            throw new AssertionError("Expected saved variable '" + variableName + "' to be '" + expected + "' but was '" + actual + "'");
        }
    }

    @Then("saved variable {string} should not be null")
    public void savedVariableShouldNotBeNull(String variableName) {
        String actual = savedVariables.get(variableName);
        if (actual == null) throw new AssertionError("Expected saved variable '" + variableName + "' to be not null");
    }

    @When("clear saved variables")
    public void clearSavedVariables() {
        savedVariables.clear();
        LogManager.info("Cleared saved variables");
    }

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

    // ------------------- Utilities -------------------

    private String replaceVariables(String text) {
        String result = text;
        for (Map.Entry<String, String> entry : savedVariables.entrySet()) {
            String placeholder = "${" + entry.getKey() + "}";
            if (result != null && result.contains(placeholder)) {
                result = result.replace(placeholder, entry.getValue());
                LogManager.info("Replaced variable: " + placeholder + " -> " + entry.getValue());
            }
        }
        return result;
    }
}

