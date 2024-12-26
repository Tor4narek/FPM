package com.example.financialmanager.storage;

import com.example.financialmanager.model.Goal;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

public class GoalStorage {
    private final FileStorage<Goal> fileStorage;

    public GoalStorage() {
        this.fileStorage = new FileStorage<Goal>("src/data/goals.json");
    }

    public List<Goal> getAllGoals() throws IOException {
        return fileStorage.getAll(Goal.class);
    }

    public void addGoal(Goal goal) throws IOException {
        fileStorage.add(goal);
    }

    public void deleteGoal(Predicate<Goal> predicate) throws IOException {
        fileStorage.delete(predicate);
    }

    public void updateGoal(Predicate<Goal> predicate, Goal updatedGoal) throws IOException {
        fileStorage.update(predicate, updatedGoal);
    }
}
