import cli.ConsoleOutput;
import cli.Parser;
import core.Game;
import core.GameRules;
import gui.swing.SwingGameApplication;
import java.util.Scanner;

/**
 * A program belepesi pontjat tartalmazo osztaly.
 */
public class Main {
    private static final int TEST_MODE = 0;
    private static final int GAME_MODE = 1;
    private static final Scanner CONSOLE_SCANNER = new Scanner(System.in);

    /**
     * A program alapertelmezetten grafikus modban indul.
     *
     * @param args parancssori argumentumok
     */
    public static void main(String[] args) {
        if (args != null && args.length > 0 && "--console".equalsIgnoreCase(args[0])) {
            if (runConsoleMode()) {
                new SwingGameApplication().start();
            }
            return;
        }

        new SwingGameApplication().start();
    }

    /**
     * Elinditja a GUI-bol valaszthato konzolos jatekmodot.
     *
     * @return igaz, ha a felhasznalo visszalepett a grafikus feluletre
     */
    public static boolean runConsoleMode() {
        String previousMap = GameRules.getMapFileName();
        GameRules.setMapFileName(GameRules.DEFAULT_CONSOLE_MAP_FILE);
        try {
            printConsoleModeHeader();
            return runInteractiveMode(GAME_MODE, CONSOLE_SCANNER);
        } finally {
            GameRules.setMapFileName(previousMap);
        }
    }

    /**
     * Tesztmod futtatasa fejlesztoi es automata validacios celra.
     *
     * @param scanner bemeneti olvaso
     */
    public static void runTestMode(Scanner scanner) {
        runInteractiveMode(TEST_MODE, scanner);
    }

    private static boolean runInteractiveMode(int mode, Scanner scanner) {
        Parser parser = new Parser(mode);
        registerConsoleDefaultPlayers(mode, parser);

        while (true) {
            System.out.print("> ");
            if (!scanner.hasNextLine()) {
                return false;
            }

            String line = scanner.nextLine().trim();
            ConsoleInputAction action = handleControlCommand(line, mode, parser);
            if (action == ConsoleInputAction.EXIT_APPLICATION) {
                return false;
            }
            if (action == ConsoleInputAction.BACK_TO_GRAPHICS) {
                clearConsole();
                return true;
            }
            if (action == ConsoleInputAction.EXECUTE_COMMAND) {
                parser.parseLine(toParserInput(mode, line));
            }
        }
    }

    private static void registerConsoleDefaultPlayers(int mode, Parser parser) {
        if (mode != GAME_MODE || parser == null) {
            return;
        }
        try {
            parser.registerCleanerPlayer("Konzol hókotrós");
            parser.registerBusDriverPlayer("Konzol buszsofőr");
            Game game = parser.getPrimaryGame();
            if (game != null) {
                game.initializeTurns(parser.getRegistry(), true);
            }
        } catch (Exception exception) {
            ConsoleOutput.error("Console player setup failed: " + exception.getMessage());
        }
    }

    private static ConsoleInputAction handleControlCommand(String line, int mode, Parser parser) {
        if (line == null || line.isEmpty()) {
            return ConsoleInputAction.HANDLED;
        }
        if (line.equalsIgnoreCase("exit") || line.equalsIgnoreCase("e")) {
            return ConsoleInputAction.EXIT_APPLICATION;
        }
        if (line.equalsIgnoreCase("back") || line.equalsIgnoreCase("b")) {
            return ConsoleInputAction.BACK_TO_GRAPHICS;
        }
        if (line.equalsIgnoreCase("help") || line.equalsIgnoreCase("h")) {
            printModeHelp();
            return ConsoleInputAction.HANDLED;
        }
        if (line.equalsIgnoreCase("command") || line.equalsIgnoreCase("c")) {
            printModeCommands(mode, parser);
            return ConsoleInputAction.HANDLED;
        }
        return ConsoleInputAction.EXECUTE_COMMAND;
    }

    private static String toParserInput(int mode, String line) {
        if (mode == TEST_MODE) {
            return normalizeTestModeInput(line);
        }
        return line;
    }

    private static void printConsoleModeHeader() {
        System.out.println();
        ConsoleOutput.help("Console mode started with fixed map: " + GameRules.DEFAULT_CONSOLE_MAP_FILE);
        ConsoleOutput.help("b/back - Return to Graphic Mode");
        ConsoleOutput.help("h/help - Show console help");
        ConsoleOutput.help("e/exit - Exit application");
        System.out.println();
    }

    /**
     * Kiirja az aktualis futasi modhoz tartozo sugouzenetet.
     */
    private static void printModeHelp() {
        System.out.println();
        System.out.println("Available commands in this mode:");
        ConsoleOutput.help("c - Show mode commands");
        ConsoleOutput.help("h - Show this help");
        ConsoleOutput.help("b - Return to Graphic Mode");
        ConsoleOutput.help("e - Exit program");
        System.out.println();
    }

    private static void printModeCommands(int mode, Parser parser) {
        System.out.println();
        System.out.println("Mode-specific commands:");

        if (mode == TEST_MODE) {
            ConsoleOutput.help("<testName> - Futtat egy tesztesetet (pl. test-bus-move-clean)");
            System.out.println();
            return;
        }

        String role = parser == null ? "Unknown" : parser.getCurrentGamePlayerRole();
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

    private static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static String normalizeTestModeInput(String line) {
        if (line == null) {
            return null;
        }

        String trimmed = line.trim();
        if (trimmed.isEmpty() || trimmed.contains(",")) {
            return trimmed;
        }
        return "test,run," + trimmed;
    }

    private enum ConsoleInputAction {
        EXECUTE_COMMAND,
        HANDLED,
        BACK_TO_GRAPHICS,
        EXIT_APPLICATION
    }
}
