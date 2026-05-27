package cli.commands;

import cli.Command;
import cli.ConsoleOutput;
import cli.Linkable;
import cli.ObjectRegistry;

/**
 * A 'link' parancs megvalósítása.
 * Ezzel a parancstípussal az objektumok állapotát vagy a topológiai kapcsolatokat
 * lehetséges beállítani (pl. sávok összekötése, járművek elhelyezése).
 * A teszt-környezetek "Arrange" fázisának felépítésére szolgál.
 */
public class LinkCommand implements Command {
    private String[] parts;
    private ObjectRegistry registry;



    /**
     * Alapértelmezett konstruktor.
     */
    public LinkCommand() {
    }

    /**
     * Paraméteres konstruktor a parancs inicializálásához.
     *
     * @param parts a felbontott bemeneti sor (pl. ["Road", "link", "road1", "addLane", "lane1"])
     * @param registry a központi memóriatérkép referenciája
     */
    public LinkCommand(String[] parts, ObjectRegistry registry) {
        this.parts = parts;
        this.registry = registry;
    }



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
     * @return A központi memóriatérkép referenciája.
     */
    public ObjectRegistry getRegistry() {
        return registry;
    }

    /**
     * Beállítja a parancshoz tartozó objektumregisztert.
     *
     * @param registry A beállítandó memóriatérkép.
     */
    public void setRegistry(ObjectRegistry registry) {
        this.registry = registry;
    }



    /**
     * Ellenőrzi, hogy a parancs paraméterei érvényesek-e.
     *
     * @return igaz, ha a parancs legalább 5 elemű
     */
    @Override
    public boolean validate() {
        if (parts == null || parts.length < 5) {
            ConsoleOutput.error("Invalid argument count. Expected correct format.");
            return false;
        }
        return true;
    }

    /**
     * Végrehajtja a linkelési logikát.
     * Kikeresi az érintett objektumokat és beállítja a kért kapcsolatot.
     */
    @Override
    public void execute() {
        String id = parts[2];
        String property = parts[3];

        String[] args = new String[parts.length - 4];
        System.arraycopy(parts, 4, args, 0, args.length);

        try {
            Object targetObj = registry.getObject(id);

            Linkable target = (Linkable) targetObj;
            target.performLink(property, args, registry);
            ConsoleOutput.ok();

        } catch (ClassCastException e) {
            ConsoleOutput.error("Object '" + id + "' does not support link operations.");
        } catch (Exception e) {
            ConsoleOutput.error(e.getMessage());
        }
    }
}
