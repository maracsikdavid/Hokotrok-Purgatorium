package statemachine;

import core.Skeleton;
import entities.Vehicle;
import topology.Lane;

public class ThinSnowCondition implements LaneCondition {
	@Override
	public void tick(Lane lane) {
		Skeleton.printCall(null, this, "tick");
		Skeleton.printReturn(this, "tick");
	}

	@Override
	public void addSnow(Lane lane) {
		Skeleton.printCall(null, this, "addSnow");
		Skeleton.printReturn(this, "addSnow");
	}

	public void trample(Lane lane) {
		Skeleton.printCall(null, this, "trample");
		Skeleton.printReturn(this, "trample");
	}

	public void applySalt(Lane lane) {
		Skeleton.printCall(null, this, "applySalt");
		Skeleton.printReturn(this, "applySalt");
	}

	@Override
	public void acceptVehicle(Lane lane, Vehicle v) {
		Skeleton.printCall(null, this, "acceptVehicle");
		Skeleton.printReturn(this, "acceptVehicle");
	}
}
