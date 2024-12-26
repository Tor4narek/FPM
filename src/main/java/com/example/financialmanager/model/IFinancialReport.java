package com.example.financialmanager.model;

import java.util.Map;

public interface IFinancialReport {
    Map<String, Double> getExpensesByCategory();
    void setExpensesByCategory(Map<String, Double> expensesByCategory);

    double getTotalIncome();
    void setTotalIncome(double totalIncome);

    double getTotalExpense();
    void setTotalExpense(double totalExpense);
}
