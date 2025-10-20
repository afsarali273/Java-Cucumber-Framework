package com.automation.reusables;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import java.util.Arrays;

/**
 * Reusable methods for mobile automation (Android/iOS) using Appium.
 */
public class MobileReusable {
    protected AppiumDriver driver;

    /**
     * Launches the mobile app with given capabilities and server URL.
     */
    public void launchApp(URL appiumServerUrl, DesiredCapabilities capabilities, boolean isAndroid) {
        if (isAndroid) {
            driver = new AndroidDriver(appiumServerUrl, capabilities);
        } else {
            driver = new IOSDriver(appiumServerUrl, capabilities);
        }
    }

    /**
     * Closes the app and quits the driver.
     */
    public void closeApp() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Finds a mobile element by locator.
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
    }

    /**
     * Types text into an element.
     */
    public void type(By locator, String text) {
        WebElement el = findElement(locator);
        el.clear();
        el.sendKeys(text);
    }

    /**
     * Gets text from an element.
     */
    public String getText(By locator) {
        return findElement(locator).getText();
    }

    /**
     * Takes a screenshot and returns the file.
     */
    public File takeScreenshot(String fileName) {
        File srcFile = driver.getScreenshotAs(OutputType.FILE);
        File destFile = new File(fileName);
        srcFile.renameTo(destFile);
        return destFile;
    }

    /**
     * Swipes from one point to another using W3C Actions API.
     */
    public void swipe(int startX, int startY, int endX, int endY, int durationMs) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);
        swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(durationMs), PointerInput.Origin.viewport(), endX, endY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Arrays.asList(swipe));
    }

    /**
     * Performs a tap gesture at coordinates using W3C Actions API.
     */
    public void tap(int x, int y) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1);
        tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Arrays.asList(tap));
    }

    /**
     * Performs a long press gesture at coordinates using W3C Actions API.
     */
    public void longPress(int x, int y, int durationMs) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence longPress = new Sequence(finger, 1);
        longPress.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
        longPress.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        longPress.addAction(new org.openqa.selenium.interactions.Pause(finger, Duration.ofMillis(durationMs)));
        longPress.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Arrays.asList(longPress));
    }

    /**
     * Drag and drop from one element to another using W3C Actions API.
     */
    public void dragAndDrop(By source, By target) {
        WebElement src = findElement(source);
        WebElement tgt = findElement(target);
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence dragDrop = new Sequence(finger, 1);
        dragDrop.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), src.getLocation().getX(), src.getLocation().getY()));
        dragDrop.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        dragDrop.addAction(finger.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), tgt.getLocation().getX(), tgt.getLocation().getY()));
        dragDrop.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Arrays.asList(dragDrop));
    }

    // ================= ANDROID SPECIFIC =================
    /** Opens Android notifications. */
    public void openNotifications() {
        if (driver instanceof AndroidDriver) {
            ((AndroidDriver) driver).openNotifications();
        }
    }

    /** Presses the Android back button. */
    public void pressBack() {
        if (driver instanceof AndroidDriver) {
            ((AndroidDriver) driver).pressKey(new io.appium.java_client.android.nativekey.KeyEvent(io.appium.java_client.android.nativekey.AndroidKey.BACK));
        }
    }

    /** Presses the Android home button. */
    public void pressHome() {
        if (driver instanceof AndroidDriver) {
            ((AndroidDriver) driver).pressKey(new io.appium.java_client.android.nativekey.KeyEvent(io.appium.java_client.android.nativekey.AndroidKey.HOME));
        }
    }

    /** Sets clipboard text on Android. */
    public void setClipboardText(String text) {
        if (driver instanceof AndroidDriver) {
            ((AndroidDriver) driver).setClipboardText(text);
        }
    }

    /** Gets clipboard text on Android. */
    public String getClipboardText() {
        if (driver instanceof AndroidDriver) {
            return ((AndroidDriver) driver).getClipboardText();
        }
        return null;
    }



    /** Unlocks the Android device. */
    public void unlockDevice() {
        if (driver instanceof AndroidDriver) {
            ((AndroidDriver) driver).unlockDevice();
        }
    }

    /** Checks if an app is installed (Android). */
    public boolean isAppInstalledAndroid(String packageName) {
        if (driver instanceof AndroidDriver) {
            return ((AndroidDriver) driver).isAppInstalled(packageName);
        }
        return false;
    }

    /** Backgrounds the app for given seconds (Android). */
    public void backgroundAppAndroid(int seconds) {
        if (driver instanceof AndroidDriver) {
            ((AndroidDriver) driver).runAppInBackground(Duration.ofSeconds(seconds));
        }
    }

    /** Gets the current Android activity. */
    public String getCurrentActivity() {
        if (driver instanceof AndroidDriver) {
            return ((AndroidDriver) driver).currentActivity();
        }
        return null;
    }

    // ================= IOS SPECIFIC =================
    /** Shakes the iOS device. */
    public void shakeDevice() {
        if (driver instanceof IOSDriver) {
            ((IOSDriver) driver).shake();
        }
    }

    /** Locks the iOS device. */
    public void lockDeviceIOS() {
        if (driver instanceof IOSDriver) {
            ((IOSDriver) driver).lockDevice();
        }
    }

    /** Unlocks the iOS device. */
    public void unlockDeviceIOS() {
        if (driver instanceof IOSDriver) {
            ((IOSDriver) driver).unlockDevice();
        }
    }

    /** Installs an app on iOS. */
    public void installAppIOS(String appPath) {
        if (driver instanceof IOSDriver) {
            ((IOSDriver) driver).installApp(appPath);
        }
    }

    /** Removes an app from iOS. */
    public void removeAppIOS(String bundleId) {
        if (driver instanceof IOSDriver) {
            ((IOSDriver) driver).removeApp(bundleId);
        }
    }

    /** Checks if an app is installed (iOS). */
    public boolean isAppInstalledIOS(String bundleId) {
        if (driver instanceof IOSDriver) {
            return ((IOSDriver) driver).isAppInstalled(bundleId);
        }
        return false;
    }

    /** Backgrounds the app for given seconds (iOS). */
    public void backgroundAppIOS(int seconds) {
        if (driver instanceof IOSDriver) {
            ((IOSDriver) driver).runAppInBackground(Duration.ofSeconds(seconds));
        }
    }

    /** Gets the device time (iOS). */
    public String getDeviceTimeIOS() {
        if (driver instanceof IOSDriver) {
            return ((IOSDriver) driver).getDeviceTime();
        }
        return null;
    }

}
