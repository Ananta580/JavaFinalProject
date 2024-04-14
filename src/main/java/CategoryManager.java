import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CategoryManager {
    private final Scanner scanner;
    private final List<Category> categories;
    private static final String CATEGORY_FILE = "categories.txt";


    public CategoryManager() {
        scanner = new Scanner(System.in);
        categories = loadCategories();
    }

    public void menu() {
        boolean backToMainMenu = false;
        displayCategoryMenu();
        while (!backToMainMenu) {
            System.out.print("Enter your choice (1 | 2 | 3 | 4 | 5): ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createCategory();
                    break;
                case 2:
                    editCategory();
                    break;
                case 3:
                    deleteCategory();
                    break;
                case 4:
                    viewCategories();
                    break;
                case 5:
                    saveCategories();
                    backToMainMenu = true;
                    break;
                default:
                    CONSOLETEXT.printError("Invalid choice. Please try again.");
            }
            System.out.println();
        }
    }

    private void displayCategoryMenu() {
        System.out.println("\n");
        System.out.println("*------------------------------------------------------------------------------------------------------------------------------------*");
        System.out.println("|                                                    📂  Category Management Menu                                                    |");
        System.out.println("|                                                                                                                                    |");
        System.out.println("|    ➕ Add Category (1)   ✏\uFE0F Edit Category (2)    ❌ Delete Category (3)    \uD83D\uDC41\uFE0F  View Categories (4)    ⬅\uFE0F Back to Main Menu (5)    |");
        System.out.println("*------------------------------------------------------------------------------------------------------------------------------------*");
    }


    private void createCategory() {
        System.out.print("Enter category name: ");
        String categoryName = scanner.nextLine();
        int nextId = categories.isEmpty() ? 1 : categories.getLast().getId() + 1;
        Category category = new Category(nextId, categoryName);
        categories.add(category);
        CONSOLETEXT.printSuccess("Category created successfully.");
    }

    private void editCategory() {
        System.out.print("Enter the ID of the category to edit: ");
        int categoryId = scanner.nextInt();
        scanner.nextLine();
        Category category = getCategoryById(categoryId);
        if (category != null) {
            System.out.print("Enter new category name: ");
            String newName = scanner.nextLine();
            category.setName(newName);
            CONSOLETEXT.printSuccess("Category updated successfully.");
        } else {
            CONSOLETEXT.printWarning("Category not found.");
        }
    }

    private void deleteCategory() {
        System.out.print("Enter the ID of the category to delete: ");
        int categoryId = scanner.nextInt();
        scanner.nextLine();

        Category category = getCategoryById(categoryId);

        if (category != null) {
            boolean usedByExpenses = false;
            boolean usedByIncomes = false;

            ExpenseManager expenseManager = new ExpenseManager(this);
            IncomeManager incomeManager = new IncomeManager(this);

            // Check if the category is used by expenses
            for (Expense expense : expenseManager.getExpenses()) {
                if (expense.getCategoryId() == categoryId) {
                    usedByExpenses = true;
                    break;
                }
            }

            // Check if the category is used by incomes
            for (Income income : incomeManager.getIncomes()) {
                if (income.getCategoryId() == categoryId) {
                    usedByIncomes = true;
                    break;
                }
            }

            if (usedByExpenses || usedByIncomes) {
                CONSOLETEXT.printWarning("Category is being used by expenses or incomes. It cannot be deleted.");
            } else {
                categories.remove(category);
                CONSOLETEXT.printSuccess("Category deleted successfully.");
            }
        } else {
            CONSOLETEXT.printWarning("Category not found.");
        }
    }

    public void viewCategories() {
        if(categories.isEmpty()){
            CONSOLETEXT.printWarning("No Categories to display.");
        }
        else{
            System.out.println("*-------------------------------------------------------------*");
            System.out.println("|    Id    |    Name                                          |");
            System.out.println("*-------------------------------------------------------------*");
            for (Category category : categories) {
                System.out.println(category);
            }
            System.out.println("*-------------------------------------------------------------*");
        }

    }

    private Category getCategoryById(int id) {
        for (Category category : categories) {
            if (category.getId() == id) {
                return category;
            }
        }
        return null;
    }

    private void saveCategories() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CATEGORY_FILE))) {
            for (Category category : categories) {
                writer.println(category.getId() + "," + category.getName());
            }
        } catch (IOException e) {
            CONSOLETEXT.printError("Error saving categories to file.");
        }
    }

    private List<Category> loadCategories() {
        List<Category> categories = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CATEGORY_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    Category category = new Category(id, name);
                    categories.add(category);
                }
            }
        } catch (IOException e) {
            CONSOLETEXT.printError("Error loading categories from file.");
        }
        return categories;
    }

    List<Category> getCategories() {
        return categories;
    }

    public boolean isValidCategory(int categoryId) {
        for (Category category : categories) {
            if (category.getId() == categoryId) {
                return true;
            }
        }
        return false;
    }

    public String getCategoryName(int categoryId) {
        for (Category category : categories) {
            if (category.getId() == categoryId) {
                return category.getName();
            }
        }
        return "Unknown";
    }

}
