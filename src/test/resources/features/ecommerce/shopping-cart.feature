@ecommerce @critical
Feature: Shopping Cart on LambdaTest E-Commerce Playground
  As a customer
  I want to add products to my shopping cart
  So that I can proceed with my purchase

  Background:
    Given I am on the LambdaTest E-Commerce homepage

  @smoke
  Scenario: Add Product to Cart
    When I click on the first "iMac" product
    And I click the "Add to Cart" button on product page
    Then the add to cart action should be executed
    And the cart functionality should work
