package equipments;
import topology.Lane;

/**
 * Jégtörő kotrófej. A sávon kialakult jégpáncélt feltöri, majd az vékony hóvá átalakul.
 * Ezzel a fejjel nem lehet letakarítani a sávon kialakult hóréteget.
 */
public class IcebreakerPlow extends Plow {
	/**
	 * Feltöri a jeget a megadott sávon. A feltört jég ezután vékony hóvá átalakul.
	 *
	 * @param lane a takarítandó sáv
	 * @return igaz, ha sikeres volt a takarítás
	 */
    @Override
    public boolean clear(Lane lane) {
        return false;
    }
}
