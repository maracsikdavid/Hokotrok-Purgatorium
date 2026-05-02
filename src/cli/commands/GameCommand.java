package cli.commands;

import cli.Command;
import cli.ConsoleOutput;
import cli.ObjectRegistry;
import actors.BusDriver;
import actors.Cleaner;
import core.Game;
import actors.Player;

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
            Game game = findGame();
            if (game == null) {
                throw new Exception("Action failed: Game instance not found.");
            }
            Player current = game.getCurrentPlayer(registry);
            if (current == null) {
                throw new Exception("Action failed: No active player in Game mode.");
            }

            String[] actionParts;

            switch (alias) {
                case "buy":
                    requireCleanerTurn(current, alias);
                    requireArgs(alias, 3);
                    actionParts = new String[]{"Cleaner", "action", registry.findId(current), "buyItem", parts[1], parts[2]};
                    break;
                case "equip":
                    requireCleanerTurn(current, alias);
                    requireArgs(alias, 3);
                    actionParts = new String[]{"Cleaner", "action", registry.findId(current), "equipPlowToSnowplow", parts[1], parts[2]};
                    break;
                case "plow":
                    requireCleanerTurn(current, alias);
                    requireArgs(alias, 4);
                    actionParts = new String[]{"Cleaner", "action", registry.findId(current), "commandSnowplow", parts[1], parts[2], parts[3]};
                    break;
                case "bus":
                    requireBusDriverTurn(current, alias);
                    requireArgs(alias, 4);
                    actionParts = new String[]{"BusDriver", "action", registry.findId(current), "commandBus", parts[1], parts[2], parts[3]};
                    break;
                case "refill":
                    requireCleanerTurn(current, alias);
                    requireArgs(alias, 3);
                    actionParts = new String[]{"Cleaner", "action", registry.findId(current), "refillPlow", parts[1], parts[2]};
                    break;
                case "status":
                    actionParts = new String[]{"Game", "action", registry.findId(game), "status"};
                    ActionCommand statusCommand = new ActionCommand(actionParts, registry);
                    if (statusCommand.validate()) {
                        statusCommand.execute();
                    }
                    return;
                default:
                    throw new Exception("Unknown game alias: " + alias);
            }

            ActionCommand delegatedCommand = new ActionCommand(actionParts, registry);
            if (delegatedCommand.validate()) {
                delegatedCommand.execute();
                if (delegatedCommand.wasSuccessful() && ("bus".equals(alias) || "plow".equals(alias))) {
                    game.finishTurn(registry);
                }
            }

        } catch (Exception e) {
            ConsoleOutput.error(e.getMessage());
        }
    }

    /**
     * Ellenőrzi, hogy Cleaner körben vagyunk-e.
     */
    private void requireCleanerTurn(Player current, String alias) throws Exception {
        if (!current.isCleaner()) {
            throw new Exception("Action failed: '" + alias + "' command is only allowed for Cleaner turn.");
        }
    }

    /**
     * Ellenőrzi, hogy BusDriver körben vagyunk-e.
     */
    private void requireBusDriverTurn(Player current, String alias) throws Exception {
        if (!current.isBusDriver()) {
            throw new Exception("Action failed: '" + alias + "' command is only allowed for BusDriver turn.");
        }
    }

    /**
     * Argumentumszám ellenőrzése alias parancsokhoz.
     */
    private void requireArgs(String alias, int expectedLength) throws Exception {
        if (parts == null || parts.length != expectedLength) {
            throw new Exception("Invalid argument count for '" + alias + "'.");
        }
    }

    /**
     * Visszaadja az első Game példányt a regiszterből.
     */
    private Game findGame() {
        for (Object obj : registry.getObjects().values()) {
            try {
                return (Game) obj;
            } catch (ClassCastException ignored) {
            }
        }
        return null;
    }

}