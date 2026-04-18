package cli;

/**
 * Egységes konzol kimeneti segéd a színezett státuszokhoz.
 */
public final class ConsoleOutput {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_BLUE = "\u001B[34m";

    private ConsoleOutput() {
    }

    public static void error(String message) {
        System.out.println("> " + ANSI_RED + "[ERROR]" + ANSI_RESET + " " + message);
    }

    public static void pass(String message) {
        System.out.println("> " + ANSI_GREEN + "[PASS]" + ANSI_RESET + " " + message);
    }

    public static void fail(String message) {
        System.out.println("> " + ANSI_RED + "[FAIL]" + ANSI_RESET + " " + message);
    }

    public static void diff(String message) {
        System.out.println("> " + message);
    }

    public static void help(String message) {
        System.out.println("> " + ANSI_BLUE + message + ANSI_RESET);
    }
}