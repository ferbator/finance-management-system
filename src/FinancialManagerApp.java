import model.User;
import service.VisualizationService;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FinancialManagerApp {
    private static final String USERS_FILE = "data/users.dat";
    private static Map<String, User> users = new HashMap<>();

    public static void main(String[] args) {
        loadUsers();
        Scanner scanner = new Scanner(System.in);
        User currentUser = null;
        int choice;
        while (true) {
            System.out.printf("\n=== Управление личными финансами пользователя %s ===\n",
                    currentUser != null ? currentUser.getUsername() : "null");
            System.out.println("1. Регистрация");
            System.out.println("2. Вход");
            System.out.println("3. Добавить доход");
            System.out.println("4. Добавить расход");
            System.out.println("5. Установить бюджет");
            System.out.println("6. Показать отчет");
            System.out.println("7. Визуализация данных");
            System.out.println("8. Сохранить и выйти");
            System.out.print("Выберите действие: ");

            String input = scanner.nextLine();
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                choice = -1;
            }

            switch (choice) {
                case 1 -> {
                    System.out.print("Введите имя пользователя: ");
                    String username = scanner.nextLine();
                    System.out.print("Введите пароль: ");
                    String password = scanner.nextLine();
                    if (!users.containsKey(username)) {
                        users.put(username, new User(username, password));
                        System.out.println("Пользователь зарегистрирован!");
                    } else {
                        System.out.println("Имя пользователя уже занято.");
                    }
                }
                case 2 -> {
                    System.out.print("Введите имя пользователя: ");
                    String username = scanner.nextLine();
                    System.out.print("Введите пароль: ");
                    String password = scanner.nextLine();
                    User user = users.get(username);
                    if (user != null && user.validatePassword(password)) {
                        currentUser = user;
                        System.out.println("Вход выполнен!");
                    } else {
                        System.out.println("Неверное имя пользователя или пароль.");
                    }
                }
                case 3 -> {
                    if (currentUser != null) {
                        try {
                            System.out.print("Введите сумму дохода: ");
                            String amountInput = scanner.nextLine();
                            double amount = validateDouble(amountInput);

                            System.out.print("Введите категорию дохода: ");
                            String category = scanner.nextLine().trim();

                            if (category.isEmpty()) {
                                System.out.println("Категория не может быть пустой.");
                            } else {
                                currentUser.getWallet().addIncome(amount, category);
                                System.out.println("Доход добавлен.");
                            }
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        System.out.println("Войдите в систему для добавления дохода.");
                    }
                }
                case 4 -> {
                    if (currentUser != null) {
                        try {
                            System.out.print("Введите сумму расхода: ");
                            String amountInput = scanner.nextLine();
                            double amount = validateDouble(amountInput);

                            System.out.print("Введите категорию расхода: ");
                            String category = scanner.nextLine().trim();

                            if (category.isEmpty()) {
                                System.out.println("Категория не может быть пустой.");
                            } else {
                                currentUser.getWallet().addExpense(amount, category);
                                System.out.println("Расход добавлен.");
                            }
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        System.out.println("Войдите в систему для добавления расхода.");
                    }
                }
                case 5 -> {
                    if (currentUser != null) {
                        try {
                            System.out.print("Введите категорию бюджета: ");
                            String category = scanner.nextLine().trim();
                            if (category.isEmpty()) {
                                throw new IllegalArgumentException("Категория не может быть пустой.");
                            }

                            System.out.print("Введите сумму бюджета: ");
                            String amountInput = scanner.nextLine();
                            double amount = validateDouble(amountInput);

                            currentUser.getWallet().setBudget(category, amount);
                            System.out.println("Бюджет установлен.");
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        System.out.println("Войдите в систему для установки бюджета.");
                    }
                }
                case 6 -> {
                    if (currentUser != null) {
                        VisualizationService.displaySummary(currentUser.getWallet());
                    } else {
                        System.out.println("Войдите в систему для просмотра отчета.");
                    }
                }
                case 7 -> {
                    if (currentUser != null) {
                        VisualizationService.visualizeData(currentUser.getWallet());
                    } else {
                        System.out.println("Войдите в систему для визуализации данных.");
                    }
                }
                case 8 -> {
                    saveUsers();
                    System.out.println("Программа завершена.");
                    return;
                }
                default -> System.out.println("Неверный выбор, попробуйте снова.");
            }
        }
    }

    private static double validateDouble(String input) {
        try {
            double value = Double.parseDouble(input);
            if (value < 0) {
                throw new IllegalArgumentException("Сумма не может быть отрицательной.");
            }
            return value;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ошибка: введите корректное число.");
        }
    }


    private static void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(users);
            System.out.println("Данные пользователей сохранены.");
        } catch (IOException e) {
            System.out.println("Ошибка сохранения данных пользователей: " + e.getMessage());
        }
    }

    private static void loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_FILE))) {
            Map<?, ?> tempMap = (Map<?, ?>) ois.readObject();
            Map<String, User> validatedMap = new HashMap<>();
            for (Map.Entry<?, ?> entry : tempMap.entrySet()) {
                if (entry.getKey() instanceof String && entry.getValue() instanceof User) {
                    validatedMap.put((String) entry.getKey(), (User) entry.getValue());
                } else {
                    throw new IllegalArgumentException("Некорректные данные в файле.");
                }
            }
            users = validatedMap;
            System.out.println("Данные пользователей загружены.");
        } catch (FileNotFoundException e) {
            System.out.println("Файл данных пользователей не найден, создается новый.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка загрузки данных пользователей: " + e.getMessage());
        }

    }
}