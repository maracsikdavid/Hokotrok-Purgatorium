package actors;
import entities.Bus;
import topology.Lane;
import topology.Road;

/**
 * A busz sofőrjét reprezentáló osztály. Feladata a busz irányítása, pontok gyűjtése,
 * valamint a célelérésekor az elért sikerek regisztrálása a szimulációban pontok formájában.
 */
public class BusDriver extends Player {
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
	 * A buszt irányítja a megadott útra és sávra. Az utasítás a tesztkörnyezetben
	 * a Tesztelő döntésétől függően aktiválódik.
	 *
	 * @param b az irányítandó busz
	 * @param toRoad a cél úthálózat
	 * @param toLane a cél sáv az útban
	 */
	public void commandBus(Bus b, Road toRoad, Lane toLane) {
	}

	/**
	 * A buszsofőr pontokat szerez, amikor a buszt sikeresen elérkeztet a célállomásra.
	 * Ez a metódus a Bus osztály által hívódik meg a sikeres cél-megérkezésekor.
	 */
	public void achievePoints() {
	}

	/**
     * Az objektum aktuális állapotának és attribútumainak kiírása a standard kimenetre.
     * * @param id Az objektum egyedi azonosítója, amellyel a Registry-ben szerepel.
     */
    public void printData(String id) {
    }
}
