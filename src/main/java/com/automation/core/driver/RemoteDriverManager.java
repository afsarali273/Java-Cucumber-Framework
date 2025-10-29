package com.automation.core.driver;

import com.automation.core.config.ConfigManager;
import com.automation.core.logging.LogManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages remote driver initialization for cloud providers
 * Supports: LambdaTest (Web), AWS Device Farm (Mobile)
 */
public class RemoteDriverManager {

    public static WebDriver initializeRemoteSeleniumDriver() {
        ConfigManager config = ConfigManager.getInstance();
        String provider = config.getProperty("remote.provider", "").toLowerCase();
        
        if ("lambdatest".equals(provider)) {
            return initializeLambdaTestDriver();
        }
        
        throw new RuntimeException("Unsupported remote provider: " + provider);
    }

    public static AppiumDriver initializeRemoteMobileDriver() {
        ConfigManager config = ConfigManager.getInstance();
        String provider = config.getProperty("remote.provider", "").toLowerCase();
        
        if ("aws".equals(provider) || "devicefarm".equals(provider)) {
            return initializeAWSDeviceFarmDriver();
        }
        
        throw new RuntimeException("Unsupported remote mobile provider: " + provider);
    }

    private static WebDriver initializeLambdaTestDriver() {
        ConfigManager config = ConfigManager.getInstance();
        String username = config.getProperty("lambdatest.username", "");
        String accessKey = config.getProperty("lambdatest.accessKey", "");
        String gridUrl = config.getProperty("lambdatest.gridUrl", "https://hub.lambdatest.com/wd/hub");
        
        if (username.isEmpty() || accessKey.isEmpty()) {
            throw new RuntimeException("LambdaTest credentials not configured");
        }

        String browser = config.getBrowser();
        boolean headless = config.isHeadless();
        
        DesiredCapabilities capabilities = new DesiredCapabilities();
        
        if ("chrome".equalsIgnoreCase(browser)) {
            ChromeOptions options = new ChromeOptions();
            if (headless) options.addArguments("--headless");
            options.addArguments("--disable-notifications");
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);
            capabilities.setCapability("browserName", "Chrome");
        } else if ("firefox".equalsIgnoreCase(browser)) {
            FirefoxOptions options = new FirefoxOptions();
            if (headless) options.addArguments("--headless");
            capabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, options);
            capabilities.setCapability("browserName", "Firefox");
        }
        
        capabilities.setCapability("browserVersion", config.getProperty("lambdatest.browserVersion", "latest"));
        capabilities.setCapability("platformName", config.getProperty("lambdatest.platform", "Windows 10"));
        
        Map<String, Object> ltOptions = new HashMap<>();
        ltOptions.put("username", username);
        ltOptions.put("accessKey", accessKey);
        ltOptions.put("build", config.getProperty("lambdatest.build", "Sands Framework Build"));
        ltOptions.put("name", config.getProperty("lambdatest.testName", "Test - " + Thread.currentThread().getName()));
        ltOptions.put("project", config.getProperty("lambdatest.project", "Sands Automation"));
        ltOptions.put("w3c", true);
        ltOptions.put("plugin", "java-testNG");
        ltOptions.put("video", config.getBooleanProperty("lambdatest.video", true));
        ltOptions.put("visual", config.getBooleanProperty("lambdatest.visual", false));
        ltOptions.put("network", config.getBooleanProperty("lambdatest.network", false));
        ltOptions.put("console", config.getBooleanProperty("lambdatest.console", false));
        
        capabilities.setCapability("LT:Options", ltOptions);
        
        try {
            WebDriver driver = new RemoteWebDriver(new URL(gridUrl), capabilities);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getIntProperty("implicit.wait", 10)));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(config.getIntProperty("page.load.timeout", 30)));
            LogManager.info("LambdaTest RemoteWebDriver initialized [Thread: " + Thread.currentThread().getName() + "]");
            return driver;
        } catch (Exception e) {
            LogManager.error("Failed to initialize LambdaTest driver: " + e.getMessage());
            throw new RuntimeException("LambdaTest driver initialization failed", e);
        }
    }

    private static AppiumDriver initializeAWSDeviceFarmDriver() {
        ConfigManager config = ConfigManager.getInstance();
        String platformName = config.getProperty("mobile.platformName", "Android");
        String deviceFarmUrl = config.getProperty("aws.devicefarm.url", "");
        
        if (deviceFarmUrl.isEmpty()) {
            throw new RuntimeException("AWS Device Farm URL not configured");
        }

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", platformName);
        capabilities.setCapability("deviceName", config.getProperty("aws.devicefarm.deviceName", ""));
        capabilities.setCapability("platformVersion", config.getProperty("mobile.platformVersion", ""));
        capabilities.setCapability("app", config.getProperty("aws.devicefarm.app", ""));
        capabilities.setCapability("automationName", config.getProperty("mobile.automationName", "UiAutomator2"));
        
        if ("Android".equalsIgnoreCase(platformName)) {
            capabilities.setCapability("appPackage", config.getProperty("mobile.appPackage", ""));
            capabilities.setCapability("appActivity", config.getProperty("mobile.appActivity", ""));
        } else if ("iOS".equalsIgnoreCase(platformName)) {
            capabilities.setCapability("bundleId", config.getProperty("mobile.bundleId", ""));
        }
        
        try {
            AppiumDriver driver;
            if ("Android".equalsIgnoreCase(platformName)) {
                driver = new AndroidDriver(new URL(deviceFarmUrl), capabilities);
            } else {
                driver = new IOSDriver(new URL(deviceFarmUrl), capabilities);
            }
            LogManager.info("AWS Device Farm driver initialized for " + platformName + " [Thread: " + Thread.currentThread().getName() + "]");
            return driver;
        } catch (Exception e) {
            LogManager.error("Failed to initialize AWS Device Farm driver: " + e.getMessage());
            throw new RuntimeException("AWS Device Farm driver initialization failed", e);
        }
    }
}
