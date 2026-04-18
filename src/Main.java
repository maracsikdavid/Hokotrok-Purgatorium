import cli.Parser;
import cli.ConsoleOutput;
import java.util.Scanner;

/**
 * A program belépési pontját tartalmazó osztály.
 */
public class Main {
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

                if (line.equalsIgnoreCase("exit")) {
                    return;
                }

                if (line.equalsIgnoreCase("back")) {
                    clearConsole();
                    backToMenu = true;
                    continue;
                }

                if (line.equalsIgnoreCase("help")) {
                    printModeHelp(mode);
                    continue;
                }

                parser.parseLine(line);
            }
        }
    }

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

            if (modeInput.equalsIgnoreCase("exit")) {
                return null;
            }

            if (modeInput.equalsIgnoreCase("help")) {
                printMainHelp();
                System.out.print("Select mode: ");
                continue;
            }

            if (modeInput.equalsIgnoreCase("back")) {
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

    private static void printModePrompt() {
        System.out.println("\n--- Choose execution mode ---");
        System.out.println("[0] Test      (Deterministic)");
        System.out.println("[1] Game  (Non-Deterministic)");
        System.out.println("--- --------------------- ---");
        System.out.print("Select mode: ");
    }

    private static void printMainHelp() {
        System.out.println("--- ------------------ ---");
        System.out.println("Available commands here:");
        ConsoleOutput.help("0      - Enter Test mode");
        ConsoleOutput.help("1      - Enter Game mode");
        ConsoleOutput.help("help   - Show this help");
        ConsoleOutput.help("exit   - Exit program");
        System.out.println("--- ------------------ ---");
    }

    private static void printModeHelp(int mode) {
        System.out.println("--- ------------------------------------ ---");
        System.out.println("Available commands in this mode:");
        ConsoleOutput.help("help - Show this help");
        ConsoleOutput.help("back - Return to mode selector");
        ConsoleOutput.help("exit - Exit program");

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

    private static void clearConsole() {
        // ANSI clear screen + cursor home (works in modern terminals)
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}