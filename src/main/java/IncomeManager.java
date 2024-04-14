import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Manages income-related operations such as adding, editing, and deleting incomes.
 */
public class IncomeManager {
    private final Scanner scanner;
    private final List<Income> incomes;
    private final CategoryManager categoryManager;
    private static final String EXPENSE_FILE = "incomes.txt";

    /**
     * Constructs an IncomeManager object.
     *
     * @param categoryManager The CategoryManager instance to manage categories.
     */
    public IncomeManager(CategoryManager categoryManager) {
        scanner = new Scanner(System.in);
        this.categoryManager = categoryManager;
        incomes = loadIncomes();
    }

    /**
     * Displays the main menu for income management and handles user choices.
     */
    public void menu() {
        boolean backToMainMenu = false;
        displayIncomeMenu();
        while (!backToMainMenu) {
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    backToMainMenu = !addIncome();
                    break;
                case 2:
                    editIncome();
                    break;
                case 3:
                    deleteIncome();
                    break;
                case 4:
                    viewIncomes();
                    break;
                case 5:
                    backToMainMenu = true;
                    saveIncomes();
                    break;
                default:
                    CONSOLETEXT.printError("Invalid choice. Please try again.");
            }
            System.out.println();
        }
    }

    /**
     * Displays the income management menu.
     */
    private void displayIncomeMenu() {
        System.out.println("\n");
        System.out.println("*------------------------------------------------------------------------------------------------------------------------------------*");
        System.out.println("|                                                     💰  Income Management Menu                                                    |");
        System.out.println("|                                                                                                                                    |");
        System.out.println("|    ➕ Add Income (1)     ✏\uFE0F Edit Income (2)     ❌ Delete Income (3)     \uD83D\uDC41\uFE0F  View Incomes (4)     ⬅\uFE0F Back to Main Menu (5)    |");
        System.out.println("*------------------------------------------------------------------------------------------------------------------------------------*");
    }

    /**
     * Displays all incomes.
     */
    private void viewIncomes() {
        if (incomes.isEmpty()) {
            CONSOLETEXT.printWarning("No Incomes to display.");
        } else {
            System.out.println("*------------------------------------------------------------------------------------------------------------------------------------*");
            System.out.println("|    Id    |    Description                                           |    Amount    |    Category ID    |    Created Date           |");
            System.out.println("*------------------------------------------------------------------------------------------------------------------------------------*");
            for (Income income : incomes) {
                System.out.println(income);
            }
            System.out.println("*------------------------------------------------------------------------------------------------------------------------------------*");
        }
    }

    /**
     * Loads incomes from a file.
     *
     * @return The list of loaded incomes.
     */
    private List<Income> loadIncomes() {
        List<Income> incomes = new ArrayList<>();
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
                    Income income = new Income(id, description, amount, categoryId, date);
                    incomes.add(income);
                }
            }
        }catch (FileNotFoundException ignored) {
        } catch (IOException e) {
            CONSOLETEXT.printError("Error loading incomes from file");
        }
        return incomes;
    }

    /**
     * Gets the list of incomes.
     *
     * @return The list of incomes.
     */
    public List<Income> getIncomes() {
        return incomes;
    }

    /**
     * Adds a new income.
     *
     * @return True if the income is successfully added, otherwise false.
     */
    private boolean addIncome() {
        int nextId = incomes.isEmpty() ? 1 : incomes.getLast().getId() + 1;
        if (categoryManager.getCategories().isEmpty()) {
            CONSOLETEXT.printWarning("There are no categories available. Please add a category first.");
            return false;
        }

        String description = readDescription("Enter income description: ", "");
        double amount = readAmount("Enter income amount: ", 0.00);
        LocalDate date = readDate("Enter income date (eg 2024/04/12): ", null);
        int categoryId = readCategoryId("Enter category ID: ", 0);

        Income income = new Income(nextId, description, amount, categoryId, date);
        incomes.add(income);
        CONSOLETEXT.printSuccess("Income added successfully.");
        return true;
    }

    /**
     * Edits an existing income.
     */
    private void editIncome() {
        System.out.print("Enter the ID of the income to edit: ");
        int incomeId = scanner.nextInt();
        scanner.nextLine();
        Income income = getIncomeById(incomeId);
        if (income != null) {
            String newDescription = readDescription("Enter new description (press enter to keep existing): ", income.getDescription());
            double newAmount = readAmount("Enter new amount (press enter to keep existing): ", income.getAmount());
            LocalDate newDate = readDate("Enter new date (eg 2024/04/12) (press enter to keep existing): ", income.getCreatedDate());
            int newCategoryId = readCategoryId("Enter new category ID (press enter to keep existing): ", income.getCategoryId());

            income.setDescription(newDescription);
            income.setAmount(newAmount);
            income.setCreatedDate(newDate);
            income.setCategoryId(newCategoryId);

            CONSOLETEXT.printSuccess("Income updated successfully.");
        } else {
            CONSOLETEXT.printWarning("Income not found.");
        }
    }

    /**
     * Deletes an existing income.
     */
    private void deleteIncome() {
        System.out.print("Enter the ID of the income to delete: ");
        int incomeId = scanner.nextInt();
        scanner.nextLine();
        Income income = getIncomeById(incomeId);
        if (income != null) {
            incomes.remove(income);
            CONSOLETEXT.printSuccess("Income deleted successfully.");
        } else {
            CONSOLETEXT.printWarning("Income not found.");
        }
    }

    /**
     * Reads income description from user input.
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
                    CONSOLETEXT.printError("Invalid description. Please provide a description for the income.");
                } else {
                    return input;
                }
            }
        }
    }

    /**
     * Reads income amount from user input.
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
     * Reads income date from user input.
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
     * Retrieves an income by ID.
     *
     * @param id The ID of the income to retrieve.
     * @return The income object if found, otherwise null.
     */
    private Income getIncomeById(int id) {
        for (Income income : incomes) {
            if (income.getId() == id) {
                return income;
            }
        }
        return null;
    }

    /**
     * Saves incomes to a file.
     */
    private void saveIncomes() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(EXPENSE_FILE))) {
            for (Income income : incomes) {
                writer.println(income.getId() + "," + income.getDescription() + "," + income.getAmount() + "," + income.getCategoryId() + "," + income.getCreatedDate());
            }
        } catch (IOException e) {
            CONSOLETEXT.printError("Error saving incomes to file");
        }
    }
}
