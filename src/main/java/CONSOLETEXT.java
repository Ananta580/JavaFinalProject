/**
 * Utility class for printing colored text to the console.
 */
public class CONSOLETEXT {
    private static final String RESET = "\033[0m";
    private static final String RED_BOLD = "\033[1;31m";
    private static final String GREEN_BOLD = "\033[1;32m";
    private static final String YELLOW_BOLD = "\033[1;33m";

    /**
     * Prints a warning message to the console in yellow color.
     *
     * @param text The text of the warning message.
     */
    public static void printWarning(String text) {
        System.out.println(YELLOW_BOLD + text + RESET);
    }

    /**
     * Prints an error message to the console in red color.
     *
     * @param text The text of the error message.
     */
    public static void printError(String text) {
        System.out.println(RED_BOLD + text + RESET);
    }

    /**
     * Prints a success message to the console in green color.
     *
     * @param text The text of the success message.
     */
    public static void printSuccess(String text) {
        System.out.println(GREEN_BOLD + text + RESET);
    }
}
