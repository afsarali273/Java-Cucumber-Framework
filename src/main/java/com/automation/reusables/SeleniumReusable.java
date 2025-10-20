package com.automation.reusables;

import com.automation.core.assertions.AssertUtils;
import com.automation.core.driver.DriverManager;
import com.automation.core.interfaces.WebActions;
import com.automation.core.logging.UnifiedLogger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.locators.RelativeLocator;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class SeleniumReusable implements WebActions<By> {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public SeleniumReusable() {
        this.driver = DriverManager.getSeleniumDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void navigateTo(String url) {
        driver.get(url);
        UnifiedLogger.info("Navigated to: " + url);
    }

    @Override
    public void click(By locator) {
        By by = locator;
        waitForElementToBeClickable(by);
        driver.findElement(by).click();
        UnifiedLogger.action("Click", by);
    }

    @Override
    public void type(By locator, String text) {
        By by = locator;
        waitForElementToBeVisible(by);
        WebElement element = driver.findElement(by);
        element.clear();
        element.sendKeys(text);
        UnifiedLogger.action("Type", by + " | Text: " + text);
    }

    @Override
    public void typeAndEnter(By locator, String text) {
        By by = locator;
        type(by, text);
        driver.findElement(by).sendKeys(Keys.ENTER);
        UnifiedLogger.action("Type and Enter", by);
    }

    @Override
    public String getText(By locator) {
        By by = locator;
        waitForElementToBeVisible(by);
        String text = driver.findElement(by).getText();
        UnifiedLogger.action("Get Text", by + " | Text: " + text);
        return text;
    }

    @Override
    public boolean isDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @Override
    public void selectDropdownByText(By locator, String text) {
        Select select = new Select(driver.findElement(locator));
        select.selectByVisibleText(text);
        UnifiedLogger.action("Select Dropdown", locator + " | Text: " + text);
    }

    @Override
    public void waitForElementToBeVisible(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    @Override
    public void waitForElementToBeClickable(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    @Override
    public void waitForSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }

    @Override
    public void scrollToElement(By locator) {
        WebElement element = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        UnifiedLogger.action("Scroll to Element", locator);
    }

    @Override
    public void jsClick(By locator) {
        WebElement element = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        UnifiedLogger.action("JS Click", locator);
    }

    // --- Advanced Selenium 4 and Web Automation Utilities ---

    // 1. Relative Locators (Selenium 4)
    public WebElement findElementAbove(By locator, By aboveLocator) {
        return driver.findElement(RelativeLocator.with(locator).above(aboveLocator));
    }
    public WebElement findElementBelow(By locator, By belowLocator) {
        return driver.findElement(RelativeLocator.with(locator).below(belowLocator));
    }
    public WebElement findElementNear(By locator, By nearLocator) {
        return driver.findElement(RelativeLocator.with(locator).near(nearLocator));
    }
    public WebElement findElementToLeftOf(By locator, By leftLocator) {
        return driver.findElement(RelativeLocator.with(locator).toLeftOf(leftLocator));
    }
    public WebElement findElementToRightOf(By locator, By rightLocator) {
        return driver.findElement(RelativeLocator.with(locator).toRightOf(rightLocator));
    }

    // 2. Window/Tab Management
    public void switchToWindow(String windowHandle) {
        driver.switchTo().window(windowHandle);
        UnifiedLogger.action("Switch to Window", windowHandle);
    }
    public void closeCurrentWindow() {
        driver.close();
        UnifiedLogger.info("Closed current window/tab");
    }
    public void openNewTab() {
        ((JavascriptExecutor) driver).executeScript("window.open()", "_blank");
        UnifiedLogger.info("Opened new tab");
    }
    public void switchToLatestTab() {
        List<String> handles = driver.getWindowHandles().stream().collect(Collectors.toList());
        driver.switchTo().window(handles.get(handles.size() - 1));
        UnifiedLogger.info("Switched to latest tab");
    }

    // 3. Screenshot Functionality
    public void takeElementScreenshot(By locator, String filePath) {
        WebElement element = driver.findElement(locator);
        File src = element.getScreenshotAs(OutputType.FILE);
        try {
            Files.move(src.toPath(), new File(filePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
            UnifiedLogger.info("Element screenshot saved: " + filePath);
        } catch (Exception e) {
            UnifiedLogger.error("Element screenshot failed", e);
        }
    }

    // 5. Advanced Waits
    public void waitForElementToBePresent(By locator) {
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }
    public void waitForElementToBeInvisible(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
    public void waitForAttributeToContain(By locator, String attribute, String value) {
        wait.until(ExpectedConditions.attributeContains(locator, attribute, value));
    }

    // 6. Actions API
    @Override
    public void mouseHover(By locator) {
        WebElement element = driver.findElement(locator);
        new org.openqa.selenium.interactions.Actions(driver).moveToElement(element).perform();
        UnifiedLogger.action("Mouse Hover", locator);
    }
    @Override
    public void dragAndDrop(By source, By target) {
        WebElement src = driver.findElement(source);
        WebElement tgt = driver.findElement(target);
        new org.openqa.selenium.interactions.Actions(driver).dragAndDrop(src, tgt).perform();
        UnifiedLogger.action("Drag and Drop", "From: " + source + " To: " + target);
    }
    @Override
    public void doubleClick(By locator) {
        WebElement element = driver.findElement(locator);
        new org.openqa.selenium.interactions.Actions(driver).doubleClick(element).perform();
        UnifiedLogger.action("Double Click", locator);
    }

    // 7. Get Element Rect (Selenium 4)
    public Rectangle getElementRect(By locator) {
        return driver.findElement(locator).getRect();
    }

    // 9. Browser Navigation
    @Override
    public void navigateBack() {
        driver.navigate().back();
        UnifiedLogger.info("Navigated back");
    }
    @Override
    public void navigateForward() {
        driver.navigate().forward();
        UnifiedLogger.info("Navigated forward");
    }
    @Override
    public void refreshPage() {
        driver.navigate().refresh();
        UnifiedLogger.info("Page refreshed");
    }

    // 10. Shadow DOM Support (Selenium 4)
    public WebElement getShadowRoot(By locator) {
        WebElement host = driver.findElement(locator);
        return (WebElement) ((JavascriptExecutor) driver).executeScript("return arguments[0].shadowRoot", host);
    }

    // 11. Dropdown by Value and Index
    @Override
    public void selectDropdownByValue(By locator, String value) {
        Select select = new Select(driver.findElement(locator));
        select.selectByValue(value);
        UnifiedLogger.action("Select Dropdown by Value", locator + " | Value: " + value);
    }

    @Override
    public void selectDropdownByIndex(By locator, int index) {
        Select select = new Select(driver.findElement(locator));
        select.selectByIndex(index);
        UnifiedLogger.action("Select Dropdown by Index", locator + " | Index: " + index);
    }

    @Override
    public void clear(By locator) {
        driver.findElement(locator).clear();
        UnifiedLogger.action("Clear", locator);
    }

    @Override
    public void check(By locator) {
        WebElement element = driver.findElement(locator);
        if (!element.isSelected()) {
            element.click();
        }
        UnifiedLogger.action("Check", locator);
    }

    @Override
    public void uncheck(By locator) {
        WebElement element = driver.findElement(locator);
        if (element.isSelected()) {
            element.click();
        }
        UnifiedLogger.action("Uncheck", locator);
    }

    @Override
    public boolean isChecked(By locator) {
        return driver.findElement(locator).isSelected();
    }

    @Override
    public String getAttribute(By locator, String attribute) {
        return driver.findElement(locator).getAttribute(attribute);
    }

    @Override
    public String getCssValue(By locator, String property) {
        return driver.findElement(locator).getCssValue(property);
    }

    @Override
    public boolean isEnabled(By locator) {
        return driver.findElement(locator).isEnabled();
    }

    @Override
    public void scrollToTop() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
        UnifiedLogger.info("Scrolled to top");
    }

    @Override
    public void scrollToBottom() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
        UnifiedLogger.info("Scrolled to bottom");
    }

    @Override
    public void takeScreenshot(String filePath) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File src = ts.getScreenshotAs(OutputType.FILE);
        try {
            Files.move(src.toPath(), new File(filePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
            UnifiedLogger.info("Screenshot saved: " + filePath);
        } catch (Exception e) {
            UnifiedLogger.error("Screenshot failed", e);
        }
    }

    @Override
    public void uploadFile(By locator, String filePath) {
        driver.findElement(locator).sendKeys(filePath);
        UnifiedLogger.action("Upload File", locator + " | File: " + filePath);
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
