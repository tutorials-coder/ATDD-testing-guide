@ecommerce @smoke
Feature: Product Search on LambdaTest E-Commerce Playground
  As a potential customer
  I want to search for products
  So that I can find items I'm interested in purchasing

  Background:
    Given I am on the LambdaTest E-Commerce homepage

  @critical
  Scenario: Successful Product Search
    When I search for "iMac"
    Then I should see search results
    And the results should contain products related to "iMac"

  @regression
  Scenario Outline: Search for <product_type> Products
    When I search for "<product_type>"
    Then I should see search results
    And the results should contain products related to "<product_type>"

    Examples:
      | product_type |
      | HTC         |
      | Canon       |
      | Samsung     |
      | HP          |

  @regression
  Scenario: Empty Search Handling
    When I search for ""
    Then I should see all available products

  @regression
  Scenario: Search with No Results
    When I search for "nonexistentproduct12345"
    Then I should see a "no results found" message
