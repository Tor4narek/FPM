package com.example.financialmanager.model;

public class FinancialTask implements IFinancialTask {
    private double amount;
    private String date;
    private String category;
    private String type; // "Income" или "Expense"

    // Getters and Setters
    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String getDate() {
        return date;
    }

    @Override
    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format(
                "Сумма: %.2f, Дата: %s, Категория: %s, Тип: %s",
                amount, date, category, type
        );
    }

}
