package com.automation.core.hooks;

import com.automation.core.api.APIClient;
import com.automation.core.config.ConfigManager;
import com.automation.core.driver.DriverManager;
import com.automation.core.logging.LogManager;
import com.automation.core.reporting.CustomReporter;
import com.automation.core.reporting.ScreenshotUtil;
import io.cucumber.java.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CucumberHooks {

    @BeforeAll
    public static void beforeAll() {
        LogManager.info("=== Test Suite Started ===");
        ConfigManager.getInstance();
    }

    @Before
    public void beforeScenario(Scenario scenario) {
        LogManager.info("Starting Scenario: " + scenario.getName());
        CustomReporter.startTest(scenario.getName());
        
        if (scenario.getSourceTagNames().contains("@UI")) {
            DriverManager.initializeDriver();
        }
        
        if (scenario.getSourceTagNames().contains("@API")) {
            APIClient.initializeAPIClient();
        }
    }

    @After
    public void afterScenario(Scenario scenario) throws IOException {
        try {
            if (scenario.isFailed()) {
                ConfigManager config = ConfigManager.getInstance();
                if (config.getBooleanProperty("screenshot.on.failure", true)) {
                    String screenshotPath = ScreenshotUtil.captureScreenshot(scenario.getName());
                    if (screenshotPath != null) {
                        byte[] screenshot = Files.readAllBytes(Paths.get(screenshotPath));
                        // Attach to Cucumber report
                        scenario.attach(screenshot, "image/png", "Screenshot");
                        // Attach to Allure report
                        io.qameta.allure.Allure.addAttachment("Screenshot on Failure", "image/png", 
                            new java.io.ByteArrayInputStream(screenshot), "png");
                    }
                }
            }
        } catch (Exception e) {
            LogManager.error("Error capturing screenshot: " + e.getMessage());
        } finally {
            CustomReporter.endTest();
            DriverManager.quitDriver();
            APIClient.clearRequestSpec();
            LogManager.info("Completed Scenario: " + scenario.getName() + " | Status: " + scenario.getStatus());
        }
    }

    @AfterAll
    public static void afterAll() {
        try {
            if (ConfigManager.isPlaywright()) {
                DriverManager.closePlaywright();
            }
        } catch (Exception e) {
            LogManager.error("Error closing Playwright: " + e.getMessage());
        } finally {
            CustomReporter.generateReport();
            generateAllureReport();
            LogManager.info("=== Test Suite Completed ===");
        }
    }

    private static void generateAllureReport() {
        try {
            LogManager.info("Generating Allure report...");
            ProcessBuilder pb = new ProcessBuilder("mvn", "allure:report");
            pb.directory(new java.io.File(System.getProperty("user.dir")));
            pb.redirectErrorStream(true);
            Process process = pb.start();
            
            java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("BUILD SUCCESS") || line.contains("Report successfully generated")) {
                    LogManager.info(line);
                }
            }
            
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                LogManager.info("Allure report generated at: test-output/allure");
                LogManager.info("To view report run: mvn allure:serve");
            }
        } catch (Exception e) {
            LogManager.warn("Allure report generation skipped: " + e.getMessage());
        }
    }
}
