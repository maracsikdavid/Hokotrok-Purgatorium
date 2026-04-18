package entities;

import actors.Cleaner;
import cli.Actionable;
import cli.Linkable;
import cli.ObjectRegistry;
import cli.Printable;
import equipments.Plow;
import topology.Lane;
import topology.MapNode;
import topology.Road;

/**
 * A hókotró speciális jármű, amely a város uthálózatának takarítására szolgál. A Cleaner által
 * irányítva, különböző kotrófejekkel lehet felszerelnini.
 */
public class Snowplow extends Vehicle implements Actionable, cli.Linkable, cli.Printable {
	private Cleaner owner;
	private Plow equippedPlow;


	// --- KONSTRUKTOROK ---

	/**
	 * Alapértelmezett konstruktor.
	 */
	public Snowplow() {
		super();
	}

	/**
	 * Paraméteres konstruktor a hókotró attribútumaihoz.
	 *
	 * @param owner a hókotró tulajdonosa
	 * @param equippedPlow a felszerelt kotrófej
	 */
	public Snowplow(Cleaner owner, Plow equippedPlow) {
		super();
		this.owner = owner;
		this.equippedPlow = equippedPlow;
	}

	
    // --- GETTEREK ÉS SETTEREK ---

    /**
     * Visszaadja a hókotró tulajdonosát.
     *
     * @return a tulajdonos takarító
     */
    public Cleaner getOwner() {
		return owner;
	}

    /**
     * Beállítja a hókotró tulajdonosát.
     *
     * @param owner a beállítandó tulajdonos
     */
    public void setOwner(Cleaner owner) {
		this.owner = owner;
	}

	/**
	 * Visszaadja a felszerelt kotrófejet.
	 *
	 * @return a felszerelt kotrófej
	 */
	public Plow getEquippedPlow() {
		return equippedPlow;
	}

	/**
	 * Beállítja a felszerelt kotrófejet.
	 *
	 * @param equippedPlow a beállítandó kotrófej
	 */
	public void setEquippedPlow(Plow equippedPlow) {
		this.equippedPlow = equippedPlow;
	}


	// --- ACTIONABLE ---

	/**
	 * Végrehajtja a megnevezett akciót a hókotró kontextusában.
	 *
	 * @param actionName az akció neve (pl. "clearLane", "equipPlow", "changeLane")
	 * @param args       a parancssor további paraméterei
	 * @param registry   a központi objektumtár
	 * @throws Exception ha az akció sikertelen
	 */
	@Override
	public void performAction(String actionName, String[] args, ObjectRegistry registry) throws Exception {
		switch (actionName) {
			case "clearLane":
				clearLane();
				break;
			case "equipPlow":
				equipPlowAction(args, registry);
				break;
			case "changeLane":
				changeLaneAction(args, registry);
				break;
			case "tick":
				tick();
				break;
			default:
				throw new Exception("Action failed: Unknown action '" + actionName + "' for Snowplow");
		}
	}

	/**
	 * Az "equipPlow" akció paramétereinek feloldása és végrehajtása.
	 * args[0] = a kotrófej registry ID-ja (pl. "saltplow1")
	 */
	private void equipPlowAction(String[] args, ObjectRegistry registry) throws Exception {
		if (args.length < 1) {
			throw new Exception("Action failed: equipPlow requires a plow ID");
		}
		try {
			Plow plow = (Plow) registry.getObject(args[0]);
			equipPlow(plow);
		} catch (ClassCastException e) {
			throw new Exception("Action failed: '" + args[0] + "' is not a valid Plow");
		}
	}

	/**
	 * A "changeLane" akció paramétereinek feloldása.
	 * args[0] = a cél sáv registry ID-ja (pl. "lane2")
	 */
	private void changeLaneAction(String[] args, ObjectRegistry registry) throws Exception {
		if (args.length < 1) {
			throw new Exception("Action failed: changeLane requires a lane ID");
		}
		try {
			Lane target = (Lane) registry.getObject(args[0]);
			changeLane(target);
		} catch (ClassCastException e) {
			throw new Exception("Action failed: '" + args[0] + "' is not a valid Lane");
		}
	}


	// --- LINKABLE ---

	@Override
	public void performLink(String property, String[] args, ObjectRegistry registry) throws Exception {
		switch (property) {
			case "equipPlow":
			case "setEquippedPlow": {
				try {
					Plow plow = (Plow) registry.getObject(args[0]);
					setEquippedPlow(plow);
				} catch (ClassCastException e) {
					throw new Exception("Action failed: '" + args[0] + "' is not a valid Plow");
				}
				break;
			}
			case "currentLane":
			case "setCurrentLane": {
				try {
					Lane lane = (Lane) registry.getObject(args[0]);
					setCurrentLane(lane);
					lane.getVehicles().add(this);
				} catch (ClassCastException e) {
					throw new Exception("Action failed: '" + args[0] + "' is not a valid Lane");
				}
				break;
			}
			case "owner":
			case "setOwner": {
				try {
					Cleaner c = (Cleaner) registry.getObject(args[0]);
					setOwner(c);
				} catch (ClassCastException e) {
					throw new Exception("Action failed: '" + args[0] + "' is not a valid Cleaner");
				}
				break;
			}
			default:
				throw new Exception("Action failed: Unknown link property '" + property + "' for Snowplow");
		}
	}


	// --- PRINTABLE ---

	@Override
	public void printData(String id, ObjectRegistry registry) {
		String plowName = (equippedPlow != null) ? equippedPlow.getClass().getSimpleName() : "null";
		System.out.println("Snowplow," + id);
		System.out.println("equippedPlow," + plowName);
	}


	// --- METÓDUSOK ---

	/**
	 * A hókotró időzítés lépése.
	 */
	@Override
	public void tick() {
    }

	/**
	 * A hókotró mozgatása.
	 */
	@Override
	protected void move() {

    }

	/**
	 * Ellenőrzi, hogy a hókotró bénulhat-e. Az immunis a jeges sávra.
	 *
	 * @return hamis (a hókotrók nem bénulhatnak)
	 */
	@Override
	public boolean isParalizable() {
		return false;
	}

	/**
	 * A hókotró sávváltása. Leveszi a járművet a régi sávról és áthelyezi az újra.
	 *
	 * @param target a cél sáv
	 * @return igaz, ha sikeres
	 */
	@Override
	public boolean changeLane(Lane target) {
		Lane old = getCurrentLane();
		if (old != null) {
			old.getVehicles().remove(this);
		}
		setCurrentLane(target);
		if (target != null) {
			target.getVehicles().add(this);
		}
		setProgress(0);
		return true;
	}

	/**
	 * A hókotró letakarítja a jelenlegi sávját. Az eredmény az aktuális kotrófejtől függ.
	 * A kotrófej clear() metódusát hívja az aktuális sávra.
	 *
	 * @return igaz, ha a takarítás sikeres
	 */
	public boolean clearLane() {
		if (equippedPlow == null) {
			return false;
		}
		Lane currentLane = getCurrentLane();
		if (currentLane == null) {
			return false;
		}
		return equippedPlow.clear(currentLane);
    }

	/**
	 * A hókotróra új kotrófejet helyezünk fel.
	 *
	 * @param p az új kotrófej
	 */
	public void equipPlow(Plow p) {
		this.equippedPlow = p;
	}

	/**
	 * A Hókotrót a játékos irányítja, ezért automatikusan nem választ utat.
	 */
	@Override
	public Road chooseNextRoad(MapNode currentNode) {
		return null; // Várakozik a csomópontban a Cleaner parancsára
	}
}
