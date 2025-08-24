package com.lambdatest.atdd.hooks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lambdatest.atdd.config.TestConfiguration;
import com.lambdatest.atdd.context.TestContext;
import com.lambdatest.atdd.core.WebDriverFactory;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

/**
 * TestHooks class for Cucumber scenario lifecycle management
 */
public class TestHooks {
    private static final Logger logger = LoggerFactory.getLogger(TestHooks.class);
    private final TestContext testContext;
    
    public TestHooks() {
        this.testContext = TestContext.getInstance();
    }
    
    @Before
    public void setUp(Scenario scenario) {
        logger.info("=== Starting Scenario: {} ===", scenario.getName());
        
        // Verify configuration
        if (!TestConfiguration.isConfigurationInValid()) {
            logger.warn("WARNING: LambdaTest credentials not configured!");
            logger.warn("Please set LT_USERNAME and LT_ACCESS_KEY environment variables");
        }
        
        // Clear any previous test data
        testContext.clearTestData();
        
        // Store enhanced scenario information for better LambdaTest labeling
        String scenarioName = scenario.getName();
        String tags = scenario.getSourceTagNames().toString();
        String featureName = scenario.getUri().toString().replaceAll(".*/([^/]+)\\.feature", "$1");
        
        testContext.setTestData("scenarioName", scenarioName);
        testContext.setTestData("scenarioTags", tags);
        testContext.setTestData("featureName", featureName);
        testContext.setTestData("fullTestName", featureName + " - " + scenarioName);
        
        // Store test identifiers for better tracking
        testContext.setTestData("testId", System.currentTimeMillis());
        testContext.setTestData("testStartTime", System.currentTimeMillis());
        
        logger.info("Feature: {}", featureName);
        logger.info("Tags: {}", tags);
    }
    
    @After
    public void tearDown(Scenario scenario) {
        logger.info("=== Finishing Scenario: {} ===", scenario.getName());
        logger.info("Scenario Status: {}", scenario.getStatus());
        
        try {
            // Update test status in LambdaTest
            if (WebDriverFactory.hasActiveDriver()) {
                if (scenario.isFailed()) {
                    WebDriverFactory.markTestFailed("Scenario failed: " + scenario.getName());
                    logger.error("Scenario '{}' FAILED!", scenario.getName());
                } else {
                    WebDriverFactory.markTestPassed();
                    logger.info("Scenario '{}' PASSED!", scenario.getName());
                }
                
                // Print session information
                String sessionId = WebDriverFactory.getSessionId();
                logger.info("LambdaTest Session ID: {}", sessionId);
                logger.info("Dashboard: https://automation.lambdatest.com/build");
            }
            
        } catch (Exception e) {
            logger.error("Error during test teardown: {}", e.getMessage(), e);
        } finally {
            // Always clean up
            testContext.cleanup();
        }
        
        logger.info("==========================================");
    }
}
