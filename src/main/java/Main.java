import java.io.IOException;

/**
 * The main class of the Expense Tracker application.
 * Initializes the application and starts the user interface.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("*------------------------------------------------------------------------------------------------------------------------------------*");
        System.out.println("|                                                       Welcome To Expense Tracker                                                   |");
        System.out.println("|                                              Track your expenses anywhere, everywhere                                              |");

        ConsoleUI consoleUI = new ConsoleUI();
        consoleUI.start();
    }
}
