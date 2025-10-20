package com.automation.core.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {
    private static ConfigManager instance;
    private Properties globalConfig;
    private Properties envConfig;
    private FrameworkConfig frameworkConfig;
    private APIConfig apiConfig;

    // Global static final variables for easy access
    public static final String ENVIRONMENT;
    public static final String FRAMEWORK_TYPE;
    public static final String BROWSER;
    public static final boolean IS_HEADLESS;
    public static final int WAIT_TIMEOUT;
    public static final int MAX_RETRY_COUNT;
    public static final int API_TIMEOUT;
    public static final String API_BASE_URL;
    public static final String API_CONTENT_TYPE;
    public static final boolean API_LOG_REQUEST;
    public static final boolean API_LOG_RESPONSE;

    static {
        ConfigManager manager = getInstance();
        ENVIRONMENT = manager.getEnvironment();
        FRAMEWORK_TYPE = manager.getFrameworkType();
        BROWSER = manager.getBrowser();
        IS_HEADLESS = manager.isHeadless();
        WAIT_TIMEOUT = manager.getWaitTimeout();
        MAX_RETRY_COUNT = manager.getMaxRetryCount();
        API_TIMEOUT = manager.getApiTimeout();
        API_BASE_URL = manager.getApiBaseUrl();
        API_CONTENT_TYPE = manager.getProperty("api.content.type", "application/json");
        API_LOG_REQUEST = manager.getBooleanProperty("api.log.request", true);
        API_LOG_RESPONSE = manager.getBooleanProperty("api.log.response", true);
    }

    private ConfigManager() {
        loadConfigurations();
        initializeFrameworkConfig();
        initializeAPIConfig();
    }

    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    private void loadConfigurations() {
        globalConfig = loadProperties("src/main/resources/config.properties");
        String environment = globalConfig.getProperty("environment", "qa");
        envConfig = loadProperties("src/main/resources/" + environment + ".properties");
    }

    private void initializeFrameworkConfig() {
        frameworkConfig = new FrameworkConfig(
            getGlobalProperty("framework.type", "selenium"),
            getGlobalProperty("browser", "chrome"),
            getGlobalBooleanProperty("headless", false),
            getGlobalProperty("environment", "qa"),
            getEnvIntProperty("wait.timeout", 10),
            getEnvIntProperty("max.retry.count", 2),
            getEnvIntProperty("api.timeout", 30)
        );
    }

    private void initializeAPIConfig() {
        apiConfig = new APIConfig(
            getEnvProperty("api.base.url"),
            getEnvIntProperty("api.timeout", 30),
            getEnvIntProperty("max.retry.count", 2),
            getProperty("api.content.type", "application/json"),
            getBooleanProperty("api.log.request", true),
            getBooleanProperty("api.log.response", true)
        );
    }

    private Properties loadProperties(String filePath) {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(filePath)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties from: " + filePath, e);
        }
        return properties;
    }

    public String getProperty(String key) {
        String value = envConfig.getProperty(key);
        return value != null ? value : globalConfig.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
    }

    public String getGlobalProperty(String key) {
        return globalConfig.getProperty(key);
    }

    public String getGlobalProperty(String key, String defaultValue) {
        String value = globalConfig.getProperty(key);
        return value != null ? value : defaultValue;
    }

    public String getEnvProperty(String key) {
        return envConfig.getProperty(key);
    }

    public String getEnvProperty(String key, String defaultValue) {
        String value = envConfig.getProperty(key);
        return value != null ? value : defaultValue;
    }

    public int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        return value != null ? Integer.parseInt(value) : defaultValue;
    }

    public int getGlobalIntProperty(String key, int defaultValue) {
        String value = getGlobalProperty(key);
        return value != null ? Integer.parseInt(value) : defaultValue;
    }

    public int getEnvIntProperty(String key, int defaultValue) {
        String value = getEnvProperty(key);
        return value != null ? Integer.parseInt(value) : defaultValue;
    }

    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }

    public boolean getGlobalBooleanProperty(String key, boolean defaultValue) {
        String value = getGlobalProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }

    public boolean getEnvBooleanProperty(String key, boolean defaultValue) {
        String value = getEnvProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }

    public String getFrameworkType() {
        return getGlobalProperty("framework.type", "selenium");
    }

    public String getBrowser() {
        return getGlobalProperty("browser", "chrome");
    }

    public boolean isHeadless() {
        return getGlobalBooleanProperty("headless", false);
    }

    public String getAppUrl() {
        return getEnvProperty("app.url");
    }

    public String getApiBaseUrl() {
        return getEnvProperty("api.base.url");
    }

    public String getApiBaseUrl(String serviceName) {
        String key = "api.base.url." + serviceName;
        String url = getEnvProperty(key);
        if (url == null) {
            throw new RuntimeException("API base URL not found for service: " + serviceName + ". Expected property: " + key);
        }
        return url;
    }

    public int getApiTimeout() {
        return getEnvIntProperty("api.timeout", 30);
    }

    public String getDbUrl() {
        return getEnvProperty("db.url");
    }

    public String getDbUsername() {
        return getEnvProperty("db.username");
    }

    public String getDbPassword() {
        return getEnvProperty("db.password");
    }

    public String getTestUserEmail() {
        return getEnvProperty("test.user.email");
    }

    public String getTestUserPassword() {
        return getEnvProperty("test.user.password");
    }

    public int getMaxRetryCount() {
        return getEnvIntProperty("max.retry.count", 2);
    }

    public int getWaitTimeout() {
        return getEnvIntProperty("wait.timeout", 10);
    }

    public String getEnvironment() {
        return getGlobalProperty("environment", "qa");
    }

    public Properties getAllGlobalProperties() {
        return new Properties(globalConfig);
    }

    public Properties getAllEnvProperties() {
        return new Properties(envConfig);
    }

    public boolean hasGlobalProperty(String key) {
        return globalConfig.containsKey(key);
    }

    public boolean hasEnvProperty(String key) {
        return envConfig.containsKey(key);
    }

    public boolean hasProperty(String key) {
        return hasEnvProperty(key) || hasGlobalProperty(key);
    }

    public FrameworkConfig getConfig() {
        return frameworkConfig;
    }

    public APIConfig getAPIConfig() {
        return apiConfig;
    }

    public static String getEnv() {
        return ENVIRONMENT;
    }

    public static boolean isSelenium() {
        return "selenium".equalsIgnoreCase(FRAMEWORK_TYPE);
    }

    public static boolean isPlaywright() {
        return "playwright".equalsIgnoreCase(FRAMEWORK_TYPE);
    }

    public static boolean isAPI() {
        return "api".equalsIgnoreCase(FRAMEWORK_TYPE);
    }

    public static boolean isMobile() {
        return "mobile".equalsIgnoreCase(getInstance().getFrameworkType());
    }
}
