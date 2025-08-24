package com.lambdatest.atdd.pages.ecommerce;

import com.lambdatest.atdd.config.TestConfiguration;
import com.lambdatest.atdd.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for LambdaTest E-Commerce Playground homepage
 * Encapsulates all interactions with the e-commerce homepage functionality
 */
public class ECommerceHomePage extends BasePage {
    
    // Locators for search functionality
    private static final By SEARCH_INPUT = By.name("search");
    private static final By SEARCH_INPUT_ALT = By.xpath("//input[@placeholder='Search']");
    
    // Locators for navigation and menu
    private static final By LOGO = By.xpath("//img[contains(@title, 'Your Store')] | //a[contains(@class, 'navbar-brand')]");
    private static final By MAIN_MENU = By.xpath("//ul[contains(@class, 'nav')]");

    // Locators for products
    private static final By FEATURED_PRODUCTS = By.xpath("//div[contains(@class, 'product-thumb')] | //div[contains(@class, 'product-layout')]");
    private static final By PRODUCT_TITLES = By.xpath("//h4[contains(@class, 'title')] | //div[contains(@class, 'name')] | //a[contains(@class, 'product-title')]");

    // Locators for cart
    private static final By CART_COUNT = By.xpath("//span[contains(@class, 'cart-count')] | //span[contains(@class, 'badge')]");
    
    // Page validation
    private static final String EXPECTED_URL_PART = "ecommerce-playground";
    
    /**
     * Constructor
     * 
     * @param driver WebDriver instance
     */
    public ECommerceHomePage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Navigate to the E-Commerce homepage
     */
    public void navigateToHomepage() {
        navigateTo(TestConfiguration.ECOMMERCE_BASE_URL);
        waitForPageLoad();
    }
    
    /**
     * Search for a product
     * 
     * @param searchTerm The term to search for
     * @return Search results page object
     */
    public ECommerceSearchResultsPage searchForProduct(String searchTerm) {
        WebElement searchBox = getSearchInput();
        searchBox.clear();
        searchBox.sendKeys(searchTerm);
        searchBox.sendKeys(Keys.ENTER);
        
        waitForPageLoad();
        return new ECommerceSearchResultsPage(driver);
    }
    
    /**
     * Get the search input element with fallback options
     * 
     * @return WebElement for search input
     */
    private WebElement getSearchInput() {
        try {
            return waitForElementToBeVisible(SEARCH_INPUT);
        } catch (Exception e) {
            try {
                return waitForElementToBeVisible(SEARCH_INPUT_ALT);
            } catch (Exception e2) {
                return waitForElementToBeVisible(By.xpath("//input[contains(@class, 'search')] | //input[contains(@placeholder, 'search')]"));
            }
        }
    }
    
    /**
     * Click on a specific product by name
     * 
     * @param productName The name of the product to click
     * @return Product details page object
     */
    public ECommerceProductPage clickProduct(String productName) {
        WebElement product = waitForElementToBeClickable(
            By.xpath("//h4[contains(text(), '" + productName + "')] | //a[contains(text(), '" + productName + "')]")
        );
        product.click();
        waitForPageLoad();
        return new ECommerceProductPage(driver);
    }
    
    /**
     * Click on the first available product
     * 
     * @return Product details page object
     */
    public ECommerceProductPage clickFirstProduct() {
        List<WebElement> products = findElements(PRODUCT_TITLES);
        if (!products.isEmpty()) {
            products.get(0).click();
            waitForPageLoad();
        }
        return new ECommerceProductPage(driver);
    }
    
    /**
     * Get all featured product names
     * 
     * @return List of product names
     */
    public List<String> getFeaturedProductNames() {
        List<WebElement> productTitles = findElements(PRODUCT_TITLES);
        return productTitles.stream()
                .map(WebElement::getText)
                .filter(text -> !text.trim().isEmpty())
                .toList();
    }
    
    /**
     * Check if products are displayed on the homepage
     * 
     * @return true if products are visible
     */
    public boolean areProductsDisplayed() {
        try {
            List<WebElement> products = findElements(FEATURED_PRODUCTS);
            return !products.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
    

    
    /**
     * Get cart item count
     * 
     * @return Number of items in cart as string
     */
    public String getCartCount() {
        try {
            return getText(CART_COUNT);
        } catch (Exception e) {
            return "0";
        }
    }
    
    /**
     * Check if the page logo is displayed
     * 
     * @return true if logo is visible
     */
    public boolean isLogoDisplayed() {
        return isElementDisplayed(LOGO);
    }
    
    /**
     * Check if the main navigation menu is displayed
     * 
     * @return true if menu is visible
     */
    public boolean isMainMenuDisplayed() {
        return isElementDisplayed(MAIN_MENU);
    }
    
    /**
     * Check if the search functionality is available
     * 
     * @return true if search input is displayed
     */
    public boolean isSearchAvailable() {
        try {
            getSearchInput();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get the current page title
     * 
     * @return Page title
     */
    public String getHomepageTitle() {
        return getPageTitle();
    }
    
    @Override
    public boolean isPageLoaded() {
        try {
            return getCurrentUrl().contains(EXPECTED_URL_PART) &&
                   isSearchAvailable() &&
                   (isLogoDisplayed() || isMainMenuDisplayed());
        } catch (Exception e) {
            return false;
        }
    }
}
