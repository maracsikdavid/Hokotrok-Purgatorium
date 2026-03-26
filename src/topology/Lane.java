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
	/**
	 * A sáv hossza az egész úton (egységben).
	 */
	public int length;
	
	/**
	 * A sáv aktuális időjárási állapota (tiszta, vékony/vastag hó, jég).
	 */
	public LaneCondition state;
	
	/**
	 * A sávon lévő járművek listája.
	 */
	private List<Vehicle> vehicles = new ArrayList<>();

	/**
	 * Jármű elfogadása a sávra az aktuális állapot ellenőrzése után.
	 *
	 * @param v az elfogadandó jármű
	 */
	public void acceptVehicle(Vehicle v) {
		Skeleton.printCall(null, this, "acceptVehicle");
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
