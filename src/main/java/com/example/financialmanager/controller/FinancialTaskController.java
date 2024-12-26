package com.example.financialmanager.controller;

import com.example.financialmanager.model.FinancialTask;
import com.example.financialmanager.storage.FinancialTaskStorage;

import java.io.IOException;
import java.util.List;

public class FinancialTaskController {
    private final FinancialTaskStorage financialTaskStorage;

    public FinancialTaskController() {
        this.financialTaskStorage = new FinancialTaskStorage();
    }

    // Получение всех финансовых задач
    public List<FinancialTask> getAllTasks() throws IOException {
        return financialTaskStorage.getAllTasks();
    }

    // Добавление новой финансовой задачи
    public void addTask(FinancialTask task) throws IOException {
        financialTaskStorage.addTask(task);
    }

    // Удаление финансовой задачи
    public void deleteTask(String taskName) throws IOException {
        financialTaskStorage.deleteTask(task -> task.getCategory().equals(taskName));
    }

    // Обновление финансовой задачи
    public void updateTask(String oldTaskName, FinancialTask updatedTask) throws IOException {
        financialTaskStorage.updateTask(task -> task.getCategory().equals(oldTaskName), updatedTask);
    }
    // Вычисление баланса
    public double calculateBalance() throws IOException {
        List<FinancialTask> tasks = financialTaskStorage.getAllTasks();
        double balance = 0.0;
        for (FinancialTask task : tasks) {
            if (task.getType().equals("Доход")) {
                balance += task.getAmount();
            } else if (task.getType().equals("Расход")) {
                balance -= task.getAmount();
            }
        }
        return balance;
    }
}
