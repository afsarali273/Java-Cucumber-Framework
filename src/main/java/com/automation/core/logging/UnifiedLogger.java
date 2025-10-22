package com.automation.core.logging;

import com.automation.core.reporting.CustomReporter;
import com.automation.core.reporting.ExtentReporter;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;

/**
 * Unified logger that logs to LogManager, CustomReporter, Allure, and ExtentReports simultaneously.
 * Thread-safe and accessible across the entire project with colorful console output.
 */
public class UnifiedLogger {

    /**
     * Logs INFO level message to all reporting systems.
     */
    public static void info(String message) {
        ColoredLogger.info(message);
        LogManager.info(message);
        CustomReporter.logInfo(message);
        ExtentReporter.logInfo(message);
        Allure.step(message);
    }

    /**
     * Logs PASS message to all reporting systems.
     */
    public static void pass(String message) {
        ColoredLogger.pass(message);
        LogManager.info("[PASS] " + message);
        CustomReporter.logPass(message);
        ExtentReporter.logPass(message);
        Allure.step("✓ " + message, Status.PASSED);
    }

    /**
     * Logs FAIL message to all reporting systems.
     */
    public static void fail(String message) {
        ColoredLogger.fail(message);
        LogManager.error("[FAIL] " + message);
        CustomReporter.logFail(message);
        ExtentReporter.logFail(message);
        Allure.step("✗ " + message, Status.FAILED);
    }

    /**
     * Logs DEBUG level message.
     */
    public static void debug(String message) {
        LogManager.debug(message);
    }

    /**
     * Logs WARN level message.
     */
    public static void warn(String message) {
        ColoredLogger.warn(message);
        LogManager.warn(message);
        CustomReporter.logInfo("[WARN] " + message);
        ExtentReporter.logWarn(message);
        Allure.step("⚠ " + message, Status.BROKEN);
    }

    /**
     * Logs ERROR with exception.
     */
    public static void error(String message, Throwable throwable) {
        ColoredLogger.error(message + " | " + throwable.getMessage());
        LogManager.error(message, throwable);
        CustomReporter.logFail(message + " | Exception: " + throwable.getMessage());
        ExtentReporter.logFail(message + " | Exception: " + throwable.getMessage());
        Allure.step("✗ " + message + " | " + throwable.getMessage(), Status.FAILED);
    }

    /**
     * Logs action step (for reusable methods).
     */
    public static void action(String action, String details) {
        String message = action + ": " + details;
        ColoredLogger.action(message);
        LogManager.info(message);
        CustomReporter.logInfo(message);
        ExtentReporter.logInfo(message);
        Allure.step(message);
    }

    /**
     * Logs action step with element locator.
     */
    public static void action(String action, Object locator) {
        action(action, locator.toString());
    }
}
