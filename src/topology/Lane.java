package topology;

import core.ITickable;
import core.Skeleton;
import entities.Vehicle;
import java.util.ArrayList;
import java.util.List;
import statemachine.LaneCondition;

/**
 * A sáv az úthálózat legkisebb, járművek által használható közlekedési egysége.
 * Felelőssége a rajta tartózkodó járművek nyilvántartása,
 * valamint a saját útviszonyának (állapotának) kezelése a State tervezési minta alapján
 */
public class Lane implements ITickable {
	private int length;
	private LaneCondition state;
	private List<Vehicle> vehicles = new ArrayList<>();


	// --- GETTEREK ÉS SETTEREK ---
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}

	public LaneCondition getState() {
		return state;
	}
	public void setState(LaneCondition state) {
		this.state = state;
	}

	public List<Vehicle> getVehicles() {
		return vehicles;
	}
	public void setVehicles(List<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}

	// --- METÓDUSOK ---
	/**
	 * Jármű elfogadása a sávra az aktuális állapot ellenőrzése után.
	 *
	 * @param v az elfogadandó jármű
	 */
	public void acceptVehicle(Vehicle v) {
        Skeleton.printCall(null, this, "acceptVehicle");
        
        // acceptVehicle(...) továbbhívás az állapotnak!
        if (state != null) {
            state.acceptVehicle(this, v);
        }
        
        vehicles.add(v);
        Skeleton.printReturn(this, "acceptVehicle");
    }

	/**
	 * Jármű eltávolítása a sávról (pl. sáv váltás vagy út vége után).
	 *
	 * @param v az eltávolítandó jármű
	 */
	public void removeVehicle(Vehicle v) {
		Skeleton.printCall(null, this, "removeVehicle");
		vehicles.remove(v);
		Skeleton.printReturn(this, "removeVehicle");
	}

	/**
	 * Sáv állapotának megváltoztatása egy új időjárási feltételre
	 *
	 * @param newCondition az új állapot
	 */
	public void changeCondition(LaneCondition newCondition) {
		Skeleton.printCall(null, this, "changeCondition");
		this.state = newCondition;
		Skeleton.printReturn(this, "changeCondition");
	}

	/**
	 * Sáv frissítése az idő előrehaladásával (tick).
	 */
	@Override
	public void tick() {
		Skeleton.printCall(null, this, "tick");
        
        // Továbbadja az idő múlását az állapotnak (jégnek, hónak)
        if (state != null) {
            state.tick(this);
        }
        
		Skeleton.printReturn(this, "tick");
	}

	/**
	 * Bal oldali szomszéd sáv lekérése 
	 *
	 * @return a bal oldali sáv, vagy null ha nincs
	 */
	public Lane getLeftLane() {
		Skeleton.printCall(null, this, "getLeftLane");
		Skeleton.printReturn(this, "getLeftLane", "Lane");
		return null;
	}

	/**
	 * Jobb oldali szomszéd sáv lekérése 
	 *
	 * @return a jobb oldali sáv, vagy null ha nincs
	 */
	public Lane getRightLane() {
		Skeleton.printCall(null, this, "getRightLane");
		Skeleton.printReturn(this, "getRightLane", "Lane");
		return null;
	}
}
