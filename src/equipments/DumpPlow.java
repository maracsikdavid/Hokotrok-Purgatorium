package equipments;

import core.Skeleton;
import statemachine.CleanCondition;
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
		Skeleton.printCall(null, this, "clear");
 boolean success;

        switch (Skeleton.getActiveTestCaseId()) {
            case 15: {
                int answer = Skeleton.getIntFromUser("Sikeres a takarítás? (1: Yes, 0: No)");
                
                if (answer == 1) {
                    CleanCondition cleanCond = new CleanCondition();
                    Skeleton.registerObject(cleanCond, "cleanCond");
                    
                    lane.changeCondition(cleanCond);
                    success = true;
                }
                else {
                    success = false;
                }
                break;
            }
            case 25: {
                int answer = Skeleton.getIntFromUser("A takarítás sikeres? (1: Yes, 0: No)");
                if (answer == 1) {
                    CleanCondition cleanCond = new CleanCondition();
                    Skeleton.registerObject(cleanCond, "cleanCond");
                    lane.changeCondition(cleanCond);
                    success = true;
                } else {
                    success = false;
                }
                break;
            }
            default:
                success = false;
                break;
        }

        Skeleton.printReturn(this, "clear", String.valueOf(success));
        return success;
    }
}
