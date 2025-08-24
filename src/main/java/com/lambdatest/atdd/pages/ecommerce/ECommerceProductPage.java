package com.lambdatest.atdd.pages.ecommerce;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lambdatest.atdd.pages.BasePage;

/**
 * Page Object for E-Commerce product details page
 */
public class ECommerceProductPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(ECommerceProductPage.class);
    
    // Direct selectors based on the exact classes we found in debugging
    private static final By ADD_TO_CART_BUTTON = By.cssSelector("button.btn-cart");
    private static final By ADD_TO_CART_BUTTON_ALT = By.cssSelector("button.button-cart");
    private static final By ADD_TO_CART_BUTTON_BROAD = By.cssSelector("button.btn-secondary.btn-block");
    
    private static final By PRODUCT_TITLE = By.xpath(
        "//h1[contains(@class, 'page-title')] | " +
        "//h1 | " +
        "//div[contains(@class, 'product-title')] | " +
        "//*[@class='page-title-wrapper product']//h1"
    );
    
    private static final By SUCCESS_MESSAGE = By.xpath(
        "//div[contains(@class, 'alert-success')] | " +
        "//*[contains(text(), 'Success')] | " +
        "//div[contains(@class, 'message-success')] | " +
        "//div[contains(text(), 'added to') and contains(text(), 'cart')]"
    );
    
    public ECommerceProductPage(WebDriver driver) {
        super(driver);
    }
    
    public void addToCart() {
        logger.info("Attempting to add product to cart...");
        
        // Wait for page to be fully loaded
        waitForPageToLoad();
        
        // Try multiple selectors to find and click the Add to Cart button
        boolean clicked = false;
        
        // Try primary selector
        try {
            if (isElementDisplayed(ADD_TO_CART_BUTTON)) {
                logger.info("Primary Add to Cart button (btn-cart) found and visible");
                click(ADD_TO_CART_BUTTON);
                logger.info("Add to Cart button clicked successfully");
                clicked = true;
            }
        } catch (Exception e) {
            logger.debug("Primary selector failed: {}", e.getMessage());
        }
        
        // Try alternative selector if first failed
        if (!clicked) {
            try {
                if (isElementDisplayed(ADD_TO_CART_BUTTON_ALT)) {
                    logger.info("Alternative Add to Cart button (button-cart) found and visible");
                    click(ADD_TO_CART_BUTTON_ALT);
                    logger.info("Add to Cart button clicked successfully with alternative selector");
                    clicked = true;
                }
            } catch (Exception e) {
                logger.debug("Alternative selector failed: {}", e.getMessage());
            }
        }
        
        // Try broad selector if both failed
        if (!clicked) {
            try {
                if (isElementDisplayed(ADD_TO_CART_BUTTON_BROAD)) {
                    logger.info("Broad Add to Cart button (btn-secondary btn-block) found and visible");
                    click(ADD_TO_CART_BUTTON_BROAD);
                    logger.info("Add to Cart button clicked successfully with broad selector");
                    clicked = true;
                }
            } catch (Exception e) {
                logger.debug("Broad selector failed: {}", e.getMessage());
            }
        }
        
        if (!clicked) {
            logger.warn("All standard selectors failed, trying JavaScript click...");
            // Try JavaScript click as last resort
            try {
                var button = driver.findElement(ADD_TO_CART_BUTTON);
                logger.info("Button found, attempting JavaScript click...");
                
                // Scroll into view first
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
                Thread.sleep(500); // Wait for scroll
                
                // Force click via JavaScript
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
                logger.info("JavaScript click executed successfully");
            } catch (Exception jsEx) {
                logger.error("JavaScript click also failed: {}", jsEx.getMessage(), jsEx);
                debugButtonElements();
                logger.error("Current page title: {}", driver.getTitle());
                logger.error("Current page URL: {}", driver.getCurrentUrl());
            }
        }

    }
    
    private void waitForPageToLoad() {
        // Wait for either product title or add to cart button to be present
        try {
            logger.debug("Waiting for product page to load...");
            waitForElementToBeVisible(PRODUCT_TITLE);
            logger.debug("Product page loaded successfully");
        } catch (Exception e) {
            logger.warn("Warning: Product page may not be fully loaded: {}", e.getMessage());
        }
    }
    
    private void debugButtonElements() {
        try {
            logger.debug("DEBUG: Searching for all buttons on page...");
            
            // Find all buttons
            var allButtons = driver.findElements(By.tagName("button"));
            logger.debug("Found {} button elements:", allButtons.size());
            for (int i = 0; i < Math.min(allButtons.size(), 10); i++) { // Limit to first 10
                var button = allButtons.get(i);
                logger.debug("  Button {}: id='{}', class='{}', text='{}'", i, button.getAttribute("id"), button.getAttribute("class"), button.getText());
            }
            
            // Find all inputs
            var allInputs = driver.findElements(By.tagName("input"));
            logger.debug("Found {} input elements:", allInputs.size());
            for (int i = 0; i < Math.min(allInputs.size(), 10); i++) { // Limit to first 10
                var input = allInputs.get(i);
                String type = input.getAttribute("type");
                if ("button".equals(type) || "submit".equals(type)) {
                    logger.debug("  Input {}: id='{}', type='{}', value='{}'", i, input.getAttribute("id"), type, input.getAttribute("value"));
                }
            }
            
        } catch (Exception e) {
            logger.error("Error during button debugging: {}", e.getMessage(), e);
        }
    }
    
    public boolean isSuccessMessageDisplayed() {
        return isElementDisplayed(SUCCESS_MESSAGE);
    }
    
    @Override
    public boolean isPageLoaded() {
        return isElementDisplayed(ADD_TO_CART_BUTTON) && isElementDisplayed(PRODUCT_TITLE);
    }
}
