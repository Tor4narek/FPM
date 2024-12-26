package com.example.financialmanager.storage;

import com.example.financialmanager.model.FinancialTask;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

public class FinancialTaskStorage {
    private final FileStorage<FinancialTask> fileStorage;

    public FinancialTaskStorage() {
        this.fileStorage = new FileStorage<FinancialTask>("src/data/financial_tasks.json");
    }

    public List<FinancialTask> getAllTasks() throws IOException {
        return fileStorage.getAll(FinancialTask.class);
    }

    public void addTask(FinancialTask task) throws IOException {
        fileStorage.add(task);
    }

    public void deleteTask(Predicate<FinancialTask> predicate) throws IOException {
        fileStorage.delete(predicate);
    }

    public void updateTask(Predicate<FinancialTask> predicate, FinancialTask updatedTask) throws IOException {
        fileStorage.update(predicate, updatedTask);
    }
}
