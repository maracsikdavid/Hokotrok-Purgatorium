package entities;

import actors.Cleaner;
import cli.Actionable;
import cli.Linkable;
import cli.ObjectRegistry;
import cli.Printable;
import equipments.Plow;
import java.util.ArrayList;
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
		setOwner(owner);
		equipPlow(equippedPlow);
	}


	// --- GETTEREK ÉS SETTEREK ---
	
	/**
	 * Visszaadja a hókotró tulajdonosát.
	 *
	 * @return A tulajdonos takarító referenciája.
	 */
	public Cleaner getOwner() {
		return owner;
	}

	/**
	 * Beállítja a hókotró tulajdonosát.
	 *
	 * @param owner A beállítandó tulajdonos takarító.
	 */
	public void setOwner(Cleaner owner) {
		if (this.owner != null && this.owner.getFleet() != null) {
			this.owner.getFleet().remove(this);
		}

		this.owner = owner;

		if (this.owner != null) {
			if (this.owner.getFleet() == null) {
				this.owner.setFleet(new ArrayList<>());
			}
			if (!this.owner.getFleet().contains(this)) {
				this.owner.getFleet().add(this);
			}
		}

		if (equippedPlow != null) {
			equippedPlow.setOwner(owner);
		}
	}

	/**
	 * Visszaadja a jelenleg felszerelt kotrófejet.
	 *
	 * @return A felszerelt kotrófej példánya.
	 */
	public Plow getEquippedPlow() {
		return equippedPlow;
	}

	/**
	 * Beállítja a felszerelt kotrófejet.
	 *
	 * @param equippedPlow A hókotróra szerelendő új kotrófej.
	 */
	public void setEquippedPlow(Plow equippedPlow) {
		equipPlow(equippedPlow);
	}


	// --- METÓDUSOK ---

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
	 * Az "equipPlow" akció paramétereinek feloldása és a felszerelés végrehajtása.
	 * 
	 * @param args A parancs argumentumai, ahol args[0] a kotrófej azonosítója.
	 * @param registry Az objektumtár.
	 * @throws Exception Ha a megadott azonosító nem kotrófejet takar vagy hiányzik az argumentum.
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
	 * A "changeLane" akció paramétereinek feloldása és a sávváltás megkísérlése.
	 * 
	 * @param args A parancs argumentumai, ahol args[0] a célsáv azonosítója.
	 * @param registry Az objektumtár.
	 * @throws Exception Ha a megadott azonosító nem sávot takar vagy hiányzik az argumentum.
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

	/**
	 * Összekapcsolja az objektumot más objektumokkal a megadott tulajdonság mentén.
	 *
	 * @param property A beállítandó tulajdonság neve.
	 * @param args     Az összekapcsoláshoz szükséges argumentumok.
	 * @param registry Az objektumtár.
	 * @throws Exception Ha a tulajdonság ismeretlen vagy az összekapcsolás sikertelen.
	 */
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
					lane.acceptVehicle(this);
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

	/**
	 * Az objektum állapotának és speciális hókotró adatainak kiírása.
	 *
	 * @param id Az objektum azonosítója.
	 * @param registry Az objektumtár.
	 */
	@Override
	public void printData(String id, ObjectRegistry registry) {
	    super.printData(id, registry);
		System.out.println("owner," + registry.findId(owner));
		System.out.println("equippedPlow," + registry.findId(equippedPlow));
	}

	/**
	 * A hókotró időzítés lépése. Minden időlépésben megpróbálja letakarítani az aktuális
	 * pozícióját, majd továbbhalad.
	 *
	 * Pszeudokód:
	 * 1. Meghívja a clearLane metódust.
	 * 2. Meghívja a move metódust.
	 */
	@Override
	public void tick() {
		try {
			clearLane();
		} catch (Exception e) {
			// Silent fail - exception a clearLane-ből nem blokkol el
		}
		if (!isParalyzed) {
			move();
		}
    }

	/**
	 * A hókotró mozgatása a sávon belül. A progress érték növekszik a sáv hosszáig.
	 *
	 * Pszeudokód:
	 * 1. Ellenőrzi az aktuális sávot.
	 * 2. Növeli a progress értéket.
	 * 3. Sáv végén csomóponti továbbhaladást kezel.
	 */
	@Override
	protected void move() {
		if (currentLane == null || isParalyzed) {
			return;
		}

		if (progress < currentLane.getLength()) {
			progress++;
		}

		if (progress > currentLane.getLength()) {
			progress = currentLane.getLength();
		}
    }

	/**
	 * Ellenőrzi, hogy a hókotró lebénulhat-e. 
	 * A hókotrók speciális kialakításuk miatt immunisak a jegesedésre.
	 *
	 * @return Hamis, mert a hókotrók nem bénulhatnak le.
	 */
	@Override
	public boolean isParalizable() {
		return false;
	}

    /**
     * Ellenőrzi, hogy a hókotró elakadt-e. 
     * A hókotrókra nem vonatkoznak az általános elakadási szabályok.
     *
     * @return Mindig hamis.
     */
    @Override
    public boolean stuck() {
        return false;
    }

	/**
	 * A hókotró letakarítja az aktuális sávot a felszerelt kotrófej segítségével. 
	 * Sikeres takarítás esetén a tulajdonos érmét kap.
	 *
	 * @return Igaz, ha a takarítás sikeres volt és volt felszerelt kotrófej.
	 * @throws Exception Ha a takarítás műveleti hiba miatt meghiúsul.
	 *
	 * Pszeudokód:
	 * 1. Ellenőrzi, hogy van-e felszerelt fej és aktuális sáv.
	 * 2. Meghívja az equippedPlow.clear(...) metódust.
	 * 3. Siker esetén tulajdonosi jutalmat ad.
	 */
	public boolean clearLane() throws Exception {
		if (equippedPlow == null || currentLane == null) {
			return false;
		}

		boolean success = equippedPlow.clear(currentLane);
		if (success && owner != null) {
			owner.achieveCoin();
		}
		return success;
    }

	/**
	 * Új kotrófej felszerelése a hókotróra.
	 *
	 * @param p A felszerelendő kotrófej példánya.
	 *
	 * Pszeudokód:
	 * 1. Beállítja az equippedPlow mezőt.
	 * 2. Szükség esetén frissíti a fej állapotát.
	 */
	public void equipPlow(Plow p) {
		if (equippedPlow != null) {
			equippedPlow.setEquipped(false);
		}

		equippedPlow = p;
		if (equippedPlow != null) {
			equippedPlow.setEquipped(true);
			equippedPlow.setOwner(owner);
		}
	}

	/**
	 * A hókotró útvonalválasztása. Mivel a takarító közvetlenül irányítja, 
	 * automatikusan nem választ következő utat.
	 *
	 * @param currentNode Az aktuális csomópont.
	 * @return Mindig null, parancsra vár.
	 */
	@Override
	public Road chooseNextRoad(MapNode currentNode) {
		return null; 
	}
}