package actors;
import cli.Actionable;
import cli.Linkable;
import cli.ObjectRegistry;
import entities.Bus;
import topology.Lane;
import topology.Road;

/**
 * A busz sofőrjét reprezentáló osztály. Feladata a busz irányítása, pontok gyűjtése,
 * valamint a célelérésekor az elért sikerek regisztrálása a szimulációban pontok formájában.
 */
public class BusDriver extends Player implements Actionable, Linkable {
	private int score;
	private Bus managedBus;


	// --- KONSTRUKTOROK ---

	/**
	 * Alapértelmezett konstruktor.
	 */
	public BusDriver() {
		super();
	}

	/**
     * Paraméteres konstruktor a név megadásához.
     *
     * @param name a takarító neve
     */
    public BusDriver(String name) {
        super(name);
    }

	/**
	 * Paraméteres konstruktor a sofőr alapadatainak beállítására.
	 *
	 * @param name a sofőr neve
	 * @param score a sofőr pontszáma
	 * @param managedBus az irányított busz
	 */
	public BusDriver(String name, int score, Bus managedBus) {
		super(name);
		this.score = score;
		this.managedBus = managedBus;
	}


	// --- GETTEREK ÉS SETTEREK ---

	/**
	 * Visszaadja a sofőr pontszámát.
	 *
	 * @return a pontszám
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Beállítja a sofőr pontszámát.
	 *
	 * @param score a beállítandó pontszám
	 */
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * Visszaadja a sofőr által irányított buszt.
	 *
	 * @return az irányított busz
	 */
	public Bus getManagedBus() {
		return managedBus;
	}

	/**
	 * Beállítja a sofőr által irányított buszt.
	 *
	 * @param managedBus a beállítandó busz
	 */
	public void setManagedBus(Bus managedBus) {
		this.managedBus = managedBus;
	}


	// --- METÓDUSOK ---

	/**
	 * Végrehajtja a megnevezett akciót a buszsofőr kontextusában.
	 *
	 * @param actionName az akció neve (pl. "commandBus")
	 * @param args       a parancssor további paraméterei
	 * @param registry   a központi objektumtár
	 * @throws Exception ha az akció sikertelen
	 */
	@Override
	public void performAction(String actionName, String[] args, ObjectRegistry registry) throws Exception {
		switch (actionName) {
			case "commandBus":
				commandBusAction(args, registry);
				break;
			default:
				throw new Exception("Action failed: Unknown action '" + actionName + "' for BusDriver");
		}
	}

	/**
	 * A "commandBus" akció feloldása.
	 * args[0] = Bus ID, args[1] = Road ID, args[2] = Lane ID
	 *
	 * Pszeudokód:
	 * 1. Argumentumszám ellenőrzése.
	 * 2. Objektumok feloldása registry-ből.
	 * 3. Meghívja a commandBus(...) metódust.
	 */
	private void commandBusAction(String[] args, ObjectRegistry registry) throws Exception {
		if (args.length < 3) {
			throw new Exception("Action failed: commandBus requires bus, road, and lane IDs");
		}
		try {
			Bus b = (Bus) registry.getObject(args[0]);
			Road toRoad = (Road) registry.getObject(args[1]);
			Lane toLane = (Lane) registry.getObject(args[2]);
			commandBus(b, toRoad, toLane);
		} catch (ClassCastException e) {
			throw new Exception("Action failed: Invalid parameter type for commandBus");
		}
	}

	/**
	 * A buszt irányítja a megadott útra és sávra. Az utasítás a tesztkörnyezetben
	 * a Tesztelő döntésétől függően aktiválódik.
	 *
	 * @param b az irányítandó busz
	 * @param toRoad a cél úthálózat
	 * @param toLane a cél sáv az útban
	 *
	 * Pszeudokód:
	 * 1. Ellenőrzi, hogy a busz a sofőrhöz tartozik-e.
	 * 2. Meghívja a b.changeLane(toLane) metódust.
	 * 3. Sikertelenség esetén hibát jelez.
	 */
	public void commandBus(Bus b, Road toRoad, Lane toLane) {

	}

	/**
	 * A buszsofőr pontokat szerez, amikor a buszt sikeresen elérkeztet a célállomásra.
	 * Ez a metódus a Bus osztály által hívódik meg a sikeres cél-megérkezésekor.
	 *
	 * Pszeudokód:
	 * 1. Lekéri az aktuális score értéket.
	 * 2. Hozzáadja a jutalompontot.
	 */
	public void achievePoints() {

	}

    /**
     * Összekapcsolja a buszsofőrt más objektumokkal a parancssori argumentumok alapján.
     *
     * @param property A beállítandó tulajdonság neve.
     * @param args     Az összekapcsoláshoz szükséges argumentumok.
     * @param registry A központi objektumtár.
     * @throws Exception Ha a tulajdonság ismeretlen vagy az összekapcsolás sikertelen.
     */
    @Override
    public void performLink(String property, String[] args, ObjectRegistry registry) throws Exception {
        switch (property) {
            case "managedBus":
            case "setManagedBus": {
                Bus b = (Bus) registry.getObject(args[0]);
                setManagedBus(b);
                break;
            }
            default:
                throw new Exception("Action failed: Unknown link property '" + property + "' for BusDriver");
        }
    }

	/**
	 * Az objektum aktuális állapotának és attribútumainak kiírása a standard kimenetre.
	 *
	 * @param id Az objektum egyedi azonosítója, amellyel a Registry-ben szerepel.
	 * @param registry A központi regiszter.
	 */
    @Override
    public void printData(String id, ObjectRegistry registry) {
        System.out.println("BusDriver," + id);
        System.out.println("name," + this.getName());
        System.out.println("score," + this.score);
        System.out.println("managedBus," + registry.findId(managedBus));
    }
}
