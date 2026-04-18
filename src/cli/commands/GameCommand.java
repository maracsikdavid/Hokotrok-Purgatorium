package cli.commands;

import cli.Command;
import cli.ConsoleOutput;
import cli.ObjectRegistry;

/**
 * A 'game' parancs megvalósítása.
 * Interaktív Játék módban használható egyszerűsített (alias) parancsokat 
 * alakítja át szabványos 'action' parancsokká a játékosok kényelme érdekében.
 */
public class GameCommand implements Command {
    private String[] parts;
    private ObjectRegistry registry;


    // --- KONSTRUKTOROK ---

    /**
     * Alapértelmezett konstruktor.
     */
    public GameCommand() {
    }

    /**
     * Paraméteres konstruktor a parancs inicializálásához.
     *
     * @param parts a felbontott bemeneti sor (pl. ["buy", "shop", "Salt"])
     * @param registry a központi memóriatérkép referenciája
     */
    public GameCommand(String[] parts, ObjectRegistry registry) {
        this.parts = parts;
        this.registry = registry;
    }


    // --- GETTEREK ÉS SETTEREK ---

    /**
     * Visszaadja a felbontott bemeneti sort.
     *
     * @return a parancs elemeit tartalmazó tömb
     */
    public String[] getParts() {
        return parts;
    }

    /**
     * Beállítja a felbontott bemeneti sort.
     *
     * @param parts a beállítandó string tömb
     */
    public void setParts(String[] parts) {
        this.parts = parts;
    }

    /**
     * Visszaadja a parancshoz tartozó objektumregisztert.
     *
     * @return a központi memóriatérkép
     */
    public ObjectRegistry getRegistry() {
        return registry;
    }

    /**
     * Beállítja a parancshoz tartozó objektumregisztert.
     *
     * @param registry a beállítandó memóriatérkép
     */
    public void setRegistry(ObjectRegistry registry) {
        this.registry = registry;
    }


    // --- METÓDUSOK ---

    /**
     * Ellenőrzi, hogy az alias parancs paraméterei érvényesek-e.
     *
     * @return igaz, ha az alias felbontható
     */
    @Override
    public boolean validate() {
        if (parts == null || parts.length < 1) {
            ConsoleOutput.error("Invalid command format.");
            return false;
        }
        return true;
    }

    /**
     * Lefordítja az aliast a megfelelő teljes action parancsra és végrehajtja azt.
     */
    @Override
    public void execute() {
        String alias = parts[0];

        try {
            // TODO: Megállapítani az aktuális játékos ID-ját (pl. state machine-ből)
            String currentPlayerId = "player1"; 
            
            String[] actionParts;

            // Alias fordítási logika (Adapter)
            switch (alias) {
                case "buy":
                    actionParts = new String[]{"Cleaner", "action", currentPlayerId, "buyItem", parts[1], parts[2]};
                    break;
                case "equip":
                    actionParts = new String[]{"Snowplow", "action", parts[1], "equipPlow", parts[2]};
                    break;
                case "status":
                    actionParts = new String[]{"Cleaner", "data", currentPlayerId};
                    // Speciális eset: a status inkább data hívás
                    new DataCommand(actionParts, registry).execute();
                    return;
                default:
                    throw new Exception("Unknown game alias: " + alias);
            }

            // A legyártott teljes parancs átadása az ActionCommand-nak
            ActionCommand delegatedCommand = new ActionCommand(actionParts, registry);
            if (delegatedCommand.validate()) {
                delegatedCommand.execute();
            }

        } catch (Exception e) {
            ConsoleOutput.error(e.getMessage());
        }
    }
}