package com.automation.reusables;

import com.automation.core.logging.UnifiedLogger;
import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.List;

/**
 * Reusable methods for Windows desktop automation using WinAppDriver.
 */
public class WindowsDesktopReusable {
    protected WindowsDriver driver;

    /**
     * Launches the Windows desktop app with given capabilities and WinAppDriver URL.
     */
    public void launchApp(URL winAppDriverUrl, DesiredCapabilities capabilities) {
        driver = new WindowsDriver(winAppDriverUrl, capabilities);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        UnifiedLogger.info("Windows app launched successfully");
    }

    /**
     * Closes the app and quits the driver.
     */
    public void closeApp() {
        if (driver != null) {
            driver.quit();
            UnifiedLogger.info("Windows app closed");
        }
    }

    /**
     * Finds an element by locator.
     */
    public WebElement findElement(By locator) {
        return driver.findElement(locator);
    }

    /**
     * Finds multiple elements by locator.
     */
    public List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }

    /**
     * Clicks on an element.
     */
    public void click(By locator) {
        findElement(locator).click();
        UnifiedLogger.action("Click", locator);
    }

    /**
     * Double clicks on an element.
     */
    public void doubleClick(By locator) {
        Actions actions = new Actions(driver);
        actions.doubleClick(findElement(locator)).perform();
        UnifiedLogger.action("Double Click", locator);
    }

    /**
     * Right clicks on an element.
     */
    public void rightClick(By locator) {
        Actions actions = new Actions(driver);
        actions.contextClick(findElement(locator)).perform();
        UnifiedLogger.action("Right Click", locator);
    }

    /**
     * Types text into an element.
     */
    public void type(By locator, String text) {
        WebElement el = findElement(locator);
        el.clear();
        el.sendKeys(text);
        UnifiedLogger.action("Type", locator + " | Text: " + text);
    }

    /**
     * Sends keys to an element without clearing.
     */
    public void sendKeys(By locator, CharSequence... keys) {
        findElement(locator).sendKeys(keys);
        UnifiedLogger.action("Send Keys", locator);
    }

    /**
     * Gets text from an element.
     */
    public String getText(By locator) {
        return findElement(locator).getText();
    }

    /**
     * Gets attribute value from an element.
     */
    public String getAttribute(By locator, String attribute) {
        return findElement(locator).getAttribute(attribute);
    }

    /**
     * Checks if element is displayed.
     */
    public boolean isDisplayed(By locator) {
        return findElement(locator).isDisplayed();
    }

    /**
     * Checks if element is enabled.
     */
    public boolean isEnabled(By locator) {
        return findElement(locator).isEnabled();
    }

    /**
     * Checks if element is selected.
     */
    public boolean isSelected(By locator) {
        return findElement(locator).isSelected();
    }

    /**
     * Takes a screenshot and returns the file.
     */
    public File takeScreenshot(String fileName) {
        File srcFile = driver.getScreenshotAs(OutputType.FILE);
        File destFile = new File(fileName);
        srcFile.renameTo(destFile);
        UnifiedLogger.info("Screenshot captured: " + fileName);
        return destFile;
    }

    /**
     * Drags and drops from source to target element.
     */
    public void dragAndDrop(By source, By target) {
        Actions actions = new Actions(driver);
        actions.dragAndDrop(findElement(source), findElement(target)).perform();
        UnifiedLogger.action("Drag and Drop", "From: " + source + " To: " + target);
    }

    /**
     * Hovers over an element.
     */
    public void hover(By locator) {
        Actions actions = new Actions(driver);
        actions.moveToElement(findElement(locator)).perform();
    }

    /**
     * Switches to a window by title.
     */
    public void switchToWindow(String windowTitle) {
        for (String handle : driver.getWindowHandles()) {
            driver.switchTo().window(handle);
            if (driver.getTitle().contains(windowTitle)) {
                break;
            }
        }
        UnifiedLogger.action("Switch to Window", windowTitle);
    }

    /**
     * Switches to a window by handle.
     */
    public void switchToWindowByHandle(String handle) {
        driver.switchTo().window(handle);
    }

    /**
     * Gets current window handle.
     */
    public String getCurrentWindowHandle() {
        return driver.getWindowHandle();
    }

    /**
     * Gets all window handles.
     */
    public java.util.Set<String> getAllWindowHandles() {
        return driver.getWindowHandles();
    }

    /**
     * Closes current window.
     */
    public void closeCurrentWindow() {
        driver.close();
    }

    /**
     * Maximizes the window.
     */
    public void maximizeWindow() {
        driver.manage().window().maximize();
    }

    /**
     * Minimizes the window.
     */
    public void minimizeWindow() {
        driver.manage().window().minimize();
    }

    /**
     * Presses keyboard shortcut (e.g., Ctrl+C, Alt+F4).
     */
    public void pressKeyboardShortcut(Keys modifier, String key) {
        Actions actions = new Actions(driver);
        actions.keyDown(modifier).sendKeys(key).keyUp(modifier).perform();
        UnifiedLogger.action("Keyboard Shortcut", modifier + " + " + key);
    }

    /**
     * Presses a single key.
     */
    public void pressKey(Keys key) {
        Actions actions = new Actions(driver);
        actions.sendKeys(key).perform();
    }

    /**
     * Selects an item from dropdown by visible text.
     */
    public void selectDropdownByText(By locator, String text) {
        org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(findElement(locator));
        select.selectByVisibleText(text);
        UnifiedLogger.action("Select Dropdown", locator + " | Text: " + text);
    }

    /**
     * Selects an item from dropdown by value.
     */
    public void selectDropdownByValue(By locator, String value) {
        org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(findElement(locator));
        select.selectByValue(value);
    }

    /**
     * Selects an item from dropdown by index.
     */
    public void selectDropdownByIndex(By locator, int index) {
        org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(findElement(locator));
        select.selectByIndex(index);
    }

    /**
     * Waits for element to be visible.
     */
    public void waitForElement(By locator, int timeoutSeconds) {
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits for element to be clickable.
     */
    public void waitForElementClickable(By locator, int timeoutSeconds) {
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Scrolls to an element.
     */
    public void scrollToElement(By locator) {
        Actions actions = new Actions(driver);
        actions.scrollToElement(findElement(locator)).perform();
    }

    /**
     * Clicks element using JavaScript executor.
     */
    public void clickUsingJS(By locator) {
        org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", findElement(locator));
    }

    /**
     * Gets page source.
     */
    public String getPageSource() {
        return driver.getPageSource();
    }

    /**
     * Refreshes the current window.
     */
    public void refresh() {
        driver.navigate().refresh();
    }

    /**
     * Launches a Windows desktop app by app path.
     */
    public void launchAppByPath(String appPath) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("app", appPath);
        capabilities.setCapability("platformName", "Windows");
        capabilities.setCapability("deviceName", "WindowsPC");
        try {
            driver = new WindowsDriver(new URL("http://127.0.0.1:4723"), capabilities);
        } catch (Exception e) {
            throw new RuntimeException("Failed to launch app: " + appPath, e);
        }
    }

    /**
     * Attaches to an existing Windows app by app ID.
     */
    public void attachToApp(String appTopLevelWindowHandle) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("appTopLevelWindow", appTopLevelWindowHandle);
        capabilities.setCapability("platformName", "Windows");
        capabilities.setCapability("deviceName", "WindowsPC");
        try {
            driver = new WindowsDriver(new URL("http://127.0.0.1:4723"), capabilities);
        } catch (Exception e) {
            throw new RuntimeException("Failed to attach to app", e);
        }
    }

    /**
     * Gets the WindowsDriver instance.
     */
    public WindowsDriver getDriver() {
        return driver;
    }
}
