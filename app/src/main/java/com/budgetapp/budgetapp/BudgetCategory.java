package com.budgetapp.budgetapp;

/**
 * Created by Tiffany on 1/19/16.
 */
public class BudgetCategory {
    private String name;
    private double expected;

    public BudgetCategory(String categoryName, double expectedAmount) {
        name = categoryName;
        expected = expectedAmount;
    }

    public String getCategoryName() {
        return name;
    }
    public double getExpected() {
        return expected;
    }
}
