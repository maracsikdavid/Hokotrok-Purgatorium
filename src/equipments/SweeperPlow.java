package equipments;

import core.Skeleton;
import statemachine.CleanCondition;
import statemachine.ThinSnowCondition;
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
        Skeleton.printCall(null, this, "clear");
        boolean success = false;
        
        switch (Skeleton.getActiveTestCaseId()) {
            case 14: {
                int answer = Skeleton.getIntFromUser("Sikeres a söprés? (1: Yes, 0: No)");
                
                if (answer == 1) {
                    Lane neighbor = lane.getRightLane();
                    
                    CleanCondition cleanCond = new CleanCondition();
                    Skeleton.registerObject(cleanCond, "cleanCond");
                    lane.changeCondition(cleanCond);
                    
                    ThinSnowCondition newCond = new ThinSnowCondition();
                    Skeleton.registerObject(newCond, "newCond");
                    
                    if (neighbor != null) {
                        neighbor.changeCondition(newCond);
                    }
                    
                    success = true;
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
