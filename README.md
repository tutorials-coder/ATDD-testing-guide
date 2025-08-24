# ATDD Guide with LambdaTest E-Commerce

This project demonstrates **Acceptance Test Driven Development (ATDD)** using Cucumber framework with LambdaTest cloud infrastructure for testing the LambdaTest E-Commerce Playground.

## Table of Contents
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Setup](#setup)
- [Project Structure](#project-structure)
- [Running Tests](#running-tests)
- [Test Scenarios](#test-scenarios)
- [Configuration](#configuration)

## Features
- **Parallel test execution** (3 concurrent threads)
- **Professional SLF4J logging** with Logback configuration
- **Enhanced LambdaTest integration** with automatic status reporting
- **Unique test naming** for easy dashboard identification
- **Robust error handling** for real-world scenarios (out-of-stock products)
- **Page Object Model** with reusable components

## Prerequisites
- Java JDK 21 or higher
- Maven 3.6+
- LambdaTest Account
  - Sign up at [LambdaTest](https://www.lambdatest.com/signup)
  - Get credentials from [LambdaTest Security Settings](https://accounts.lambdatest.com/security/username-accesskey)

## Setup

1. **Navigate to ATDD module:**
   ```bash
   cd atdd-guide
   ```

2. **Set LambdaTest credentials (choose one method):**
   
   **Option A: Environment Variables**
   ```bash
   export LT_USERNAME=your_username
   export LT_ACCESS_KEY=your_access_key
   ```
   
   **Option B: Maven System Properties (Recommended)**
   ```bash
   # Pass credentials directly with Maven command
   mvn verify -DLT_USERNAME=your_username -DLT_ACCESS_KEY=your_access_key
   ```

3. **Install dependencies:**
   ```bash
   mvn clean install
   ```

## Project Structure
```
atdd-guide/
├── src/main/java/com/lambdatest/atdd/
│   ├── config/         # Configuration classes
│   ├── core/           # WebDriver factory & utilities
│   └── pages/          # Page Object Model classes
├── src/test/java/com/lambdatest/atdd/
│   ├── context/        # Test context management
│   ├── hooks/          # Cucumber hooks
│   ├── runners/        # Test runners
│   └── steps/          # Step definitions
└── src/test/resources/features/ecommerce/
    ├── product-search.feature
    └── shopping-cart.feature
```

## Running Tests

### **Parallel Execution (Recommended)**
```bash
# Run all tests (parallel) with credentials
mvn clean verify -DLT_USERNAME=your_username -DLT_ACCESS_KEY=your_access_key

# Run specific test suites
mvn verify -DLT_USERNAME=your_username -DLT_ACCESS_KEY=your_access_key -Dcucumber.filter.tags="@smoke"
mvn verify -DLT_USERNAME=your_username -DLT_ACCESS_KEY=your_access_key -Dcucumber.filter.tags="@critical"
mvn verify -DLT_USERNAME=your_username -DLT_ACCESS_KEY=your_access_key -Dcucumber.filter.tags="@regression"
```

### **Sequential Execution (For Debugging)**
```bash
# Using shell script
./run-atdd-tests.sh smoke

# Direct Cucumber CLI
java -cp "target/test-classes:target/classes:$(mvn dependency:build-classpath -q -Dmdep.outputFile=/dev/stdout)" io.cucumber.core.cli.Main --glue com.lambdatest.atdd --plugin pretty src/test/resources/features/ecommerce --tags "@ecommerce"
```

## Test Scenarios

### **Product Search** (`@ecommerce @smoke`)
- Successful product search (iMac, HTC, Canon, Samsung, HP)
- Empty search handling
- No results scenarios

### **Shopping Cart** (`@ecommerce @critical`)
- Add product to cart with out-of-stock handling
- Cart functionality verification
- JavaScript click fallback for complex interactions

### **Test Results**
```
8 Scenarios (8 passed)
31 Steps (31 passed)
1m49s (parallel execution)

2025-08-25 05:11:29.253 [ForkJoinPool-1-worker-1] INFO  c.l.atdd.core.WebDriverFactory - LambdaTest: Test marked as PASSED
2025-08-25 05:11:29.253 [ForkJoinPool-1-worker-1] INFO  c.l.atdd.core.WebDriverFactory - LambdaTest Session: 41dbe58994649d3a0edf4411e52c088f
2025-08-25 05:11:29.253 [ForkJoinPool-1-worker-1] INFO  c.l.atdd.core.WebDriverFactory - Dashboard: https://automation.lambdatest.com/build
```

## Configuration

### **Parallel Execution** (`cucumber.properties`)
```properties
cucumber.execution.parallel.enabled=true
cucumber.execution.parallel.config.fixed.parallelism=3
```

### **Maven Configuration** (`pom.xml`)
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-failsafe-plugin</artifactId>
    <configuration>
        <parallel>methods</parallel>
        <threadCount>3</threadCount>
    </configuration>
</plugin>
```

### **Professional Logging** (`logback-test.xml`)
```xml
<appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
        <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
    </encoder>
</appender>
```

### **LambdaTest Status Reporting**
Tests automatically report PASSED/FAILED status to LambdaTest dashboard with session tracking and direct links.

---

**This ATDD implementation demonstrates production-ready acceptance testing with parallel execution and comprehensive cloud integration.**