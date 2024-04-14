import java.util.Scanner;

class ConsoleUI {
    private final Scanner scanner;
    private final CategoryManager categoryManager;
    private final ExpenseManager expenseManager;
    private final IncomeManager incomeManager;
    private final GraphUI graphUI;

    public ConsoleUI() {
        scanner = new Scanner(System.in);
        categoryManager = new CategoryManager();
        expenseManager = new ExpenseManager(categoryManager);
        incomeManager = new IncomeManager(categoryManager);
        graphUI = new GraphUI(categoryManager, expenseManager, incomeManager);
    }

    public void start() {
        boolean exit = false;
        while (!exit) {
            displayMainMenu();
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    categoryManager.menu();
                    break;
                case 2:
                    incomeManager.menu();
                    break;
                case 3:
                    expenseManager.menu();
                    break;
                case 4:
                    graphUI.menu();
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        System.out.println("Exiting Expense Tracker. See you again. ");
        scanner.close();
    }

    private void displayMainMenu() {
        System.out.println("\n");
        System.out.println("*------------------------------------------------------------------------------------------------------------------------------------*");
        System.out.println("|                                                            🏠  Home Menu                                                           |");
        System.out.println("|                                                                                                                                    |");
        System.out.println("|    📂 Category Management (1)  \uD83D\uDCB8 Income Management (2)   💰 Expense Management (3)    📊 Generate Graphs (4)    🚪 Exit (5)      |");
        System.out.println("*------------------------------------------------------------------------------------------------------------------------------------*");
    }

}