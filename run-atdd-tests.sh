#!/bin/bash

# ATDD Test Execution Script for LambdaTest
# Provides convenient ways to run Acceptance Test Driven Development scenarios

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Function to check prerequisites
check_prerequisites() {
    print_status "Checking prerequisites..."
    
    # Check Java
    if ! command -v java &> /dev/null; then
        print_error "Java is not installed or not in PATH"
        exit 1
    fi
    
    java_version=$(java -version 2>&1 | head -n 1)
    print_status "Java version: $java_version"
    
    # Check Maven
    if ! command -v mvn &> /dev/null; then
        print_error "Maven is not installed or not in PATH"
        exit 1
    fi
    
    mvn_version=$(mvn -version | head -n 1)
    print_status "Maven version: $mvn_version"
    
    # Check LambdaTest credentials
    if [[ -z "$LT_USERNAME" ]] || [[ -z "$LT_ACCESS_KEY" ]]; then
        print_error "LambdaTest credentials not found!"
        print_error "Please set LT_USERNAME and LT_ACCESS_KEY environment variables"
        print_error "Example:"
        print_error "  export LT_USERNAME='your_username'"
        print_error "  export LT_ACCESS_KEY='your_access_key'"
        exit 1
    fi
    
    print_success "Prerequisites check passed"
}

# Function to clean previous test artifacts
clean_previous_runs() {
    print_status "Cleaning previous test artifacts..."
    mvn clean -q
    print_success "Cleanup completed"
}

# Function to compile the project
compile_project() {
    print_status "Compiling project..."
    mvn compile test-compile -q
    print_success "Project compiled successfully"
}

# Function to run tests with specific tags
run_tests_with_tags() {
    local tags=$1
    local description=$2
    
    print_status "Running $description tests..."
    mvn verify -Dcucumber.filter.tags="$tags"
    print_success "$description tests completed"
}

# Function to run all ATDD tests
run_all_tests() {
    print_status "Running all ATDD tests..."
    mvn verify
    print_success "All ATDD tests completed"
}

# Function to run specific feature file
run_feature_file() {
    local feature_file=$1
    
    print_status "Running feature file: $feature_file"
    mvn verify -Dcucumber.features="$feature_file"
    print_success "Feature file execution completed"
}

# Function to generate reports
generate_reports() {
    print_status "Generating test reports..."
    
    if [[ -d "target/cucumber-reports" ]]; then
        print_status "Test reports generated:"
        echo "  📊 Cucumber HTML: target/cucumber-reports/overview-features.html"
        
        if [[ -d "target/cucumber-html-reports" ]]; then
            echo "  📈 Advanced Reports: target/cucumber-html-reports/overview-features.html"
        fi
        
        if [[ -f "target/cucumber-reports/Cucumber.json" ]]; then
            echo "  📋 JSON Report: target/cucumber-reports/Cucumber.json"
        fi
        
        print_status "LambdaTest Dashboard: https://automation.lambdatest.com/build"
    else
        print_warning "No test reports found. Tests may not have run successfully."
    fi
}

# Function to show usage
show_usage() {
    echo "Usage: $0 [OPTION] [ARGS]"
    echo ""
    echo "ATDD Test Execution Script for LambdaTest"
    echo ""
    echo "Options:"
    echo "  all                    Run all E-Commerce ATDD tests (default)"
    echo "  smoke                  Run smoke tests only (@smoke tag)"
    echo "  regression             Run regression tests only (@regression tag)"
    echo "  critical               Run critical tests only (@critical tag)"
    echo "  feature <file>         Run specific feature file"
    echo "  tags <tag_expression>  Run tests with custom tag expression"
    echo "  clean                  Clean previous test artifacts only"
    echo "  help                   Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0                                    # Run all e-commerce tests"
    echo "  $0 smoke                             # Run smoke tests"
    echo "  $0 critical                          # Run critical tests"
    echo "  $0 feature src/test/resources/features/ecommerce/product-search.feature"
    echo "  $0 tags \"@smoke and @critical\"       # Custom tag expression"
    echo ""
    echo "Environment Variables Required:"
    echo "  LT_USERNAME          Your LambdaTest username"
    echo "  LT_ACCESS_KEY        Your LambdaTest access key"
    echo ""
    echo "Setup Example:"
    echo "  export LT_USERNAME='your_username'"
    echo "  export LT_ACCESS_KEY='your_access_key'"
    echo "  $0 smoke"
}

# Main execution logic
main() {
    local action=${1:-all}
    
    case $action in
        "help"|"-h"|"--help")
            show_usage
            exit 0
            ;;
        "clean")
            clean_previous_runs
            exit 0
            ;;
        "all"|"")
            check_prerequisites
            clean_previous_runs
            compile_project
            run_all_tests
            generate_reports
            ;;
        "smoke")
            check_prerequisites
            clean_previous_runs
            compile_project
            run_tests_with_tags "@smoke" "smoke"
            generate_reports
            ;;
        "regression")
            check_prerequisites
            clean_previous_runs
            compile_project
            run_tests_with_tags "@regression" "regression"
            generate_reports
            ;;


        "critical")
            check_prerequisites
            clean_previous_runs
            compile_project
            run_tests_with_tags "@critical" "critical"
            generate_reports
            ;;
        "feature")
            if [[ -z "$2" ]]; then
                print_error "Feature file path is required"
                echo "Usage: $0 feature <feature_file_path>"
                exit 1
            fi
            check_prerequisites
            clean_previous_runs
            compile_project
            run_feature_file "$2"
            generate_reports
            ;;
        "tags")
            if [[ -z "$2" ]]; then
                print_error "Tag expression is required"
                echo "Usage: $0 tags \"<tag_expression>\""
                exit 1
            fi
            check_prerequisites
            clean_previous_runs
            compile_project
            run_tests_with_tags "$2" "custom tag"
            generate_reports
            ;;
        *)
            print_error "Unknown option: $action"
            show_usage
            exit 1
            ;;
    esac
}

# Script header
echo "=============================================="
echo "  🧪 ATDD E-Commerce Test Suite"
echo "  🎯 LambdaTest E-Commerce Playground ATDD"
echo "=============================================="
echo ""

# Execute main function with all arguments
main "$@"

print_success "Script execution completed!"
echo ""
print_status "📋 Next Steps:"
echo "  1. 📊 Check test reports in target/cucumber-reports/"
echo "  2. 🌐 View detailed results in LambdaTest dashboard"
echo "  3. 🔍 Review any failed scenarios and update code"
echo "  4. 📚 Update documentation based on test results"
echo ""
