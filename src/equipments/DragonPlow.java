package equipments;

import core.Skeleton;
import statemachine.CleanCondition;
import topology.Lane;

/**
 * Sárkányfej típusú kotrófej. Használatához biokerozin szükséges, amely
 * ha kifogy, a sárkányfej nem működik. A biokerozin használatakor az adott
 * sávon lévő hó és jég azonnal elolvad, és a sáv tiszta lesz. Ha elfogy
 * belőle az üzemanyag, újra kell tölteni, hogy a sárkányfej újra használható legyen.
 */
public class DragonPlow extends Plow {
	private Biokerosene fuelSource;


	// --- GETTEREK ÉS SETTEREK ---
	public Biokerosene getFuelSource() {
		return fuelSource;
	}
	public void setFuelSource(Biokerosene fuelSource) {
		this.fuelSource = fuelSource;
	}


	// --- METÓDUSOK ---
	/**
	 * Takarítja a megadott sávot biokerozinnal, ha elég üzemanyag áll rendelkezésre.
	 * Ha nincs, nem működik.
	 *
	 * @param lane a takarítandó sáv
	 * @return igaz, ha sikeres volt a takarítás
	 */
	@Override
	public boolean clear(Lane lane) {
		Skeleton.printCall(null, this, "clear");

		boolean success = false;

        switch (Skeleton.getActiveTestCaseId()) {
            case 17, 18: {
                int answer = Skeleton.getIntFromUser("Van elég biokerozin a tartályban? (1: Igen, 0: Nem)");
                
                if (answer == 1) {
                    if (this.fuelSource != null) {
                        this.fuelSource.use();
                        
                        CleanCondition cleanCond = new CleanCondition();
                        Skeleton.registerObject(cleanCond, "cleanCond");
                        lane.changeCondition(cleanCond);
                        
                        success = true;
                    } else {
                        success = false;
                    }
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

	/**
	 * Újra feltölti biokerozinnel a fejet.
	 *
	 * @param fuel az új biokerozin entitás, amelyet a sárkányfej használni fog
	 */
	public void refill(Biokerosene fuel) {
		Skeleton.printCall(null, this, "refill");
		this.fuelSource = fuel;
		Skeleton.printReturn(this, "refill");
	}
}
