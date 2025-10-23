package com.automation.core.mainframe;

public class MainFrameDriver {

    private final EHLLAPIWrapper ehllapi;
    private boolean connected = false;

    public MainFrameDriver() {
        this.ehllapi = new EHLLAPIWrapper();
    }

    public void connect(String sessionId) {
        ehllapi.connect(sessionId);
        connected = true;
    }

    public void disconnect() {
        if (connected) {
            ehllapi.disconnect();
            connected = false;
        }
    }

    public void sendKeys(String keys) {
        ensureConnected();
        ehllapi.sendKeys(keys);
    }

    public void enter() {
        sendKeys("@E");
        waitForScreenReady();
    }

    public String getText(int row, int col, int length) {
        ensureConnected();
        return ehllapi.copyFromScreen(row, col, length);
    }

    public void setText(int row, int col, String text) {
        ensureConnected();
        ehllapi.moveCursor(row, col);
        ehllapi.sendKeys(text);
    }

    public boolean waitForText(String expectedText, int timeoutSec) {
        long endTime = System.currentTimeMillis() + (timeoutSec * 1000L);
        while (System.currentTimeMillis() < endTime) {
            if (ehllapi.getScreenText().contains(expectedText)) {
                return true;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return false;
    }

    public String getScreen() {
        return ehllapi.getScreenText();
    }

    public void waitForScreenReady() {
        ehllapi.waitForUnlock();
    }

    private void ensureConnected() {
        if (!connected) {
            throw new IllegalStateException("MainFrameDriver not connected. Call connect() first.");
        }
    }

    public void close() {
        disconnect();
    }
}
