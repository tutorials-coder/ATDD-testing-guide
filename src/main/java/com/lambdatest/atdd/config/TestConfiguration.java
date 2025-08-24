package com.lambdatest.atdd.config;

/**
 * Main configuration class for ATDD test execution
 * This contains all the configuration constants used across the application
 */
public class TestConfiguration {

    private TestConfiguration(){

    }
    // LambdaTest Configuration
    public static final String USERNAME = getConfigValue("LT_USERNAME");
    public static final String ACCESS_KEY = getConfigValue("LT_ACCESS_KEY");
    public static final String GRID_URL = "https://" + USERNAME + ":" + ACCESS_KEY + "@hub.lambdatest.com/wd/hub";
    
    /**
     * Gets configuration value from system properties first, then environment variables
     * This allows Maven system properties to override environment variables
     */
    private static String getConfigValue(String key) {
        String value = System.getProperty(key);
        if (value != null && !value.trim().isEmpty()) {
            return value;
        }
        return System.getenv(key);
    }
    

    // E-Commerce URLs
    public static final String ECOMMERCE_BASE_URL = "https://ecommerce-playground.lambdatest.io/";

    // Browser Configuration
    public static class Browser {
        public static final String CHROME = "chrome";
    }
    
    // Platform Configuration
    public static class Platform {
        public static final String WINDOWS_10 = "Windows 10";
    }
    
    // Version Configuration
    public static class Version {
        public static final String LATEST = "latest";
    }
    
    // Test Configuration
    public static class TestConfig {
        public static final int DEFAULT_TIMEOUT = 30;
        public static final int PAGE_LOAD_TIMEOUT = 60;
        public static final int IMPLICIT_WAIT = 10;
        public static final String BUILD_NAME = "ATDD Tests - " + 
            java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        public static final String PROJECT_NAME = "ATDD Guide Project";
        public static final String RESOLUTION = "1920x1080";
    }
    
    // Test Status
    public static class Status {
        public static final String PASSED = "passed";
        public static final String FAILED = "failed";
    }

    /**
     * Validates if required environment variables are set
     * @return true if credentials are properly configured
     */
    public static boolean isConfigurationInValid() {
        return USERNAME == null || USERNAME.isEmpty() ||
                ACCESS_KEY == null || ACCESS_KEY.isEmpty();
    }
    
    /**
     * Gets the complete LambdaTest Grid URL with credentials
     * @return formatted grid URL
     * @throws IllegalStateException if credentials are not configured
     */
    public static String getGridUrl() {
        if (isConfigurationInValid()) {
            throw new IllegalStateException("LambdaTest credentials not configured. Please set LT_USERNAME and LT_ACCESS_KEY environment variables.");
        }
        return GRID_URL;
    }
}
