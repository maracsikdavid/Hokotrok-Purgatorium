package equipments;
import topology.Lane;

/**
 * Sószoró típusú kotrófej. Az elszórt só a vastag vagy vékony havat, illetve a jeget
 * olvasztja fel az adott sávon. Hatása 2 tick után jelentkezik: tiszta lesz a sáv.
 */
public class SaltPlow extends Plow {
	private Salt saltSource;

	// --- GETTEREK ÉS SETTEREK ---
	public Salt getSaltSource() {
		return saltSource;
	}
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
		this.saltSource = salt;
	}
}
