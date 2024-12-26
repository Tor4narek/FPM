package com.example.financialmanager.view;

import com.example.financialmanager.controller.FinancialTaskController;
import com.example.financialmanager.model.FinancialTask;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExpenseAnalysisView extends JFrame {

    private final FinancialTaskController taskController;

    private JFreeChart chart;
    private ChartPanel chartPanel;
    private DefaultPieDataset dataset;

    private JLabel totalExpensesLabel;
    private JSpinner startDateSpinner;
    private JSpinner endDateSpinner;

    public ExpenseAnalysisView(FinancialTaskController taskController) {
        this.taskController = taskController;

        setTitle("Анализ расходов");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Верхняя панель для общего расхода и дат
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        totalExpensesLabel = new JLabel("Общие расходы: 0", JLabel.CENTER);
        totalExpensesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(totalExpensesLabel);

        JPanel datePanel = new JPanel(new FlowLayout());
        startDateSpinner = createDateSpinner();
        endDateSpinner = createDateSpinner();
        JButton updateButton = new JButton("Обновить");
        updateButton.addActionListener(e -> updateChart());

        datePanel.add(new JLabel("С:"));
        datePanel.add(startDateSpinner);
        datePanel.add(new JLabel("По:"));
        datePanel.add(endDateSpinner);
        datePanel.add(updateButton);

        topPanel.add(datePanel);

        // Инициализация диаграммы
        dataset = new DefaultPieDataset();
        chart = ChartFactory.createPieChart("Анализ расходов по категориям", dataset, true, true, false);
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(780, 400));

        // Добавление компонентов в окно
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(chartPanel, BorderLayout.CENTER);

        updateChart(); // Первоначальная загрузка данных
    }

    // Создание спиннера для выбора даты
    private JSpinner createDateSpinner() {
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(editor);
        dateSpinner.setValue(java.sql.Date.valueOf(LocalDate.now())); // Установить сегодняшнюю дату
        return dateSpinner;
    }

    // Обновление диаграммы
    private void updateChart() {
        try {
            LocalDate startDate = LocalDate.parse(((JSpinner.DateEditor) startDateSpinner.getEditor()).getFormat().format(startDateSpinner.getValue()), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate endDate = LocalDate.parse(((JSpinner.DateEditor) endDateSpinner.getEditor()).getFormat().format(endDateSpinner.getValue()), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            List<FinancialTask> tasks = taskController.getAllTasks();
            Map<String, Double> categoryExpenses = tasks.stream()
                    .filter(task -> task.getType().equalsIgnoreCase("Расход"))
                    .filter(task -> {
                        LocalDate taskDate = LocalDate.parse(task.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        return !taskDate.isBefore(startDate) && !taskDate.isAfter(endDate);
                    })
                    .collect(Collectors.groupingBy(FinancialTask::getCategory, Collectors.summingDouble(FinancialTask::getAmount)));

            // Обновляем данные диаграммы
            dataset.clear();
            double totalExpenses = 0;
            for (Map.Entry<String, Double> entry : categoryExpenses.entrySet()) {
                dataset.setValue(entry.getKey(), entry.getValue());
                totalExpenses += entry.getValue();
            }

            totalExpensesLabel.setText("Общие расходы: " + totalExpenses);

            // Перерисовываем диаграмму
            chartPanel.repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ошибка при обновлении данных: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
}
