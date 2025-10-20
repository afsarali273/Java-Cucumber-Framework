package com.automation.pages;

import com.automation.core.config.ConfigManager;
import com.automation.reusables.SeleniumReusable;
import org.openqa.selenium.By;

public class FlipkartSeleniumPage extends SeleniumReusable {
    
    private By searchBox = By.name("q");
    private By searchButton = By.cssSelector("button[type='submit']");
    private By firstProduct = By.cssSelector("div[data-id] a");
    private By addToCartButton = By.xpath("//button[text()='Add to cart']");
    private By cartIcon = By.cssSelector("a[href='/viewcart']");
    private By closeLoginPopup = By.cssSelector("button._2KpZ6l._2doB4z");

    public void openFlipkart() {
        navigateTo(ConfigManager.getInstance().getAppUrl());
        try {
            if (isDisplayed(closeLoginPopup)) {
                click(closeLoginPopup);
            }
        } catch (Exception e) {
            // Login popup not displayed
        }
    }

    public void searchProduct(String productName) {
        type(searchBox, productName);
        click(searchButton);
        waitForSeconds(2);
    }

    public void clickFirstProduct() {
        scrollToElement(firstProduct);
        click(firstProduct);
        waitForSeconds(2);
    }

    public void addToCart() {
        // Switch to new window if opened
        String mainWindow = driver.getWindowHandle();
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(mainWindow)) {
                driver.switchTo().window(handle);
                break;
            }
        }
        
        waitForSeconds(2);
        if (isDisplayed(addToCartButton)) {
            click(addToCartButton);
        }
    }

    public boolean isProductInCart() {
        return isDisplayed(cartIcon);
    }
}
