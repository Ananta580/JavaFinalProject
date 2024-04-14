import java.time.LocalDate;
import java.util.*;

/**
 * Handles user interface and graph generation.
 */
public class GraphUI {
    private final Scanner scanner;
    private final ExpenseManager expenseManager;
    private final IncomeManager incomeManager;
    private final CategoryManager categoryManager;

    /**
     * Constructs a new GraphUI object with the given managers.
     *
     * @param categoryManager The category manager.
     * @param expenseManager  The expense manager.
     * @param incomeManager   The income manager.
     */
    public GraphUI(CategoryManager categoryManager, ExpenseManager expenseManager, IncomeManager incomeManager) {
        scanner = new Scanner(System.in);
        this.categoryManager = categoryManager;
        this.expenseManager = expenseManager;
        this.incomeManager = incomeManager;
    }

    /**
     * Displays the graph menu and handles user input.
     */
    public void menu() {
        if(expenseManager.getExpenses().isEmpty() && incomeManager.getIncomes().isEmpty()){
            CONSOLETEXT.printWarning("Couldn't find any income or expense yet.");
            return;
        }

        boolean backToMainMenu = false;
        displayMenu();
        while (!backToMainMenu) {
            System.out.print("Enter your choice: ");
            int timeChoice = scanner.nextInt();
            scanner.nextLine();

            switch (timeChoice) {
                case 1:
                case 2:
                case 3:
                case 4:
                    generateGraph(timeChoice);
                    break;
                case 5:
                    backToMainMenu = true;
                    break;
                default:
                    CONSOLETEXT.printError("Invalid choice. Please try again.");
                    return;
            }
            System.out.println();
        }
    }

    /**
     * Displays the graph menu.
     */
    private void displayMenu() {
        System.out.println("\n");
        System.out.println("*------------------------------------------------------------------------------------------------------------------------------------*");
        System.out.println("|                                                           📊  Graph Menu                                                           |");
        System.out.println("|                                                                                                                                    |");
        System.out.println("|   \uD83D\uDCC5 Daily Graph (1)     \uD83D\uDCC6 Weekly Graph (2)      \uD83D\uDDD3\uFE0F Monthly Graph (3)     \uD83D\uDCC5  All Time Graph (4)     ⬅\uFE0F Back to Main Menu (5)    |");
        System.out.println("*------------------------------------------------------------------------------------------------------------------------------------*");
    }

    /**
     * Generates a graph based on the selected time range.
     *
     * @param timeChoice The user's choice of time range.
     */
    public void generateGraph(int timeChoice) {
        String timeRange = getTimeRange(timeChoice);
        boolean filterByCategory = promptFilterByCategory();
        int categoryId = getCategoryID(filterByCategory);
        processGraphData(timeRange, filterByCategory, categoryId);
    }

    /**
     * Prompts the user to filter the graph by category.
     *
     * @return True if filtering by category, false otherwise.
     */
    private boolean promptFilterByCategory() {
        while (true){
            System.out.print("Filter by Category? (Yes/No): ");
            String filterInput = scanner.nextLine();
            if(filterInput.equalsIgnoreCase("Yes") || filterInput.equalsIgnoreCase("No")){
                return filterInput.equalsIgnoreCase("Yes");
            }
            else{
                CONSOLETEXT.printError("Invalid choice, Please try again: ");
            }
        }

    }

    /**
     * Gets the category ID from user input.
     *
     * @param filterByCategory True if filtering by category, false otherwise.
     * @return The category ID entered by the user.
     */
    private int getCategoryID(boolean filterByCategory) {
        int categoryId = 0;
        if (filterByCategory) {
            while(true){
                    System.out.print("Enter category ID: ");
                    categoryId = scanner.nextInt();
                    scanner.nextLine();
                    if (categoryManager.isValidCategory(categoryId)) {
                        return  categoryId;
                    }
                CONSOLETEXT.printWarning("Category Not found.");
            }
        }
        return categoryId;
    }

    /**
     * Processes the graph data and generates the graph.
     *
     * @param timeRange        The selected time range.
     * @param filterByCategory True if filtering by category, false otherwise.
     * @param categoryId       The category ID if filtering by category.
     */
    private void processGraphData(String timeRange, boolean filterByCategory, int categoryId) {
        // Get expenses and incomes data
        List<Expense> expenses = expenseManager.getExpenses();
        List<Income> incomes = incomeManager.getIncomes();

        // Generate and display the graph
        generateGraph(expenses, incomes, timeRange, filterByCategory, categoryId);
    }

    /**
     * Generates and displays the graph based on the given data.
     *
     * @param expenses        The list of expenses.
     * @param incomes         The list of incomes.
     * @param timeRange       The selected time range.
     * @param filterByCategory True if filtering by category, false otherwise.
     * @param categoryId      The category ID if filtering by category.
     */
    public void generateGraph(List<Expense> expenses, List<Income> incomes, String timeRange, boolean filterByCategory, int categoryId) {
        System.out.println();

        // Create a Set to store all unique dates from both expenses and incomes
        Set<LocalDate> allDates = new HashSet<>();

        // Create a HashMap to store both expenses and incomes by date
        Map<LocalDate, Double> expenseMap = new LinkedHashMap<>();
        Map<LocalDate, Double> incomeMap = new LinkedHashMap<>();

        // Add expenses to the expense map
        for (Expense expense : expenses) {
            if (!filterByCategory || expense.getCategoryId() == categoryId) {
                if (isInRange(expense.getCreatedDate(), timeRange)) {
                    allDates.add(expense.getCreatedDate());
                    expenseMap.merge(expense.getCreatedDate(), expense.getAmount(), Double::sum);
                }
            }
        }

        // Add incomes to the income map
        for (Income income : incomes) {
            if (!filterByCategory || income.getCategoryId() == categoryId) {
                if (isInRange(income.getCreatedDate(), timeRange)) {
                    allDates.add(income.getCreatedDate());
                    incomeMap.merge(income.getCreatedDate(), income.getAmount(), Double::sum);
                }
            }
        }

        // Determine maximum transaction amount
        double maxExpense = expenseMap.values().stream().mapToDouble(Math::abs).max().orElse(0.0);
        double maxIncome = incomeMap.values().stream().mapToDouble(Math::abs).max().orElse(0.0);
        double maxTransaction = Math.max(maxExpense, maxIncome);

        // Sorting allDates:
        List<LocalDate> sortedDates = new ArrayList<>(allDates);
        Collections.sort(sortedDates);

        for (LocalDate date : sortedDates) {
            double expenseAmount = expenseMap.getOrDefault(date, 0.0);
            double incomeAmount = incomeMap.getOrDefault(date, 0.0);

            // Print date
            System.out.println(date + (filterByCategory ? categoryManager.getCategoryName(categoryId) : ""));

            // Print expense and income bars (if applicable)
            if (maxTransaction > 0) {
                int expenseBarLength = (int) (Math.abs(expenseAmount) / maxTransaction * 30);
                int incomeBarLength = (int) (Math.abs(incomeAmount) / maxTransaction * 30);
                if (expenseBarLength > 0) {
                    String expenseBar = "\uD83D\uDFE5".repeat(expenseBarLength);
                    System.out.printf("%-20s | %s%n", formatAmount("Expense : ", expenseAmount), expenseBar);
                }
                if (incomeBarLength > 0) {
                    String incomeBar = "\uD83D\uDFE9".repeat(incomeBarLength);
                    System.out.printf("%-20s | %s%n", formatAmount("Income : ", incomeAmount), incomeBar);
                }
            }

            System.out.println();
        }
    }


    /**
     * Gets the time range based on the user's choice.
     *
     * @param timeChoice The user's choice of time range.
     * @return The corresponding time range string.
     */
    private String getTimeRange(int timeChoice) {
        return switch (timeChoice) {
            case 1 -> "Daily";
            case 2 -> "Weekly";
            case 3 -> "Monthly";
            case 4 -> "Total";
            default -> "";
        };
    }

    /**
     * Checks if a given date falls within the specified time range.
     *
     * @param date      The date to check.
     * @param timeRange The time range (daily, weekly, monthly, or total).
     * @return True if the date falls within the time range, false otherwise.
     */
    private static boolean isInRange(LocalDate date, String timeRange) {
        LocalDate currentDate = LocalDate.now();
        switch (timeRange.toLowerCase()) {
            case "daily":
                return date.equals(currentDate);
            case "weekly":
                LocalDate startOfWeek = currentDate.minusDays(currentDate.getDayOfWeek().getValue() - 1);
                LocalDate endOfWeek = startOfWeek.plusDays(6);
                return !date.isBefore(startOfWeek) && !date.isAfter(endOfWeek);
            case "monthly":
                LocalDate startOfMonth = currentDate.withDayOfMonth(1);
                LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);
                return !date.isBefore(startOfMonth) && !date.isAfter(endOfMonth);
            case "total":
                // All dates are within the total time range
                return true;
            default:
                // Invalid time range
                return false;
        }
    }


    /**
     * Formats the amount with the given label.
     *
     * @param label  The label for the amount.
     * @param amount The amount to be formatted.
     * @return The formatted amount with the label.
     */
    private String formatAmount(String label, double amount) {
        return label + String.format("%.2f", amount);
    }

}
