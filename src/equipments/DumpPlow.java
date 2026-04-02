package equipments;
import topology.Lane;

/**
 * Hányófej típusú kotrófej. A sávon lévő vékony vagy vastag havat
 * olyan messzire szórja el, hogy az végleg eltűnik, nem szóródik át
 * más sávokra. 
 */
public class DumpPlow extends Plow {
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
}
