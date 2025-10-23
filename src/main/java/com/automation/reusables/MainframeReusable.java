package com.automation.reusables;

import com.automation.core.assertions.AssertUtils;
import com.automation.core.logging.UnifiedLogger;
import com.automation.core.mainframe.MainFrameDriver;

public class MainframeReusable {

    private static final ThreadLocal<MainFrameDriver> mainframeDriver = new ThreadLocal<>();

    protected MainFrameDriver getMainframeDriver() {
        if (mainframeDriver.get() == null) {
            mainframeDriver.set(new MainFrameDriver());
        }
        return mainframeDriver.get();
    }

    protected void connectToMainframe(String sessionId) {
        UnifiedLogger.action("Connect to Mainframe", "Session ID: " + sessionId);
        getMainframeDriver().connect(sessionId);
        UnifiedLogger.pass("Connected to mainframe session: " + sessionId);
    }

    protected void disconnectFromMainframe() {
        UnifiedLogger.action("Disconnect from Mainframe", "Closing session");
        getMainframeDriver().disconnect();
        UnifiedLogger.info("Disconnected from mainframe");
    }

    protected void enterText(int row, int col, String text) {
        UnifiedLogger.action("Enter Text", String.format("Row: %d, Col: %d, Text: %s", row, col, text));
        getMainframeDriver().setText(row, col, text);
    }

    protected void pressEnter() {
        UnifiedLogger.action("Press Enter", "Submitting screen");
        getMainframeDriver().enter();
    }

    protected void sendKeys(String keys) {
        UnifiedLogger.action("Send Keys", keys);
        getMainframeDriver().sendKeys(keys);
    }

    protected String getTextFromScreen(int row, int col, int length) {
        String text = getMainframeDriver().getText(row, col, length);
        UnifiedLogger.info(String.format("Retrieved text from Row: %d, Col: %d, Length: %d -> %s", row, col, length, text));
        return text;
    }

    protected String getFullScreen() {
        String screen = getMainframeDriver().getScreen();
        UnifiedLogger.info("Retrieved full screen content");
        return screen;
    }

    protected void waitForScreenReady() {
        UnifiedLogger.action("Wait for Screen", "Waiting for screen unlock");
        getMainframeDriver().waitForScreenReady();
    }

    protected boolean waitForText(String expectedText, int timeoutSeconds) {
        UnifiedLogger.action("Wait for Text", String.format("Expected: '%s', Timeout: %ds", expectedText, timeoutSeconds));
        boolean found = getMainframeDriver().waitForText(expectedText, timeoutSeconds);
        if (found) {
            UnifiedLogger.pass("Text found: " + expectedText);
        } else {
            UnifiedLogger.fail("Text not found within timeout: " + expectedText);
        }
        return found;
    }

    protected void verifyTextOnScreen(String expectedText) {
        String screen = getFullScreen();
        AssertUtils.assertContains(screen, expectedText, "Verify text on mainframe screen");
    }

    protected void verifyTextAtPosition(int row, int col, int length, String expectedText) {
        String actualText = getTextFromScreen(row, col, length);
        AssertUtils.assertEquals(actualText, expectedText, String.format("Verify text at Row: %d, Col: %d", row, col));
    }

    protected void closeMainframeSession() {
        if (mainframeDriver.get() != null) {
            getMainframeDriver().close();
            mainframeDriver.remove();
            UnifiedLogger.info("Mainframe session closed");
        }
    }

    protected void sendFunctionKey(String functionKey) {
        UnifiedLogger.action("Send Function Key", functionKey);
        getMainframeDriver().sendKeys("@" + functionKey);
    }

    protected void sendPF(int pfNumber) {
        sendFunctionKey("" + pfNumber);
    }

    protected void clearScreen() {
        UnifiedLogger.action("Clear Screen", "Sending clear command");
        getMainframeDriver().sendKeys("@C");
    }

    protected void tab() {
        UnifiedLogger.action("Tab", "Moving to next field");
        getMainframeDriver().sendKeys("@T");
    }
}
