package com.lambdatest.atdd.core;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lambdatest.atdd.config.TestConfiguration;

/**
 * Factory class for creating WebDriver instances
 * Handles LambdaTest cloud configuration and local driver setup
 */
public class WebDriverFactory {
    private static final Logger logger = LoggerFactory.getLogger(WebDriverFactory.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    
    /**
     * Creates a WebDriver instance for LambdaTest cloud execution
     * 
     * @param testName The name of the test being executed
     * @param browser The browser to use
     * @param browserVersion The browser version
     * @param platform The platform/OS to run on
     * @return WebDriver instance configured for LambdaTest
     */
    public static WebDriver createRemoteDriver(String testName, String browser, String browserVersion, String platform) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        
        // W3C Standard capabilities
        capabilities.setCapability("browserName", browser);
        capabilities.setCapability("browserVersion", browserVersion);
        capabilities.setCapability("platformName", platform);
        
        // LambdaTest specific capabilities (all go in LT:Options for W3C compliance)
        Map<String, Object> ltOptions = new HashMap<>();
        ltOptions.put("w3c", true);
        ltOptions.put("plugin", "java-cucumber");
        ltOptions.put("build", TestConfiguration.TestConfig.BUILD_NAME);
        ltOptions.put("name", testName);
        ltOptions.put("project", TestConfiguration.TestConfig.PROJECT_NAME);
        ltOptions.put("selenium_version", "4.15.0");
        
        // Performance and debugging options
        ltOptions.put("visual", true);
        ltOptions.put("video", true);
        ltOptions.put("network", true);
        ltOptions.put("console", true);
        ltOptions.put("terminal", true);
        
        // Timeouts and resolution
        ltOptions.put("idleTimeout", 300);
        ltOptions.put("resolution", TestConfiguration.TestConfig.RESOLUTION);
        
        capabilities.setCapability("LT:Options", ltOptions);
        
        try {
            WebDriver driver = new RemoteWebDriver(new URL(TestConfiguration.getGridUrl()), capabilities);
            
            // Set timeouts
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TestConfiguration.TestConfig.IMPLICIT_WAIT));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(TestConfiguration.TestConfig.PAGE_LOAD_TIMEOUT));
            driver.manage().window().maximize();
            
            driverThreadLocal.set(driver);
            return driver;
            
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid LambdaTest Grid URL: " + TestConfiguration.getGridUrl(), e);
        }
    }
    
    /**
     * Creates a WebDriver instance with default browser configuration and enhanced test naming
     * 
     * @param testName The name of the test being executed
     * @return WebDriver instance with default configuration
     */
    public static WebDriver createDefaultRemoteDriver(String testName) {
        return createRemoteDriver(
            testName, 
            TestConfiguration.Browser.CHROME, 
            TestConfiguration.Version.LATEST, 
            TestConfiguration.Platform.WINDOWS_10
        );
    }
    
    /**
     * Creates a WebDriver instance with enhanced naming for better LambdaTest dashboard tracking
     * 
     * @param featureName The feature being tested
     * @param scenarioName The scenario being executed
     * @param tags The test tags for categorization
     * @return WebDriver instance with detailed naming
     */
    public static WebDriver createNamedRemoteDriver(String featureName, String scenarioName, String tags) {
        String enhancedTestName = featureName + " | " + scenarioName;
        if (tags != null && !tags.isEmpty()) {
            enhancedTestName += " " + tags;
        }
        return createDefaultRemoteDriver(enhancedTestName);
    }
    
    /**
     * Gets the current WebDriver instance for the current thread
     * 
     * @return WebDriver instance
     */
    public static WebDriver getCurrentDriver() {
        return driverThreadLocal.get();
    }
    
    /**
     * Marks the test as passed in LambdaTest dashboard
     */
    public static void markTestPassed() {
        updateTestStatus(TestConfiguration.Status.PASSED, null);
    }
    
    /**
     * Marks the test as failed in LambdaTest dashboard
     * 
     * @param reason The reason for test failure
     */
    public static void markTestFailed(String reason) {
        updateTestStatus(TestConfiguration.Status.FAILED, reason);
    }
    
    /**
     * Updates the test status in LambdaTest dashboard
     * 
     * @param status The test status (passed/failed)
     * @param reason Optional reason for the status
     */
    private static void updateTestStatus(String status, String reason) {
        WebDriver driver = getCurrentDriver();
        if (driver instanceof RemoteWebDriver remoteDriver) {
            try {
                remoteDriver.executeScript("lambda-status=" + status);
                
                if (reason != null && TestConfiguration.Status.FAILED.equals(status)) {
                    remoteDriver.executeScript("lambda-exceptions", reason);
                    logger.warn("LambdaTest: Test marked as FAILED - {}", reason);
                } else if (TestConfiguration.Status.PASSED.equals(status)) {
                    logger.info("LambdaTest: Test marked as PASSED");
                }
                
                // Log session information for easy tracking
                String sessionId = getSessionId();
                if (sessionId != null) {
                    logger.info("LambdaTest Session: {}", sessionId);
                    logger.info("Dashboard: https://automation.lambdatest.com/build");
                }
            } catch (Exception e) {
                logger.error("Failed to update LambdaTest status: {}", e.getMessage(), e);
            }
        } else {
            logger.debug("Local execution - LambdaTest status not applicable");
        }
    }
    
    /**
     * Quits the current WebDriver instance and cleans up thread local variables
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                logger.error("Error while quitting driver: {}", e.getMessage(), e);
            } finally {
                driverThreadLocal.remove();
            }
        }
    }
    
    /**
     * Gets the current session ID for debugging purposes
     * 
     * @return Session ID string or "N/A" if not available
     */
    public static String getSessionId() {
        WebDriver driver = getCurrentDriver();
        if (driver instanceof RemoteWebDriver) {
            return ((RemoteWebDriver) driver).getSessionId().toString();
        }
        return "N/A";
    }
    
    /**
     * Checks if a WebDriver instance exists for the current thread
     * 
     * @return true if driver exists, false otherwise
     */
    public static boolean hasActiveDriver() {
        return driverThreadLocal.get() != null;
    }
}
