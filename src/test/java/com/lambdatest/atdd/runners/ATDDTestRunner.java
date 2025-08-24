package com.lambdatest.atdd.runners;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.FEATURES_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

/**
 * Main ATDD Test Runner for E-Commerce Playground
 * Executes E-Commerce Cucumber features for Acceptance Test Driven Development
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/ecommerce")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty," +
    "html:target/cucumber-reports/html," +
    "json:target/cucumber-reports/cucumber.json," +
    "junit:target/cucumber-reports/cucumber.xml")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.lambdatest.atdd")
@ConfigurationParameter(key = FEATURES_PROPERTY_NAME, value = "src/test/resources/features/ecommerce")
public class ATDDTestRunner {
    // JUnit 5 will automatically discover and run all Cucumber features
}
