package com.automation.reusables;

import com.automation.core.config.ConfigManager;
import com.automation.core.interfaces.ModularTestLifecycle;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.DataProvider;
import com.automation.core.driver.DriverManager;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebDriver;

/**
 * ModularTestConfig provides TestNG lifecycle hooks and DataProviders for modular execution.
 * Supports browser, mobile, and API test initialization and cleanup.
 */
public class ModularTestConfig implements ModularTestLifecycle {

    @BeforeSuite
    public void setUpSuite() {
        ConfigManager.getInstance();
        DriverManager.initializeDriver();
    }

    @AfterSuite
    public void tearDownSuite() {
        DriverManager.quitDriver();
    }

    @DataProvider(name = "ChromeBrowser")
    public Object[][] desktopBrowsers() {
        // Example: Provide browser test parameters (can be extended)
        return new Object[][] { { "Instance1" } };
    }

    @DataProvider(name = "MobileAndroid")
    public Object[][] mobileDevice() {
        // Example: Provide mobile test parameters (can be extended)
        return new Object[][] { { "Instance1" } };
    }

    @DataProvider(name = "API")
    public Object[][] apiTests() {
        // Example: Provide API test parameters (can be extended)
        return new Object[][] { { "Instance1" } };
    }

    @Override
    public void setUp() {
        // TODO Auto-generated method stub
    }

    @Override
    public void executeTest() {
        // TODO Auto-generated method stub
    }

    @Override
    public void tearDown() {
        // TODO Auto-generated method stub
    }

    // Sample usage in your test class:
    //
    // @Test(dataProvider = "ChromeBrowser", dataProviderClass = ModularTestConfig.class)
    // public void webTest(String instance) {
    //     WebDriver driver = DriverManager.getSeleniumDriver();
    //     driver.get("https://example.com");
    // }
    //
    // @Test(dataProvider = "MobileAndroid", dataProviderClass = ModularTestConfig.class)
    // public void mobileTest(String instance) {
    //     AppiumDriver driver = DriverManager.getAppiumDriver();
    //     // ...mobile test steps...
    // }
    //
    // @Test(dataProvider = "API", dataProviderClass = ModularTestConfig.class)
    // public void apiTest(String instance) {
    //     // Use APIKeywords for API tests
    // }
}
