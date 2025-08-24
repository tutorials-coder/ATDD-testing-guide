package com.lambdatest.atdd.pages.ecommerce;

import com.lambdatest.atdd.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for E-Commerce search results page
 */
public class ECommerceSearchResultsPage extends BasePage {
    
    private static final By SEARCH_RESULTS = By.className("product-thumb");
    private static final By PRODUCT_NAMES = By.xpath("//h4[contains(@class, 'title')] | //div[contains(@class, 'name')]");
    private static final By NO_RESULTS_MESSAGE = By.xpath("//*[contains(text(), 'no results') or contains(text(), 'No results')]");
    
    public ECommerceSearchResultsPage(WebDriver driver) {
        super(driver);
    }
    
    public List<WebElement> getSearchResults() {
        return findElements(SEARCH_RESULTS);
    }
    
    public boolean hasResults() {
        return !getSearchResults().isEmpty();
    }
    
    public boolean containsProduct(String productName) {
        List<WebElement> productNames = findElements(PRODUCT_NAMES);
        return productNames.stream()
                .anyMatch(element -> element.getText().toLowerCase().contains(productName.toLowerCase()));
    }
    
    @Override
    public boolean isPageLoaded() {
        return getCurrentUrl().contains("search") || hasResults() || isElementDisplayed(NO_RESULTS_MESSAGE);
    }
}
