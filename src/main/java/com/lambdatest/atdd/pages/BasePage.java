package com.lambdatest.atdd.pages;

import com.lambdatest.atdd.config.TestConfiguration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Base Page Object class providing common functionality for all page objects
 * Implements the Page Object Model pattern for better test maintainability
 */
public abstract class BasePage {
    
    protected final WebDriver driver;
    protected final WebDriverWait wait;
    
    /**
     * Constructor for BasePage
     * 
     * @param driver WebDriver instance
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(TestConfiguration.TestConfig.DEFAULT_TIMEOUT));
    }
    
    /**
     * Navigate to a specific URL
     * 
     * @param url The URL to navigate to
     */
    public void navigateTo(String url) {
        driver.get(url);
    }
    
    /**
     * Get the current page title
     * 
     * @return Page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }
    
    /**
     * Get the current page URL
     * 
     * @return Current URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    /**
     * Wait for an element to be visible
     * 
     * @param locator The element locator
     * @return The visible WebElement
     */
    protected WebElement waitForElementToBeVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    /**
     * Wait for an element to be clickable
     * 
     * @param locator The element locator
     * @return The clickable WebElement
     */
    protected WebElement waitForElementToBeClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Find multiple elements
     * 
     * @param locator The element locator
     * @return List of WebElements
     */
    protected List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }
    
    /**
     * Click on an element with wait
     * 
     * @param locator The element locator
     */
    protected void click(By locator) {
        waitForElementToBeClickable(locator).click();
    }
    
    /**
     * Get text from an element
     * 
     * @param locator The element locator
     * @return Element text
     */
    protected String getText(By locator) {
        return waitForElementToBeVisible(locator).getText();
    }
    
    /**
     * Check if an element is displayed
     * 
     * @param locator The element locator
     * @return true if element is displayed
     */
    protected boolean isElementDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Wait for page to load completely
     */
    protected void waitForPageLoad() {
        wait.until(webDriver -> 
            ((org.openqa.selenium.JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
    }

    /**
     * Abstract method to verify if page is loaded
     * Each page should implement this to check for specific page elements
     * 
     * @return true if page is loaded correctly
     */
    public abstract boolean isPageLoaded();
}
