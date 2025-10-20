package com.automation.core.driver;

import com.automation.core.config.ConfigManager;
import com.automation.core.logging.LogManager;
import com.microsoft.playwright.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

public class DriverManager {
    private static final ThreadLocal<WebDriver> seleniumDriver = new ThreadLocal<>();
    private static final ThreadLocal<Playwright> playwright = new ThreadLocal<>();
    private static final ThreadLocal<Browser> playwrightBrowser = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> playwrightContext = new ThreadLocal<>();
    private static final ThreadLocal<Page> playwrightPage = new ThreadLocal<>();

    public static void initializeDriver() {
        if (ConfigManager.isAPI()) {
            LogManager.info("API framework type - skipping driver initialization");
            return;
        }

        if (ConfigManager.isSelenium()) {
            initializeSeleniumDriver();
        } else if (ConfigManager.isPlaywright()) {
            initializePlaywrightDriver();
        }
    }

    private static void initializeSeleniumDriver() {
        ConfigManager config = ConfigManager.getInstance();
        String browser = config.getBrowser();
        boolean headless = config.isHeadless();

        WebDriver driver;
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
                driver = new ChromeDriver(chromeOptions);
                break;
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getIntProperty("implicit.wait", 10)));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(config.getIntProperty("page.load.timeout", 30)));
        driver.manage().window().maximize();
        seleniumDriver.set(driver);
        LogManager.info("Selenium WebDriver initialized: " + browser);
    }

    private static void initializePlaywrightDriver() {
        ConfigManager config = ConfigManager.getInstance();
        String browser = config.getBrowser();
        boolean headless = config.isHeadless();

        try {
            // Create Playwright instance per thread
            if (playwright.get() == null) {
                playwright.set(Playwright.create());
                LogManager.info("Playwright instance created for thread: " + Thread.currentThread().getName());
            }

            // Create Browser instance per thread
            if (playwrightBrowser.get() == null || !playwrightBrowser.get().isConnected()) {
                BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions().setHeadless(headless);
                Browser browserInstance;
                switch (browser.toLowerCase()) {
                    case "firefox":
                        browserInstance = playwright.get().firefox().launch(launchOptions);
                        break;
                    case "webkit":
                        browserInstance = playwright.get().webkit().launch(launchOptions);
                        break;
                    case "chrome":
                    default:
                        browserInstance = playwright.get().chromium().launch(launchOptions);
                        break;
                }
                playwrightBrowser.set(browserInstance);
                LogManager.info("Playwright Browser launched: " + browser);
            }

            // Create new context and page for each scenario
            BrowserContext context = playwrightBrowser.get().newContext();
            playwrightContext.set(context);
            Page page = context.newPage();
            page.setDefaultTimeout(config.getIntProperty("explicit.wait", 20) * 1000);
            playwrightPage.set(page);
            LogManager.info("Playwright Context and Page created");
        } catch (Exception e) {
            LogManager.error("Error initializing Playwright: " + e.getMessage());
            throw new RuntimeException("Failed to initialize Playwright driver", e);
        }
    }

    public static WebDriver getSeleniumDriver() {
        return seleniumDriver.get();
    }

    public static Page getPlaywrightPage() {
        return playwrightPage.get();
    }
    
    public static void setPlaywrightPage(Page page) {
        playwrightPage.set(page);
    }

    public static void quitDriver() {
        if (ConfigManager.isAPI()) {
            return;
        }

        if (ConfigManager.isSelenium()) {
            if (seleniumDriver.get() != null) {
                seleniumDriver.get().quit();
                seleniumDriver.remove();
                LogManager.info("Selenium WebDriver closed");
            }
        } else if (ConfigManager.isPlaywright()) {
            // Only close Context after each scenario, keep Browser and Playwright alive
            if (playwrightPage.get() != null) {
                playwrightPage.remove();
            }
            if (playwrightContext.get() != null) {
                try {
                    playwrightContext.get().close();
                    LogManager.info("Playwright Context closed");
                } catch (Exception e) {
                    LogManager.error("Error closing Playwright context: " + e.getMessage());
                }
                playwrightContext.remove();
            }
        }
    }

    public static void closePlaywright() {
        if (playwrightBrowser.get() != null) {
            try {
                playwrightBrowser.get().close();
                LogManager.info("Playwright Browser closed for thread: " + Thread.currentThread().getName());
            } catch (Exception e) {
                LogManager.error("Error closing Playwright browser: " + e.getMessage());
            }
            playwrightBrowser.remove();
        }
        if (playwright.get() != null) {
            try {
                playwright.get().close();
                LogManager.info("Playwright instance closed for thread: " + Thread.currentThread().getName());
            } catch (Exception e) {
                LogManager.error("Error closing Playwright: " + e.getMessage());
            }
            playwright.remove();
        }
    }
}
