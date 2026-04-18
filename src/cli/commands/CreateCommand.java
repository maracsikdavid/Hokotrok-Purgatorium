package cli.commands;

import actors.BusDriver;
import actors.Cleaner;
import cli.Command;
import cli.ConsoleOutput;
import cli.ObjectRegistry;
import core.Game;
import core.Shop;
import entities.Bus;
import entities.Car;
import entities.Snowplow;
import equipments.Biokerosene;
import equipments.DragonPlow;
import equipments.DumpPlow;
import equipments.Gravel;
import equipments.GravelPlow;
import equipments.IcebreakerPlow;
import equipments.Salt;
import equipments.SaltPlow;
import equipments.SweeperPlow;
import topology.Bridge;
import topology.Building;
import topology.BusStop;
import topology.Depot;
import topology.Intersection;
import topology.Lane;
import topology.SimpleRoad;
import topology.Tunnel;

/**
 * A 'create' parancs megvalósítása.
 * Ezek a parancsok konstruktorként funkcionálnak. Segítségükkel objektumokat
 * (topológia elemek, entitások, felszerelések) tudunk létrehozni és névvel (ID) 
 * ellátni a tesztünkhöz vagy a pálya megépítéséhez.
 */
public class CreateCommand implements Command {
    private String[] parts;
    private ObjectRegistry registry;


    // --- KONSTRUKTOROK ---

    /**
     * Alapértelmezett konstruktor.
     */
    public CreateCommand() {
    }

    /**
     * Paraméteres konstruktor a parancs inicializálásához.
     *
     * @param parts a felbontott bemeneti sor (pl. ["Lane", "create", "lane1"])
     * @param registry a központi memóriatérkép referenciája
     */
    public CreateCommand(String[] parts, ObjectRegistry registry) {
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
     * @return igaz, ha a parancs formátuma helyes (pontosan 3 elemű)
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
     * Végrehajtja a létrehozási logikát.
     * Ellenőrzi a névütközést, legyártja az objektumot és beregisztrálja a memóriába.
     */
    @Override
    public void execute() {
        String category = parts[0];
        String id = parts[2];

        try {
            if (registry.getObjects().containsKey(id)) {
                throw new Exception("Object already exists with ID: " + id);
            }

            Object newObj = instantiateByCategory(category);
            registry.register(id, newObj);

        } catch (Exception e) {
            ConsoleOutput.error(e.getMessage());
        }
    }

    /**
     * Segédmetódus a konkrét Java objektumok példányosítására a kategória neve alapján.
     *
     * @param category a kategória neve (pl. "Lane", "Snowplow")
     * @return a példányosított objektum
     * @throws Exception ha a megadott kategória ismeretlen
     */
    private Object instantiateByCategory(String category) throws Exception {
        switch (category) {
            // --- Topológia ---
            case "Lane":
                return new Lane();
            case "SimpleRoad":
                return new SimpleRoad();
            case "Bridge":
                return new Bridge();
            case "Tunnel":
                return new Tunnel();
            case "Intersection":
                return new Intersection();
            case "Building":
                return new Building();
            case "BusStop":
                return new BusStop();
            case "Depot":
                return new Depot();

            // --- Entitások ---
            case "Snowplow":
                return new Snowplow();
            case "Bus":
                return new Bus();
            case "Car":
                return new Car();

            // --- Aktorok ---
            case "Cleaner":
                return new Cleaner();
            case "BusDriver":
                return new BusDriver();

            // --- Felszerelések ---
            case "SaltPlow":
                return new SaltPlow();
            case "DragonPlow":
                return new DragonPlow();
            case "DumpPlow":
                return new DumpPlow();
            case "SweeperPlow":
                return new SweeperPlow();
            case "IcebreakerPlow":
                return new IcebreakerPlow();
            case "GravelPlow":
                return new GravelPlow();
            case "Salt":
                return new Salt();
            case "Gravel":
                return new Gravel();
            case "Biokerosene":
                return new Biokerosene();

            // --- Rendszer ---
            case "Shop":
                return new Shop();
            case "Game":
                return new Game();

            default:
                throw new Exception("Invalid argument type: " + category);
        }
    }
}