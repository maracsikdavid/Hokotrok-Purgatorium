package equipments;

import cli.ObjectRegistry;
import cli.Printable;
import statemachine.CleanCondition;
import statemachine.ThickSnowCondition;
import statemachine.ThinSnowCondition;
import topology.Lane;

/**
 * A seprűs kotrófej, amely a havat a sávjáról a mellette lévő sávra kotorja.
 * Hatékony a vékony és a vastag hóréteggel szemben is.
 */
public class SweeperPlow extends Plow implements Printable {


	// --- KONSTRUKTOROK ---

	/**
	 * Alapértelmezett konstruktor.
	 */
	public SweeperPlow() {
		super();
	}


	// --- METÓDUSOK ---

	/**
	 * Megtisztítja a sávot a hótól úgy, hogy azt a jobb oldali szomszédos sávra (ha van) áthelyezi.
	 *
	 * @param lane A letakarítandó sáv.
	 * @return Igaz, ha a takarítás sikeres volt (volt hó a sávon), egyébként hamis.
	 * @throws Exception Ha a takarítás műveleti hiba miatt meghiúsul.
	 *
	 * Pszeudokód:
	 * 1. Ellenőrzi, hogy a sávon vékony/vastag hó van-e.
	 * 2. Jobb szomszédra tolja a havat (ha létezik).
	 * 3. Az aktuális sávot tisztára állítja.
	 */
	@Override
	public boolean clear(Lane lane) throws Exception {
		if (lane == null) {
			return false;
		}

		boolean hadThinSnow = lane.getState() instanceof ThinSnowCondition;
		boolean hadThickSnow = lane.getState() instanceof ThickSnowCondition;
		if (!hadThinSnow && !hadThickSnow) {
			return false;
		}

		Lane right = lane.getRightLane();
		if (right == null && lane.getRoad() != null) {
			int idx = lane.getRoad().getLanes().indexOf(lane);
			if (idx >= 0 && idx + 1 < lane.getRoad().getLanes().size()) {
				right = lane.getRoad().getLanes().get(idx + 1);
			}
		}

		if (right != null) {
			if (hadThickSnow) {
				right.setState(new ThickSnowCondition());
			} else if (right.getState() instanceof CleanCondition) {
				right.setState(new ThinSnowCondition());
			} else if (right.getState() instanceof ThinSnowCondition) {
				right.setState(new ThickSnowCondition());
			}
		}

		lane.setState(new CleanCondition());
		return true;
	}

	/**
	 * Az objektum adatainak kiírása.
	 *
	 * @param id Az objektum azonosítója.
	 * @param registry Az objektumtár.
	 */
	@Override
	public void printData(String id, ObjectRegistry registry) {
		super.printData(id, registry);
	}
}
