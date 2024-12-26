package com.example.financialmanager.view;

import com.example.financialmanager.controller.GoalController;
import com.example.financialmanager.controller.FinancialTaskController;
import com.example.financialmanager.model.Goal;
import com.example.financialmanager.model.FinancialTask;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class GoalDetailView extends JFrame {

    private final Goal goal;
    private final GoalController goalController; // Используем контроллер для целей
    private final FinancialTaskController taskController; // Используем контроллер для финансовых задач
    private final MainView mainView; // Ссылка на MainView

    private JButton addMoneyButton;
    private JButton backButton;  // Кнопка "Назад"

    public GoalDetailView(Goal goal, MainView mainView) {
        this.goal = goal;
        this.goalController = new GoalController();
        this.taskController = new FinancialTaskController();
        this.mainView = mainView;

        setTitle("Детали цели: " + goal.getName());
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Инициализация диаграммы
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Накоплено", goal.getCurrentAmount());
        dataset.setValue("Осталось", goal.getTargetAmount() - goal.getCurrentAmount());

        JFreeChart chart = ChartFactory.createPieChart(
                "Прогресс по цели",
                dataset,
                true, true, false
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 300));
        add(chartPanel, BorderLayout.CENTER);

        // Кнопка для добавления денег
        addMoneyButton = new JButton("Добавить деньги");
        addMoneyButton.addActionListener(this::addMoney);

        // Кнопка "Назад"
        backButton = new JButton("Назад");
        backButton.addActionListener(e -> closeWindow());

        // Панель с кнопками
        JPanel panel = new JPanel();
        panel.add(addMoneyButton);
        panel.add(backButton);  // Добавляем кнопку "Назад"
        add(panel, BorderLayout.SOUTH);
    }

    // Добавление денег на цель
    private void addMoney(ActionEvent e) {
        String amountStr = JOptionPane.showInputDialog(this, "Введите сумму:");
        try {
            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Сумма должна быть положительной", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Уменьшаем общий баланс
            double balance = mainView.getCurrentBalance();  // Получаем текущий баланс из MainView
            if (balance < amount) {
                JOptionPane.showMessageDialog(this, "Недостаточно средств", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Уменьшаем баланс
            mainView.setCurrentBalance(balance - amount);

            // Добавляем операцию
            FinancialTask task = new FinancialTask();
            task.setAmount(amount);
            task.setCategory(goal.getName() + " Копилка");
            LocalDate today = LocalDate.now();

// Форматируем дату в формате yyyy-MM-dd
            String formattedDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

// Устанавливаем дату в задаче
            task.setDate(formattedDate);
            task.setType("Расход");

            taskController.addTask(task);

            // Обновляем цель
            goal.setCurrentAmount(goal.getCurrentAmount() + amount);
            goalController.updateGoal(goal.getName(), goal.getName(),goal.getCurrentAmount(), goal.getTargetAmount());
            // Обновляем диаграмму
            updateProgress();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ошибка ввода данных", "Ошибка", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    // Обновление диаграммы
    private void updateProgress() {
        // Создаем новый набор данных для диаграммы
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Накоплено", goal.getCurrentAmount());
        dataset.setValue("Осталось", goal.getTargetAmount() - goal.getCurrentAmount());

        // Создаем новую диаграмму
        JFreeChart chart = ChartFactory.createPieChart(
                "Прогресс по цели",
                dataset,
                true, true, false
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 300));

        // Удаляем все компоненты из окна
        getContentPane().removeAll();

        // Добавляем диаграмму обратно
        add(chartPanel, BorderLayout.CENTER);

        // Создаем новую панель для кнопок
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addMoneyButton);
        buttonPanel.add(backButton);

        // Добавляем панель с кнопками в южную часть окна
        add(buttonPanel, BorderLayout.SOUTH);

        // Обновляем отображение окна
        revalidate();
        repaint();
    }

    // Закрытие окна
    private void closeWindow() {
        dispose();
    }
}
