package cli.commands;

import cli.Command;
import cli.ConsoleOutput;
import cli.ObjectRegistry;
import cli.Printable;

/**
 * A 'data' parancs megvalósítása.
 * Ezzel a paranccsal az objektumok összes belső adatát ki tudjuk írni a
 * konzolra. Ezt a parancsot használjuk az Assert (ellenőrzési) fázisban.
 */
public class DataCommand implements Command {
    private String[] parts;
    private ObjectRegistry registry;


    // --- KONSTRUKTOROK ---

    /**
     * Alapértelmezett konstruktor.
     */
    public DataCommand() {
    }

    /**
     * Paraméteres konstruktor a parancs inicializálásához.
     *
     * @param parts a felbontott bemeneti sor (pl. ["Lane", "data", "lane1"])
     * @param registry a központi memóriatérkép referenciája
     */
    public DataCommand(String[] parts, ObjectRegistry registry) {
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
     * @return igaz, ha a parancs pontosan 3 elemű
     */
    @Override
    public boolean validate() {
        if (parts == null || parts.length != 3) {
            ConsoleOutput.error("Invalid argument count. Expected: 3, Got: " + (parts == null ? 0 : parts.length));
            return false;
        }
        return true;
    }

    /**
     * Végrehajtja az adatlekérdezési logikát.
     * Formázottan a kimenetre írja az objektum adatait.
     */
    @Override
    public void execute() {
        String id = parts[2];

        try {
            Object targetObj = registry.getObject(id);

            Printable target = (Printable) targetObj;
            target.printData(id, registry);

        } catch (ClassCastException e) {
            ConsoleOutput.error("Object '" + id + "' does not support data printing.");
        } catch (Exception e) {
            ConsoleOutput.error(e.getMessage());
        }
    }
}