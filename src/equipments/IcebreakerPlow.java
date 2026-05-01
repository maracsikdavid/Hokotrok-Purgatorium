package equipments;

import cli.ObjectRegistry;
import cli.Printable;
import statemachine.IceCondition;
import statemachine.ThinSnowCondition;
import topology.Lane;

/**
 * A jégtörő kotrófej, amely a jégpáncélt vékony hóréteggé töri össze.
 */
public class IcebreakerPlow extends Plow implements Printable {


	// --- KONSTRUKTOROK ---

	/**
	 * Alapértelmezett konstruktor.
	 */
	public IcebreakerPlow() {
		super();
	}


	// --- METÓDUSOK ---

	/**
	 * Megkísérli feltörni a jeget a sávon. Ha a sáv jeges, vékony hóra módosítja az állapotát.
	 *
	 * @param lane A letakarítandó sáv.
	 * @return Igaz, ha a jégtörés sikeres volt, egyébként hamis.
	 * @throws Exception Ha a takarítás műveleti hiba miatt meghiúsul.
	 *
	 * Pszeudokód:
	 * 1. Ellenőrzi, hogy a sáv állapota IceCondition.
	 * 2. Állapotot ThinSnowCondition-re váltja.
	 */
	@Override
	public boolean clear(Lane lane) throws Exception {
		if (lane != null && lane.getState() instanceof IceCondition) {
				lane.setState(new ThinSnowCondition());
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
