package cli;

/**
 * Egységes konzol kimeneti segéd a színezett státuszokhoz.
 */
public final class ConsoleOutput {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_YELLOW = "\u001B[33m";

    private ConsoleOutput() {
    }

    /**
     * Sikeres művelet visszajelzése zöld színnel.
     */
    public static void ok() {
        System.out.println("> " + ANSI_GREEN + "OK" + ANSI_RESET);
    }

    /**
     * Hibaüzenet kiírása piros színnel.
     * 
     * @param message A hiba leírása.
     */
    public static void error(String message) {
        System.out.println("> " + ANSI_RED + "ERROR: " + message + ANSI_RESET);
    }

    /**
     * Sikeres tesztüzenet kiírása zöld színnel.
     * 
     * @param message A sikeres művelet leírása.
     */
    public static void pass(String message) {
        System.out.println("> " + ANSI_GREEN + "[PASS]" + ANSI_RESET + " " + message);
    }

    /**
     * Sikertelen tesztüzenet kiírása piros színnel.
     * 
     * @param message A sikertelen művelet leírása.
     */
    public static void fail(String message) {
        System.out.println("> " + ANSI_RED + "[FAIL]" + ANSI_RESET + " " + message);
    }

    /**
     * Különbség (diff) kiírása sárga színnel.
     * 
     * @param message Az eltérés leírása.
     */
    public static void diff(String message) {
        System.out.println("> " + ANSI_YELLOW + message + ANSI_RESET);
    }

    /**
     * Súgó üzenet kiírása kék színnel.
     * 
     * @param message A súgó szövege.
     */
    public static void help(String message) {
        System.out.println("> " + ANSI_BLUE + message + ANSI_RESET);
    }
}
