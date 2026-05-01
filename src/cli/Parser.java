package cli;

import cli.commands.ActionCommand;
import cli.commands.CreateCommand;
import cli.commands.DataCommand;
import cli.commands.LinkCommand;
import cli.commands.TestCommand;
import java.util.HashMap;
import java.util.Map;

/**
 * A bemeneti parancsok (sorok) értelmezéséért felelős központi osztály.
 * Felbontja a kapott sorokat, kikeresi a megfelelő {@link CommandFactory}-t, 
 * legyártja a parancsot, majd menedzseli a validálást és futtatást.
 */
public class Parser {
    private static final int MODE_UNRESTRICTED = -1;

    private ObjectRegistry registry;
    private Map<String, CommandFactory> factories;
    private int mode;


    // --- KONSTRUKTOROK ---

    /**
     * Konstruktor, amely inicializálja a memóriatérképet és a Factory-kat.
     */
    public Parser() {
        this.registry = new ObjectRegistry();
        this.factories = new HashMap<>();
        this.mode = MODE_UNRESTRICTED;
    }

    /**
     * Paraméteres konstruktor futási módhoz.
     *
     * @param mode a futási mód (0: test, 1: játék)
     */
    public Parser(int mode) {
        this();
        this.mode = mode;
    }

    /**
     * Paraméteres konstruktor minden attribútummal.
     *
     * @param registry az objektumregiszter
     * @param factories a parancsgyárak tárolója
     */
    public Parser(ObjectRegistry registry, Map<String, CommandFactory> factories) {
        this.registry = registry;
        this.factories = factories;
        this.mode = MODE_UNRESTRICTED;
    }


    // --- GETTEREK ÉS SETTEREK ---

    /**
     * Visszaadja az objektumregisztert, amely a beolvasott objektumokat tárolja.
     *
     * @return Az objektumregiszter referenciája.
     */
    public ObjectRegistry getRegistry() {
        return registry;
    }

    /**
     * Beállítja az objektumregisztert.
     *
     * @param registry A beállítandó regiszter.
     */
    public void setRegistry(ObjectRegistry registry) {
        this.registry = registry;
    }

    /**
     * Visszaadja a parancsgyárakat tartalmazó tárolót.
     *
     * @return A gyártípusokat és Factory-kat tartalmazó Map.
     */
    public Map<String, CommandFactory> getFactories() {
        return factories;
    }

    /**
     * Beállítja a parancsgyárakat.
     *
     * @param factories A beállítandó Factory Map.
     */
    public void setFactories(Map<String, CommandFactory> factories) {
        this.factories = factories;
    }

    /**
     * Visszaadja az aktuális futási módot.
     *
     * @return A mód értéke (0: test, 1: game, -1: unrestricted).
     */
    public int getMode() {
        return mode;
    }

    /**
     * Beállítja az aktuális futási módot.
     *
     * @param mode A beállítandó módérték.
     */
    public void setMode(int mode) {
        this.mode = mode;
    }


    // --- METÓDUSOK ---
    
    /**
     * Egyetlen bemeneti sor feldolgozása. Kiszűri a kommenteket és az üres sorokat, 
     * ellenőrzi a futási módot, majd a validálás után végrehajtja a parancsot. 
     * Hibás bemenet esetén hibaüzenetet ír a konzolra.
     * 
     * @param line A feldolgozandó bemeneti sor.
     */
    public void parseLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return;
        }

        String trimmed = line.trim();

        // Kommentek kiszűrése
        if (trimmed.startsWith("#") || trimmed.startsWith("//")) {
            return;
        }

        String[] parts = trimmed.split(",");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }

        if (parts.length >= 1) {
            boolean isTestCommand = "test".equals(parts[0]);
            if (mode == 0 && !isTestCommand) {
                ConsoleOutput.error("Only 'test' commands are allowed in Test mode.");
                return;
            }
            if (mode == 1 && isTestCommand) {
                ConsoleOutput.error("Test commands are not allowed in Game mode.");
                return;
            }
        }

        try {
            Command command = createCommand(parts);
            if (command != null && command.validate()) {
                command.execute();
            }
        } catch (Exception e) {
            ConsoleOutput.error(e.getMessage());
        }
    }

    /**
     * A megfelelő Command objektum létrehozása a parancs típusa és a bemeneti darabok alapján.
     * 
     * @param parts A felbontott bemeneti sor elemei.
     * @return A létrehozott parancsobjektum.
     * @throws Exception Ha a parancs formátuma érvénytelen vagy a típusa ismeretlen.
     */
    private Command createCommand(String[] parts) throws Exception {
        if (parts.length < 2) {
            throw new Exception("Invalid command format.");
        }

        if ("test".equals(parts[0])) {
            TestRunner testRunner = new TestRunner(this);
            return new TestCommand(parts, testRunner);
        }

        String commandType = parts[1];

        switch (commandType) {
            case "create":
                return new CreateCommand(parts, registry);
            case "action":
                return new ActionCommand(parts, registry);
            case "link":
                return new LinkCommand(parts, registry);
            case "data":
                return new DataCommand(parts, registry);
            default:
                throw new Exception("Unknown command type: " + commandType);
        }
    }
}