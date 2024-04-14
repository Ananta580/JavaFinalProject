import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class IncomeManager {
    private final Scanner scanner;
    private final List<Income> incomes;
    private final CategoryManager categoryManager;
    private static final String EXPENSE_FILE = "incomes.txt";

    public IncomeManager(CategoryManager categoryManager) {
        scanner = new Scanner(System.in);
        this.categoryManager = categoryManager;
        incomes = loadIncomes();
    }

    public void menu() {
        boolean backToMainMenu = false;
        displayIncomeMenu();
        while (!backToMainMenu) {
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addIncome();
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

    private void displayIncomeMenu() {
        System.out.println("\n");
        System.out.println("*------------------------------------------------------------------------------------------------------------------------------------*");
        System.out.println("|                                                     💰  Income Management Menu                                                    |");
        System.out.println("|                                                                                                                                    |");
        System.out.println("|    ➕ Add Income (1)     ✏\uFE0F Edit Income (2)     ❌ Delete Income (3)     \uD83D\uDC41\uFE0F  View Incomes (4)     ⬅\uFE0F Back to Main Menu (5)    |");
        System.out.println("*------------------------------------------------------------------------------------------------------------------------------------*");
    }

    private void addIncome() {
        int nextId = incomes.isEmpty() ? 1 : incomes.getLast().getId() + 1;
        if (categoryManager.getCategories().isEmpty()) {
            CONSOLETEXT.printWarning("There are no categories available. Please add a category first.");
            return;
        }

        System.out.print("Enter income description: ");
        String description = scanner.nextLine();

        boolean validAmount = false;
        double amount =0.2f;
        while (!validAmount) {
            System.out.print("Enter income amount: ");
            if (scanner.hasNextDouble()) {
                amount = scanner.nextDouble();
                validAmount = true; // Set flag to true to exit the loop
            } else {
                String invalidInput = scanner.next();
                CONSOLETEXT.printError("Invalid input. Please enter a valid number.");
            }
        }

        scanner.nextLine();


        boolean validDate = false;
        LocalDate date = null;
        while (!validDate) {
            System.out.print("Enter income date (eg 2024/04/12): ");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            String stringDate = scanner.nextLine();
            try{
                date = LocalDate.parse(stringDate, formatter);
                validDate=true;
            }

            catch (Exception ex){
                CONSOLETEXT.printError("Invalid date format. Please provide a valid date.");
            }
        }


        // Display available categories
        categoryManager.viewCategories();

        boolean validCategory = false;
        int categoryId =0;
        while (!validCategory) {
            System.out.print("Enter category ID: ");

                categoryId = scanner.nextInt();
                scanner.nextLine();
                // Check if the entered category ID is valid
                validCategory = categoryManager.isValidCategory(categoryId);
            if(!validCategory){
                CONSOLETEXT.printError("Invalid Category ID, Please see above table.");
            }
        }

        Income income = new Income(nextId, description, amount, categoryId, date);
        incomes.add(income);
        CONSOLETEXT.printSuccess("Income added successfully.");
    }


    private void viewIncomes() {
        if(incomes.isEmpty()){
            CONSOLETEXT.printWarning("No Incomes to display.");
        }
        else{
            System.out.println("*------------------------------------------------------------------------------------------------------------------------------------*");
            System.out.println("|    Id    |    Description                                           |    Amount    |    Category ID    |    Created Date           |");
            System.out.println("*------------------------------------------------------------------------------------------------------------------------------------*");
            for (Income income : incomes) {
                System.out.println(income);
            }
            System.out.println("*------------------------------------------------------------------------------------------------------------------------------------*");
        }
    }

    private void editIncome() {
        System.out.print("Enter the ID of the income to edit: ");
        int incomeId = scanner.nextInt();
        scanner.nextLine();
        Income income = getIncomeById(incomeId);
        if (income != null) {
            System.out.print("Enter new description (press enter to keep existing): ");
            String newDescription = scanner.nextLine();
            if (!newDescription.isEmpty()) {
                income.setDescription(newDescription);
            }

            boolean validAmount = false;
            double newAmount = income.getAmount(); // Default to existing amount
            while (!validAmount) {
                System.out.print("Enter new amount (press enter to keep existing): ");
                String amountInput = scanner.nextLine();
                if (amountInput.isEmpty()) {
                    validAmount = true;
                } else {
                    try {
                        newAmount = Double.parseDouble(amountInput);
                        validAmount = true; // Set flag to true to exit the loop
                    } catch (NumberFormatException e) {
                        CONSOLETEXT.printError("Invalid amount format. Please enter a valid number.");
                    }
                }
            }

            boolean validDate = false;
            LocalDate newDate = income.getCreatedDate(); // Default to existing date
            while (!validDate) {
                System.out.print("Enter new date (eg 2024/04/12) (press enter to keep existing): ");
                String dateInput = scanner.nextLine();
                if (dateInput.isEmpty()) {
                    validDate = true;
                } else {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    try {
                        newDate = LocalDate.parse(dateInput, formatter);
                        validDate = true; // Set flag to true to exit the loop
                    } catch (Exception e) {
                        CONSOLETEXT.printError("Invalid date format. Please provide a valid date format.");
                    }
                }
            }

            // Display available categories
            categoryManager.viewCategories();
            boolean validCategoryId = false;
            int newCategoryId = income.getCategoryId(); // Default to existing category ID
            while (!validCategoryId) {
                System.out.print("Enter new category ID (press enter to keep existing): ");
                String categoryIdInput = scanner.nextLine();
                if (categoryIdInput.isEmpty()) {
                    validCategoryId = true;
                } else {
                    try {
                        newCategoryId = Integer.parseInt(categoryIdInput);
                        // Check if the entered category ID is valid
                        if (!categoryManager.isValidCategory(newCategoryId)) {
                            CONSOLETEXT.printError("Invalid Category ID. Please enter a valid ID.");
                        } else {
                            validCategoryId = true; // Set flag to true to exit the loop
                        }
                    } catch (NumberFormatException e) {
                        CONSOLETEXT.printError("Invalid Category ID format. Please enter a valid number.");
                    }
                }
            }

            // Update income with new values
            income.setAmount(newAmount);
            income.setCreatedDate(newDate);
            income.setCategoryId(newCategoryId);

            CONSOLETEXT.printSuccess("Income updated successfully.");
        } else {
            CONSOLETEXT.printWarning("Income not found.");
        }
    }



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

    private Income getIncomeById(int id) {
        for (Income income : incomes) {
            if (income.getId() == id) {
                return income;
            }
        }
        return null;
    }

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
                    Income income = new Income(id,description, amount, categoryId, date);
                    incomes.add(income);
                }
            }
        } catch (IOException e) {
            CONSOLETEXT.printError("Error loading incomes from file");
        }
        return incomes;
    }

    private void saveIncomes() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(EXPENSE_FILE))) {
            for (Income income : incomes) {
                writer.println(income.getId()+","+ income.getDescription() + "," + income.getAmount() + "," + income.getCategoryId() + "," + income.getCreatedDate());
            }
        } catch (IOException e) {
            CONSOLETEXT.printError("Error saving incomes to file");
        }
    }

    public List<Income> getIncomes() {
        return incomes;
    }
}
