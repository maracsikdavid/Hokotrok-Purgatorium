package equipments;

import core.Skeleton;
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
        Skeleton.printCall(null, this, "clear");
        
        int answer = Skeleton.getIntFromUser("Sikeres a jégtörés? (1: Yes, 0: No)");
        boolean success = (answer == 1);

        if (success) {
            statemachine.ThinSnowCondition thinSnow = new statemachine.ThinSnowCondition();
            Skeleton.registerObject(thinSnow, "thinSnow");
            lane.changeCondition(thinSnow);
        }

        Skeleton.printReturn(this, "clear", String.valueOf(success));
        return success;
    }
}
