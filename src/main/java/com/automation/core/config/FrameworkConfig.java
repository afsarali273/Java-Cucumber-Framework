package com.automation.core.config;

public class FrameworkConfig {
    private final String frameworkType;
    private final String executionType;
    private final String browser;
    private final boolean headless;
    private final String environment;
    private final int implicitWait;
    private final int explicitWait;
    private final int pageLoadTimeout;
    private final int maxRetryCount;
    private final int threadCount;
    private final boolean parallelExecution;
    private final boolean screenshotOnFailure;
    private final String reportPath;

    public FrameworkConfig(String frameworkType, String executionType, String browser, boolean headless,
                          String environment, int implicitWait, int explicitWait, int pageLoadTimeout,
                          int maxRetryCount, int threadCount, boolean parallelExecution,
                          boolean screenshotOnFailure, String reportPath) {
        this.frameworkType = frameworkType;
        this.executionType = executionType;
        this.browser = browser;
        this.headless = headless;
        this.environment = environment;
        this.implicitWait = implicitWait;
        this.explicitWait = explicitWait;
        this.pageLoadTimeout = pageLoadTimeout;
        this.maxRetryCount = maxRetryCount;
        this.threadCount = threadCount;
        this.parallelExecution = parallelExecution;
        this.screenshotOnFailure = screenshotOnFailure;
        this.reportPath = reportPath;
    }

    public String getFrameworkType() {
        return frameworkType;
    }

    public String getExecutionType() {
        return executionType;
    }

    public String getBrowser() {
        return browser;
    }

    public boolean isHeadless() {
        return headless;
    }

    public String getEnvironment() {
        return environment;
    }

    public int getImplicitWait() {
        return implicitWait;
    }

    public int getExplicitWait() {
        return explicitWait;
    }

    public int getPageLoadTimeout() {
        return pageLoadTimeout;
    }

    public int getMaxRetryCount() {
        return maxRetryCount;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public boolean isParallelExecution() {
        return parallelExecution;
    }

    public boolean isScreenshotOnFailure() {
        return screenshotOnFailure;
    }

    public String getReportPath() {
        return reportPath;
    }

    public boolean isSelenium() {
        return "selenium".equalsIgnoreCase(frameworkType);
    }

    public boolean isPlaywright() {
        return "playwright".equalsIgnoreCase(frameworkType);
    }

    public boolean isAPI() {
        return "api".equalsIgnoreCase(frameworkType);
    }

    public boolean isDesktop() {
        return "desktop".equalsIgnoreCase(frameworkType) || "windows".equalsIgnoreCase(frameworkType);
    }

    public boolean isMobile() {
        return "mobile".equalsIgnoreCase(frameworkType) || "appium".equalsIgnoreCase(frameworkType);
    }

    public boolean isCucumber() {
        return "cucumber".equalsIgnoreCase(executionType);
    }

    public boolean isTestNG() {
        return "testng".equalsIgnoreCase(executionType);
    }

    public boolean isModular() {
        return "modular".equalsIgnoreCase(executionType);
    }

    @Override
    public String toString() {
        return "FrameworkConfig{" +
                "frameworkType='" + frameworkType + '\'' +
                ", executionType='" + executionType + '\'' +
                ", browser='" + browser + '\'' +
                ", headless=" + headless +
                ", environment='" + environment + '\'' +
                ", implicitWait=" + implicitWait +
                ", explicitWait=" + explicitWait +
                ", pageLoadTimeout=" + pageLoadTimeout +
                ", maxRetryCount=" + maxRetryCount +
                ", threadCount=" + threadCount +
                ", parallelExecution=" + parallelExecution +
                ", screenshotOnFailure=" + screenshotOnFailure +
                ", reportPath='" + reportPath + '\'' +
                ", isSelenium=" + isSelenium() +
                ", isPlaywright=" + isPlaywright() +
                ", isAPI=" + isAPI() +
                ", isDesktop=" + isDesktop() +
                ", isMobile=" + isMobile() +
                ", isCucumber=" + isCucumber() +
                ", isTestNG=" + isTestNG() +
                ", isModular=" + isModular() +
                '}';
    }
}
