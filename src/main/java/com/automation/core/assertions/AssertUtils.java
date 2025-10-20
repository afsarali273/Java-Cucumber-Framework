package com.automation.core.assertions;

import com.automation.core.config.ConfigManager;
import com.automation.core.driver.DriverManager;
import com.automation.core.logging.LogManager;
import com.automation.core.reporting.CustomReporter;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.io.ByteArrayInputStream;

public class AssertUtils {
    private static final ThreadLocal<SoftAssert> softAssert = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> isSoftAssert = ThreadLocal.withInitial(() -> false);

    public static void enableSoftAssert() {
        softAssert.set(new SoftAssert());
        isSoftAssert.set(true);
        LogManager.info("Soft assertions enabled");
    }

    public static void assertAll() {
        if (isSoftAssert.get() && softAssert.get() != null) {
            try {
                softAssert.get().assertAll();
                LogManager.info("All soft assertions passed");
            } finally {
                softAssert.remove();
                isSoftAssert.set(false);
            }
        }
    }

    public static void assertEquals(Object actual, Object expected, String message) {
        try {
            if (isSoftAssert.get()) {
                softAssert.get().assertEquals(actual, expected, message);
            } else {
                Assert.assertEquals(actual, expected, message);
            }
            String passMsg = message + " | Expected: " + expected + ", Actual: " + actual;
            LogManager.info("PASS: " + passMsg);
            CustomReporter.logPass(passMsg);
            Allure.step(passMsg);
        } catch (AssertionError e) {
            String failMsg = message + " | Expected: " + expected + ", Actual: " + actual;
            LogManager.error("FAIL: " + failMsg);
            CustomReporter.logFail(failMsg);
            Allure.step(failMsg);
            attachScreenshotOnFailure();
            if (!isSoftAssert.get()) throw e;
        }
    }

    public static void assertNotEquals(Object actual, Object expected, String message) {
        try {
            if (isSoftAssert.get()) {
                softAssert.get().assertNotEquals(actual, expected, message);
            } else {
                Assert.assertNotEquals(actual, expected, message);
            }
            String passMsg = message + " | Values are not equal";
            LogManager.info("PASS: " + passMsg);
            CustomReporter.logPass(passMsg);
            Allure.step(passMsg);
        } catch (AssertionError e) {
            String failMsg = message + " | Both values are: " + actual;
            LogManager.error("FAIL: " + failMsg);
            CustomReporter.logFail(failMsg);
            Allure.step(failMsg);
            attachScreenshotOnFailure();
            if (!isSoftAssert.get()) throw e;
        }
    }

    public static void assertTrue(boolean condition, String message) {
        try {
            if (isSoftAssert.get()) {
                softAssert.get().assertTrue(condition, message);
            } else {
                Assert.assertTrue(condition, message);
            }
            LogManager.info("PASS: " + message);
            CustomReporter.logPass(message);
            Allure.step(message);
        } catch (AssertionError e) {
            LogManager.error("FAIL: " + message);
            CustomReporter.logFail(message);
            Allure.step(message);
            attachScreenshotOnFailure();
            if (!isSoftAssert.get()) throw e;
        }
    }

    public static void assertFalse(boolean condition, String message) {
        try {
            if (isSoftAssert.get()) {
                softAssert.get().assertFalse(condition, message);
            } else {
                Assert.assertFalse(condition, message);
            }
            LogManager.info("PASS: " + message);
            CustomReporter.logPass(message);
            Allure.step(message);
        } catch (AssertionError e) {
            LogManager.error("FAIL: " + message);
            CustomReporter.logFail(message);
            Allure.step(message);
            attachScreenshotOnFailure();
            if (!isSoftAssert.get()) throw e;
        }
    }

    public static void assertNull(Object object, String message) {
        try {
            if (isSoftAssert.get()) {
                softAssert.get().assertNull(object, message);
            } else {
                Assert.assertNull(object, message);
            }
            LogManager.info("PASS: " + message);
            CustomReporter.logPass(message);
            Allure.step(message);
        } catch (AssertionError e) {
            LogManager.error("FAIL: " + message);
            CustomReporter.logFail(message);
            Allure.step(message);
            attachScreenshotOnFailure();
            if (!isSoftAssert.get()) throw e;
        }
    }

    public static void assertNotNull(Object object, String message) {
        try {
            if (isSoftAssert.get()) {
                softAssert.get().assertNotNull(object, message);
            } else {
                Assert.assertNotNull(object, message);
            }
            LogManager.info("PASS: " + message);
            CustomReporter.logPass(message);
            Allure.step(message);
        } catch (AssertionError e) {
            LogManager.error("FAIL: " + message);
            CustomReporter.logFail(message);
            Allure.step(message);
            attachScreenshotOnFailure();
            if (!isSoftAssert.get()) throw e;
        }
    }

    public static void assertContains(String actual, String expected, String message) {
        try {
            boolean contains = actual.contains(expected);
            if (isSoftAssert.get()) {
                softAssert.get().assertTrue(contains, message);
            } else {
                Assert.assertTrue(contains, message);
            }
            String passMsg = message + " | '" + actual + "' contains '" + expected + "'";
            LogManager.info("PASS: " + passMsg);
            CustomReporter.logPass(passMsg);
            Allure.step(passMsg);
        } catch (AssertionError e) {
            String failMsg = message + " | '" + actual + "' does not contain '" + expected + "'";
            LogManager.error("FAIL: " + failMsg);
            CustomReporter.logFail(failMsg);
            Allure.step(failMsg);
            attachScreenshotOnFailure();
            if (!isSoftAssert.get()) throw e;
        }
    }

    public static void assertGreaterThan(double actual, double expected, String message) {
        try {
            boolean isGreater = actual > expected;
            if (isSoftAssert.get()) {
                softAssert.get().assertTrue(isGreater, message);
            } else {
                Assert.assertTrue(isGreater, message);
            }
            String passMsg = message + " | " + actual + " > " + expected;
            LogManager.info("PASS: " + passMsg);
            CustomReporter.logPass(passMsg);
            Allure.step(passMsg);
        } catch (AssertionError e) {
            String failMsg = message + " | " + actual + " is not greater than " + expected;
            LogManager.error("FAIL: " + failMsg);
            CustomReporter.logFail(failMsg);
            Allure.step(failMsg);
            attachScreenshotOnFailure();
            if (!isSoftAssert.get()) throw e;
        }
    }

    public static void assertGreaterThan(long actual, long expected, String message) {
        assertGreaterThan((double) actual, (double) expected, message);
    }

    public static void assertGreaterThan(int actual, int expected, String message) {
        assertGreaterThan((double) actual, (double) expected, message);
    }

    public static void assertLessThan(double actual, double expected, String message) {
        try {
            boolean isLess = actual < expected;
            if (isSoftAssert.get()) {
                softAssert.get().assertTrue(isLess, message);
            } else {
                Assert.assertTrue(isLess, message);
            }
            String passMsg = message + " | " + actual + " < " + expected;
            LogManager.info("PASS: " + passMsg);
            CustomReporter.logPass(passMsg);
            Allure.step(passMsg);
        } catch (AssertionError e) {
            String failMsg = message + " | " + actual + " is not less than " + expected;
            LogManager.error("FAIL: " + failMsg);
            CustomReporter.logFail(failMsg);
            Allure.step(failMsg);
            attachScreenshotOnFailure();
            if (!isSoftAssert.get()) throw e;
        }
    }

    public static void assertLessThan(long actual, long expected, String message) {
        assertLessThan((double) actual, (double) expected, message);
    }

    public static void assertLessThan(int actual, int expected, String message) {
        assertLessThan((double) actual, (double) expected, message);
    }

    public static void assertGreaterThanOrEqual(double actual, double expected, String message) {
        try {
            boolean isGreaterOrEqual = actual >= expected;
            if (isSoftAssert.get()) {
                softAssert.get().assertTrue(isGreaterOrEqual, message);
            } else {
                Assert.assertTrue(isGreaterOrEqual, message);
            }
            String passMsg = message + " | " + actual + " >= " + expected;
            LogManager.info("PASS: " + passMsg);
            CustomReporter.logPass(passMsg);
            Allure.step(passMsg);
        } catch (AssertionError e) {
            String failMsg = message + " | " + actual + " is not greater than or equal to " + expected;
            LogManager.error("FAIL: " + failMsg);
            CustomReporter.logFail(failMsg);
            Allure.step(failMsg);
            attachScreenshotOnFailure();
            if (!isSoftAssert.get()) throw e;
        }
    }

    public static void assertGreaterThanOrEqual(long actual, long expected, String message) {
        assertGreaterThanOrEqual((double) actual, (double) expected, message);
    }

    public static void assertGreaterThanOrEqual(int actual, int expected, String message) {
        assertGreaterThanOrEqual((double) actual, (double) expected, message);
    }

    public static void assertLessThanOrEqual(double actual, double expected, String message) {
        try {
            boolean isLessOrEqual = actual <= expected;
            if (isSoftAssert.get()) {
                softAssert.get().assertTrue(isLessOrEqual, message);
            } else {
                Assert.assertTrue(isLessOrEqual, message);
            }
            String passMsg = message + " | " + actual + " <= " + expected;
            LogManager.info("PASS: " + passMsg);
            CustomReporter.logPass(passMsg);
            Allure.step(passMsg);
        } catch (AssertionError e) {
            String failMsg = message + " | " + actual + " is not less than or equal to " + expected;
            LogManager.error("FAIL: " + failMsg);
            CustomReporter.logFail(failMsg);
            Allure.step(failMsg);
            attachScreenshotOnFailure();
            if (!isSoftAssert.get()) throw e;
        }
    }

    public static void assertLessThanOrEqual(long actual, long expected, String message) {
        assertLessThanOrEqual((double) actual, (double) expected, message);
    }

    public static void assertLessThanOrEqual(int actual, int expected, String message) {
        assertLessThanOrEqual((double) actual, (double) expected, message);
    }

    private static void attachScreenshotOnFailure() {
        try {
            if (ConfigManager.isSelenium() || ConfigManager.isPlaywright()) {
                byte[] screenshot;
                if (ConfigManager.isSelenium()) {
                    screenshot = ((TakesScreenshot) DriverManager.getSeleniumDriver()).getScreenshotAs(OutputType.BYTES);
                } else {
                    screenshot = DriverManager.getPlaywrightPage().screenshot();
                }
                Allure.addAttachment("Screenshot", "image/png", new ByteArrayInputStream(screenshot), "png");
            }
        } catch (Exception e) {
            LogManager.warn("Failed to attach screenshot: " + e.getMessage());
        }
    }
}
