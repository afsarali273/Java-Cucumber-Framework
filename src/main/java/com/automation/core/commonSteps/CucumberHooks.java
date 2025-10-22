package com.automation.core.commonSteps;

import com.automation.core.api.APIClient;
import com.automation.core.config.ConfigManager;
import com.automation.core.driver.DriverManager;
import com.automation.core.logging.ColoredLogger;
import com.automation.core.logging.UnifiedLogger;
import com.automation.core.reporting.CustomReporter;
import com.automation.core.reporting.ExtentReporter;
import com.automation.core.reporting.ScreenshotUtil;
import io.cucumber.java.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class CucumberHooks {

    @BeforeAll
    public static void beforeAll() {
        ExtentReporter.initReports();
        ColoredLogger.header("TEST SUITE STARTED");
        UnifiedLogger.info("Initializing framework configuration...");
        ConfigManager.getInstance(); // Initialize configuration
    }

    @Before
    public void beforeScenario(Scenario scenario) {
        String scenarioName = scenario.getName();
        Set<String> tags = new HashSet<>(scenario.getSourceTagNames());

        ExtentReporter.startTest(scenarioName);
        CustomReporter.startTest(scenarioName);

        // Assign category based on tags
        assignCategory(tags);

        UnifiedLogger.info("Starting Scenario: " + scenarioName);

        // Initialize drivers or clients based on tag
        if (tags.contains("@UI") || tags.contains("@Mobile") || tags.contains("@Desktop")) {
            DriverManager.initializeDriver();
        } else if (tags.contains("@API")) {
            APIClient.initializeAPIClient();
        }
    }

    @After
    public void afterScenario(Scenario scenario) throws IOException {
        String scenarioName = scenario.getName();
        boolean isFailed = scenario.isFailed();

        try {
            if (isFailed) {
                // Get failure details
                String failureDetails = getFailureDetails(scenario);
                if (failureDetails != null && !failureDetails.isEmpty()) {
                    UnifiedLogger.fail(scenarioName + " - Failed\n" + failureDetails);
                    ExtentReporter.logFail("Failure Reason:\n" + failureDetails);
                    CustomReporter.logFail("Failure Reason: " + failureDetails);
                } else {
                    UnifiedLogger.fail(scenarioName + " - Failed");
                }
                handleFailureScreenshot(scenario);
            } else {
                UnifiedLogger.pass(scenarioName + " - Passed");
            }
        } catch (Exception e) {
            UnifiedLogger.fail("Error during afterScenario: " + e.getMessage());
        } finally {
            // Cleanup and finalize reports
            ExtentReporter.endTest();
            CustomReporter.endTest();
            DriverManager.quitDriver();
            APIClient.clearRequestSpec();
            
            // Clear scenario context to prevent memory leaks
            com.automation.core.context.ScenarioContext.reset();
            
            UnifiedLogger.info("Completed Scenario: " + scenarioName + " | Status: " + scenario.getStatus());
        }
    }

    private String getFailureDetails(Scenario scenario) {
        try {
            java.lang.reflect.Field delegateField = scenario.getClass().getDeclaredField("delegate");
            delegateField.setAccessible(true);
            Object delegate = delegateField.get(scenario);
            
            java.lang.reflect.Method getErrorMethod = delegate.getClass().getMethod("getError");
            Throwable error = (Throwable) getErrorMethod.invoke(delegate);
            
            if (error != null) {
                StringBuilder details = new StringBuilder();
                details.append(error.getClass().getSimpleName()).append(": ").append(error.getMessage());
                
                StackTraceElement[] stackTrace = error.getStackTrace();
                if (stackTrace != null && stackTrace.length > 0) {
                    details.append("\n");
                    int limit = Math.min(5, stackTrace.length);
                    for (int i = 0; i < limit; i++) {
                        details.append("  at ").append(stackTrace[i].toString()).append("\n");
                    }
                }
                return details.toString();
            }
        } catch (Exception e) {
            // Reflection failed, return null
        }
        return null;
    }

    @AfterAll
    public static void afterAll() {
        try {
            if (ConfigManager.isPlaywright()) {
                DriverManager.closePlaywright();
            }
        } catch (Exception e) {
            UnifiedLogger.error("Error closing Playwright: ", e);
        } finally {
            ExtentReporter.flushReports();
            CustomReporter.generateReport();
            generateAllureReport();
            ColoredLogger.header("TEST SUITE COMPLETED");
        }
    }

    // ---------------------- Helper Methods ----------------------

    private static void assignCategory(Set<String> tags) {
        if (tags.contains("@UI")) ExtentReporter.assignCategory("UI");
        if (tags.contains("@API")) ExtentReporter.assignCategory("API");
        if (tags.contains("@Mobile")) ExtentReporter.assignCategory("Mobile");
        if (tags.contains("@Desktop")) ExtentReporter.assignCategory("Desktop");
    }

    private static void handleFailureScreenshot(Scenario scenario) throws IOException {
        ConfigManager config = ConfigManager.getInstance();
        Set<String> tags = new HashSet<>(scenario.getSourceTagNames());

        if (config.getBooleanProperty("screenshot.on.failure", true) && !tags.contains("@API")) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(scenario.getName());
            if (screenshotPath != null) {
                byte[] screenshot = Files.readAllBytes(Paths.get(screenshotPath));

                // Attach to Cucumber
                scenario.attach(screenshot, "image/png", "Screenshot");

                // Attach to Allure
                io.qameta.allure.Allure.addAttachment(
                        "Screenshot on Failure", "image/png",
                        new ByteArrayInputStream(screenshot), "png"
                );

                // Attach to ExtentReport
                ExtentReporter.attachScreenshot(screenshotPath);
            }
        }
    }

    private static void generateAllureReport() {
        try {
            UnifiedLogger.info("Generating Allure report...");

            ProcessBuilder pb = new ProcessBuilder("mvn", "allure:report");
            pb.directory(new File(System.getProperty("user.dir")));
            pb.redirectErrorStream(true);

            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.contains("BUILD SUCCESS") || line.contains("Report successfully generated")) {
                    UnifiedLogger.info(line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                UnifiedLogger.info("Allure report generated at: test-output/allure");
                UnifiedLogger.info("To view the report, run: mvn allure:serve");
            }
        } catch (Exception e) {
            UnifiedLogger.warn("Allure report generation skipped: " + e.getMessage());
        }
    }
}
