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
                    printModeHelp();
                    continue;
                }

                if (line.equalsIgnoreCase("command") || line.equalsIgnoreCase("c")) {
                    printModeCommands(mode, parser);
                    continue;
                }

                if (mode == 0) {
                    line = normalizeTestModeInput(line);
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
        System.out.println("[0] Test Mode");
        System.out.println("[1] Game Mode");
        System.out.println("--- --------------------- ---");
        System.out.print("Select mode (h for help): ");
    }

    /**
     * Kiírja az alapvető súgó üzenetet a módválasztóhoz.
     */
    private static void printMainHelp() {
        System.out.println();
        System.out.println("Available commands here:");
        ConsoleOutput.help("0 - Enter Test Mode");
        ConsoleOutput.help("1 - Enter Game Mode");
        ConsoleOutput.help("h - Show this help");
        ConsoleOutput.help("e - Exit program");
        System.out.println();
    }

    /**
     * Kiírja az aktuális futási módhoz tartozó súgó üzenetet.
     * 
     * @param mode Az aktuális futási mód.
     */
    private static void printModeHelp() {
        System.out.println();
        System.out.println("Available commands in this mode:");
        ConsoleOutput.help("c - Show mode commands");
        ConsoleOutput.help("h - Show this help");
        ConsoleOutput.help("b - Return to mode selector");
        ConsoleOutput.help("e - Exit program");

        System.out.println();
    }

    /**
     * Kiírja az aktuális futási módhoz tartozó ténylegesen használható parancsokat.
     *
     * @param mode Az aktuális futási mód.
     * @param parser Az aktuális parser példány (Game módban körszerepkör lekéréséhez).
     */
    private static void printModeCommands(int mode, Parser parser) {
        System.out.println();
        System.out.println("Mode-specific commands:");

        if (mode == 0) {
            ConsoleOutput.help("<testName> - Futtat egy tesztesetet (pl. test-bus-move-clean)");
            System.out.println();

        } else {
            String role = (parser == null) ? "Unknown" : parser.getCurrentGamePlayerRole();
            ConsoleOutput.help("s - Display the current player's status");
            ConsoleOutput.help("w - Show where the player can move next");
            if ("BusDriver".equals(role)) {
                ConsoleOutput.help("bus,<busID>,<roadID>,<laneID>");
            } else {
                ConsoleOutput.help("buy,<shopID>,<ShopItem>");
                ConsoleOutput.help("equip,<snowplowID>,<plowID>");
                ConsoleOutput.help("refill,<snowplowID>,<consumableID>");
                ConsoleOutput.help("plow,<snowplowID>,<roadID>,<laneID>");
            }
            System.out.println();
        }
    }

    /**
     * Letisztítja a konzolt (ANSI escape szekvenciával).
     */
    private static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Test módban egyszerűsíti a felhasználói bemenetet:
     * - "<tesztnev>" -> "test,run,<tesztnev>"
     * A teljes, vesszős formátumot változatlanul hagyja.
     *
     * @param line A nyers felhasználói bemenet.
     * @return A parser számára normalizált parancs.
     */
    private static String normalizeTestModeInput(String line) {
        if (line == null) {
            return null;
        }

        String trimmed = line.trim();
        if (trimmed.isEmpty()) {
            return trimmed;
        }
        if (trimmed.contains(",")) {
            return trimmed;
        }
        return "test,run," + trimmed;
    }
}