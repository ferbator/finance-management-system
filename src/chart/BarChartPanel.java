package chart;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class BarChartPanel extends JPanel {
    private final Map<String, Double> data;
    private final String title;
    private final double maxValue;
    private final int NUM_Y_TICKS = 5;

    public BarChartPanel(Map<String, Double> data, String title, double maxValue) {
        this.data = data;
        this.title = title;
        this.maxValue = maxValue;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Настройка размеров
        int width = getWidth();
        int height = getHeight();
        int padding = 50;
        int barWidth = 40;
        int maxBarHeight = height - 2 * padding;

        // Рисуем заголовок
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString(title, width / 2 - g2d.getFontMetrics().stringWidth(title) / 2, padding - 20);

        // Рисуем оси
        g2d.drawLine(padding, height - padding, width - padding, height - padding); // Ось X
        g2d.drawLine(padding, padding, padding, height - padding);// Ось Y

        // Рисуем деления на оси Y
        int yTickSpacing = maxBarHeight / NUM_Y_TICKS; // Расстояние между делениями
        for (int i = 0; i <= NUM_Y_TICKS; i++) {
            int y = height - padding - (i * yTickSpacing);
            double value = (maxValue / NUM_Y_TICKS) * i;

            // Рисуем деление
            g2d.drawLine(padding - 5, y, padding, y);

            // Подписываем деление
            String valueLabel = String.format("%.1f", value);
            g2d.drawString(valueLabel, padding - 40, y + 5);
        }

        // Рисуем столбцы
        int x = padding + 10;
        for (Map.Entry<String, Double> entry : data.entrySet()) {
            String category = entry.getKey();
            double value = entry.getValue();

            int barHeight = (int) ((value / maxValue) * maxBarHeight);

            // Рисуем столбец
            g2d.setColor(title.equals("Доходы") ? Color.BLUE : Color.RED);
            g2d.fillRect(x, height - padding - barHeight, barWidth, barHeight);

            // Подписи категорий
            g2d.setColor(Color.BLACK);
            g2d.drawString(category, x, height - padding + 15);

            // Сдвиг для следующей категории
            x += barWidth + 20;
        }

        // Подпись оси X
        g2d.drawString("Категории", width / 2, height - padding + 30);
    }
}

