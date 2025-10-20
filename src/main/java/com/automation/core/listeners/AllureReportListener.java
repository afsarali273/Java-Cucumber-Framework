package com.automation.core.listeners;

import com.automation.core.logging.LogManager;
import org.testng.IExecutionListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class AllureReportListener implements IExecutionListener {

    @Override
    public void onExecutionStart() {
        LogManager.info("Test execution started");
    }

    @Override
    public void onExecutionFinish() {
        LogManager.info("Test execution finished - Generating Allure report");
        generateAllureReport();
    }

    private void generateAllureReport() {
        try {
            ProcessBuilder pb = new ProcessBuilder("mvn", "allure:report");
            pb.redirectErrorStream(true);
            Process process = pb.start();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                LogManager.info(line);
            }
            
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                LogManager.info("Allure report generated successfully at: target/site/allure-maven-plugin");
                LogManager.info("To view report run: mvn allure:serve");
            } else {
                LogManager.error("Allure report generation failed with exit code: " + exitCode);
            }
        } catch (Exception e) {
            LogManager.error("Error generating Allure report: " + e.getMessage());
        }
    }
}
