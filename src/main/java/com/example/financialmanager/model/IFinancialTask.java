package com.example.financialmanager.model;

public interface IFinancialTask {
    double getAmount();
    void setAmount(double amount);

    String getDate();
    void setDate(String date);

    String getCategory();
    void setCategory(String category);

    String getType(); // "Income" или "Expense"
    void setType(String type);
}
