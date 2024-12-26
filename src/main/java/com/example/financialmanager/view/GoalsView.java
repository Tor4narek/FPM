package com.example.financialmanager.view;

import com.example.financialmanager.controller.GoalController;
import com.example.financialmanager.model.Goal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;

public class GoalsView extends JFrame {

    private final GoalController goalController;
    private final MainView mainView;

    private JList<String> goalList;
    private DefaultListModel<String> listModel;
    private JButton addGoalButton;
    private JButton backButton;

    public GoalsView(MainView mainView) {
        this.mainView = mainView;
        this.goalController = new GoalController();

        setTitle("Финансовые цели");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Инициализация компонентов
        listModel = new DefaultListModel<>();
        goalList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(goalList);
        scrollPane.setPreferredSize(new Dimension(580, 250));

        addGoalButton = new JButton("Создать цель");
        addGoalButton.addActionListener(this::openAddGoalDialog);

        // Кнопка "Назад"
        backButton = new JButton("Назад");
        backButton.addActionListener(this::goToMainView);

        // Размещение компонентов на экране
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.add(addGoalButton);
        panel.add(backButton);  // Добавляем кнопку "Назад"
        add(panel, BorderLayout.SOUTH);

        goalList.addListSelectionListener(e -> openGoalDetailView(1));

        loadGoals();  // Загружаем цели при инициализации окна
    }

    // Загружаем список целей

    private void loadGoals() {
        try {
            List<Goal> goals = goalController.getAllGoals();
            listModel.clear();
            for (Goal goal : goals) {
                listModel.addElement(goal.getName());
            }

            // Удаляем предыдущие обработчики и добавляем новый
            for (javax.swing.event.ListSelectionListener listener : goalList.getListSelectionListeners()) {
                goalList.removeListSelectionListener(listener);
            }

            goalList.addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    int selectedIndex = goalList.getSelectedIndex();
                    if (selectedIndex >= 0) {
                        openGoalDetailView(selectedIndex);
                        // Сбрасываем выделение, чтобы позволить повторно выбрать ту же цель
                        goalList.clearSelection();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Открытие окна добавления новой цели
    private void openAddGoalDialog(ActionEvent e) {
        JDialog dialog = new JDialog(this, "Создать финансовую цель", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JTextField goalNameField = new JTextField();
        JTextField targetAmountField = new JTextField();

        JButton saveGoalButton = new JButton("Сохранить");

        saveGoalButton.addActionListener(event -> {
            try {
                String goalName = goalNameField.getText();
                double targetAmount = Double.parseDouble(targetAmountField.getText());

                goalController.addGoal(goalName, targetAmount);  // Создаем новую цель через контроллер
                loadGoals();
                dialog.dispose();
            } catch (NumberFormatException | IOException ex) {
                JOptionPane.showMessageDialog(dialog, "Ошибка ввода данных", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(new JLabel("Название цели:"));
        panel.add(goalNameField);
        panel.add(new JLabel("Целевая сумма:"));
        panel.add(targetAmountField);
        panel.add(new JLabel(""));
        panel.add(saveGoalButton);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    // Открытие окна подробностей цели
    private void openGoalDetailView(int index) {
        try {
            List<Goal> goals = goalController.getAllGoals();
            Goal selectedGoal = goals.get(index);
            GoalDetailView detailView = new GoalDetailView(selectedGoal, mainView);
            detailView.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Не удалось загрузить данные цели", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Переход на главный экран при нажатии на кнопку "Назад"
    private void goToMainView(ActionEvent e) {
        mainView.setVisible(true);  // Показываем главный экран
        this.dispose();  // Закрываем окно GoalsView
    }
}
