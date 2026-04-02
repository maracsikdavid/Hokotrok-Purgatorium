package equipments;
import topology.Lane;

/**
 * Söprőfej típusú kotrófej. A sávról letolja a vékony vagy vastag
 * hóréteget, amely ezután a szomszédos sávra kerül át.
 */
public class SweeperPlow extends Plow {
	/**
	 * Takarítja a sávot söprőfejjel. A hó a szomszédos jobb oldali sávra kerül.
	 *
	 * @param lane a takarítandó sáv
	 * @return igaz, ha sikeres volt a takarítás
	 */
	@Override
    public boolean clear(Lane lane) {
        return false;
    }
}
