package topology;

import core.ITickable;
import core.Skeleton;
import entities.Vehicle;
import java.util.ArrayList;
import java.util.List;
import statemachine.LaneCondition;

public class Lane implements ITickable {
	public int length;
	public LaneCondition state;
	private List<Vehicle> vehicles = new ArrayList<>();

	public void acceptVehicle(Vehicle v) {
		Skeleton.printCall(null, this, "acceptVehicle");
		vehicles.add(v);
		Skeleton.printReturn(this, "acceptVehicle");
	}

	public void removeVehicle(Vehicle v) {
		Skeleton.printCall(null, this, "removeVehicle");
		vehicles.remove(v);
		Skeleton.printReturn(this, "removeVehicle");
	}

	public void changeCondition(LaneCondition newCondition) {
		Skeleton.printCall(null, this, "changeCondition");
		this.state = newCondition;
		Skeleton.printReturn(this, "changeCondition");
	}

	@Override
	public void tick() {
		Skeleton.printCall(null, this, "tick");
		Skeleton.printReturn(this, "tick");
	}

	public Lane getLeftLane() {
		Skeleton.printCall(null, this, "getLeftLane");
		Skeleton.printReturn(this, "getLeftLane", "Lane");
		return null;
	}

	public Lane getRightLane() {
		Skeleton.printCall(null, this, "getRightLane");
		Skeleton.printReturn(this, "getRightLane", "Lane");
		return null;
	}
}
