package com.automation.core.reporting;

import com.automation.core.config.ConfigManager;
import com.automation.core.driver.DriverManager;
import com.automation.core.logging.LogManager;
import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {
    private static final String SCREENSHOT_DIR = "test-output/screenshots/";

    public static String captureScreenshot(String testName) {
        ConfigManager config = ConfigManager.getInstance();
        String frameworkType = config.getFrameworkType();
        
        File screenshotDir = new File(SCREENSHOT_DIR);
        if (!screenshotDir.exists()) {
            screenshotDir.mkdirs();
        }

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = testName.replaceAll("[^a-zA-Z0-9]", "_") + "_" + timestamp + ".png";
        String filePath = SCREENSHOT_DIR + fileName;

        try {
            byte[] screenshot;
            if ("selenium".equalsIgnoreCase(frameworkType)) {
                screenshot = ((TakesScreenshot) DriverManager.getSeleniumDriver()).getScreenshotAs(OutputType.BYTES);
            } else {
                screenshot = DriverManager.getPlaywrightPage().screenshot();
            }

            Files.write(Paths.get(filePath), screenshot);
            Allure.addAttachment("Screenshot", new ByteArrayInputStream(screenshot));
            CustomReporter.attachScreenshot(filePath);
            LogManager.info("Screenshot captured: " + filePath);
            return filePath;
        } catch (Exception e) {
            LogManager.error("Failed to capture screenshot", e);
            return null;
        }
    }
}
