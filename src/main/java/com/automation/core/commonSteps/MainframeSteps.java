package com.automation.core.commonSteps;

import com.automation.core.context.ScenarioContext;
import com.automation.core.logging.LogManager;
import com.automation.reusables.MainframeReusable;
import io.cucumber.java.en.*;

/**
 * Comprehensive mainframe step definitions for 3270 terminal automation.
 * 
 * Built on EHLLAPI with full support for variable substitution using ${variableName}.
 * All screen data can be saved to ScenarioContext for reuse across steps.
 * 
 * Categories:
 * - Connection: Connect/disconnect from mainframe sessions
 * - Input: Enter text, send keys, function keys, special keys
 * - Navigation: Tab, Enter, PF keys, Clear
 * - Screen Reading: Get text from position, full screen, wait for text
 * - Verification: Verify text on screen, at position, wait for text
 * - Variables: Save/retrieve screen data to ScenarioContext
 * - Advanced: Multiple sessions, screen capture, field detection
 * 
 * Example usage:
 *   Given user connects to mainframe session "A"
 *   When user enters "${username}" at row 10 column 20
 *   Then mainframe screen should contain "WELCOME"
 *   When user saves text from row 15 column 5 length 20 as "accountNumber"
 */
public class MainframeSteps extends MainframeReusable {

    // ========== CONNECTION MANAGEMENT ==========

    @Given("user connects to mainframe session {string}")
    public void userConnectsToMainframeSession(String sessionId) {
        connectToMainframe(replaceVariables(sessionId));
    }

    @When("user disconnects from mainframe")
    public void userDisconnectsFromMainframe() {
        disconnectFromMainframe();
    }

    @When("user closes mainframe session")
    public void userClosesMainframeSession() {
        closeMainframeSession();
    }

    // ========== TEXT INPUT ==========

    @When("user enters {string} at row {int} column {int}")
    public void userEntersTextAtPosition(String text, int row, int col) {
        enterText(row, col, replaceVariables(text));
    }

    @When("user types {string} at position {int},{int}")
    public void userTypesAtPosition(String text, int row, int col) {
        enterText(row, col, replaceVariables(text));
    }

    @When("user enters text {string} at row {int} col {int}")
    public void userEntersTextAtRowCol(String text, int row, int col) {
        enterText(row, col, replaceVariables(text));
    }

    // ========== KEYBOARD ACTIONS ==========

    @When("user presses Enter key")
    public void userPressesEnter() {
        pressEnter();
    }

    @When("user presses Enter")
    public void userPressesEnterShort() {
        pressEnter();
    }

    @When("user submits screen")
    public void userSubmitsScreen() {
        pressEnter();
    }

    @When("user sends keys {string}")
    public void userSendsKeys(String keys) {
        sendKeys(replaceVariables(keys));
    }

    @When("user sends raw keys {string}")
    public void userSendsRawKeys(String keys) {
        sendKeys(replaceVariables(keys));
    }

    // ========== FUNCTION KEYS ==========

    @When("user presses PF{int}")
    public void userPressesPF(int pfNumber) {
        sendPF(pfNumber);
    }

    @When("user presses function key {string}")
    public void userPressesFunctionKey(String key) {
        sendFunctionKey(replaceVariables(key));
    }

    @When("user presses PA{int}")
    public void userPressesPA(int paNumber) {
        sendFunctionKey("PA" + paNumber);
    }

    // ========== NAVIGATION ==========

    @When("user clears the screen")
    public void userClearsScreen() {
        clearScreen();
    }

    @When("user clears screen")
    public void userClearsScreenShort() {
        clearScreen();
    }

    @When("user presses Tab")
    public void userPressesTab() {
        tab();
    }

    @When("user tabs to next field")
    public void userTabsToNextField() {
        tab();
    }

    @When("user tabs {int} times")
    public void userTabsMultipleTimes(int count) {
        for (int i = 0; i < count; i++) {
            tab();
        }
    }

    @When("user waits for screen ready")
    public void userWaitsForScreenReady() {
        waitForScreenReady();
    }

    @When("user waits for mainframe screen")
    public void userWaitsForMainframeScreen() {
        waitForScreenReady();
    }

    // ========== SCREEN READING ==========

    @When("user gets text from row {int} column {int} length {int}")
    public void userGetsTextFromPosition(int row, int col, int length) {
        String text = getTextFromScreen(row, col, length);
        LogManager.info("Retrieved text: " + text);
    }

    @When("user reads full screen")
    public void userReadsFullScreen() {
        String screen = getFullScreen();
        LogManager.info("Full screen content retrieved");
    }

    @When("user captures screen content")
    public void userCapturesScreenContent() {
        getFullScreen();
    }

    // ========== VERIFICATION ==========

    @Then("mainframe screen should contain {string}")
    public void mainframeScreenShouldContain(String expectedText) {
        verifyTextOnScreen(replaceVariables(expectedText));
    }

    @Then("screen should contain text {string}")
    public void screenShouldContainText(String expectedText) {
        verifyTextOnScreen(replaceVariables(expectedText));
    }

    @Then("mainframe should display {string}")
    public void mainframeShouldDisplay(String expectedText) {
        verifyTextOnScreen(replaceVariables(expectedText));
    }

    @Then("text at row {int} column {int} length {int} should be {string}")
    public void textAtPositionShouldBe(int row, int col, int length, String expectedText) {
        verifyTextAtPosition(row, col, length, replaceVariables(expectedText));
    }

    @Then("text at position {int},{int} with length {int} should be {string}")
    public void textAtPositionWithLengthShouldBe(int row, int col, int length, String expectedText) {
        verifyTextAtPosition(row, col, length, replaceVariables(expectedText));
    }

    @Then("field at row {int} col {int} length {int} should contain {string}")
    public void fieldAtPositionShouldContain(int row, int col, int length, String expectedText) {
        String actualText = getTextFromScreen(row, col, length);
        if (!actualText.contains(replaceVariables(expectedText))) {
            throw new AssertionError("Expected field to contain '" + expectedText + "' but was '" + actualText + "'");
        }
    }

    @Then("mainframe screen should display {string} within {int} seconds")
    public void mainframeScreenShouldDisplayWithinTimeout(String expectedText, int timeout) {
        waitForText(replaceVariables(expectedText), timeout);
    }

    @Then("screen should show {string} within {int} seconds")
    public void screenShouldShowWithinTimeout(String expectedText, int timeout) {
        waitForText(replaceVariables(expectedText), timeout);
    }

    @Then("wait for text {string} for {int} seconds")
    public void waitForTextForSeconds(String expectedText, int timeout) {
        waitForText(replaceVariables(expectedText), timeout);
    }

    @Then("mainframe screen should not contain {string}")
    public void mainframeScreenShouldNotContain(String unexpectedText) {
        String screen = getFullScreen();
        if (screen.contains(replaceVariables(unexpectedText))) {
            throw new AssertionError("Screen should not contain: " + unexpectedText);
        }
    }

    @Then("text at row {int} column {int} length {int} should not be empty")
    public void textAtPositionShouldNotBeEmpty(int row, int col, int length) {
        String text = getTextFromScreen(row, col, length);
        if (text == null || text.trim().isEmpty()) {
            throw new AssertionError("Text at row " + row + " column " + col + " is empty");
        }
    }

    @Then("text at row {int} column {int} length {int} should be empty")
    public void textAtPositionShouldBeEmpty(int row, int col, int length) {
        String text = getTextFromScreen(row, col, length);
        if (text != null && !text.trim().isEmpty()) {
            throw new AssertionError("Text at row " + row + " column " + col + " should be empty but was: " + text);
        }
    }

    @Then("field at row {int} col {int} length {int} should not contain {string}")
    public void fieldAtPositionShouldNotContain(int row, int col, int length, String unexpectedText) {
        String actualText = getTextFromScreen(row, col, length);
        if (actualText.contains(replaceVariables(unexpectedText))) {
            throw new AssertionError("Field should not contain '" + unexpectedText + "' but was '" + actualText + "'");
        }
    }

    @Then("text at row {int} column {int} length {int} should start with {string}")
    public void textAtPositionShouldStartWith(int row, int col, int length, String prefix) {
        String text = getTextFromScreen(row, col, length);
        if (!text.startsWith(replaceVariables(prefix))) {
            throw new AssertionError("Text should start with '" + prefix + "' but was '" + text + "'");
        }
    }

    @Then("text at row {int} column {int} length {int} should end with {string}")
    public void textAtPositionShouldEndWith(int row, int col, int length, String suffix) {
        String text = getTextFromScreen(row, col, length);
        if (!text.endsWith(replaceVariables(suffix))) {
            throw new AssertionError("Text should end with '" + suffix + "' but was '" + text + "'");
        }
    }

    @Then("text at row {int} column {int} length {int} should match regex {string}")
    public void textAtPositionShouldMatchRegex(int row, int col, int length, String regex) {
        String text = getTextFromScreen(row, col, length);
        if (!text.matches(replaceVariables(regex))) {
            throw new AssertionError("Text '" + text + "' does not match regex '" + regex + "'");
        }
    }

    @Then("text at row {int} column {int} length {int} should be numeric")
    public void textAtPositionShouldBeNumeric(int row, int col, int length) {
        String text = getTextFromScreen(row, col, length).trim();
        if (!text.matches("\\d+")) {
            throw new AssertionError("Text should be numeric but was: " + text);
        }
    }

    @Then("text at row {int} column {int} length {int} should be alphanumeric")
    public void textAtPositionShouldBeAlphanumeric(int row, int col, int length) {
        String text = getTextFromScreen(row, col, length).trim();
        if (!text.matches("[a-zA-Z0-9]+")) {
            throw new AssertionError("Text should be alphanumeric but was: " + text);
        }
    }

    @Then("screen should be empty")
    public void screenShouldBeEmpty() {
        String screen = getFullScreen();
        if (screen != null && !screen.trim().isEmpty()) {
            throw new AssertionError("Screen should be empty but contains text");
        }
    }

    @Then("screen should not be empty")
    public void screenShouldNotBeEmpty() {
        String screen = getFullScreen();
        if (screen == null || screen.trim().isEmpty()) {
            throw new AssertionError("Screen should not be empty");
        }
    }

    @Then("text at row {int} column {int} length {int} should equal {string} ignoring case")
    public void textAtPositionShouldEqualIgnoringCase(int row, int col, int length, String expected) {
        String text = getTextFromScreen(row, col, length);
        if (!text.equalsIgnoreCase(replaceVariables(expected))) {
            throw new AssertionError("Expected '" + expected + "' (ignoring case) but was '" + text + "'");
        }
    }

    @Then("text at row {int} column {int} length {int} should have length {int}")
    public void textAtPositionShouldHaveLength(int row, int col, int length, int expectedLength) {
        String text = getTextFromScreen(row, col, length).trim();
        if (text.length() != expectedLength) {
            throw new AssertionError("Expected length " + expectedLength + " but was " + text.length() + ": '" + text + "'");
        }
    }

    @Then("text at row {int} column {int} length {int} should be greater than {string}")
    public void textAtPositionShouldBeGreaterThan(int row, int col, int length, String value) {
        String text = getTextFromScreen(row, col, length).trim();
        try {
            double actual = Double.parseDouble(text);
            double expected = Double.parseDouble(replaceVariables(value));
            if (actual <= expected) {
                throw new AssertionError("Expected value > " + expected + " but was " + actual);
            }
        } catch (NumberFormatException e) {
            throw new AssertionError("Cannot compare non-numeric values: '" + text + "' and '" + value + "'");
        }
    }

    @Then("text at row {int} column {int} length {int} should be less than {string}")
    public void textAtPositionShouldBeLessThan(int row, int col, int length, String value) {
        String text = getTextFromScreen(row, col, length).trim();
        try {
            double actual = Double.parseDouble(text);
            double expected = Double.parseDouble(replaceVariables(value));
            if (actual >= expected) {
                throw new AssertionError("Expected value < " + expected + " but was " + actual);
            }
        } catch (NumberFormatException e) {
            throw new AssertionError("Cannot compare non-numeric values: '" + text + "' and '" + value + "'");
        }
    }

    @Then("mainframe screen should contain all of:")
    public void mainframeScreenShouldContainAllOf(io.cucumber.datatable.DataTable dataTable) {
        String screen = getFullScreen();
        java.util.List<String> expectedTexts = dataTable.asList();
        for (String expected : expectedTexts) {
            if (!screen.contains(replaceVariables(expected))) {
                throw new AssertionError("Screen does not contain: " + expected);
            }
        }
    }

    @Then("mainframe screen should contain any of:")
    public void mainframeScreenShouldContainAnyOf(io.cucumber.datatable.DataTable dataTable) {
        String screen = getFullScreen();
        java.util.List<String> expectedTexts = dataTable.asList();
        for (String expected : expectedTexts) {
            if (screen.contains(replaceVariables(expected))) {
                return;
            }
        }
        throw new AssertionError("Screen does not contain any of the expected texts");
    }

    // ========== VARIABLE MANAGEMENT ==========

    @When("user saves text from row {int} column {int} length {int} as {string}")
    public void userSavesTextAs(int row, int col, int length, String variableName) {
        String text = getTextFromScreen(row, col, length);
        ScenarioContext.set(variableName, text);
        LogManager.info("Saved variable '" + variableName + "' = " + text);
    }

    @When("user saves text at position {int},{int} length {int} as {string}")
    public void userSavesTextAtPositionAs(int row, int col, int length, String variableName) {
        String text = getTextFromScreen(row, col, length);
        ScenarioContext.set(variableName, text);
        LogManager.info("Saved variable '" + variableName + "' = " + text);
    }

    @When("user saves full screen as {string}")
    public void userSavesFullScreenAs(String variableName) {
        String screen = getFullScreen();
        ScenarioContext.set(variableName, screen);
        LogManager.info("Saved full screen as '" + variableName + "'");
    }

    @When("user stores field at row {int} column {int} length {int} as {string}")
    public void userStoresFieldAs(int row, int col, int length, String variableName) {
        String text = getTextFromScreen(row, col, length);
        ScenarioContext.set(variableName, text);
        LogManager.info("Stored field as '" + variableName + "' = " + text);
    }

    // ========== ADVANCED OPERATIONS ==========

    @When("user enters multiple fields:")
    public void userEntersMultipleFields(io.cucumber.datatable.DataTable dataTable) {
        java.util.List<java.util.Map<String, String>> rows = dataTable.asMaps();
        for (java.util.Map<String, String> row : rows) {
            int rowNum = Integer.parseInt(row.get("row"));
            int colNum = Integer.parseInt(row.get("col"));
            String text = replaceVariables(row.get("text"));
            enterText(rowNum, colNum, text);
        }
    }

    @When("user fills form at row {int} column {int} with:")
    public void userFillsFormWith(int startRow, int startCol, io.cucumber.datatable.DataTable dataTable) {
        java.util.List<java.util.Map<String, String>> rows = dataTable.asMaps();
        int currentRow = startRow;
        for (java.util.Map<String, String> row : rows) {
            String value = replaceVariables(row.get("value"));
            enterText(currentRow, startCol, value);
            currentRow++;
        }
    }

    @When("user pauses for {int} seconds")
    public void userPausesForSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
            LogManager.info("Paused for " + seconds + " seconds");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @When("user waits {int} seconds")
    public void userWaitsSeconds(int seconds) {
        userPausesForSeconds(seconds);
    }

    @Then("saved variable {string} should not be empty")
    public void savedVariableShouldNotBeEmpty(String variableName) {
        String value = ScenarioContext.get(variableName, String.class);
        if (value == null || value.trim().isEmpty()) {
            throw new AssertionError("Variable '" + variableName + "' is empty or null");
        }
    }

    @Then("saved variable {string} should contain {string}")
    public void savedVariableShouldContain(String variableName, String expectedText) {
        String value = ScenarioContext.get(variableName, String.class);
        if (value == null || !value.contains(replaceVariables(expectedText))) {
            throw new AssertionError("Variable '" + variableName + "' does not contain '" + expectedText + "'");
        }
    }

    @When("user logs screen content")
    public void userLogsScreenContent() {
        String screen = getFullScreen();
        LogManager.info("Screen Content:\n" + screen);
    }

    // ========== SPECIAL KEYS ==========

    @When("user presses Attention key")
    public void userPressesAttentionKey() {
        sendKeys("@A");
    }

    @When("user presses Reset key")
    public void userPressesResetKey() {
        sendKeys("@R");
    }

    @When("user presses SysReq")
    public void userPressesSysReq() {
        sendKeys("@A");
    }

    // ========== UTILITY METHODS ==========

    private String replaceVariables(String text) {
        if (text == null) return null;
        String result = text;
        for (java.util.Map.Entry<String, Object> entry : ScenarioContext.getAll().entrySet()) {
            String placeholder = "${" + entry.getKey() + "}";
            if (result.contains(placeholder)) {
                result = result.replace(placeholder, String.valueOf(entry.getValue()));
                LogManager.info("Replaced variable: " + placeholder + " -> " + entry.getValue());
            }
        }
        return result;
    }
}
