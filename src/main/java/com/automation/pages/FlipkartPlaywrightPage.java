package com.automation.pages;

import com.automation.core.config.ConfigManager;
import com.automation.core.driver.DriverManager;
import com.automation.reusables.PlaywrightReusable;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class FlipkartPlaywrightPage extends PlaywrightReusable {
    
    private Locator searchBox() { return locator("input[name='q']"); }
    private Locator searchButton() { return locator("button[type='submit']"); }
    private Locator firstProduct() { return locator("div[data-id] a").first(); }
    private Locator addToCartButton() { return locator("button:has-text('Add to cart')"); }
    private Locator cartIcon() { return locator("a[href='/viewcart']"); }
    private Locator closeLoginPopup() { return locator("button._2KpZ6l._2doB4z"); }

    public void openFlipkart() {
        navigateTo(ConfigManager.getInstance().getAppUrl());
        try {
            if (isDisplayed(closeLoginPopup())) {
                click(closeLoginPopup());
            }
        } catch (Exception e) {
            // Login popup not displayed
        }
    }

    public void searchProduct(String productName) {
        type(searchBox(), productName);
        click(searchButton());
        waitForSeconds(2);
    }

    public void clickFirstProduct() {
        scrollToElement(firstProduct());
        
        // Handle new page opening
        Page newPage = page.context().waitForPage(() -> {
            click(firstProduct());
        });
        
        // Switch to new page and update DriverManager
        page = newPage;
        DriverManager.setPlaywrightPage(newPage);
        waitForSeconds(2);
    }

    public void addToCart() {
        waitForSeconds(2);
        try {
            if (isDisplayed(addToCartButton())) {
                click(addToCartButton());
            }
        } catch (Exception e) {
            // Add to cart button might not be visible
        }
    }

    public boolean isProductInCart() {
        try {
            return isDisplayed(cartIcon());
        } catch (Exception e) {
            return false;
        }
    }
}
