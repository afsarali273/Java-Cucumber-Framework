package com.automation.stepdefinitions;

import com.automation.core.config.ConfigManager;
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
        if (ConfigManager.isSelenium()) {
            seleniumPage.searchProduct(productName);
        } else if (ConfigManager.isPlaywright()) {
            playwrightPage.searchProduct(productName);
        }
    }

    @When("user clicks on first product")
    public void userClicksOnFirstProduct() {
        LogManager.info("Clicking on first product");
        if (ConfigManager.isSelenium()) {
            seleniumPage.clickFirstProduct();
        } else if (ConfigManager.isPlaywright()) {
            playwrightPage.clickFirstProduct();
        }
    }

    @When("user adds product to cart")
    public void userAddsProductToCart() {
        LogManager.info("Adding product to cart");
        if (ConfigManager.isSelenium()) {
            seleniumPage.addToCart();
        } else if (ConfigManager.isPlaywright()) {
            playwrightPage.addToCart();
        }
    }

    @Then("product should be added to cart successfully")
    public void productShouldBeAddedToCartSuccessfully() {
        if (ConfigManager.isSelenium()) {
            seleniumPage.assertTrue(seleniumPage.isProductInCart(), "Product should be in cart");
        } else if (ConfigManager.isPlaywright()) {
            playwrightPage.assertTrue(playwrightPage.isProductInCart(), "Product should be in cart");
        }
    }
}
