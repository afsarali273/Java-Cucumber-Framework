package com.automation.stepdefinitions;

import com.automation.core.config.ConfigManager;
import com.automation.core.context.ScenarioContext;
import com.automation.core.logging.LogManager;
import com.automation.pages.FlipkartPlaywrightPage;
import com.automation.pages.FlipkartSeleniumPage;
import io.cucumber.java.en.*;

public class FlipkartSteps {
    
    private FlipkartSeleniumPage seleniumPage;
    private FlipkartPlaywrightPage playwrightPage;

    public FlipkartSteps() {
        if (ConfigManager.isSelenium()) {
            seleniumPage = new FlipkartSeleniumPage();
        } else if (ConfigManager.isPlaywright()) {
            playwrightPage = new FlipkartPlaywrightPage();
        }
    }

    @Given("user opens Flipkart website")
    public void userOpensFlipkartWebsite() {
        LogManager.info("Opening Flipkart website");
        if (ConfigManager.isSelenium()) {
            seleniumPage.openFlipkart();
        } else if (ConfigManager.isPlaywright()) {
            playwrightPage.openFlipkart();
        }
    }

    @When("user searches for {string}")
    public void userSearchesFor(String productName) {
        LogManager.info("Searching for product: " + productName);
        
        // Store product name in context for later use
        ScenarioContext.set("productName", productName);
        
        if (ConfigManager.isSelenium()) {
            seleniumPage.searchProduct(productName);
        } else if (ConfigManager.isPlaywright()) {
            playwrightPage.searchProduct(productName);
        }
    }

    @When("user clicks on first product")
    public void userClicksOnFirstProduct() {
        LogManager.info("Clicking on first product");
        
        // Store product clicked flag
        ScenarioContext.set("productClicked", true);
        
        if (ConfigManager.isSelenium()) {
            seleniumPage.clickFirstProduct();
        } else if (ConfigManager.isPlaywright()) {
            playwrightPage.clickFirstProduct();
        }
    }

    @When("user adds product to cart")
    public void userAddsProductToCart() {
        // Retrieve product name from context
        String productName = ScenarioContext.get("productName", String.class);
        LogManager.info("Adding product to cart: " + productName);
        
        if (ConfigManager.isSelenium()) {
            seleniumPage.addToCart();
        } else if (ConfigManager.isPlaywright()) {
            playwrightPage.addToCart();
        }
        
        // Store cart status
        ScenarioContext.putIfAbsent("addedToCart", true);
        ScenarioContext.set("cartItemCount", 1);
    }

    @Then("product should be added to cart successfully")
    public void productShouldBeAddedToCartSuccessfully() {
        // Verify using context data
        String productName = ScenarioContext.getString("productName");
        Boolean addedToCart = ScenarioContext.getBoolean("addedToCart"); // may be null
        boolean isAdded = Boolean.TRUE.equals(addedToCart); // null-safe

        LogManager.info("Verifying product in cart: " + productName + ", Added: " + isAdded);

        if (ConfigManager.isSelenium()) {
            seleniumPage.assertTrue(seleniumPage.isProductInCart(), "Product should be in cart");
        } else if (ConfigManager.isPlaywright()) {
            playwrightPage.assertTrue(playwrightPage.isProductInCart(), "Product should be in cart");
        }
    }
}
