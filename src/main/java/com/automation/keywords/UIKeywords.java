package com.automation.keywords;

import com.automation.core.config.ConfigManager;
import com.automation.core.driver.DriverManager;
import com.automation.core.logging.LogManager;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


/**
 * Manual QA-friendly keywords for UI automation
 * Simple methods that can be used without deep technical knowledge
 */
public class UIKeywords {
    
    private static String getFrameworkType() {
        return ConfigManager.getInstance().getFrameworkType();
    }
    
    public static void openBrowser() {
        DriverManager.initializeDriver();
        LogManager.info("Browser opened");
    }
    
    public static void navigateToURL(String url) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            DriverManager.getSeleniumDriver().get(url);
        } else {
            DriverManager.getPlaywrightPage().navigate(url);
        }
        LogManager.info("Navigated to: " + url);
    }
    
    public static void clickElement(String locator) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator)).click();
        } else {
            DriverManager.getPlaywrightPage().click(locator);
        }
        LogManager.info("Clicked on: " + locator);
    }
    
    public static void enterText(String locator, String text) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            WebDriver driver = DriverManager.getSeleniumDriver();
            driver.findElement(By.cssSelector(locator)).clear();
            driver.findElement(By.cssSelector(locator)).sendKeys(text);
        } else {
            DriverManager.getPlaywrightPage().fill(locator, text);
        }
        LogManager.info("Entered text '" + text + "' in: " + locator);
    }
    
    public static String getTextFromElement(String locator) {
        String text;
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            text = DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator)).getText();
        } else {
            text = DriverManager.getPlaywrightPage().textContent(locator);
        }
        LogManager.info("Retrieved text: " + text);
        return text;
    }
    
    public static boolean isElementVisible(String locator) {
        boolean visible;
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            try {
                visible = DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator)).isDisplayed();
            } catch (Exception e) {
                visible = false;
            }
        } else {
            visible = DriverManager.getPlaywrightPage().isVisible(locator);
        }
        LogManager.info("Element visible: " + visible);
        return visible;
    }
    
    public static void waitForElement(String locator, int seconds) {
        if ("playwright".equalsIgnoreCase(getFrameworkType())) {
            DriverManager.getPlaywrightPage().waitForSelector(locator, 
                new Page.WaitForSelectorOptions().setTimeout(seconds * 1000));
        }
        LogManager.info("Waited for element: " + locator);
    }
    
    public static void closeBrowser() {
        DriverManager.quitDriver();
        LogManager.info("Browser closed");
    }

    // --- Keyword-driven UI Automation Methods ---

    public static void selectDropdownByText(String locator, String text) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            new org.openqa.selenium.support.ui.Select(DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator))).selectByVisibleText(text);
        } else {
            DriverManager.getPlaywrightPage().selectOption(locator, text);
        }
        LogManager.info("Selected dropdown by text: " + text + " in: " + locator);
    }

    public static void selectDropdownByValue(String locator, String value) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            new org.openqa.selenium.support.ui.Select(DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator))).selectByValue(value);
        } else {
            DriverManager.getPlaywrightPage().selectOption(locator, value);
        }
        LogManager.info("Selected dropdown by value: " + value + " in: " + locator);
    }

    public static void selectDropdownByIndex(String locator, int index) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            new org.openqa.selenium.support.ui.Select(DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator))).selectByIndex(index);
        } else {
            DriverManager.getPlaywrightPage().selectOption(locator, String.valueOf(index));
        }
        LogManager.info("Selected dropdown by index: " + index + " in: " + locator);
    }

    public static void checkCheckbox(String locator) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            WebDriver driver = DriverManager.getSeleniumDriver();
            if (!driver.findElement(By.cssSelector(locator)).isSelected()) {
                driver.findElement(By.cssSelector(locator)).click();
            }
        } else {
            if (!DriverManager.getPlaywrightPage().isChecked(locator)) {
                DriverManager.getPlaywrightPage().check(locator);
            }
        }
        LogManager.info("Checked checkbox: " + locator);
    }

    public static void uncheckCheckbox(String locator) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            WebDriver driver = DriverManager.getSeleniumDriver();
            if (driver.findElement(By.cssSelector(locator)).isSelected()) {
                driver.findElement(By.cssSelector(locator)).click();
            }
        } else {
            if (DriverManager.getPlaywrightPage().isChecked(locator)) {
                DriverManager.getPlaywrightPage().uncheck(locator);
            }
        }
        LogManager.info("Unchecked checkbox: " + locator);
    }

    public static void mouseHover(String locator) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            WebDriver driver = DriverManager.getSeleniumDriver();
            new org.openqa.selenium.interactions.Actions(driver).moveToElement(driver.findElement(By.cssSelector(locator))).perform();
        } else {
            DriverManager.getPlaywrightPage().hover(locator);
        }
        LogManager.info("Mouse hovered on: " + locator);
    }

    public static void doubleClick(String locator) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            WebDriver driver = DriverManager.getSeleniumDriver();
            new org.openqa.selenium.interactions.Actions(driver).doubleClick(driver.findElement(By.cssSelector(locator))).perform();
        } else {
            DriverManager.getPlaywrightPage().dblclick(locator);
        }
        LogManager.info("Double clicked on: " + locator);
    }

    public static void dragAndDrop(String sourceLocator, String targetLocator) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            WebDriver driver = DriverManager.getSeleniumDriver();
            new org.openqa.selenium.interactions.Actions(driver).dragAndDrop(
                driver.findElement(By.cssSelector(sourceLocator)),
                driver.findElement(By.cssSelector(targetLocator))).perform();
        } else {
            DriverManager.getPlaywrightPage().dragAndDrop(sourceLocator, targetLocator);
        }
        LogManager.info("Dragged element: " + sourceLocator + " to " + targetLocator);
    }

    public static void waitForVisible(String locator, int seconds) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            new org.openqa.selenium.support.ui.WebDriverWait(DriverManager.getSeleniumDriver(), java.time.Duration.ofSeconds(seconds))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(By.cssSelector(locator)));
        } else {
            DriverManager.getPlaywrightPage().waitForSelector(locator, new Page.WaitForSelectorOptions().setTimeout(seconds * 1000).setState(WaitForSelectorState.VISIBLE));
        }
        LogManager.info("Waited for visible: " + locator);
    }

    public static void waitForClickable(String locator, int seconds) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            new org.openqa.selenium.support.ui.WebDriverWait(DriverManager.getSeleniumDriver(), java.time.Duration.ofSeconds(seconds))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(By.cssSelector(locator)));
        } else {
            // Playwright does not have direct clickable wait, use visible
            DriverManager.getPlaywrightPage().waitForSelector(locator, new Page.WaitForSelectorOptions().setTimeout(seconds * 1000).setState(WaitForSelectorState.VISIBLE));
        }
        LogManager.info("Waited for clickable: " + locator);
    }

    public static void waitForInvisible(String locator, int seconds) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            new org.openqa.selenium.support.ui.WebDriverWait(DriverManager.getSeleniumDriver(), java.time.Duration.ofSeconds(seconds))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(locator)));
        } else {
            DriverManager.getPlaywrightPage().waitForSelector(locator, new Page.WaitForSelectorOptions().setTimeout(seconds * 1000).setState(WaitForSelectorState.HIDDEN));
        }
        LogManager.info("Waited for invisible: " + locator);
    }

    public static void waitForText(String locator, String text, int seconds) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            new org.openqa.selenium.support.ui.WebDriverWait(DriverManager.getSeleniumDriver(), java.time.Duration.ofSeconds(seconds))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(locator), text));
        } else {
            DriverManager.getPlaywrightPage().waitForSelector(locator, new Page.WaitForSelectorOptions().setTimeout(seconds * 1000));
            // Playwright does not have direct text wait, so check manually
            long end = System.currentTimeMillis() + seconds * 1000;
            while (System.currentTimeMillis() < end) {
                if (text.equals(DriverManager.getPlaywrightPage().textContent(locator))) break;
                try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            }
        }
        LogManager.info("Waited for text: '" + text + "' in: " + locator);
    }

    public static void takeScreenshot(String filePath) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            org.openqa.selenium.TakesScreenshot ts = (org.openqa.selenium.TakesScreenshot) DriverManager.getSeleniumDriver();
            java.io.File src = ts.getScreenshotAs(org.openqa.selenium.OutputType.FILE);
            try {
                java.nio.file.Files.move(src.toPath(), new java.io.File(filePath).toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                LogManager.info("Screenshot saved: " + filePath);
            } catch (Exception e) {
                LogManager.info("Screenshot failed: " + e.getMessage());
            }
        } else {
            DriverManager.getPlaywrightPage().screenshot(new Page.ScreenshotOptions().setPath(java.nio.file.Paths.get(filePath)));
            LogManager.info("Screenshot saved: " + filePath);
        }
    }

    public static void takeElementScreenshot(String locator, String filePath) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            WebDriver driver = DriverManager.getSeleniumDriver();
            org.openqa.selenium.WebElement element = driver.findElement(By.cssSelector(locator));
            java.io.File src = element.getScreenshotAs(org.openqa.selenium.OutputType.FILE);
            try {
                java.nio.file.Files.move(src.toPath(), new java.io.File(filePath).toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                LogManager.info("Element screenshot saved: " + filePath);
            } catch (Exception e) {
                LogManager.info("Element screenshot failed: " + e.getMessage());
            }
        } else {
            DriverManager.getPlaywrightPage().locator(locator).screenshot(new Locator.ScreenshotOptions().setPath(java.nio.file.Paths.get(filePath)));
            LogManager.info("Element screenshot saved: " + filePath);
        }
    }

    public static void uploadFile(String locator, String filePath) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator)).sendKeys(filePath);
        } else {
            DriverManager.getPlaywrightPage().setInputFiles(locator, java.nio.file.Paths.get(filePath));
        }
        LogManager.info("File uploaded: " + filePath + " to: " + locator);
    }

    public static void navigateBack() {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            DriverManager.getSeleniumDriver().navigate().back();
        } else {
            DriverManager.getPlaywrightPage().goBack();
        }
        LogManager.info("Navigated back");
    }

    public static void navigateForward() {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            DriverManager.getSeleniumDriver().navigate().forward();
        } else {
            DriverManager.getPlaywrightPage().goForward();
        }
        LogManager.info("Navigated forward");
    }

    public static void refreshPage() {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            DriverManager.getSeleniumDriver().navigate().refresh();
        } else {
            DriverManager.getPlaywrightPage().reload();
        }
        LogManager.info("Page refreshed");
    }

    public static String getAttribute(String locator, String attribute) {
        String value;
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            value = DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator)).getAttribute(attribute);
        } else {
            value = DriverManager.getPlaywrightPage().getAttribute(locator, attribute);
        }
        LogManager.info("Got attribute '" + attribute + "' from: " + locator + " value: " + value);
        return value;
    }

    public static String getCssValue(String locator, String property) {
        String value;
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            value = DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator)).getCssValue(property);
        } else {
            value = DriverManager.getPlaywrightPage().locator(locator).evaluate("el => getComputedStyle(el).getPropertyValue('" + property + "')").toString();
        }
        LogManager.info("Got CSS value '" + property + "' from: " + locator + " value: " + value);
        return value;
    }

    public static void scrollToElement(String locator) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            WebDriver driver = DriverManager.getSeleniumDriver();
            org.openqa.selenium.WebElement element = driver.findElement(By.cssSelector(locator));
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        } else {
            DriverManager.getPlaywrightPage().locator(locator).scrollIntoViewIfNeeded();
        }
        LogManager.info("Scrolled to element: " + locator);
    }

    public static void executeJS(String script, Object... args) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            ((org.openqa.selenium.JavascriptExecutor) DriverManager.getSeleniumDriver()).executeScript(script, args);
        } else {
            DriverManager.getPlaywrightPage().evaluate(script, args);
        }
        LogManager.info("Executed JS: " + script);
    }

    public static String getElementRect(String locator) {
        String rectInfo = "";
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            org.openqa.selenium.Rectangle rect = DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator)).getRect();
            rectInfo = String.format("x:%d y:%d width:%d height:%d", rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
        } else {
            Object box = DriverManager.getPlaywrightPage().locator(locator).boundingBox();
            rectInfo = box != null ? box.toString() : "null";
        }
        LogManager.info("Element rect: " + rectInfo);
        return rectInfo;
    }

    public static boolean isTextPresent(String text) {
        boolean present;
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            present = DriverManager.getSeleniumDriver().getPageSource().contains(text);
        } else {
            present = DriverManager.getPlaywrightPage().content().contains(text);
        }
        LogManager.info("Text present: '" + text + "' = " + present);
        return present;
    }

    public static boolean isElementEnabled(String locator) {
        boolean enabled;
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            enabled = DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator)).isEnabled();
        } else {
            enabled = DriverManager.getPlaywrightPage().isEnabled(locator);
        }
        LogManager.info("Element enabled: " + enabled);
        return enabled;
    }

    // --- Selenium 4 Only ---
    public static String getShadowRootText(String locator, String shadowLocator) {
        String text = "";
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            WebDriver driver = DriverManager.getSeleniumDriver();
            WebElement host = driver.findElement(By.cssSelector(locator));
            WebElement shadowRoot = (WebElement) ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("return arguments[0].shadowRoot", host);
            text = shadowRoot.findElement(By.cssSelector(shadowLocator)).getText();
            LogManager.info("Shadow root text: " + text);
        }
        return text;
    }

    public static String findElementRelative(String locator, String relativeType, String relativeLocator) {
        String text = "";
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            WebDriver driver = DriverManager.getSeleniumDriver();
            org.openqa.selenium.By by = By.cssSelector(locator);
            org.openqa.selenium.By relBy = By.cssSelector(relativeLocator);
            org.openqa.selenium.WebElement element = null;
            switch (relativeType.toLowerCase()) {
                case "above":
                    element = driver.findElement(org.openqa.selenium.support.locators.RelativeLocator.with(by).above(relBy));
                    break;
                case "below":
                    element = driver.findElement(org.openqa.selenium.support.locators.RelativeLocator.with(by).below(relBy));
                    break;
                case "near":
                    element = driver.findElement(org.openqa.selenium.support.locators.RelativeLocator.with(by).near(relBy));
                    break;
                case "leftof":
                    element = driver.findElement(org.openqa.selenium.support.locators.RelativeLocator.with(by).toLeftOf(relBy));
                    break;
                case "rightof":
                    element = driver.findElement(org.openqa.selenium.support.locators.RelativeLocator.with(by).toRightOf(relBy));
                    break;
            }
            if (element != null) text = element.getText();
            LogManager.info("Relative element text: " + text);
        }
        return text;
    }
}
