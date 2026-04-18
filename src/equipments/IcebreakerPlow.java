package equipments;

import cli.ObjectRegistry;
import cli.Printable;
import statemachine.IceCondition;
import statemachine.ThinSnowCondition;
import topology.Lane;

/**
 * Jégtörő kotrófej. A sávon kialakult jégpáncélt feltöri, majd az vékony hóvá átalakul.
 * Ezzel a fejjel nem lehet letakarítani a sávon kialakult hóréteget.
 */
public class IcebreakerPlow extends Plow implements Printable {

	// --- METÓDUSOK ---

	/**
	 * Feltöri a jeget a megadott sávon. A feltört jég ezután vékony hóvá átalakul.
	 * Csak IceCondition állapotú sávon működik — egyéb állapotban nincs hatása.
	 *
	 * @param lane a takarítandó sáv
	 * @return igaz, ha sikeres volt a jégtörés
	 */
	@Override
	public boolean clear(Lane lane) {
		if (lane == null || lane.getState() == null) {
			return false;
		}

		// A jégtörő csak jégpáncélon működik
		try {
			IceCondition ice = (IceCondition) lane.getState();
			// Jég → vékony hó
			lane.setState(new ThinSnowCondition());
			return true;
		} catch (ClassCastException e) {
			// Nem jég állapotú sáv — a jégtörő nem hat rá
			return false;
		}
	}

	/**
	 * Az objektum aktuális állapotának és attribútumainak kiírása a standard kimenetre.
	 */
	@Override
	public void printData(String id, ObjectRegistry registry) {
		System.out.println("IcebreakerPlow," + id);
	}
}
