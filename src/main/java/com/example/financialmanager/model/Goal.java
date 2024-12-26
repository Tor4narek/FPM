package com.example.financialmanager.model;

public class Goal implements IGoal {
    private double targetAmount;
    private double currentAmount;
    private String name;

    // Getters and Setters
    @Override
    public double getTargetAmount() {
        return targetAmount;
    }

    @Override
    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
    }

    @Override
    public double getCurrentAmount() {
        return currentAmount;
    }

    @Override
    public void setCurrentAmount(double currentAmount) {
        this.currentAmount = currentAmount;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SavingGoal{" +
                "targetAmount=" + targetAmount +
                ", currentAmount=" + currentAmount +
                ", name='" + name + '\'' +
                '}';
    }
}
