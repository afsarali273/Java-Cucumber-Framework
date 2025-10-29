package com.automation.keywords;

import com.automation.core.config.ConfigManager;
import com.automation.core.driver.DriverManager;
import com.automation.core.logging.UnifiedLogger;
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
        UnifiedLogger.info("Browser opened");
    }
    
    public static void navigateToURL(String url) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            DriverManager.getSeleniumDriver().get(url);
        } else {
            DriverManager.getPlaywrightPage().navigate(url);
        }
        UnifiedLogger.action("Navigate", url);
    }
    
    public static void clickElement(String locator) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator)).click();
        } else {
            DriverManager.getPlaywrightPage().click(locator);
        }
        UnifiedLogger.action("Click", locator);
    }
    
    public static void enterText(String locator, String text) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            WebDriver driver = DriverManager.getSeleniumDriver();
            driver.findElement(By.cssSelector(locator)).clear();
            driver.findElement(By.cssSelector(locator)).sendKeys(text);
        } else {
            DriverManager.getPlaywrightPage().fill(locator, text);
        }
        UnifiedLogger.action("Type", text + " into " + locator);
    }
    
    public static String getTextFromElement(String locator) {
        String text;
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            text = DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator)).getText();
        } else {
            text = DriverManager.getPlaywrightPage().textContent(locator);
        }
        UnifiedLogger.info("Retrieved text: " + text);
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
        UnifiedLogger.info("Element visible: " + visible);
        return visible;
    }
    
    public static void waitForElement(String locator, int seconds) {
        if ("playwright".equalsIgnoreCase(getFrameworkType())) {
            DriverManager.getPlaywrightPage().waitForSelector(locator, 
                new Page.WaitForSelectorOptions().setTimeout(seconds * 1000));
        }
        UnifiedLogger.info("Waited for element: " + locator);
    }
    
    public static void closeBrowser() {
        DriverManager.quitDriver();
        UnifiedLogger.info("Browser closed");
    }

    // --- Keyword-driven UI Automation Methods ---

    public static void selectDropdownByText(String locator, String text) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            new org.openqa.selenium.support.ui.Select(DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator))).selectByVisibleText(text);
        } else {
            DriverManager.getPlaywrightPage().selectOption(locator, text);
        }
        UnifiedLogger.action("Select dropdown", text + " in " + locator);
    }

    public static void selectDropdownByValue(String locator, String value) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            new org.openqa.selenium.support.ui.Select(DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator))).selectByValue(value);
        } else {
            DriverManager.getPlaywrightPage().selectOption(locator, value);
        }
        UnifiedLogger.info("Selected dropdown by value: " + value + " in: " + locator);
    }

    public static void selectDropdownByIndex(String locator, int index) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            new org.openqa.selenium.support.ui.Select(DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator))).selectByIndex(index);
        } else {
            DriverManager.getPlaywrightPage().selectOption(locator, String.valueOf(index));
        }
        UnifiedLogger.info("Selected dropdown by index: " + index + " in: " + locator);
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
        UnifiedLogger.info("Checked checkbox: " + locator);
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
        UnifiedLogger.info("Unchecked checkbox: " + locator);
    }

    public static void mouseHover(String locator) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            WebDriver driver = DriverManager.getSeleniumDriver();
            new org.openqa.selenium.interactions.Actions(driver).moveToElement(driver.findElement(By.cssSelector(locator))).perform();
        } else {
            DriverManager.getPlaywrightPage().hover(locator);
        }
        UnifiedLogger.info("Mouse hovered on: " + locator);
    }

    public static void doubleClick(String locator) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            WebDriver driver = DriverManager.getSeleniumDriver();
            new org.openqa.selenium.interactions.Actions(driver).doubleClick(driver.findElement(By.cssSelector(locator))).perform();
        } else {
            DriverManager.getPlaywrightPage().dblclick(locator);
        }
        UnifiedLogger.info("Double clicked on: " + locator);
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
        UnifiedLogger.info("Dragged element: " + sourceLocator + " to " + targetLocator);
    }

    public static void waitForVisible(String locator, int seconds) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            new org.openqa.selenium.support.ui.WebDriverWait(DriverManager.getSeleniumDriver(), java.time.Duration.ofSeconds(seconds))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(By.cssSelector(locator)));
        } else {
            DriverManager.getPlaywrightPage().waitForSelector(locator, new Page.WaitForSelectorOptions().setTimeout(seconds * 1000).setState(WaitForSelectorState.VISIBLE));
        }
        UnifiedLogger.info("Waited for visible: " + locator);
    }

    public static void waitForClickable(String locator, int seconds) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            new org.openqa.selenium.support.ui.WebDriverWait(DriverManager.getSeleniumDriver(), java.time.Duration.ofSeconds(seconds))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(By.cssSelector(locator)));
        } else {
            // Playwright does not have direct clickable wait, use visible
            DriverManager.getPlaywrightPage().waitForSelector(locator, new Page.WaitForSelectorOptions().setTimeout(seconds * 1000).setState(WaitForSelectorState.VISIBLE));
        }
        UnifiedLogger.info("Waited for clickable: " + locator);
    }

    public static void waitForInvisible(String locator, int seconds) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            new org.openqa.selenium.support.ui.WebDriverWait(DriverManager.getSeleniumDriver(), java.time.Duration.ofSeconds(seconds))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(locator)));
        } else {
            DriverManager.getPlaywrightPage().waitForSelector(locator, new Page.WaitForSelectorOptions().setTimeout(seconds * 1000).setState(WaitForSelectorState.HIDDEN));
        }
        UnifiedLogger.info("Waited for invisible: " + locator);
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
        UnifiedLogger.info("Waited for text: '" + text + "' in: " + locator);
    }

    public static void takeScreenshot(String filePath) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            org.openqa.selenium.TakesScreenshot ts = (org.openqa.selenium.TakesScreenshot) DriverManager.getSeleniumDriver();
            java.io.File src = ts.getScreenshotAs(org.openqa.selenium.OutputType.FILE);
            try {
                java.nio.file.Files.move(src.toPath(), new java.io.File(filePath).toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                UnifiedLogger.info("Screenshot saved: " + filePath);
            } catch (Exception e) {
                UnifiedLogger.info("Screenshot failed: " + e.getMessage());
            }
        } else {
            DriverManager.getPlaywrightPage().screenshot(new Page.ScreenshotOptions().setPath(java.nio.file.Paths.get(filePath)));
            UnifiedLogger.info("Screenshot saved: " + filePath);
        }
    }

    public static void takeElementScreenshot(String locator, String filePath) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            WebDriver driver = DriverManager.getSeleniumDriver();
            org.openqa.selenium.WebElement element = driver.findElement(By.cssSelector(locator));
            java.io.File src = element.getScreenshotAs(org.openqa.selenium.OutputType.FILE);
            try {
                java.nio.file.Files.move(src.toPath(), new java.io.File(filePath).toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                UnifiedLogger.info("Element screenshot saved: " + filePath);
            } catch (Exception e) {
                UnifiedLogger.info("Element screenshot failed: " + e.getMessage());
            }
        } else {
            DriverManager.getPlaywrightPage().locator(locator).screenshot(new Locator.ScreenshotOptions().setPath(java.nio.file.Paths.get(filePath)));
            UnifiedLogger.info("Element screenshot saved: " + filePath);
        }
    }

    public static void uploadFile(String locator, String filePath) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator)).sendKeys(filePath);
        } else {
            DriverManager.getPlaywrightPage().setInputFiles(locator, java.nio.file.Paths.get(filePath));
        }
        UnifiedLogger.info("File uploaded: " + filePath + " to: " + locator);
    }

    public static void navigateBack() {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            DriverManager.getSeleniumDriver().navigate().back();
        } else {
            DriverManager.getPlaywrightPage().goBack();
        }
        UnifiedLogger.info("Navigated back");
    }

    public static void navigateForward() {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            DriverManager.getSeleniumDriver().navigate().forward();
        } else {
            DriverManager.getPlaywrightPage().goForward();
        }
        UnifiedLogger.info("Navigated forward");
    }

    public static void refreshPage() {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            DriverManager.getSeleniumDriver().navigate().refresh();
        } else {
            DriverManager.getPlaywrightPage().reload();
        }
        UnifiedLogger.info("Page refreshed");
    }

    public static String getAttribute(String locator, String attribute) {
        String value;
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            value = DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator)).getAttribute(attribute);
        } else {
            value = DriverManager.getPlaywrightPage().getAttribute(locator, attribute);
        }
        UnifiedLogger.info("Got attribute '" + attribute + "' from: " + locator + " value: " + value);
        return value;
    }

    public static String getCssValue(String locator, String property) {
        String value;
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            value = DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator)).getCssValue(property);
        } else {
            value = DriverManager.getPlaywrightPage().locator(locator).evaluate("el => getComputedStyle(el).getPropertyValue('" + property + "')").toString();
        }
        UnifiedLogger.info("Got CSS value '" + property + "' from: " + locator + " value: " + value);
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
        UnifiedLogger.info("Scrolled to element: " + locator);
    }

    public static void executeJS(String script, Object... args) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            ((org.openqa.selenium.JavascriptExecutor) DriverManager.getSeleniumDriver()).executeScript(script, args);
        } else {
            DriverManager.getPlaywrightPage().evaluate(script, args);
        }
        UnifiedLogger.info("Executed JS: " + script);
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
        UnifiedLogger.info("Element rect: " + rectInfo);
        return rectInfo;
    }

    public static boolean isTextPresent(String text) {
        boolean present;
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            present = DriverManager.getSeleniumDriver().getPageSource().contains(text);
        } else {
            present = DriverManager.getPlaywrightPage().content().contains(text);
        }
        UnifiedLogger.info("Text present: '" + text + "' = " + present);
        return present;
    }

    public static boolean isElementEnabled(String locator) {
        boolean enabled;
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            enabled = DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator)).isEnabled();
        } else {
            enabled = DriverManager.getPlaywrightPage().isEnabled(locator);
        }
        UnifiedLogger.info("Element enabled: " + enabled);
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
            UnifiedLogger.info("Shadow root text: " + text);
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
            UnifiedLogger.info("Relative element text: " + text);
        }
        return text;
    }

    public static boolean isElementPresent(String locator) {
        boolean present;
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            try {
                DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator));
                present = true;
            } catch (org.openqa.selenium.NoSuchElementException e) {
                present = false;
            }
        } else {
            present = DriverManager.getPlaywrightPage().locator(locator).count() > 0;
        }
        UnifiedLogger.info("Element present: " + present);
        return present;
    }

    public static int getElementCount(String locator) {
        int count;
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            count = DriverManager.getSeleniumDriver().findElements(By.cssSelector(locator)).size();
        } else {
            count = DriverManager.getPlaywrightPage().locator(locator).count();
        }
        UnifiedLogger.info("Element count: " + count);
        return count;
    }

    public static boolean isCheckboxChecked(String locator) {
        boolean checked;
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            checked = DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator)).isSelected();
        } else {
            checked = DriverManager.getPlaywrightPage().isChecked(locator);
        }
        UnifiedLogger.info("Checkbox checked: " + checked);
        return checked;
    }

    public static void scrollToTop() {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            ((org.openqa.selenium.JavascriptExecutor) DriverManager.getSeleniumDriver()).executeScript("window.scrollTo(0, 0);");
        } else {
            DriverManager.getPlaywrightPage().evaluate("window.scrollTo(0, 0)");
        }
        UnifiedLogger.info("Scrolled to top");
    }

    public static void scrollToBottom() {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            ((org.openqa.selenium.JavascriptExecutor) DriverManager.getSeleniumDriver()).executeScript("window.scrollTo(0, document.body.scrollHeight);");
        } else {
            DriverManager.getPlaywrightPage().evaluate("window.scrollTo(0, document.body.scrollHeight)");
        }
        UnifiedLogger.info("Scrolled to bottom");
    }

    public static void pressKey(String locator, String key) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator)).sendKeys(org.openqa.selenium.Keys.valueOf(key.toUpperCase()));
        } else {
            DriverManager.getPlaywrightPage().press(locator, key);
        }
        UnifiedLogger.info("Pressed key '" + key + "' on: " + locator);
    }

    public static String getSelectedDropdownOption(String locator) {
        String selected;
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            selected = new org.openqa.selenium.support.ui.Select(DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator))).getFirstSelectedOption().getText();
        } else {
            selected = DriverManager.getPlaywrightPage().locator(locator + " option:checked").textContent();
        }
        UnifiedLogger.info("Selected dropdown option: " + selected);
        return selected;
    }

    public static void rightClick(String locator) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            WebDriver driver = DriverManager.getSeleniumDriver();
            new org.openqa.selenium.interactions.Actions(driver).contextClick(driver.findElement(By.cssSelector(locator))).perform();
        } else {
            DriverManager.getPlaywrightPage().click(locator, new Page.ClickOptions().setButton(com.microsoft.playwright.options.MouseButton.RIGHT));
        }
        UnifiedLogger.info("Right clicked on: " + locator);
    }

    public static void acceptAlert() {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            DriverManager.getSeleniumDriver().switchTo().alert().accept();
        } else {
            DriverManager.getPlaywrightPage().onDialog(dialog -> dialog.accept());
        }
        UnifiedLogger.info("Accepted alert");
    }

    public static void dismissAlert() {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            DriverManager.getSeleniumDriver().switchTo().alert().dismiss();
        } else {
            DriverManager.getPlaywrightPage().onDialog(dialog -> dialog.dismiss());
        }
        UnifiedLogger.info("Dismissed alert");
    }

    public static String getAlertText() {
        String text = "";
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            text = DriverManager.getSeleniumDriver().switchTo().alert().getText();
        } else {
            // Playwright handles dialogs differently - text needs to be captured in dialog handler
            UnifiedLogger.info("Playwright alert text retrieval requires dialog handler");
        }
        UnifiedLogger.info("Alert text: " + text);
        return text;
    }

    public static void switchToFrame(String frameLocator) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            DriverManager.getSeleniumDriver().switchTo().frame(DriverManager.getSeleniumDriver().findElement(By.cssSelector(frameLocator)));
        } else {
            // Playwright auto-handles frames, but can use frameLocator if needed
            UnifiedLogger.info("Playwright auto-handles frames");
        }
        UnifiedLogger.info("Switched to frame: " + frameLocator);
    }

    public static void switchToDefaultContent() {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            DriverManager.getSeleniumDriver().switchTo().defaultContent();
        } else {
            UnifiedLogger.info("Playwright auto-handles frames");
        }
        UnifiedLogger.info("Switched to default content");
    }

    public static String getPageTitle() {
        String title;
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            title = DriverManager.getSeleniumDriver().getTitle();
        } else {
            title = DriverManager.getPlaywrightPage().title();
        }
        UnifiedLogger.info("Page title: " + title);
        return title;
    }

    public static String getCurrentURL() {
        String url;
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            url = DriverManager.getSeleniumDriver().getCurrentUrl();
        } else {
            url = DriverManager.getPlaywrightPage().url();
        }
        UnifiedLogger.info("Current URL: " + url);
        return url;
    }

    // ------------------- Additional Wait Methods -------------------

    public static void waitForPageLoad() {
        waitForPageLoad(30);
    }

    public static void waitForPageLoad(int seconds) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            new org.openqa.selenium.support.ui.WebDriverWait(DriverManager.getSeleniumDriver(), java.time.Duration.ofSeconds(seconds))
                .until(driver -> ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
        } else {
            DriverManager.getPlaywrightPage().waitForLoadState(com.microsoft.playwright.options.LoadState.LOAD, new Page.WaitForLoadStateOptions().setTimeout(seconds * 1000));
        }
        UnifiedLogger.info("Waited for page load");
    }

    public static void waitForElementPresent(String locator) {
        waitForElementPresent(locator, 30);
    }

    public static void waitForElementPresent(String locator, int seconds) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            new org.openqa.selenium.support.ui.WebDriverWait(DriverManager.getSeleniumDriver(), java.time.Duration.ofSeconds(seconds))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(By.cssSelector(locator)));
        } else {
            DriverManager.getPlaywrightPage().waitForSelector(locator, new Page.WaitForSelectorOptions().setTimeout(seconds * 1000));
        }
        UnifiedLogger.info("Waited for element present: " + locator);
    }

    public static void waitForElementEnabled(String locator) {
        waitForElementEnabled(locator, 30);
    }

    public static void waitForElementEnabled(String locator, int seconds) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            new org.openqa.selenium.support.ui.WebDriverWait(DriverManager.getSeleniumDriver(), java.time.Duration.ofSeconds(seconds))
                .until(driver -> driver.findElement(By.cssSelector(locator)).isEnabled());
        } else {
            DriverManager.getPlaywrightPage().waitForSelector(locator, new Page.WaitForSelectorOptions().setTimeout(seconds * 1000));
            long end = System.currentTimeMillis() + seconds * 1000;
            while (System.currentTimeMillis() < end) {
                if (DriverManager.getPlaywrightPage().isEnabled(locator)) break;
                try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            }
        }
        UnifiedLogger.info("Waited for element enabled: " + locator);
    }

    public static void waitForTextPresent(String text) {
        waitForTextPresent(text, 30);
    }

    public static void waitForTextPresent(String text, int seconds) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            new org.openqa.selenium.support.ui.WebDriverWait(DriverManager.getSeleniumDriver(), java.time.Duration.ofSeconds(seconds))
                .until(driver -> driver.getPageSource().contains(text));
        } else {
            long end = System.currentTimeMillis() + seconds * 1000;
            while (System.currentTimeMillis() < end) {
                if (DriverManager.getPlaywrightPage().content().contains(text)) break;
                try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            }
        }
        UnifiedLogger.info("Waited for text present: " + text);
    }

    public static void waitForTextToDisappear(String text) {
        waitForTextToDisappear(text, 30);
    }

    public static void waitForTextToDisappear(String text, int seconds) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            new org.openqa.selenium.support.ui.WebDriverWait(DriverManager.getSeleniumDriver(), java.time.Duration.ofSeconds(seconds))
                .until(driver -> !driver.getPageSource().contains(text));
        } else {
            long end = System.currentTimeMillis() + seconds * 1000;
            while (System.currentTimeMillis() < end) {
                if (!DriverManager.getPlaywrightPage().content().contains(text)) break;
                try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            }
        }
        UnifiedLogger.info("Waited for text to disappear: " + text);
    }

    public static void waitForAttributeContains(String locator, String attribute, String value) {
        waitForAttributeContains(locator, attribute, value, 30);
    }

    public static void waitForAttributeContains(String locator, String attribute, String value, int seconds) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            new org.openqa.selenium.support.ui.WebDriverWait(DriverManager.getSeleniumDriver(), java.time.Duration.ofSeconds(seconds))
                .until(driver -> {
                    String attrValue = driver.findElement(By.cssSelector(locator)).getAttribute(attribute);
                    return attrValue != null && attrValue.contains(value);
                });
        } else {
            long end = System.currentTimeMillis() + seconds * 1000;
            while (System.currentTimeMillis() < end) {
                String attrValue = DriverManager.getPlaywrightPage().getAttribute(locator, attribute);
                if (attrValue != null && attrValue.contains(value)) break;
                try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            }
        }
        UnifiedLogger.info("Waited for attribute '" + attribute + "' to contain '" + value + "' in: " + locator);
    }

    public static void waitForURLContains(String urlPart) {
        waitForURLContains(urlPart, 30);
    }

    public static void waitForURLContains(String urlPart, int seconds) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            new org.openqa.selenium.support.ui.WebDriverWait(DriverManager.getSeleniumDriver(), java.time.Duration.ofSeconds(seconds))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains(urlPart));
        } else {
            DriverManager.getPlaywrightPage().waitForURL("**/*" + urlPart + "*", new Page.WaitForURLOptions().setTimeout(seconds * 1000));
        }
        UnifiedLogger.info("Waited for URL to contain: " + urlPart);
    }

    public static void waitForTitle(String title) {
        waitForTitle(title, 30);
    }

    public static void waitForTitle(String title, int seconds) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            new org.openqa.selenium.support.ui.WebDriverWait(DriverManager.getSeleniumDriver(), java.time.Duration.ofSeconds(seconds))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.titleIs(title));
        } else {
            long end = System.currentTimeMillis() + seconds * 1000;
            while (System.currentTimeMillis() < end) {
                if (title.equals(DriverManager.getPlaywrightPage().title())) break;
                try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            }
        }
        UnifiedLogger.info("Waited for title: " + title);
    }

    public static void waitForTitleContains(String titlePart) {
        waitForTitleContains(titlePart, 30);
    }

    public static void waitForTitleContains(String titlePart, int seconds) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            new org.openqa.selenium.support.ui.WebDriverWait(DriverManager.getSeleniumDriver(), java.time.Duration.ofSeconds(seconds))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.titleContains(titlePart));
        } else {
            long end = System.currentTimeMillis() + seconds * 1000;
            while (System.currentTimeMillis() < end) {
                if (DriverManager.getPlaywrightPage().title().contains(titlePart)) break;
                try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            }
        }
        UnifiedLogger.info("Waited for title to contain: " + titlePart);
    }

    public static void waitForElementCount(String locator, int count) {
        waitForElementCount(locator, count, 30);
    }

    public static void waitForElementCount(String locator, int count, int seconds) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            new org.openqa.selenium.support.ui.WebDriverWait(DriverManager.getSeleniumDriver(), java.time.Duration.ofSeconds(seconds))
                .until(driver -> driver.findElements(By.cssSelector(locator)).size() == count);
        } else {
            long end = System.currentTimeMillis() + seconds * 1000;
            while (System.currentTimeMillis() < end) {
                if (DriverManager.getPlaywrightPage().locator(locator).count() == count) break;
                try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            }
        }
        UnifiedLogger.info("Waited for element count " + count + " of: " + locator);
    }

    public static void waitForAlert() {
        waitForAlert(30);
    }

    public static void waitForAlert(int seconds) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            new org.openqa.selenium.support.ui.WebDriverWait(DriverManager.getSeleniumDriver(), java.time.Duration.ofSeconds(seconds))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent());
        } else {
            // Playwright handles alerts via dialog events
            UnifiedLogger.info("Playwright handles alerts via dialog events");
        }
        UnifiedLogger.info("Waited for alert");
    }

    public static void waitForFrameAvailable(String locator) {
        waitForFrameAvailable(locator, 30);
    }

    public static void waitForFrameAvailable(String locator, int seconds) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            new org.openqa.selenium.support.ui.WebDriverWait(DriverManager.getSeleniumDriver(), java.time.Duration.ofSeconds(seconds))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.cssSelector(locator)));
        } else {
            // Playwright auto-handles frames
            DriverManager.getPlaywrightPage().waitForSelector(locator, new Page.WaitForSelectorOptions().setTimeout(seconds * 1000));
        }
        UnifiedLogger.info("Waited for frame available: " + locator);
    }

    // ------------------- Advanced Element Operations -------------------

    public static void clickWithJS(String locator) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            WebElement element = DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator));
            ((org.openqa.selenium.JavascriptExecutor) DriverManager.getSeleniumDriver()).executeScript("arguments[0].click();", element);
        } else {
            DriverManager.getPlaywrightPage().evaluate(
                    "locator => { const el = document.querySelector(locator); if (el) el.click(); }",
                    locator
            );


        }
        UnifiedLogger.info("Clicked with JS: " + locator);
    }

    public static void clickIfVisible(String locator) {
        if (isElementVisible(locator)) {
            clickElement(locator);
        } else {
            UnifiedLogger.info("Element not visible, skipping click: " + locator);
        }
    }

    public static void typeWithoutClear(String locator, String text) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator)).sendKeys(text);
        } else {
            DriverManager.getPlaywrightPage().type(locator, text);
        }
        UnifiedLogger.info("Typed without clear: " + text + " into " + locator);
    }

    public static void clearText(String locator) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator)).clear();
        } else {
            DriverManager.getPlaywrightPage().fill(locator, "");
        }
        UnifiedLogger.info("Cleared text: " + locator);
    }

    public static void focusElement(String locator) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            WebElement element = DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator));
            ((org.openqa.selenium.JavascriptExecutor) DriverManager.getSeleniumDriver()).executeScript("arguments[0].focus();", element);
        } else {
            DriverManager.getPlaywrightPage().focus(locator);
        }
        UnifiedLogger.info("Focused element: " + locator);
    }


    public static void highlightElement(String locator) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            WebElement element = DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator));
            ((org.openqa.selenium.JavascriptExecutor) DriverManager.getSeleniumDriver())
                .executeScript("arguments[0].style.border='3px solid red'", element);
        } else {
            DriverManager.getPlaywrightPage().evaluate(
                    "locator => { const el = document.querySelector(locator); if (el) el.style.border = '3px solid red'; }",
                    locator
            );

        }
        UnifiedLogger.info("Highlighted element: " + locator);
    }

    public static void removeHighlight(String locator) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            WebElement element = DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator));
            ((org.openqa.selenium.JavascriptExecutor) DriverManager.getSeleniumDriver())
                .executeScript("arguments[0].style.border=''", element);
        } else {
            DriverManager.getPlaywrightPage().evaluate(
                    "locator => { const el = document.querySelector(locator); if (el) el.style.border = ''; }",
                    locator
            );
        }
        UnifiedLogger.info("Removed highlight: " + locator);
    }

    // ------------------- Text & Content Operations -------------------

    public static String getInnerHTML(String locator) {
        String html;
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            html = DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator)).getAttribute("innerHTML");
        } else {
            html = DriverManager.getPlaywrightPage().innerHTML(locator);
        }
        UnifiedLogger.info("Got innerHTML from: " + locator);
        return html;
    }

    public static String getInnerText(String locator) {
        String text;
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            text = DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator)).getAttribute("innerText");
        } else {
            text = DriverManager.getPlaywrightPage().innerText(locator);
        }
        UnifiedLogger.info("Got innerText from: " + locator);
        return text;
    }

    public static String getInputValue(String locator) {
        String value;
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            value = DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator)).getAttribute("value");
        } else {
            value = DriverManager.getPlaywrightPage().inputValue(locator);
        }
        UnifiedLogger.info("Got input value from: " + locator + " = " + value);
        return value;
    }

    public static String getPageSource() {
        String source;
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            source = DriverManager.getSeleniumDriver().getPageSource();
        } else {
            source = DriverManager.getPlaywrightPage().content();
        }
        UnifiedLogger.info("Retrieved page source");
        return source;
    }

    // ------------------- Window & Tab Operations -------------------

    public static void maximizeWindow() {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            DriverManager.getSeleniumDriver().manage().window().maximize();
        } else {
            DriverManager.getPlaywrightPage().setViewportSize(1920, 1080);
        }
        UnifiedLogger.info("Maximized window");
    }

    public static void setWindowSize(int width, int height) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            DriverManager.getSeleniumDriver().manage().window().setSize(new org.openqa.selenium.Dimension(width, height));
        } else {
            DriverManager.getPlaywrightPage().setViewportSize(width, height);
        }
        UnifiedLogger.info("Set window size: " + width + "x" + height);
    }

    public static String getWindowHandle() {
        String handle = "";
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            handle = DriverManager.getSeleniumDriver().getWindowHandle();
        }
        UnifiedLogger.info("Got window handle: " + handle);
        return handle;
    }

    public static java.util.Set<String> getAllWindowHandles() {
        java.util.Set<String> handles = new java.util.HashSet<>();
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            handles = DriverManager.getSeleniumDriver().getWindowHandles();
        }
        UnifiedLogger.info("Got all window handles: " + handles.size());
        return handles;
    }

    public static void switchToWindow(String windowHandle) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            DriverManager.getSeleniumDriver().switchTo().window(windowHandle);
        }
        UnifiedLogger.info("Switched to window: " + windowHandle);
    }

    public static void closeCurrentWindow() {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            DriverManager.getSeleniumDriver().close();
        } else {
            DriverManager.getPlaywrightPage().close();
        }
        UnifiedLogger.info("Closed current window");
    }

    // ------------------- Cookie Operations -------------------

    public static void addCookie(String name, String value) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            DriverManager.getSeleniumDriver().manage().addCookie(new org.openqa.selenium.Cookie(name, value));
        } else {
            DriverManager.getPlaywrightPage().context().addCookies(java.util.Arrays.asList(
                new com.microsoft.playwright.options.Cookie(name, value).setUrl(DriverManager.getPlaywrightPage().url())));
        }
        UnifiedLogger.info("Added cookie: " + name + " = " + value);
    }

    public static String getCookie(String name) {
        String value = "";
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            org.openqa.selenium.Cookie cookie = DriverManager.getSeleniumDriver().manage().getCookieNamed(name);
            value = cookie != null ? cookie.getValue() : "";
        } else {
            java.util.List<com.microsoft.playwright.options.Cookie> cookies = DriverManager.getPlaywrightPage().context().cookies();
            value = cookies.stream().filter(c -> c.name.equals(name)).findFirst().map(c -> c.value).orElse("");
        }
        UnifiedLogger.info("Got cookie: " + name + " = " + value);
        return value;
    }

    public static void deleteAllCookies() {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            DriverManager.getSeleniumDriver().manage().deleteAllCookies();
        } else {
            DriverManager.getPlaywrightPage().context().clearCookies();
        }
        UnifiedLogger.info("Deleted all cookies");
    }

    public static void deleteCookie(String name) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            DriverManager.getSeleniumDriver().manage().deleteCookieNamed(name);
        } else {
            java.util.List<com.microsoft.playwright.options.Cookie> cookies = DriverManager.getPlaywrightPage().context().cookies();
            cookies.stream().filter(c -> !c.name.equals(name)).forEach(c -> 
                DriverManager.getPlaywrightPage().context().addCookies(java.util.Arrays.asList(c)));
        }
        UnifiedLogger.info("Deleted cookie: " + name);
    }

    // ------------------- Scroll Operations -------------------

    public static void scrollByPixels(int x, int y) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            ((org.openqa.selenium.JavascriptExecutor) DriverManager.getSeleniumDriver())
                .executeScript("window.scrollBy(" + x + "," + y + ");");
        } else {
            DriverManager.getPlaywrightPage().evaluate("(x, y) => { window.scrollBy(x, y); }");
        }
        UnifiedLogger.info("Scrolled by pixels: x=" + x + ", y=" + y);
    }

    public static void scrollToCoordinates(int x, int y) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            ((org.openqa.selenium.JavascriptExecutor) DriverManager.getSeleniumDriver())
                .executeScript("window.scrollTo(" + x + "," + y + ");");
        } else {
            DriverManager.getPlaywrightPage().evaluate("(x, y) => { window.scrollTo(x, y); }");
        }
        UnifiedLogger.info("Scrolled to coordinates: x=" + x + ", y=" + y);
    }

    public static void scrollElementIntoCenter(String locator) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            WebElement element = DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator));
            ((org.openqa.selenium.JavascriptExecutor) DriverManager.getSeleniumDriver())
                .executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        } else {
            DriverManager.getPlaywrightPage().locator(locator).evaluate("el => el.scrollIntoView({block: 'center'})");
        }
        UnifiedLogger.info("Scrolled element into center: " + locator);
    }

    // ------------------- Keyboard Actions -------------------

    public static void pressKeyboardKey(String key) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            new org.openqa.selenium.interactions.Actions(DriverManager.getSeleniumDriver())
                .sendKeys(org.openqa.selenium.Keys.valueOf(key.toUpperCase())).perform();
        } else {
            DriverManager.getPlaywrightPage().keyboard().press(key);
        }
        UnifiedLogger.info("Pressed keyboard key: " + key);
    }

    public static void typeText(String text) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            new org.openqa.selenium.interactions.Actions(DriverManager.getSeleniumDriver()).sendKeys(text).perform();
        } else {
            DriverManager.getPlaywrightPage().keyboard().type(text);
        }
        UnifiedLogger.info("Typed text: " + text);
    }

    public static void pressEnter() {
        pressKeyboardKey("Enter");
    }

    public static void pressEscape() {
        pressKeyboardKey("Escape");
    }

    public static void pressTab() {
        pressKeyboardKey("Tab");
    }

    // ------------------- Mouse Actions -------------------

    public static void clickAtCoordinates(int x, int y) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            new org.openqa.selenium.interactions.Actions(DriverManager.getSeleniumDriver())
                .moveByOffset(x, y).click().perform();
        } else {
            DriverManager.getPlaywrightPage().mouse().click(x, y);
        }
        UnifiedLogger.info("Clicked at coordinates: x=" + x + ", y=" + y);
    }

    public static void moveMouseToCoordinates(int x, int y) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            new org.openqa.selenium.interactions.Actions(DriverManager.getSeleniumDriver())
                .moveByOffset(x, y).perform();
        } else {
            DriverManager.getPlaywrightPage().mouse().move(x, y);
        }
        UnifiedLogger.info("Moved mouse to coordinates: x=" + x + ", y=" + y);
    }

    // ------------------- Element State Checks -------------------

    public static boolean isElementDisabled(String locator) {
        return !isElementEnabled(locator);
    }

    public static boolean isElementHidden(String locator) {
        return !isElementVisible(locator);
    }

    public static boolean isElementEditable(String locator) {
        boolean editable;
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            WebElement element = DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator));
            editable = element.isEnabled() && !"true".equals(element.getAttribute("readonly"));
        } else {
            editable = DriverManager.getPlaywrightPage().isEditable(locator);
        }
        UnifiedLogger.info("Element editable: " + editable);
        return editable;
    }

    public static boolean isElementFocused(String locator) {
        boolean focused;
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            WebElement element = DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator));
            WebElement activeElement = DriverManager.getSeleniumDriver().switchTo().activeElement();
            focused = element.equals(activeElement);
        } else {
            focused = DriverManager.getPlaywrightPage().locator(locator)
                .evaluate("el => el === document.activeElement").toString().equals("true");
        }
        UnifiedLogger.info("Element focused: " + focused);
        return focused;
    }

    // ------------------- Dropdown Advanced Operations -------------------

    public static java.util.List<String> getAllDropdownOptions(String locator) {
        java.util.List<String> options = new java.util.ArrayList<>();
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(
                DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator)));
            options = select.getOptions().stream().map(WebElement::getText).collect(java.util.stream.Collectors.toList());
        } else {
            int count = DriverManager.getPlaywrightPage().locator(locator + " option").count();
            for (int i = 0; i < count; i++) {
                options.add(DriverManager.getPlaywrightPage().locator(locator + " option").nth(i).textContent());
            }
        }
        UnifiedLogger.info("Got all dropdown options: " + options.size());
        return options;
    }

    public static int getDropdownOptionsCount(String locator) {
        return getAllDropdownOptions(locator).size();
    }

    public static void deselectAllDropdownOptions(String locator) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(
                DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator)));
            select.deselectAll();
        }
        UnifiedLogger.info("Deselected all dropdown options: " + locator);
    }

    // ------------------- File Operations -------------------

    public static void uploadMultipleFiles(String locator, String... filePaths) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            String allPaths = String.join("\n", filePaths);
            DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator)).sendKeys(allPaths);
        } else {
            java.nio.file.Path[] paths = java.util.Arrays.stream(filePaths)
                .map(java.nio.file.Paths::get).toArray(java.nio.file.Path[]::new);
            DriverManager.getPlaywrightPage().setInputFiles(locator, paths);
        }
        UnifiedLogger.info("Uploaded multiple files: " + filePaths.length);
    }

    // ------------------- Wait for Conditions -------------------

    public static void waitForElementToBeStale(String locator, int seconds) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            WebElement element = DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator));
            new org.openqa.selenium.support.ui.WebDriverWait(DriverManager.getSeleniumDriver(), java.time.Duration.ofSeconds(seconds))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.stalenessOf(element));
        }
        UnifiedLogger.info("Waited for element to be stale: " + locator);
    }

    public static void waitForElementAttributeToBe(String locator, String attribute, String value, int seconds) {
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            new org.openqa.selenium.support.ui.WebDriverWait(DriverManager.getSeleniumDriver(), java.time.Duration.ofSeconds(seconds))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.attributeToBe(By.cssSelector(locator), attribute, value));
        } else {
            long end = System.currentTimeMillis() + seconds * 1000;
            while (System.currentTimeMillis() < end) {
                String attrValue = DriverManager.getPlaywrightPage().getAttribute(locator, attribute);
                if (value.equals(attrValue)) break;
                try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            }
        }
        UnifiedLogger.info("Waited for attribute '" + attribute + "' to be '" + value + "' in: " + locator);
    }

    // ------------------- Utility Methods -------------------

    public static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
            UnifiedLogger.info("Slept for " + milliseconds + " ms");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static String getElementTagName(String locator) {
        String tagName;
        if ("selenium".equalsIgnoreCase(getFrameworkType())) {
            tagName = DriverManager.getSeleniumDriver().findElement(By.cssSelector(locator)).getTagName();
        } else {
            tagName = DriverManager.getPlaywrightPage().locator(locator).evaluate("el => el.tagName").toString().toLowerCase();
        }
        UnifiedLogger.info("Got tag name: " + tagName);
        return tagName;
    }



}
