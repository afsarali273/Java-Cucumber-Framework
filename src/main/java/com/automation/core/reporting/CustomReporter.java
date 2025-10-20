package com.automation.core.reporting;

import com.automation.core.config.ConfigManager;
import com.automation.core.logging.LogManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CustomReporter {
    private static final Map<String, TestResult> testResults = new ConcurrentHashMap<>();
    private static final ThreadLocal<String> currentTest = new ThreadLocal<>();
    private static final ThreadLocal<Long> testStartTime = new ThreadLocal<>();

    public static void startTest(String testName) {
        currentTest.set(testName);
        testStartTime.set(System.currentTimeMillis());
        testResults.put(testName, new TestResult(testName));
        LogManager.info("Test started: " + testName);
    }

    public static void logPass(String message) {
        String testName = currentTest.get();
        if (testName != null && testResults.containsKey(testName)) {
            testResults.get(testName).addLog("PASS", message);
        }
    }

    public static void logFail(String message) {
        String testName = currentTest.get();
        if (testName != null && testResults.containsKey(testName)) {
            testResults.get(testName).addLog("FAIL", message);
            testResults.get(testName).setStatus("FAIL");
        }
    }

    public static void logInfo(String message) {
        String testName = currentTest.get();
        if (testName != null && testResults.containsKey(testName)) {
            testResults.get(testName).addLog("INFO", message);
        }
    }

    public static void attachScreenshot(String screenshotPath) {
        String testName = currentTest.get();
        if (testName != null && testResults.containsKey(testName)) {
            testResults.get(testName).addScreenshot(screenshotPath);
        }
    }

    public static void endTest() {
        String testName = currentTest.get();
        if (testName != null && testResults.containsKey(testName)) {
            long duration = System.currentTimeMillis() - testStartTime.get();
            testResults.get(testName).setDuration(duration);
            LogManager.info("Test ended: " + testName + " | Duration: " + duration + "ms");
        }
        currentTest.remove();
        testStartTime.remove();
    }

    public static void generateReport() {
        String reportPath = ConfigManager.getInstance().getProperty("report.path", "test-output/reports");
        File reportDir = new File(reportPath);
        if (!reportDir.exists()) {
            reportDir.mkdirs();
        }

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String reportFile = reportPath + "/CustomReport_" + timestamp + ".html";

        try (FileWriter writer = new FileWriter(reportFile)) {
            writer.write(generateHtmlReport());
            LogManager.info("Custom HTML Report generated: " + reportFile);
        } catch (IOException e) {
            LogManager.error("Failed to generate report", e);
        }
    }

    private static String generateHtmlReport() {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><meta charset='UTF-8'>");
        html.append("<title>Automation Test Report</title>");
        html.append("<style>");
        html.append("body{font-family:Arial,sans-serif;margin:20px;background:#f5f5f5}");
        html.append(".header{background:#2c3e50;color:white;padding:20px;text-align:center}");
        html.append(".summary{background:white;padding:20px;margin:20px 0;border-radius:5px}");
        html.append(".test{background:white;margin:10px 0;padding:15px;border-radius:5px;border-left:5px solid #3498db}");
        html.append(".test.pass{border-left-color:#27ae60}");
        html.append(".test.fail{border-left-color:#e74c3c}");
        html.append(".log{margin:10px 0;padding:5px;font-size:14px}");
        html.append(".pass{color:#27ae60}.fail{color:#e74c3c}.info{color:#3498db}");
        html.append("table{width:100%;border-collapse:collapse}");
        html.append("th,td{padding:10px;text-align:left;border-bottom:1px solid #ddd}");
        html.append("th{background:#34495e;color:white}");
        html.append("</style></head><body>");

        html.append("<div class='header'><h1>Automation Test Report</h1>");
        html.append("<p>Generated: ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append("</p></div>");

        int total = testResults.size();
        int passed = (int) testResults.values().stream().filter(t -> "PASS".equals(t.getStatus())).count();
        int failed = total - passed;

        html.append("<div class='summary'><h2>Summary</h2>");
        html.append("<table><tr><th>Total</th><th>Passed</th><th>Failed</th><th>Pass Rate</th></tr>");
        html.append("<tr><td>").append(total).append("</td>");
        html.append("<td style='color:#27ae60'>").append(passed).append("</td>");
        html.append("<td style='color:#e74c3c'>").append(failed).append("</td>");
        html.append("<td>").append(total > 0 ? String.format("%.2f%%", (passed * 100.0 / total)) : "0%").append("</td></tr></table></div>");

        html.append("<h2>Test Details</h2>");
        for (TestResult result : testResults.values()) {
            html.append("<div class='test ").append(result.getStatus().toLowerCase()).append("'>");
            html.append("<h3>").append(result.getTestName()).append(" - ").append(result.getStatus()).append("</h3>");
            html.append("<p><strong>Duration:</strong> ").append(result.getDuration()).append("ms</p>");

            for (String log : result.getLogs()) {
                html.append("<div class='log'>").append(log).append("</div>");
            }

            for (String screenshot : result.getScreenshots()) {
                html.append("<p><a href='").append(screenshot).append("' target='_blank'>View Screenshot</a></p>");
            }
            html.append("</div>");
        }

        html.append("</body></html>");
        return html.toString();
    }

    static class TestResult {
        private String testName;
        private String status = "PASS";
        private long duration;
        private List<String> logs = new ArrayList<>();
        private List<String> screenshots = new ArrayList<>();

        public TestResult(String testName) {
            this.testName = testName;
        }

        public void addLog(String level, String message) {
            logs.add("<span class='" + level.toLowerCase() + "'>[" + level + "]</span> " + message);
        }

        public void addScreenshot(String path) {
            screenshots.add(path);
        }

        public String getTestName() { return testName; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public long getDuration() { return duration; }
        public void setDuration(long duration) { this.duration = duration; }
        public List<String> getLogs() { return logs; }
        public List<String> getScreenshots() { return screenshots; }
    }
}
