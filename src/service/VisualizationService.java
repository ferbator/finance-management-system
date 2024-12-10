package service;

import chart.BarChartPanel;
import model.Wallet;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class VisualizationService {
    public static void displaySummary(Wallet wallet) {
        System.out.println("=== Финансовый отчет ===");
        System.out.println("Общий доход: " + wallet.getTotalIncome());
        System.out.println("Доходы по категориям:");
        Map<String, Double> incomeByCategory = wallet.getIncomeByCategory();
        for (String category : incomeByCategory.keySet()) {
            System.out.printf("\t%s: %.2f%n", category, incomeByCategory.get(category));
        }

        System.out.println("\nОбщие расходы: " + Math.abs(wallet.getTotalExpenses()));

        System.out.println("Бюджет по категориям:");
        for (String category : wallet.getBudgets().keySet()) {
            double remaining = wallet.getRemainingBudget(category);
            System.out.printf("\t%s: %.2f, Оставшийся бюджет: %.2f%n", category, wallet.getBudgets().get(category), remaining);
        }
    }

    public static void visualizeData(Wallet wallet) {
        // Создание окна для визуализации
        JFrame frame = new JFrame("Визуализация данных");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);

        // Создаём панели для доходов и расходов
        Map<String, Double> incomeByCategory = wallet.getIncomeByCategory();
        Map<String, Double> expenseByCategory = wallet.getExpenseByCategory();
        double maxValue = wallet.calculateMaxValue(incomeByCategory, expenseByCategory);
        JPanel incomePanel = new BarChartPanel(incomeByCategory, "Доходы", maxValue);
        JPanel expensePanel = new BarChartPanel(expenseByCategory, "Расходы", maxValue);

        // Размещаем панели в окне
        frame.setLayout(new GridLayout(1, 2));
        frame.add(incomePanel);
        frame.add(expensePanel);

        frame.setVisible(true);
    }
}
