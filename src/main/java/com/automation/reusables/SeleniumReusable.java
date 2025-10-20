package com.automation.reusables;

import com.automation.core.assertions.AssertUtils;
import com.automation.core.driver.DriverManager;
import com.automation.core.logging.LogManager;
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
        LogManager.info("Navigated to: " + url);
    }

    @Override
    public void click(By locator) {
        By by = locator;
        waitForElementToBeClickable(by);
        driver.findElement(by).click();
        LogManager.info("Clicked on element: " + by);
    }

    @Override
    public void type(By locator, String text) {
        By by = locator;
        waitForElementToBeVisible(by);
        WebElement element = driver.findElement(by);
        element.clear();
        element.sendKeys(text);
        LogManager.info("Typed '" + text + "' into element: " + by);
    }

    @Override
    public void typeAndEnter(By locator, String text) {
        By by = locator;
        type(by, text);
        driver.findElement(by).sendKeys(Keys.ENTER);
        LogManager.info("Pressed ENTER after typing");
    }

    @Override
    public String getText(By locator) {
        By by = locator;
        waitForElementToBeVisible(by);
        String text = driver.findElement(by).getText();
        LogManager.info("Retrieved text: " + text);
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
        LogManager.info("Selected dropdown option: " + text);
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
        LogManager.info("Scrolled to element: " + locator);
    }

    @Override
    public void jsClick(By locator) {
        WebElement element = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        LogManager.info("JavaScript clicked on element: " + locator);
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
        LogManager.info("Switched to window: " + windowHandle);
    }
    public void closeCurrentWindow() {
        driver.close();
        LogManager.info("Closed current window/tab");
    }
    public void openNewTab() {
        ((JavascriptExecutor) driver).executeScript("window.open()", "_blank");
        LogManager.info("Opened new tab");
    }
    public void switchToLatestTab() {
        List<String> handles = driver.getWindowHandles().stream().collect(Collectors.toList());
        driver.switchTo().window(handles.get(handles.size() - 1));
        LogManager.info("Switched to latest tab");
    }

    // 3. Screenshot Functionality
    public void takeElementScreenshot(By locator, String filePath) {
        WebElement element = driver.findElement(locator);
        File src = element.getScreenshotAs(OutputType.FILE);
        try {
            Files.move(src.toPath(), new File(filePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
            LogManager.info("Element screenshot saved: " + filePath);
        } catch (Exception e) {
            LogManager.info("Element screenshot failed: " + e.getMessage());
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
        LogManager.info("Mouse hovered on: " + locator);
    }
    @Override
    public void dragAndDrop(By source, By target) {
        WebElement src = driver.findElement(source);
        WebElement tgt = driver.findElement(target);
        new org.openqa.selenium.interactions.Actions(driver).dragAndDrop(src, tgt).perform();
        LogManager.info("Dragged element: " + source + " to " + target);
    }
    @Override
    public void doubleClick(By locator) {
        WebElement element = driver.findElement(locator);
        new org.openqa.selenium.interactions.Actions(driver).doubleClick(element).perform();
        LogManager.info("Double clicked on: " + locator);
    }

    // 7. Get Element Rect (Selenium 4)
    public Rectangle getElementRect(By locator) {
        return driver.findElement(locator).getRect();
    }

    // 9. Browser Navigation
    @Override
    public void navigateBack() {
        driver.navigate().back();
        LogManager.info("Navigated back");
    }
    @Override
    public void navigateForward() {
        driver.navigate().forward();
        LogManager.info("Navigated forward");
    }
    @Override
    public void refreshPage() {
        driver.navigate().refresh();
        LogManager.info("Page refreshed");
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
        LogManager.info("Selected dropdown value: " + value);
    }

    @Override
    public void selectDropdownByIndex(By locator, int index) {
        Select select = new Select(driver.findElement(locator));
        select.selectByIndex(index);
        LogManager.info("Selected dropdown index: " + index);
    }

    @Override
    public void clear(By locator) {
        driver.findElement(locator).clear();
        LogManager.info("Cleared element: " + locator);
    }

    @Override
    public void check(By locator) {
        WebElement element = driver.findElement(locator);
        if (!element.isSelected()) {
            element.click();
        }
        LogManager.info("Checked element: " + locator);
    }

    @Override
    public void uncheck(By locator) {
        WebElement element = driver.findElement(locator);
        if (element.isSelected()) {
            element.click();
        }
        LogManager.info("Unchecked element: " + locator);
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
        LogManager.info("Scrolled to top");
    }

    @Override
    public void scrollToBottom() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
        LogManager.info("Scrolled to bottom");
    }

    @Override
    public void takeScreenshot(String filePath) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File src = ts.getScreenshotAs(OutputType.FILE);
        try {
            Files.move(src.toPath(), new File(filePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
            LogManager.info("Screenshot saved: " + filePath);
        } catch (Exception e) {
            LogManager.error("Screenshot failed: " + e.getMessage());
        }
    }

    @Override
    public void uploadFile(By locator, String filePath) {
        driver.findElement(locator).sendKeys(filePath);
        LogManager.info("File uploaded: " + filePath);
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
