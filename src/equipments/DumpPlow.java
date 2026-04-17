package equipments;
import topology.Lane;

/**
 * Hányófej típusú kotrófej. A sávon lévő vékony vagy vastag havat
 * olyan messzire szórja el, hogy az végleg eltűnik, nem szóródik át
 * más sávokra. 
 */
public class DumpPlow extends Plow {

	// --- METÓDUSOK ---

	/**
	 * Takarítja a sávot a havat elhányva. Az elhányt hó végleg eltűnik, nem szóródik át más sávokra.
	 *
	 * @param lane a takarítandó sáv
	 * @return igaz, ha sikeres volt a takarítás
	 */
	@Override
	public boolean clear(Lane lane) {
        return false;
    }

	/**
     * Az objektum aktuális állapotának és attribútumainak kiírása a standard kimenetre.
     * * @param id Az objektum egyedi azonosítója, amellyel a Registry-ben szerepel.
     */
    public void printData(String id) {
    }
}
