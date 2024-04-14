import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Manages expense-related operations such as adding, editing, and deleting expenses.
 */
public class ExpenseManager {
    private final Scanner scanner;
    private final List<Expense> expenses;
    private final CategoryManager categoryManager;
    private static final String EXPENSE_FILE = "expenses.txt";

    /**
     * Constructs an ExpenseManager object.
     *
     * @param categoryManager The CategoryManager instance to manage categories.
     */
    public ExpenseManager(CategoryManager categoryManager) {
        scanner = new Scanner(System.in);
        this.categoryManager = categoryManager;
        expenses = loadExpenses();
    }

    /**
     * Displays the main menu for expense management and handles user choices.
     */
    public void menu() {
        boolean backToMainMenu = false;
        displayExpenseMenu();
        while (!backToMainMenu) {
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    backToMainMenu = !addExpense();
                    break;
                case 2:
                    editExpense();
                    break;
                case 3:
                    deleteExpense();
                    break;
                case 4:
                    viewExpenses();
                    break;
                case 5:
                    backToMainMenu = true;
                    saveExpenses();
                    break;
                default:
                    CONSOLETEXT.printError("Invalid choice. Please try again.");
            }
            System.out.println();
        }
    }

    /**
     * Displays the expense management menu.
     */
    private void displayExpenseMenu() {
        System.out.println("\n");
        System.out.println("*------------------------------------------------------------------------------------------------------------------------------------*");
        System.out.println("|                                                     💰  Expense Management Menu                                                    |");
        System.out.println("|                                                                                                                                    |");
        System.out.println("|    ➕ Add Expense (1)     ✏\uFE0F Edit Expense (2)     ❌ Delete Expense (3)     \uD83D\uDC41\uFE0F  View Expenses (4)     ⬅\uFE0F Back to Main Menu (5)    |");
        System.out.println("*------------------------------------------------------------------------------------------------------------------------------------*");
    }

    /**
     * Displays all expenses.
     */
    private void viewExpenses() {
        if (expenses.isEmpty()) {
            CONSOLETEXT.printWarning("No Expenses to display.");
        } else {
            System.out.println("*------------------------------------------------------------------------------------------------------------------------------------*");
            System.out.println("|    Id    |    Description                                           |    Amount    |    Category ID    |    Created Date           |");
            System.out.println("*------------------------------------------------------------------------------------------------------------------------------------*");
            for (Expense expense : expenses) {
                System.out.println(expense);
            }
            System.out.println("*------------------------------------------------------------------------------------------------------------------------------------*");
        }
    }

    /**
     * Loads expenses from a file.
     *
     * @return The list of loaded expenses.
     */
    private List<Expense> loadExpenses() {
        List<Expense> expenses = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(EXPENSE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    int id = Integer.parseInt(parts[0]);
                    String description = parts[1];
                    double amount = Double.parseDouble(parts[2]);
                    int categoryId = Integer.parseInt(parts[3]);
                    LocalDate date = LocalDate.parse(parts[4]);
                    Expense expense = new Expense(id, description, amount, categoryId, date);
                    expenses.add(expense);
                }
            }
        }
        catch (FileNotFoundException ignored) {
        }catch (IOException e) {
            CONSOLETEXT.printError("Error loading expenses from file");
        }
        return expenses;
    }

    /**
     * Gets the list of expenses.
     *
     * @return The list of expenses.
     */
    public List<Expense> getExpenses() {
        return expenses;
    }

    /**
     * Adds a new expense.
     *
     * @return True if the expense is successfully added, otherwise false.
     */
    private boolean addExpense() {
        int nextId = expenses.isEmpty() ? 1 : expenses.getLast().getId() + 1;
        if (categoryManager.getCategories().isEmpty()) {
            CONSOLETEXT.printWarning("There are no categories available. Please add a category first.");
            return false;
        }

        String description = readDescription("Enter expense description: ", "");
        double amount = readAmount("Enter expense amount: ", 0.00);
        LocalDate date = readDate("Enter expense date (eg 2024/04/12): ", null);
        int categoryId = readCategoryId("Enter category ID: ", 0);

        Expense expense = new Expense(nextId, description, amount, categoryId, date);
        expenses.add(expense);
        CONSOLETEXT.printSuccess("Expense added successfully.");
        return true;
    }

    /**
     * Edits an existing expense.
     */
    private void editExpense() {
        System.out.print("Enter the ID of the expense to edit: ");
        int expenseId = scanner.nextInt();
        scanner.nextLine();
        Expense expense = getExpenseById(expenseId);
        if (expense != null) {
            String newDescription = readDescription("Enter new description (press enter to keep existing): ", expense.getDescription());
            double newAmount = readAmount("Enter new amount (press enter to keep existing): ", expense.getAmount());
            LocalDate newDate = readDate("Enter new date (eg 2024/04/12) (press enter to keep existing): ", expense.getCreatedDate());
            int newCategoryId = readCategoryId("Enter new category ID (press enter to keep existing): ", expense.getCategoryId());

            expense.setDescription(newDescription);
            expense.setAmount(newAmount);
            expense.setCreatedDate(newDate);
            expense.setCategoryId(newCategoryId);

            CONSOLETEXT.printSuccess("Expense updated successfully.");
        } else {
            CONSOLETEXT.printWarning("Expense not found.");
        }
    }

    /**
     * Deletes an existing expense.
     */
    private void deleteExpense() {
        System.out.print("Enter the ID of the expense to delete: ");
        int expenseId = scanner.nextInt();
        scanner.nextLine();
        Expense expense = getExpenseById(expenseId);
        if (expense != null) {
            expenses.remove(expense);
            CONSOLETEXT.printSuccess("Expense deleted successfully.");
        } else {
            CONSOLETEXT.printWarning("Expense not found.");
        }
    }

    /**
     * Reads expense description from user input.
     *
     * @param prompt       The prompt message.
     * @param defaultValue The default value.
     * @return The entered description.
     */
    private String readDescription(String prompt, String defaultValue) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (!defaultValue.isEmpty()) {
                return input.isEmpty() ? defaultValue : input;
            } else {
                if (input.isEmpty()) {
                    CONSOLETEXT.printError("Invalid description. Please provide a description for the expense.");
                } else {
                    return input;
                }
            }
        }
    }

    /**
     * Reads expense amount from user input.
     *
     * @param prompt       The prompt message.
     * @param defaultValue The default value.
     * @return The entered amount.
     */
    private double readAmount(String prompt, double defaultValue) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (input.isEmpty()) {
                if (defaultValue == 0.0) {
                    CONSOLETEXT.printError("Invalid amount. Please enter a valid number for the amount.");
                } else {
                    return defaultValue;
                }
            } else {
                try {
                    double amount = Double.parseDouble(input);
                    if (amount > 0) {
                        return amount;
                    }
                    CONSOLETEXT.printError("Invalid amount. Please enter a valid positive number for the amount.");
                } catch (NumberFormatException e) {
                    CONSOLETEXT.printError("Invalid amount. Please enter a valid positive number for the amount.");
                }
            }
        }
    }

    /**
     * Reads expense date from user input.
     *
     * @param prompt       The prompt message.
     * @param defaultValue The default value.
     * @return The entered date.
     */
    private LocalDate readDate(String prompt, LocalDate defaultValue) {
        while (true) {
            System.out.print(prompt);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            String input = scanner.nextLine();
            if (input.isEmpty()) {
                if (defaultValue == null) {
                    CONSOLETEXT.printError("Invalid date. Please enter the date in the format YYYY/MM/DD.");
                } else {
                    return defaultValue;
                }
            } else {
                try {
                    LocalDate date = LocalDate.parse(input, formatter);
                    if (!date.isAfter(LocalDate.now())) {
                        return date;
                    }
                    CONSOLETEXT.printError("Invalid date. Please don't enter future date.");
                } catch (Exception ex) {
                    CONSOLETEXT.printError("Invalid date. Please enter the date in the format YYYY/MM/DD.");
                }
            }
        }
    }

    /**
     * Reads category ID from user input.
     *
     * @param prompt       The prompt message.
     * @param defaultValue The default value.
     * @return The entered category ID.
     */
    private int readCategoryId(String prompt, int defaultValue) {
        categoryManager.viewCategories();
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (input.isEmpty()) {
                if (defaultValue == 0) {
                    CONSOLETEXT.printError("Invalid category ID. Please select a valid category ID from the table above.");
                } else {
                    return defaultValue;
                }
            } else {
                try {
                    int categoryId = Integer.parseInt(input);
                    boolean validCategory = categoryManager.isValidCategory(categoryId);
                    if (validCategory) {
                        return categoryId;
                    }
                    CONSOLETEXT.printError("Invalid category ID. Please select a valid category ID from the table above.");
                } catch (NumberFormatException e) {
                    CONSOLETEXT.printError("Invalid category ID. Please select a valid category ID from the table above.");
                }
            }
        }
    }

    /**
     * Retrieves an expense by ID.
     *
     * @param id The ID of the expense to retrieve.
     * @return The expense object if found, otherwise null.
     */
    private Expense getExpenseById(int id) {
        for (Expense expense : expenses) {
            if (expense.getId() == id) {
                return expense;
            }
        }
        return null;
    }

    /**
     * Saves expenses to a file.
     */
    private void saveExpenses() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(EXPENSE_FILE))) {
            for (Expense expense : expenses) {
                writer.println(expense.getId() + "," + expense.getDescription() + "," + expense.getAmount() + "," + expense.getCategoryId() + "," + expense.getCreatedDate());
            }
        } catch (IOException e) {
            CONSOLETEXT.printError("Error saving expenses to file");
        }
    }
}
