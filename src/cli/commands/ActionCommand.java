package cli.commands;

import cli.Actionable;
import cli.Command;
import cli.ConsoleOutput;
import cli.ObjectRegistry;

/**
 * Az 'action' parancs megvalósítása.
 * Ezekkel a parancsokkal a függvényhívásokat hajtjuk végre az objektumokon.
 * Ide tartozik a játékosok interakciója, a takarítás és a mozgás egyaránt.
 */
public class ActionCommand implements Command {
    private String[] parts;
    private ObjectRegistry registry;


    // --- KONSTRUKTOROK ---

    /**
     * Alapértelmezett konstruktor.
     */
    public ActionCommand() {
    }

    /**
     * Paraméteres konstruktor a parancs inicializálásához.
     *
     * @param parts a felbontott bemeneti sor (pl. ["Snowplow", "action", "sp1", "clearLane"])
     * @param registry a központi memóriatérkép referenciája
     */
    public ActionCommand(String[] parts, ObjectRegistry registry) {
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
     * Ellenőrzi, hogy a parancs paraméterei érvényesek-e.
     *
     * @return igaz, ha a parancs legalább 4 elemű
     */
    @Override
    public boolean validate() {
        if (parts == null || parts.length < 4) {
            ConsoleOutput.error("Invalid argument count. Expected: >=4, Got: " + (parts == null ? 0 : parts.length));
            return false;
        }
        return true;
    }

    /**
     * Végrehajtja az akciós logikát.
     * Kikeresi az objektumot és meghívja rajta a kért metódust, lekezelve az üzleti hibákat.
     */
    @Override
    public void execute() {
        String id = parts[2];
        String method = parts[3];

        String[] args = new String[parts.length - 4];
        System.arraycopy(parts, 4, args, 0, args.length);

        try {
            Object targetObj = registry.getObject(id);

            Actionable target = (Actionable) targetObj;
            target.performAction(method, args, registry);

        } catch (ClassCastException e) {
            ConsoleOutput.error("Object '" + id + "' does not support actions.");
        } catch (Exception e) {
            if (e.getMessage() != null && (e.getMessage().startsWith("Action failed:")
                    || e.getMessage().startsWith("Object not found:"))) {
                ConsoleOutput.error(e.getMessage());
            } else {
                ConsoleOutput.error("Unknown property or method: " + method);
            }
        }
    }
}