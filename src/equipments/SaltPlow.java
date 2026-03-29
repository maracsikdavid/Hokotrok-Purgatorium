package equipments;

import core.Skeleton;
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
		Skeleton.printCall(this, this, "clear");
                int answer = Skeleton.getIntFromUser("Van-e só a tartályban? (1: Igen, 0: Nem)");
                if (answer == 1) {
                        if (saltSource != null) saltSource.use();
                        if (lane.getState() != null) lane.getState().applySalt(lane);
                        Skeleton.printReturn(this, "clear", "true");
                        return true;
                }
		Skeleton.printReturn(this, "clear", "false");
		return false;
	}

	/**
	 * Újra feltölti sóval a sószóró fejet.
	 *
	 * @param salt az új sómennyiség, ami hozzáadódik a sószóró fejhez
	 */
	public void refill(Salt salt) {
		Skeleton.printCall(null, this, "refill");
		this.saltSource = salt;
		Skeleton.printReturn(this, "refill");
	}
}
