package com.example.financialmanager.controller;

import com.example.financialmanager.model.Goal;
import com.example.financialmanager.storage.GoalStorage;

import java.io.IOException;
import java.util.List;

public class GoalController {
    private final GoalStorage goalStorage;

    public GoalController() {
        this.goalStorage = new GoalStorage();
    }

    // Получение всех целей
    public List<Goal> getAllGoals() throws IOException {
        return goalStorage.getAllGoals();
    }

    // Получение цели по названию
    public Goal getGoalByName(String goalName) throws IOException {
        List<Goal> goals = goalStorage.getAllGoals();
        for (Goal goal : goals) {
            if (goal.getName().equals(goalName)) {
                return goal;
            }
        }
        return null;  // Если цель не найдена
    }

    // Добавление новой цели
    public void addGoal(String name, double targetAmount) throws IOException {
        Goal goal = new Goal();
        goal.setName(name);
        goal.setTargetAmount(targetAmount);
        goal.setCurrentAmount(0); // Начальный баланс 0
        goalStorage.addGoal(goal);
    }

    // Удаление цели
    public void deleteGoal(String goalName) throws IOException {
        goalStorage.deleteGoal(goal -> goal.getName().equals(goalName));
    }

    // Обновление цели
    public void updateGoal(String oldGoalName, String newGoalName, double newCurrentAmount, double target) throws IOException {
        Goal updatedGoal = new Goal();
        updatedGoal.setName(newGoalName);
        updatedGoal.setTargetAmount(target);
        updatedGoal.setCurrentAmount(newCurrentAmount);
        goalStorage.updateGoal(goal -> goal.getName().equals(oldGoalName), updatedGoal);
    }
}
