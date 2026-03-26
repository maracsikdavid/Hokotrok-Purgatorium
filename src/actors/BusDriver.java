package actors;

import core.Skeleton;
import entities.Bus;
import topology.Lane;
import topology.Road;

public class BusDriver extends Player {
	public int score;
	public Bus managedBus;

	public void commandBus(Bus b, Road toRoad, Lane toLane) {
		Skeleton.printCall(null, this, "commandBus");
		Skeleton.printReturn(this, "commandBus");
	}

	public void achievePoints() {
		Skeleton.printCall(null, this, "achievePoints");
		Skeleton.printReturn(this, "achievePoints");
	}
}
