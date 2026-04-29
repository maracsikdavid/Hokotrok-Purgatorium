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
	 *
	 * Pszeudokód:
	 * 1. Ellenőrzi, hogy a sávon vékony/vastag hó van-e.
	 * 2. Jobb szomszédra tolja a havat (ha létezik).
	 * 3. Az aktuális sávot tisztára állítja.
	 */
	@Override
	public boolean clear(Lane lane) {
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
