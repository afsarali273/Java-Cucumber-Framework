package com.automation.core.driver;

import com.automation.core.config.ConfigManager;
import com.automation.core.logging.LogManager;
import com.automation.core.mainframe.MainFrameDriver;
import com.microsoft.playwright.*;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.time.Duration;

public class DriverManager {
    private static final ThreadLocal<WebDriver> seleniumDriver = new ThreadLocal<>();
    private static final ThreadLocal<Playwright> playwright = new ThreadLocal<>();
    private static final ThreadLocal<Browser> playwrightBrowser = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> playwrightContext = new ThreadLocal<>();
    private static final ThreadLocal<Page> playwrightPage = new ThreadLocal<>();
    private static final ThreadLocal<AppiumDriver> appiumDriver = new ThreadLocal<>();
    private static final ThreadLocal<WindowsDriver> windowsDriver = new ThreadLocal<>();
    private static final ThreadLocal<MainFrameDriver> mainframeDriver = new ThreadLocal<>();
    
    // Track all active drivers for emergency cleanup
    private static final java.util.Set<WebDriver> activeSeleniumDrivers = 
        java.util.Collections.synchronizedSet(new java.util.HashSet<>());
    private static final java.util.Set<Playwright> activePlaywrights = 
        java.util.Collections.synchronizedSet(new java.util.HashSet<>());
    
    static {
        // Register shutdown hook to cleanup any remaining drivers
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LogManager.info("Shutdown hook triggered - cleaning up remaining drivers");
            forceCleanupAll();
        }));
    }

    public static void initializeDriver() {
        if (ConfigManager.isAPI()) {
            LogManager.info("API framework type - skipping driver initialization");
            return;
        }

        if (ConfigManager.isSelenium()) {
            initializeSeleniumDriver();
        } else if (ConfigManager.isPlaywright()) {
            initializePlaywrightDriver();
        } else if (ConfigManager.isMobile()) {
            initializeAppiumDriver();
        } else if (ConfigManager.isDesktop()) {
            initializeWindowsDriver();
        } else if (ConfigManager.isMainframe()) {
            initializeMainframeDriver();
        }
    }

    private static void initializeSeleniumDriver() {
        ConfigManager config = ConfigManager.getInstance();
        String browser = config.getBrowser();
        boolean headless = config.isHeadless();

        WebDriver driver = null;
        try {
            switch (browser.toLowerCase()) {
                case "firefox":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    if (headless) firefoxOptions.addArguments("--headless");
                    driver = new FirefoxDriver(firefoxOptions);
                    break;
                case "chrome":
                default:
                    ChromeOptions chromeOptions = new ChromeOptions();
                    if (headless) chromeOptions.addArguments("--headless");
                    chromeOptions.addArguments("--disable-notifications");
                    chromeOptions.addArguments("--remote-allow-origins=*");
                    driver = new ChromeDriver(chromeOptions);
                    break;
            }

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getIntProperty("implicit.wait", 10)));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(config.getIntProperty("page.load.timeout", 30)));
            driver.manage().window().maximize();
            seleniumDriver.set(driver);
            activeSeleniumDrivers.add(driver);
            LogManager.info("Selenium WebDriver initialized: " + browser + " [Thread: " + Thread.currentThread().getName() + "]");
        } catch (Exception e) {
            LogManager.error("Error initializing Selenium driver: " + e.getMessage());
            if (driver != null) {
                try {
                    driver.quit();
                } catch (Exception quitEx) {
                    LogManager.error("Error quitting driver after init failure: " + quitEx.getMessage());
                }
            }
            throw new RuntimeException("Failed to initialize Selenium driver", e);
        }
    }

    private static void initializePlaywrightDriver() {
        ConfigManager config = ConfigManager.getInstance();
        String browser = config.getBrowser();
        boolean headless = config.isHeadless();

        try {
            // Create fresh Playwright instance for each scenario
            Playwright pw = Playwright.create();
            playwright.set(pw);
            activePlaywrights.add(pw);
            LogManager.info("Playwright instance created for thread: " + Thread.currentThread().getName());

            // Create Browser instance
            BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions().setHeadless(headless);
            Browser browserInstance;
            switch (browser.toLowerCase()) {
                case "firefox":
                    browserInstance = pw.firefox().launch(launchOptions);
                    break;
                case "webkit":
                    browserInstance = pw.webkit().launch(launchOptions);
                    break;
                case "chrome":
                default:
                    browserInstance = pw.chromium().launch(launchOptions);
                    break;
            }
            playwrightBrowser.set(browserInstance);
            LogManager.info("Playwright Browser launched: " + browser);

            // Create context and page
            BrowserContext context = browserInstance.newContext();
            playwrightContext.set(context);
            Page page = context.newPage();
            page.setDefaultTimeout(config.getIntProperty("explicit.wait", 20) * 1000);
            playwrightPage.set(page);
            LogManager.info("Playwright Context and Page created");
        } catch (Exception e) {
            LogManager.error("Error initializing Playwright: " + e.getMessage());
            // Cleanup on failure
            cleanupPlaywrightOnError();
            throw new RuntimeException("Failed to initialize Playwright driver", e);
        }
    }

    private static void cleanupPlaywrightOnError() {
        try {
            if (playwrightPage.get() != null) playwrightPage.get().close();
        } catch (Exception ignored) {}
        try {
            if (playwrightContext.get() != null) playwrightContext.get().close();
        } catch (Exception ignored) {}
        try {
            if (playwrightBrowser.get() != null) playwrightBrowser.get().close();
        } catch (Exception ignored) {}
        try {
            if (playwright.get() != null) playwright.get().close();
        } catch (Exception ignored) {}
        playwrightPage.remove();
        playwrightContext.remove();
        playwrightBrowser.remove();
        playwright.remove();
    }

    private static void initializeAppiumDriver() {
        ConfigManager config = ConfigManager.getInstance();
        String platformName = config.getGlobalProperty("mobile.platformName", "Android");
        String deviceName = config.getGlobalProperty("mobile.deviceName", "emulator-5554");
        String appiumServerUrl = config.getGlobalProperty("mobile.appiumServerUrl", "http://127.0.0.1:4723/wd/hub");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", platformName);
        capabilities.setCapability("deviceName", deviceName);
        if (platformName.equalsIgnoreCase("Android")) {
            capabilities.setCapability("appPackage", config.getGlobalProperty("mobile.appPackage", ""));
            capabilities.setCapability("appActivity", config.getGlobalProperty("mobile.appActivity", ""));
            capabilities.setCapability("app", config.getGlobalProperty("mobile.appPath", ""));
        } else if (platformName.equalsIgnoreCase("iOS")) {
            capabilities.setCapability("bundleId", config.getGlobalProperty("mobile.bundleId", ""));
            capabilities.setCapability("app", config.getGlobalProperty("mobile.appPath", ""));
        }
        try {
            AppiumDriver driver;
            if (platformName.equalsIgnoreCase("Android")) {
                driver = new AndroidDriver(new URL(appiumServerUrl), capabilities);
            } else {
                driver = new IOSDriver(new URL(appiumServerUrl), capabilities);
            }
            appiumDriver.set(driver);
            LogManager.info("AppiumDriver initialized for platform: " + platformName);
        } catch (Exception e) {
            LogManager.error("Error initializing AppiumDriver: " + e.getMessage());
            throw new RuntimeException("Failed to initialize Appium driver", e);
        }
    }

    public static WebDriver getSeleniumDriver() {
        WebDriver driver = seleniumDriver.get();
        if (driver == null && ConfigManager.isSelenium()) {
            // Lazy initialization - create driver on first access
            initializeSeleniumDriver();
            driver = seleniumDriver.get();
        }
        return driver;
    }

    public static Page getPlaywrightPage() {
        Page page = playwrightPage.get();
        if (page == null && ConfigManager.isPlaywright()) {
            // Lazy initialization - create driver on first access
            initializePlaywrightDriver();
            page = playwrightPage.get();
        }
        return page;
    }
    
    public static void setPlaywrightPage(Page page) {
        playwrightPage.set(page);
    }

    public static AppiumDriver getAppiumDriver() {
        return appiumDriver.get();
    }

    public static WindowsDriver getWindowsDriver() {
        return windowsDriver.get();
    }

    public static MainFrameDriver getMainframeDriver() {
        return mainframeDriver.get();
    }

    public static void quitDriver() {
        if (ConfigManager.isAPI()) {
            return;
        }

        if (ConfigManager.isSelenium()) {
            WebDriver driver = seleniumDriver.get();
            if (driver != null) {
                try {
                    driver.quit();
                    activeSeleniumDrivers.remove(driver);
                    LogManager.info("Selenium WebDriver closed [Thread: " + Thread.currentThread().getName() + "]");
                } catch (Exception e) {
                    LogManager.error("Error closing Selenium driver: " + e.getMessage());
                } finally {
                    seleniumDriver.remove();
                }
            }
        } else if (ConfigManager.isPlaywright()) {
            // Close page, context, browser, and playwright for each scenario in parallel execution
            if (playwrightPage.get() != null) {
                try {
                    playwrightPage.get().close();
                } catch (Exception e) {
                    LogManager.error("Error closing Playwright page: " + e.getMessage());
                } finally {
                    playwrightPage.remove();
                }
            }
            if (playwrightContext.get() != null) {
                try {
                    playwrightContext.get().close();
                    LogManager.info("Playwright Context closed");
                } catch (Exception e) {
                    LogManager.error("Error closing Playwright context: " + e.getMessage());
                } finally {
                    playwrightContext.remove();
                }
            }
            if (playwrightBrowser.get() != null) {
                try {
                    playwrightBrowser.get().close();
                    LogManager.info("Playwright Browser closed");
                } catch (Exception e) {
                    LogManager.error("Error closing Playwright browser: " + e.getMessage());
                } finally {
                    playwrightBrowser.remove();
                }
            }
            Playwright pw = playwright.get();
            if (pw != null) {
                try {
                    pw.close();
                    activePlaywrights.remove(pw);
                    LogManager.info("Playwright instance closed [Thread: " + Thread.currentThread().getName() + "]");
                } catch (Exception e) {
                    LogManager.error("Error closing Playwright: " + e.getMessage());
                } finally {
                    playwright.remove();
                }
            }
        } else if (ConfigManager.isMobile()) {
            quitAppiumDriver();
        } else if (ConfigManager.isDesktop()) {
            quitWindowsDriver();
        } else if (ConfigManager.isMainframe()) {
            quitMainframeDriver();
        }
    }

    public static void quitAppiumDriver() {
        if (appiumDriver.get() != null) {
            try {
                appiumDriver.get().quit();
                LogManager.info("AppiumDriver closed");
            } catch (Exception e) {
                LogManager.error("Error closing AppiumDriver: " + e.getMessage());
            }
            appiumDriver.remove();
        }
    }

    private static void initializeWindowsDriver() {
        ConfigManager config = ConfigManager.getInstance();
        String winAppDriverUrl = config.getProperty("desktop.winAppDriverUrl", "http://127.0.0.1:4723");
        String app = config.getProperty("desktop.app", "C:\\\\Windows\\\\System32\\\\calc.exe");
        
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("app", app);
        capabilities.setCapability("platformName", config.getProperty("desktop.platformName", "Windows"));
        capabilities.setCapability("deviceName", config.getProperty("desktop.deviceName", "WindowsPC"));
        
        try {
            WindowsDriver driver = new WindowsDriver(new URL(winAppDriverUrl), capabilities);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getImplicitWait()));
            windowsDriver.set(driver);
            LogManager.info("WindowsDriver initialized for app: " + app);
        } catch (Exception e) {
            LogManager.error("Error initializing WindowsDriver: " + e.getMessage());
            throw new RuntimeException("Failed to initialize Windows driver", e);
        }
    }

    public static void quitWindowsDriver() {
        if (windowsDriver.get() != null) {
            try {
                windowsDriver.get().quit();
                LogManager.info("WindowsDriver closed");
            } catch (Exception e) {
                LogManager.error("Error closing WindowsDriver: " + e.getMessage());
            }
            windowsDriver.remove();
        }
    }

    public static void closePlaywright() {
        // This method is now deprecated - cleanup happens in quitDriver()
        // Kept for backward compatibility
        LogManager.info("closePlaywright() called - cleanup handled by quitDriver()");
    }
    
    /**
     * Force cleanup all active drivers from all threads.
     * Called by shutdown hook to ensure no orphaned browsers.
     */
    public static void forceCleanupAll() {
        // Cleanup all Selenium drivers
        synchronized (activeSeleniumDrivers) {
            for (WebDriver driver : activeSeleniumDrivers) {
                try {
                    driver.quit();
                    LogManager.info("Force closed Selenium driver");
                } catch (Exception e) {
                    LogManager.error("Error force closing Selenium driver: " + e.getMessage());
                }
            }
            activeSeleniumDrivers.clear();
        }
        
        // Cleanup all Playwright instances
        synchronized (activePlaywrights) {
            for (Playwright pw : activePlaywrights) {
                try {
                    pw.close();
                    LogManager.info("Force closed Playwright instance");
                } catch (Exception e) {
                    LogManager.error("Error force closing Playwright: " + e.getMessage());
                }
            }
            activePlaywrights.clear();
        }
    }

    private static void initializeMainframeDriver() {
        ConfigManager config = ConfigManager.getInstance();
        String sessionId = config.getProperty("mainframe.sessionId", "A");
        MainFrameDriver driver = new MainFrameDriver();
        driver.connect(sessionId);
        mainframeDriver.set(driver);
        LogManager.info("MainframeDriver initialized with session: " + sessionId);
    }

    public static void quitMainframeDriver() {
        if (mainframeDriver.get() != null) {
            try {
                mainframeDriver.get().close();
                LogManager.info("MainframeDriver closed");
            } catch (Exception e) {
                LogManager.error("Error closing MainframeDriver: " + e.getMessage());
            }
            mainframeDriver.remove();
        }
    }
}
