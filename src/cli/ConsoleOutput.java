package cli;

import java.util.ArrayList;
import java.util.List;

/**
 * Egységes konzol kimeneti segéd a színezett státuszokhoz.
 */
public final class ConsoleOutput {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_ORANGE = "\u001B[38;5;208m";
    private static final String ANSI_PURPLE = "\u001B[35m";

    private static boolean testMode = false;
    private static final List<OutputListener> OUTPUT_LISTENERS = new ArrayList<>();

    public enum OutputType {
        SUCCESS,
        ERROR,
        WARNING,
        HELP,
        INFO
    }

    @FunctionalInterface
    public interface OutputListener {
        void onOutput(OutputType type, String message);
    }

    private ConsoleOutput() {
    }

    /**
     * Beállítja az test módot.
     * Test módban az OK/ERROR sorok nem nyomtatódnak, csak az adatok.
     */
    public static void setTestMode(boolean mode) {
        testMode = mode;
    }

    /**
     * Jelzi, hogy a rendszer éppen teszt módban fut-e.
     *
     * @return igaz, ha teszt mód aktív
     */
    public static boolean isTestMode() {
        return testMode;
    }

    /**
     * Feliratkozik a konzolra írt üzenetekre.
     *
     * @param listener az eseményfigyelő
     */
    public static void addOutputListener(OutputListener listener) {
        if (listener == null) {
            return;
        }
        synchronized (OUTPUT_LISTENERS) {
            if (!OUTPUT_LISTENERS.contains(listener)) {
                OUTPUT_LISTENERS.add(listener);
            }
        }
    }

    /**
     * Leiratkozik a konzolüzenet figyeléséről.
     *
     * @param listener az eseményfigyelő
     */
    public static void removeOutputListener(OutputListener listener) {
        synchronized (OUTPUT_LISTENERS) {
            OUTPUT_LISTENERS.remove(listener);
        }
    }

    /**
     * Sikeres művelet visszajelzése.
     * Az test módban nem nyomtat.
     */
    public static void ok() {
        if (!testMode) {
            System.out.println("> " + ANSI_GREEN + "OK" + ANSI_RESET);
        }
        notifyOutput(OutputType.SUCCESS, "OK");
    }

    /**
     * Hibaüzenet kiírása.
     * Az test módban csak az üzenet nyomtat ANSI kódok nélkül.
     * 
     * @param message A hiba leírása.
     */
    public static void error(String message) {
        if (testMode) {
            System.out.println("> ERROR: " + message);
        } else {
            System.out.println("> " + ANSI_RED + "ERROR: " + message + ANSI_RESET);
        }
        notifyOutput(OutputType.ERROR, message);
    }

    /**
     * Sikeres tesztüzenet kiírása zöld színnel.
     * 
     * @param message A sikeres művelet leírása.
     */
    public static void pass(String message) {
        System.out.println("> " + ANSI_GREEN + "[PASS]" + ANSI_RESET + " " + message);
        notifyOutput(OutputType.SUCCESS, message);
    }

    /**
     * Sikertelen tesztüzenet kiírása piros színnel.
     * 
     * @param message A sikertelen művelet leírása.
     */
    public static void fail(String message) {
        System.out.println("> " + ANSI_RED + "[FAIL]" + ANSI_RESET + " " + message);
        notifyOutput(OutputType.ERROR, message);
    }

    /**
     * Különbség (diff) kiírása sárga színnel.
     * 
     * @param message Az eltérés leírása.
     */
    public static void diff(String message) {
        System.out.println("> " + ANSI_YELLOW + message + ANSI_RESET);
        notifyOutput(OutputType.WARNING, message);
    }

    /**
     * Súgó üzenet kiírása kék színnel.
     * 
     * @param message A súgó szövege.
     */
    public static void help(String message) {
        System.out.println("> " + ANSI_BLUE + message + ANSI_RESET);
        notifyOutput(OutputType.HELP, message);
    }

    /**
     * Ures sor kiirasa blokk-elvalasztashoz.
     */
    public static void blankLine() {
        if (!testMode) {
            System.out.println();
        }
    }

    /**
     * Figyelmezteto uzenet kiirasa sarga szinnel.
     *
     * @param message a figyelmeztetes szovege
     */
    public static void warning(String message) {
        if (testMode) {
            System.out.println("> " + message);
        } else {
            System.out.println("> " + ANSI_YELLOW + message + ANSI_RESET);
        }
        notifyOutput(OutputType.WARNING, message);
    }

    /**
     * Semleges (színezés nélküli) információ kiírása.
     *
     * @param message A kiírandó üzenet.
     */
    public static void plain(String message) {
        System.out.println("> " + message);
        notifyOutput(OutputType.INFO, message);
    }

    /**
     * Sikeres visszajelzés kiírása zöld színnel.
     *
     * @param message A sikeres művelet leírása.
     */
    public static void success(String message) {
        if (testMode) {
            System.out.println("> " + message);
        } else {
            System.out.println("> " + ANSI_GREEN + message + ANSI_RESET);
        }
        notifyOutput(OutputType.SUCCESS, message);
    }

    /**
     * Buszhoz és utcához kapcsolódó információ kiírása narancssárga színnel.
     *
     * @param message A kiírandó üzenet.
     */
    public static void bus(String message) {
        System.out.println("> " + ANSI_ORANGE + message + ANSI_RESET);
        notifyOutput(OutputType.INFO, message);
    }

    /**
     * Hókotróhoz kapcsolódó információ kiírása lila színnel.
     *
     * @param message A kiírandó üzenet.
     */
    public static void snowplow(String message) {
        System.out.println("> " + ANSI_PURPLE + message + ANSI_RESET);
        notifyOutput(OutputType.INFO, message);
    }

    /**
     * Vegeredmeny jatekos fejlecenek szerepkor szerinti szinezese.
     *
     * @param message a kiirando eredmenysor
     */
    public static void resultPlayer(String message) {
        if (testMode) {
            System.out.println("> " + message);
        } else if (message != null && message.contains("Busz sofőr")) {
            System.out.println("> " + ANSI_ORANGE + message + ANSI_RESET);
        } else if (message != null && message.contains("Hókotrós")) {
            System.out.println("> " + ANSI_PURPLE + message + ANSI_RESET);
        } else {
            System.out.println("> " + ANSI_GREEN + message + ANSI_RESET);
        }
        notifyOutput(OutputType.INFO, message);
    }

    /**
     * key=value sor kiírása, ahol csak az érték narancssárga (busz).
     *
     * @param key A kulcs neve (pl. "name").
     * @param value Az érték.
     */
    public static void keyValueBus(String key, String value) {
        String message = key + "=" + value;
        if (testMode) {
            System.out.println("> " + message);
        } else {
            System.out.println("> " + key + "=" + ANSI_ORANGE + value + ANSI_RESET);
        }
        notifyOutput(OutputType.INFO, message);
    }

    /**
     * key=value sor kiírása, ahol csak az érték lila (snowplow/cleaner).
     *
     * @param key A kulcs neve (pl. "wallet").
     * @param value Az érték.
     */
    public static void keyValueSnowplow(String key, String value) {
        String message = key + "=" + value;
        if (testMode) {
            System.out.println("> " + message);
        } else {
            System.out.println("> " + key + "=" + ANSI_PURPLE + value + ANSI_RESET);
        }
        notifyOutput(OutputType.INFO, message);
    }

    /**
     * Szerepkör-specifikus információ kiírása.
     * BusDriver esetén narancs, Cleaner esetén zöld színnel jelenik meg.
     *
     * @param role A szerepkör neve (pl. "BusDriver", "Cleaner").
     * @param message A kiírandó üzenet.
     */
    public static void roleInfo(String role, String message) {
        if ("BusDriver".equals(role)) {
            bus(message);
        } else if ("Cleaner".equals(role)) {
            snowplow(message);
        } else {
            plain(message);
        }
    }

    private static void notifyOutput(OutputType type, String message) {
        OutputListener[] listenersSnapshot;
        synchronized (OUTPUT_LISTENERS) {
            listenersSnapshot = OUTPUT_LISTENERS.toArray(new OutputListener[0]);
        }
        for (OutputListener listener : listenersSnapshot) {
            listener.onOutput(type, message);
        }
    }
}
