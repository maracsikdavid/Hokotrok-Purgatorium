package equipments;

import topology.Lane;

/**
 * Absztrakt osztály minden kotrófej típushoz. A különböző kotrófejek különböző módokon
 * takarítanak: söpréssel, messzire elhányva, illetve biokerozint vagy sót használva.
 */
public abstract class Plow {

	// --- METÓDUSOK ---

	/**
	 * Megkísérli egy sáv takarítását az aktuális kotrófej típusától függően.
	 * Az egyes leszármazott fejek eltérő mechanizmusokat valósítanak meg.
	 *
	 * @param lane a takarítandó sáv
	 * @return igaz, ha a takarítás sikeres volt
	 */
	public abstract boolean clear(Lane lane);

	/**
     * Az objektum aktuális állapotának és attribútumainak kiírása a standard kimenetre.
     * * @param id Az objektum egyedi azonosítója, amellyel a Registry-ben szerepel.
     */
    public void printData(String id) {
    }
}
