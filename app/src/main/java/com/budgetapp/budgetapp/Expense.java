package com.budgetapp.budgetapp;

/**
 * Created by Tiffany on 1/19/16.
 */
public class Expense {
    private String name;
    private String category;
    private double amount;
    private String payment;

    public Expense(String expenseName, String categoryName, double cost, String paymentType) {
        name = expenseName;
        category = categoryName;
        amount = cost;
        payment = paymentType;
    }

    public String getName() {
        return name;
    }
    public String getCategory() {
        return category;
    }
    public double getAmount() {
        return amount;
    }
    public String getPayment() {
        return payment;
    }
}
