import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ExpenseManager {
    private final Scanner scanner;
    private final List<Expense> expenses;
    private final CategoryManager categoryManager;
    private static final String EXPENSE_FILE = "expenses.txt";

    public ExpenseManager(CategoryManager categoryManager) {
        scanner = new Scanner(System.in);
        this.categoryManager = categoryManager;
        expenses = loadExpenses();
    }

    public void menu() {
        boolean backToMainMenu = false;
        displayExpenseMenu();
        while (!backToMainMenu) {
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addExpense();
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

    private void displayExpenseMenu() {
        System.out.println("\n");
        System.out.println("*------------------------------------------------------------------------------------------------------------------------------------*");
        System.out.println("|                                                     💰  Expense Management Menu                                                    |");
        System.out.println("|                                                                                                                                    |");
        System.out.println("|    ➕ Add Expense (1)     ✏\uFE0F Edit Expense (2)     ❌ Delete Expense (3)     \uD83D\uDC41\uFE0F  View Expenses (4)     ⬅\uFE0F Back to Main Menu (5)    |");
        System.out.println("*------------------------------------------------------------------------------------------------------------------------------------*");
    }

    private void addExpense() {
        int nextId = expenses.isEmpty() ? 1 : expenses.getLast().getId() + 1;
        if (categoryManager.getCategories().isEmpty()) {
           CONSOLETEXT.printWarning("There are no categories available. Please add a category first.");
            categoryManager.menu();
            return;
        }

        System.out.print("Enter expense description: ");
        String description = scanner.nextLine();

        System.out.print("Enter expense amount: ");
        double amount = scanner.nextDouble();

        scanner.nextLine();

        System.out.print("Enter expense date: ");
        String stringDate = scanner.nextLine();
        if (stringDate.isEmpty()) {
            CONSOLETEXT.printError("Invalid date format. Please provide a valid date.");
            return;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
        LocalDate date = null;
        try{
            date = LocalDate.parse(stringDate, formatter);
        }

        catch (Exception ex){
            CONSOLETEXT.printError("Invalid date format. Please provide a valid date.");
        }

        // Display available categories
        categoryManager.viewCategories();

        // Prompt user to enter category ID
        System.out.print("Enter category ID: ");
        int categoryId = scanner.nextInt();
        scanner.nextLine();

        // Check if the entered category ID is valid
        boolean validCategory = categoryManager.isValidCategory(categoryId);

        // If the entered category ID is valid, add the expense
        if (validCategory) {
            Expense expense = new Expense(nextId, description, amount, categoryId, date);
            expenses.add(expense);
            CONSOLETEXT.printSuccess("Expense added successfully.");
        } else {
            CONSOLETEXT.printWarning("Category Not found.");
        }
    }


    private void viewExpenses() {
        if(expenses.isEmpty()){
            CONSOLETEXT.printWarning("No Expenses to display.");
        }
        else{
            System.out.println("*------------------------------------------------------------------------------------------------------------------------------------*");
            System.out.println("|    Id    |    Description                                           |    Amount    |    Category ID    |    Created Date           |");
            System.out.println("*------------------------------------------------------------------------------------------------------------------------------------*");
            for (Expense expense : expenses) {
                System.out.println(expense);
            }
            System.out.println("*------------------------------------------------------------------------------------------------------------------------------------*");
        }
    }

    private void editExpense() {
        System.out.print("Enter the ID of the expense to edit: ");
        int expenseId = scanner.nextInt();
        scanner.nextLine();
        Expense expense = getExpenseById(expenseId);
        if (expense != null) {
            System.out.print("Enter new description (press enter to keep existing): ");
            String newDescription = scanner.nextLine();
            if (!newDescription.isEmpty()) {
                expense.setDescription(newDescription);
            }
            System.out.print("Enter new amount (press enter to keep existing): ");
            String amountInput = scanner.nextLine();
            if (!amountInput.isEmpty()) {
                double newAmount = Double.parseDouble(amountInput);
                expense.setAmount(newAmount);
            }

            System.out.print("Enter new date (press enter to keep existing): ");
            String dateInput = scanner.nextLine();
            if (!dateInput.isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
                LocalDate newDate = LocalDate.parse(dateInput, formatter);
                expense.setCreatedDate(newDate);
            }

            System.out.print("Enter new category ID (press enter to keep existing): ");
            String categoryIdInput = scanner.nextLine();
            if (!categoryIdInput.isEmpty()) {
                int newCategoryId = Integer.parseInt(categoryIdInput);
                expense.setCategoryId(newCategoryId);
            }
            CONSOLETEXT.printSuccess("Expense edited successfully.");
        } else {
            CONSOLETEXT.printWarning("Expense not found.");
        }
    }

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

    private Expense getExpenseById(int id) {
        for (Expense expense : expenses) {
            if (expense.getId() == id) {
                return expense;
            }
        }
        return null;
    }

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
                    Expense expense = new Expense(id,description, amount, categoryId, date);
                    expenses.add(expense);
                }
            }
        } catch (IOException e) {
            CONSOLETEXT.printError("Error loading expenses from file");
        }
        return expenses;
    }

    private void saveExpenses() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(EXPENSE_FILE))) {
            for (Expense expense : expenses) {
                writer.println(expense.getId()+","+ expense.getDescription() + "," + expense.getAmount() + "," + expense.getCategoryId() + "," + expense.getCreatedDate());
            }
        } catch (IOException e) {
            CONSOLETEXT.printError("Error saving expenses to file");
        }
    }

    public List<Expense> getExpenses() {
        return expenses;
    }
}
