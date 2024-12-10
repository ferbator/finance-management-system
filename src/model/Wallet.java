package model;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Wallet implements Serializable {
    private double balance;

    private final Map<String, Double> budgets;

    private final List<Transaction> transactions;

    public Wallet() {
        this.balance = 0;
        this.budgets = new HashMap<>();
        this.transactions = new ArrayList<>();
    }

    public void addIncome(double amount, String category) {
        balance += amount;
        transactions.add(new Transaction(amount, category, "Income"));
    }

    public void addExpense(double amount, String category) {
        if (balance >= amount) {
            balance -= amount;
            transactions.add(new Transaction(-amount, category, "Expense"));
        } else {
            System.out.println("Недостаточно средств на кошельке!");
        }
    }

    public void setBudget(String category, double amount) {
        budgets.put(category, amount);
    }

    public Map<String, Double> getBudgets() {
        return budgets;
    }

    public double getRemainingBudget(String category) {
        double totalSpent = transactions.stream()
                .filter(t -> t.getCategory().equals(category) && t.getType().equals("Expense"))
                .mapToDouble(Transaction::getAmount)
                .sum();
        return budgets.getOrDefault(category, 0.0) - Math.abs(totalSpent);
    }

    public double getTotalIncome() {
        return transactions.stream()
                .filter(t -> t.getType().equals("Income"))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getTotalExpenses() {
        return transactions.stream()
                .filter(t -> t.getType().equals("Expense"))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public Map<String, Double> getIncomeByCategory() {
        Map<String, Double> incomeByCategory = new HashMap<>();
        for (Transaction t : transactions) {
            if (t.getType().equals("Income")) {
                incomeByCategory.put(t.getCategory(),
                        incomeByCategory.getOrDefault(t.getCategory(), 0.0) + t.getAmount());
            }
        }
        return incomeByCategory;
    }

    public Map<String, Double> getExpenseByCategory() {
        Map<String, Double> expenseByCategory = new HashMap<>();
        for (Transaction t : transactions) {
            if (t.getType().equals("Expense")) {
                expenseByCategory.put(t.getCategory(),
                        expenseByCategory.getOrDefault(t.getCategory(), 0.0) + Math.abs(t.getAmount()));
            }
        }
        return expenseByCategory;
    }

    public double calculateMaxValue(Map<String, Double> income, Map<String, Double> expense) {
        double maxIncome = income.values().stream().max(Double::compareTo).orElse(0.0);
        double maxExpense = expense.values().stream().max(Double::compareTo).orElse(0.0);
        return Math.max(maxIncome, maxExpense);
    }
}
