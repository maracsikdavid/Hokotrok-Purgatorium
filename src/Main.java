import cli.Parser;
import cli.ConsoleOutput;
import java.util.Scanner;

/**
 * A program belépési pontját tartalmazó osztály.
 */
public class Main {
    /**
     * A program belépési pontja. Kezeli a módválasztó menüt és az interaktív parancssort.
     * 
     * @param args Parancssori argumentumok (nem használt).
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            Integer mode = selectMode(scanner);
            if (mode == null) {
                return;
            }

            Parser parser = new Parser(mode);
            boolean backToMenu = false;

            while (!backToMenu) {
                System.out.print("> ");
                if (!scanner.hasNextLine()) {
                    return;
                }

                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }

                if (line.equalsIgnoreCase("exit") || line.equalsIgnoreCase("e")) {
                    return;
                }

                if (line.equalsIgnoreCase("back") || line.equalsIgnoreCase("b")) {
                    clearConsole();
                    backToMenu = true;
                    continue;
                }

                if (line.equalsIgnoreCase("help") || line.equalsIgnoreCase("h")) {
                    printModeHelp(mode);
                    continue;
                }

                parser.parseLine(line);
            }
        }
    }

    /**
     * Megjeleníti a módválasztó menüt és beolvassa a felhasználó választását.
     * 
     * @param scanner A bemeneti olvasó.
     * @return A választott mód (0: teszt, 1: játék), vagy null a kilépéshez.
     */
    private static Integer selectMode(Scanner scanner) {
        printModePrompt();

        while (true) {
            if (!scanner.hasNextLine()) {
                return null;
            }

            String modeInput = scanner.nextLine().trim();
            if (modeInput.isEmpty()) {
                continue;
            }

            if (modeInput.equalsIgnoreCase("exit") || modeInput.equalsIgnoreCase("e")) {
                return null;
            }

            if (modeInput.equalsIgnoreCase("help") || modeInput.equalsIgnoreCase("h")) {
                printMainHelp();
                System.out.print("Select mode: ");
                continue;
            }

            if (modeInput.equalsIgnoreCase("back") || modeInput.equalsIgnoreCase("b")) {
                ConsoleOutput.help("Already in mode selector.");
                System.out.print("Select mode: ");
                continue;
            }

            try {
                int parsedMode = Integer.parseInt(modeInput);
                if (parsedMode == 0 || parsedMode == 1) {
                    return parsedMode;
                }

                ConsoleOutput.error("Out of range: " + modeInput);
                System.out.println();
                printModePrompt();
            } catch (NumberFormatException e) {
                ConsoleOutput.error("Invalid argument type: " + modeInput);
                System.out.println();
                printModePrompt();
            }
        }
    }

    /**
     * Kiírja a módválasztó promptot a konzolra.
     */
    private static void printModePrompt() {
        System.out.println("\n--- Choose execution mode ---");
        System.out.println("[0] Test      (Deterministic)");
        System.out.println("[1] Game  (Non-Deterministic)");
        System.out.println("--- --------------------- ---");
        System.out.print("Select mode (h for help): ");
    }

    /**
     * Kiírja az alapvető súgó üzenetet a módválasztóhoz.
     */
    private static void printMainHelp() {
        System.out.println("--- ------------------ ---");
        System.out.println("Available commands here:");
        ConsoleOutput.help("0          - Enter Test mode");
        ConsoleOutput.help("1          - Enter Game mode");
        ConsoleOutput.help("help (h)   - Show this help");
        ConsoleOutput.help("exit (e)   - Exit program");
        System.out.println("--- ------------------ ---");
    }

    /**
     * Kiírja az aktuális futási módhoz tartozó súgó üzenetet.
     * 
     * @param mode Az aktuális futási mód.
     */
    private static void printModeHelp(int mode) {
        System.out.println("--- ------------------------------------ ---");
        System.out.println("Available commands in this mode:");
        ConsoleOutput.help("help (h) - Show this help");
        ConsoleOutput.help("back (b) - Return to mode selector");
        ConsoleOutput.help("exit (e) - Exit program");

        if (mode == 0) {
            ConsoleOutput.help("test,run,<testName> - Run a test case");
            System.out.println("--- ------------------------------------ ---");

        } else {
            ConsoleOutput.help("<Category>,create,<id>");
            ConsoleOutput.help("<Category>,link,<id>,<property>,<args...>");
            ConsoleOutput.help("<Category>,action,<id>,<method>,<args...>");
            ConsoleOutput.help("<Category>,data,<id>");
            System.out.println("--- ------------------------------------ ---");
        }
    }

    /**
     * Letisztítja a konzolt (ANSI escape szekvenciával).
     */
    private static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}