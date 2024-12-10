package model;

import java.io.Serializable;
import java.util.Date;

public class Transaction implements Serializable {
    private final double amount;
    private final String category;
    private final String type;
    private final Date date;

    public Transaction(double amount, String category, String type) {
        this.amount = amount;
        this.category = category;
        this.type = type;
        this.date = new Date();
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("Тип: %s, Категория: %s, Сумма: %.2f, Дата: %s",
                type, category, amount, date);
    }
}
