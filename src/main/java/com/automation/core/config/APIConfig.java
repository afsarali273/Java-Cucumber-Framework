package com.automation.core.config;

public class APIConfig {
    private final String baseUrl;
    private final int timeout;
    private final int maxRetryCount;
    private final String contentType;
    private final boolean logRequest;
    private final boolean logResponse;

    public APIConfig(String baseUrl, int timeout, int maxRetryCount, 
                    String contentType, boolean logRequest, boolean logResponse) {
        this.baseUrl = baseUrl;
        this.timeout = timeout;
        this.maxRetryCount = maxRetryCount;
        this.contentType = contentType;
        this.logRequest = logRequest;
        this.logResponse = logResponse;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public int getTimeout() {
        return timeout;
    }

    public int getMaxRetryCount() {
        return maxRetryCount;
    }

    public String getContentType() {
        return contentType;
    }

    public boolean isLogRequest() {
        return logRequest;
    }

    public boolean isLogResponse() {
        return logResponse;
    }

    @Override
    public String toString() {
        return "APIConfig{" +
                "baseUrl='" + baseUrl + '\'' +
                ", timeout=" + timeout +
                ", maxRetryCount=" + maxRetryCount +
                ", contentType='" + contentType + '\'' +
                ", logRequest=" + logRequest +
                ", logResponse=" + logResponse +
                '}';
    }
}
