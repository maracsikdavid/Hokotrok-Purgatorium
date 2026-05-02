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
    private static final String ANSI_ORANGE = "\u001B[38;5;208m";
    private static final String ANSI_PURPLE = "\u001B[35m";

    private static boolean testMode = false;

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
     * Sikeres művelet visszajelzése.
     * Az test módban nem nyomtat.
     */
    public static void ok() {
        if (!testMode) {
            System.out.println("> " + ANSI_GREEN + "OK" + ANSI_RESET);
        }
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

    /**
     * Semleges (színezés nélküli) információ kiírása.
     *
     * @param message A kiírandó üzenet.
     */
    public static void plain(String message) {
        System.out.println("> " + message);
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
    }

    /**
     * Buszhoz és utcához kapcsolódó információ kiírása narancssárga színnel.
     *
     * @param message A kiírandó üzenet.
     */
    public static void bus(String message) {
        System.out.println("> " + ANSI_ORANGE + message + ANSI_RESET);
    }

    /**
     * Hókotróhoz kapcsolódó információ kiírása lila színnel.
     *
     * @param message A kiírandó üzenet.
     */
    public static void snowplow(String message) {
        System.out.println("> " + ANSI_PURPLE + message + ANSI_RESET);
    }

    /**
     * key=value sor kiírása, ahol csak az érték narancssárga (busz).
     *
     * @param key A kulcs neve (pl. "name").
     * @param value Az érték.
     */
    public static void keyValueBus(String key, String value) {
        if (testMode) {
            System.out.println("> " + key + "=" + value);
        } else {
            System.out.println("> " + key + "=" + ANSI_ORANGE + value + ANSI_RESET);
        }
    }

    /**
     * key=value sor kiírása, ahol csak az érték lila (snowplow/cleaner).
     *
     * @param key A kulcs neve (pl. "wallet").
     * @param value Az érték.
     */
    public static void keyValueSnowplow(String key, String value) {
        if (testMode) {
            System.out.println("> " + key + "=" + value);
        } else {
            System.out.println("> " + key + "=" + ANSI_PURPLE + value + ANSI_RESET);
        }
    }

    /**
     * Szerepkör-specifikus információ kiírása.
     * BusDriver esetén narancs, Cleaner esetén zöld színnel jelenik meg.
     *
     * @param role A szerepkör neve (pl. "BusDriver", "Cleaner").
     * @param message A kiírandó üzenet.
     */
    public static void roleInfo(String role, String message) {
        plain(message);
    }
}
