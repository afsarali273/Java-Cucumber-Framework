package com.automation.core.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.automation.core.config.ConfigManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Thread-safe ExtentReports manager for test reporting.
 */
public class ExtentReporter {
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static String reportPath;

    /**
     * Initializes ExtentReports with configuration.
     */
    public static synchronized void initReports() {
        if (extent == null) {
            String reportDir = ConfigManager.getInstance().getProperty("report.path", "test-output/reports");
            new File(reportDir).mkdirs();
            
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            reportPath = reportDir + "/ExtentReport_" + timestamp + ".html";
            
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setTheme(Theme.DARK);
            sparkReporter.config().setDocumentTitle("Automation Test Report");
            sparkReporter.config().setReportName("Sands Automation Framework");
            sparkReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");
            
            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("Framework", "Sands Automation Framework");
            extent.setSystemInfo("Environment", ConfigManager.getInstance().getProperty("environment", "QA"));
            extent.setSystemInfo("Browser", ConfigManager.getInstance().getProperty("browser", "chrome"));
        }
    }

    /**
     * Creates a new test in the report.
     */
    public static void startTest(String testName) {
        if (extent == null) initReports();
        ExtentTest extentTest = extent.createTest(testName);
        test.set(extentTest);
    }

    /**
     * Logs info message.
     */
    public static void logInfo(String message) {
        if (test.get() != null) {
            test.get().log(Status.INFO, message);
        }
    }

    /**
     * Logs pass message.
     */
    public static void logPass(String message) {
        if (test.get() != null) {
            test.get().log(Status.PASS, message);
        }
    }

    /**
     * Logs fail message.
     */
    public static void logFail(String message) {
        if (test.get() != null) {
            test.get().log(Status.FAIL, message);
        }
    }

    /**
     * Logs warning message.
     */
    public static void logWarn(String message) {
        if (test.get() != null) {
            test.get().log(Status.WARNING, message);
        }
    }

    /**
     * Logs skip message.
     */
    public static void logSkip(String message) {
        if (test.get() != null) {
            test.get().log(Status.SKIP, message);
        }
    }

    /**
     * Attaches screenshot to the report.
     */
    public static void attachScreenshot(String screenshotPath) {
        if (test.get() != null) {
            try {
                // Use relative path from report directory
                // Report is in test-output/reports/, screenshots are in test-output/screenshots/
                String relativePath = "../screenshots/" + new java.io.File(screenshotPath).getName();
                test.get().addScreenCaptureFromPath(relativePath);
            } catch (Exception e) {
                logWarn("Failed to attach screenshot: " + e.getMessage());
            }
        }
    }

    /**
     * Assigns category to the test.
     */
    public static void assignCategory(String... categories) {
        if (test.get() != null) {
            test.get().assignCategory(categories);
        }
    }

    /**
     * Assigns author to the test.
     */
    public static void assignAuthor(String... authors) {
        if (test.get() != null) {
            test.get().assignAuthor(authors);
        }
    }

    /**
     * Ends the current test.
     */
    public static void endTest() {
        test.remove();
    }

    /**
     * Flushes and generates the report.
     */
    public static synchronized void flushReports() {
        if (extent != null) {
            extent.flush();
            System.out.println("ExtentReport generated at: " + reportPath);
        }
    }

    /**
     * Gets the current test instance.
     */
    public static ExtentTest getTest() {
        return test.get();
    }
}
