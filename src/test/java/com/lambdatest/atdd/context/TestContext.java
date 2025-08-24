package com.lambdatest.atdd.context;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;

import com.lambdatest.atdd.core.WebDriverFactory;
import com.lambdatest.atdd.pages.ecommerce.ECommerceHomePage;

/**
 * Test Context for sharing data and page objects between Cucumber steps
 * Implements Singleton pattern for thread-safe access
 */
public class TestContext {
    
    private static final ThreadLocal<TestContext> instance = new ThreadLocal<>();
    private final Map<String, Object> testData;
    private WebDriver driver;
    
    // Page Objects
    private ECommerceHomePage eCommerceHomePage;
    
    private TestContext() {
        this.testData = new HashMap<>();
    }
    
    public static TestContext getInstance() {
        if (instance.get() == null) {
            instance.set(new TestContext());
        }
        return instance.get();
    }
    
    public WebDriver getDriver() {
        if (driver == null) {
            driver = WebDriverFactory.getCurrentDriver();
        }
        return driver;
    }
    
    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }
    
    // Page Object getters with lazy initialization
    public ECommerceHomePage getECommerceHomePage() {
        if (eCommerceHomePage == null) {
            eCommerceHomePage = new ECommerceHomePage(getDriver());
        }
        return eCommerceHomePage;
    }
    
    // Test data management
    public void setTestData(String key, Object value) {
        testData.put(key, value);
    }
    
    public Object getTestData(String key) {
        return testData.get(key);
    }
    
    public String getTestDataAsString(String key) {
        Object value = testData.get(key);
        return value != null ? value.toString() : null;
    }
    
    public void clearTestData() {
        testData.clear();
    }
    
    public void cleanup() {
        if (driver != null) {
            WebDriverFactory.quitDriver();
            driver = null;
        }
        clearTestData();
        clearPageObjects();
    }
    
    private void clearPageObjects() {
        eCommerceHomePage = null;
    }
    
    public static void reset() {
        TestContext context = instance.get();
        if (context != null) {
            context.cleanup();
        }
        instance.remove();
    }
}
