package com.example.financialmanager.view;

import com.example.financialmanager.controller.FinancialTaskController;
import com.example.financialmanager.controller.CategoryController;
import com.example.financialmanager.model.FinancialTask;
import com.example.financialmanager.model.Category;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;

public class FinancialOperationsView extends JFrame {

    private final FinancialTaskController taskController;  // Используем контроллер
    private final CategoryController categoryController;    // Используем контроллер
    private final MainView mainView;

    private JList<String> operationList;
    private DefaultListModel<String> listModel;
    private JButton backButton;
    private JButton addButton;

    public FinancialOperationsView(MainView mainView) {
        this.mainView = mainView;
        taskController = new FinancialTaskController();  // Инициализация контроллера для задач
        categoryController = new CategoryController();  // Инициализация контроллера для категорий

        setTitle("Финансовые операции");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Инициализация компонентов
        listModel = new DefaultListModel<>();
        operationList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(operationList);
        scrollPane.setPreferredSize(new Dimension(580, 250));

        addButton = new JButton("Добавить операцию");
        backButton = new JButton("Назад");

        addButton.addActionListener(e -> openAddOperationDialog());
        backButton.addActionListener(e -> goBackToMainScreen());

        // Размещение компонентов на экране
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        JPanel panel = new JPanel();
        panel.add(addButton);
        panel.add(backButton);
        add(panel, BorderLayout.SOUTH);

        loadOperations();
    }

    // Загружаем список операций
    private void loadOperations() {
        try {
            List<FinancialTask> tasks = taskController.getAllTasks(); // Используем контроллер
            listModel.clear();
            for (FinancialTask task : tasks) {
                listModel.addElement(task.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Открытие окна добавления операции
    private void openAddOperationDialog() {
        JDialog dialog = new JDialog(this, "Добавить операцию", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Поля для ввода
        JLabel amountLabel = new JLabel("Сумма:");
        JTextField amountField = new JTextField();

        JLabel dateLabel = new JLabel("Дата:");
        JTextField dateField = new JTextField(java.time.LocalDate.now().toString());

        JLabel categoryLabel = new JLabel("Категория:");
        JTextField categoryField = new JTextField();
        JButton categoryButton = new JButton("Выбрать категорию");

        JLabel typeLabel = new JLabel("Тип:");
        JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"Доход", "Расход"});

        JButton saveButton = new JButton("Сохранить");

        // Установка расположения компонентов
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(amountLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(amountField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(dateLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(dateField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(categoryLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(categoryField, gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        panel.add(categoryButton, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(typeLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        panel.add(typeComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(saveButton, gbc);

        // Действия кнопок
        categoryButton.addActionListener(e -> openCategorySelectionDialog(categoryField));
        saveButton.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Сумма должна быть положительным числом больше 0", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String date = dateField.getText();
                String category = categoryField.getText();
                String type = (String) typeComboBox.getSelectedItem();

                FinancialTask task = new FinancialTask();
                task.setAmount(amount);
                task.setDate(date);
                task.setCategory(category);
                task.setType(type);

                taskController.addTask(task);
                loadOperations();
                dialog.dispose();
            } catch (NumberFormatException | IOException ex) {
                JOptionPane.showMessageDialog(dialog, "Ошибка ввода данных", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(panel);
        dialog.setVisible(true);
    }


    // Открытие окна выбора категории
    private void openCategorySelectionDialog(JTextField categoryField) {
        JDialog dialog = new JDialog(this, "Выберите категорию", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        DefaultListModel<String> categoryListModel = new DefaultListModel<>();
        JList<String> categoryList = new JList<>(categoryListModel);
        JScrollPane scrollPane = new JScrollPane(categoryList);

        try {
            List<Category> categories = categoryController.getAllCategories(); // Получаем категории через контроллер
            for (Category category : categories) {
                categoryListModel.addElement(category.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JButton addCategoryButton = new JButton("Добавить новую категорию");
        JButton selectButton = new JButton("Выбрать");

        addCategoryButton.addActionListener(e -> openAddCategoryDialog(dialog));
        selectButton.addActionListener(e -> {
            String selectedCategory = categoryList.getSelectedValue();
            if (selectedCategory != null) {
                categoryField.setText(selectedCategory);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Выберите категорию", "Ошибка", JOptionPane.WARNING_MESSAGE);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addCategoryButton);
        buttonPanel.add(selectButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    // Открытие окна добавления новой категории
    private void openAddCategoryDialog(JDialog parentDialog) {
        JDialog addCategoryDialog = new JDialog(parentDialog, "Добавить новую категорию", true);
        addCategoryDialog.setSize(400, 200);
        addCategoryDialog.setLocationRelativeTo(parentDialog);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Поля для ввода
        JLabel nameLabel = new JLabel("Название категории:");
        JTextField categoryNameField = new JTextField();

        JLabel descriptionLabel = new JLabel("Описание:");
        JTextField categoryDescriptionField = new JTextField();

        JButton saveButton = new JButton("Сохранить");

        // Установка расположения компонентов
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(nameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(categoryNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(descriptionLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(categoryDescriptionField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(saveButton, gbc);

        // Действия кнопки
        saveButton.addActionListener(e -> {
            try {
                String categoryName = categoryNameField.getText();
                String categoryDescription = categoryDescriptionField.getText();
                categoryController.addCategory(categoryName, categoryDescription);
                JOptionPane.showMessageDialog(addCategoryDialog, "Категория добавлена", "Успех", JOptionPane.INFORMATION_MESSAGE);
                addCategoryDialog.dispose();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(addCategoryDialog, "Ошибка при добавлении категории", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        addCategoryDialog.add(panel);
        addCategoryDialog.setVisible(true);
    }


    // Переход назад на главный экран
    private void goBackToMainScreen() {
        this.setVisible(false);
        mainView.updateBalance();
        mainView.setVisible(true);
    }
}
