package equipments;

import cli.ObjectRegistry;
import cli.Printable;
import statemachine.CleanCondition;
import statemachine.ThickSnowCondition;
import statemachine.ThinSnowCondition;
import topology.Lane;

/**
 * A hószóró kotrófej, amely a sávjáról a játéktéren kívülre szórja a havat.
 * Nincs szüksége szomszédos sávra a takarításhoz.
 */
public class DumpPlow extends Plow implements Printable {


	// --- KONSTRUKTOROK ---

	/**
	 * Alapértelmezett konstruktor.
	 */
	public DumpPlow() {
		super();
	}


	// --- METÓDUSOK ---

	/**
	 * Megtisztítja a sávot a hótól úgy, hogy azt eltávolítja a szimulációból.
	 *
	 * @param lane A letakarítandó sáv.
	 * @return Igaz, ha a takarítás sikeres volt, egyébként hamis.
	 * @throws Exception Ha a takarítás műveleti hiba miatt meghiúsul.
	 *
	 * Pszeudokód:
	 * 1. Ellenőrzi, hogy a sáv állapota takarítható hó.
	 * 2. Állapotot CleanCondition-re állítja.
	 */
	@Override
	public boolean clear(Lane lane) throws Exception {
		if (lane == null) {
			return false;
		}

		if (lane.getState().isThinSnow() || lane.getState().isThickSnow()) {
			lane.setState(new CleanCondition());
			return true;
		}

		return false;
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
