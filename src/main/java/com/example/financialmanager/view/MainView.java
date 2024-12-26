package com.example.financialmanager.view;

import com.example.financialmanager.controller.FinancialTaskController;
import com.example.financialmanager.model.FinancialTask;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class MainView extends JFrame {

    private JButton financialOperationsButton;
    private JButton financialAnalysisButton;
    private JButton financialGoalsButton;
    private JLabel balanceLabel;

    private final FinancialTaskController taskController; // Используем контроллер для финансовых задач

    private double currentBalance; // Баланс хранится в этом поле

    public MainView() {
        taskController = new FinancialTaskController(); // Инициализация контроллера
        currentBalance = 0.0; // Изначально баланс равен 0.0

        setTitle("Главный экран");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Создаем компоненты
        financialOperationsButton = new JButton("Финансовые операции");
        financialAnalysisButton = new JButton("Анализ расходов");
        financialGoalsButton = new JButton("Финансовые цели");
        balanceLabel = new JLabel("Текущий баланс: 0.00 руб.", JLabel.CENTER);

        // Добавляем обработчик событий для кнопок
        financialOperationsButton.addActionListener(e -> openFinancialOperationsView());
        financialGoalsButton.addActionListener(e -> openFinancialGoalsView());
        financialAnalysisButton.addActionListener(e -> openExpenseAnalysisView());
        // Размещение компонентов на экране
        setLayout(new GridLayout(5, 1));
        add(balanceLabel);
        add(financialOperationsButton);
        add(financialAnalysisButton);
        add(financialGoalsButton);

        updateBalance(); // Обновляем баланс при запуске
    }

    private void openFinancialOperationsView() {
        // Скрываем главное окно
        this.setVisible(false);

        // Открываем окно финансовых операций
        FinancialOperationsView financialOperationsView = new FinancialOperationsView(this);
        financialOperationsView.setVisible(true);
    }
    private void openExpenseAnalysisView() {
        ExpenseAnalysisView expenseAnalysisView = new ExpenseAnalysisView(taskController);
        expenseAnalysisView.setVisible(true);
    }
    private void openFinancialGoalsView() {
        // Скрываем главное окно
        this.setVisible(false);

        // Открываем окно финансовых целей
        GoalsView financialGoalsView = new GoalsView(this);
        financialGoalsView.setVisible(true);
    }

    public void updateBalance() {
        try {
            double balance = taskController.calculateBalance(); // Получаем баланс с помощью контроллера
            this.currentBalance = balance;
            balanceLabel.setText("Текущий баланс: " + balance + " руб.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для получения текущего баланса
    public double getCurrentBalance() {
        return currentBalance;
    }

    // Метод для обновления текущего баланса
    public void setCurrentBalance(double balance) {
        this.currentBalance = balance;
        updateBalance(); // Обновляем баланс в интерфейсе
    }
}
