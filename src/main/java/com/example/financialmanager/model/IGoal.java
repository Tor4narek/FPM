package com.example.financialmanager.model;

public interface IGoal {
    double getTargetAmount();
    void setTargetAmount(double targetAmount);

    double getCurrentAmount();
    void setCurrentAmount(double currentAmount);

    String getName();
    void setName(String name);
}
