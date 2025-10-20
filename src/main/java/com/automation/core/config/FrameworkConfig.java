package com.automation.core.config;

public class FrameworkConfig {
    private final String frameworkType;
    private final String browser;
    private final boolean headless;
    private final String environment;
    private final int waitTimeout;
    private final int maxRetryCount;
    private final int apiTimeout;

    public FrameworkConfig(String frameworkType, String browser, boolean headless, 
                          String environment, int waitTimeout, int maxRetryCount, int apiTimeout) {
        this.frameworkType = frameworkType;
        this.browser = browser;
        this.headless = headless;
        this.environment = environment;
        this.waitTimeout = waitTimeout;
        this.maxRetryCount = maxRetryCount;
        this.apiTimeout = apiTimeout;
    }

    public String getFrameworkType() {
        return frameworkType;
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

    public int getWaitTimeout() {
        return waitTimeout;
    }

    public int getMaxRetryCount() {
        return maxRetryCount;
    }

    public int getApiTimeout() {
        return apiTimeout;
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

    @Override
    public String toString() {
        return "FrameworkConfig{" +
                "frameworkType='" + frameworkType + '\'' +
                ", browser='" + browser + '\'' +
                ", headless=" + headless +
                ", environment='" + environment + '\'' +
                ", waitTimeout=" + waitTimeout +
                ", maxRetryCount=" + maxRetryCount +
                ", apiTimeout=" + apiTimeout +
                '}';
    }
}
