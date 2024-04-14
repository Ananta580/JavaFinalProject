public class CONSOLETEXT {
    private static final String RESET = "\033[0m";
    private static final String RED_BOLD = "\033[1;31m";
    private static final String GREEN_BOLD = "\033[1;32m";
    private static final String YELLOW_BOLD = "\033[1;33m";

    public static  void printWarning(String text){
        System.out.println(YELLOW_BOLD + text + RESET);
    }

    public static  void printError(String text){
        System.out.println(RED_BOLD + text + RESET);
    }

    public static  void printSuccess(String text){
        System.out.println(GREEN_BOLD + text + RESET);
    }


}
