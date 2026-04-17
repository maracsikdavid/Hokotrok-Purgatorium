package equipments;
import topology.Lane;

/**
 * Sószoró típusú kotrófej. Az elszórt só a vastag vagy vékony havat, illetve a jeget
 * olvasztja fel az adott sávon. Hatása 2 tick után jelentkezik: tiszta lesz a sáv.
 */
public class SaltPlow extends Plow {
	private Salt saltSource;


	// --- KONSTRUKTOROK ---

	/**
	 * Alapértelmezett konstruktor.
	 */
	public SaltPlow() {
		super();
	}

	/**
	 * Paraméteres konstruktor a sóforrás megadásához.
	 *
	 * @param saltSource a használt sóforrás
	 */
	public SaltPlow(Salt saltSource) {
		super();
		this.saltSource = saltSource;
	}


	// --- GETTEREK ÉS SETTEREK ---

	/**
	 * Visszaadja a sószóró fej sóforrását.
	 *
	 * @return a sóforrás
	 */
	public Salt getSaltSource() {
		return saltSource;
	}

	/**
	 * Beállítja a sószóró fej sóforrását.
	 *
	 * @param saltSource a beállítandó sóforrás
	 */
	public void setSaltSource(Salt saltSource) {
		this.saltSource = saltSource;
	}


	// --- METÓDUSOK ---

	/**
	 * Takarítja a sávot sóval, ha van rendelkezésre álló só.
	 * Ha nincs, nem működik.
	 *
	 * @param lane a takarítandó sáv
	 * @return igaz, ha sikeres volt a takarítás
	 */
	@Override
	public boolean clear(Lane lane) {
		return false;
	}

	/**
	 * Újra feltölti sóval a sószóró fejet.
	 *
	 * @param salt az új sómennyiség, ami hozzáadódik a sószóró fejhez
	 */
	public void refill(Salt salt) {
	}

	/**
     * Az objektum aktuális állapotának és attribútumainak kiírása a standard kimenetre.
     * * @param id Az objektum egyedi azonosítója, amellyel a Registry-ben szerepel.
     */
    public void printData(String id) {
    }
	}

	/**
	 * Kényelmi metódus, amely ellenőrzi, hogy a sószóró fej kifogyott-e a sóból.
	 * * @return igaz, ha nincs beállítva forrás, vagy a mennyisége 0
	 */
	public boolean isEmpty() {
		return this.saltSource == null || this.saltSource.getAmount() == 0;
	}
}
