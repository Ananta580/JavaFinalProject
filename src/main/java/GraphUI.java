import java.time.LocalDate;
import java.util.*;

public class GraphUI {
    private final Scanner scanner;
    private final ExpenseManager expenseManager;

    private final IncomeManager incomeManager;
    private final CategoryManager categoryManager;

    public GraphUI(CategoryManager categoryManager, ExpenseManager expenseManager, IncomeManager incomeManager) {
        scanner = new Scanner(System.in);
        this.categoryManager = categoryManager;
        this.expenseManager = expenseManager;
        this.incomeManager = incomeManager;
    }

    public void menu() {
        boolean backToMainMenu = false;
        displayMenu();
        while (!backToMainMenu) {
            System.out.print("Enter your choice: ");
            int timeChoice = scanner.nextInt();
            scanner.nextLine();

            switch (timeChoice) {
                case 1:
                    generateGraph(expenseManager.getExpenses(),incomeManager.getIncomes(), "Daily");
                    break;
                case 2:
                    generateGraph(expenseManager.getExpenses(),incomeManager.getIncomes(), "Weekly");
                    break;
                case 3:
                    generateGraph(expenseManager.getExpenses(),incomeManager.getIncomes(), "Monthly");
                    break;
                case 4:
                    generateGraph(expenseManager.getExpenses(),incomeManager.getIncomes(), "Total");
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

    private void displayMenu() {
        System.out.println("\n");
        System.out.println("*------------------------------------------------------------------------------------------------------------------------------------*");
        System.out.println("|                                                           📊  Graph Menu                                                           |");
        System.out.println("|                                                                                                                                    |");
        System.out.println("|   \uD83D\uDCC5 Daily Graph (1)     \uD83D\uDCC6 Weekly Graph (2)      \uD83D\uDDD3\uFE0F Monthly Graph (3)     \uD83D\uDCC5  All Time Graph (4)     ⬅\uFE0F Back to Main Menu (5)    |");
        System.out.println("*------------------------------------------------------------------------------------------------------------------------------------*");
    }

    public void generateGraph(List<Expense> expenses, List<Income> incomes, String timeRange) {
        System.out.print("Filter by Category? (Yes/No): ");
        String filterInput = scanner.nextLine();
        boolean filterByCategory = filterInput.equalsIgnoreCase("Yes");
        int categoryId = 0;
        if (filterByCategory) {
            System.out.print("Enter category ID: ");
            categoryId = scanner.nextInt();
            scanner.nextLine();
            boolean validCategory = categoryManager.isValidCategory(categoryId);
            if (!validCategory) {
                CONSOLETEXT.printWarning("Category Not found.");
                return;
            }
        }

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

            // Calculate total transaction for the day
            double totalTransaction = Math.abs(expenseAmount) + Math.abs(incomeAmount);

            // Print date
            System.out.println(date + (filterByCategory?categoryManager.getCategoryName(categoryId):""));

            // Print expense and income bars (if applicable)
            if (maxTransaction > 0) {
                int expenseBarLength = (int) (Math.abs(expenseAmount) / maxTransaction * 30);
                int incomeBarLength = (int) (Math.abs(incomeAmount) / maxTransaction * 30);
                if(expenseBarLength>0){
                    String expenseBar = "\uD83D\uDFE5".repeat(expenseBarLength);
                    System.out.printf("%-20s | %s%n", formatAmount("Expense : ",expenseAmount), expenseBar);

                }
                if(incomeBarLength>0){
                    String incomeBar = "\uD83D\uDFE9".repeat(incomeBarLength);
                    System.out.printf("%-20s | %s%n", formatAmount("Income : ",incomeAmount), incomeBar);
                }
            }

            System.out.println();
        }
    }

    private String formatAmount(String label ,double amount) {
        return label + String.format("%.2f", amount);
    }


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
                return true; // All dates are within the total time range
            default:
                return false; // Invalid time range
        }
    }

}
