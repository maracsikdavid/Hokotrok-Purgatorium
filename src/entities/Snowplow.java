package entities;

import actors.Cleaner;
import core.Skeleton;
import equipments.Plow;
import topology.Lane;

public class Snowplow extends Vehicle {
	public Cleaner owner;
	public Plow equippedPlow;

	@Override
	public void tick() {
		Skeleton.printCall(null, this, "tick");
		Skeleton.printReturn(this, "tick");
	}

	@Override
	protected void move() {
		Skeleton.printCall(null, this, "move");
		Skeleton.printReturn(this, "move");
	}

	@Override
	public boolean isParalizable() {
		Skeleton.printCall(null, this, "isParalizable");
		Skeleton.printReturn(this, "isParalizable", "false");
		return false;
	}

	@Override
	public boolean changeLane(Lane target) {
		Skeleton.printCall(null, this, "changeLane");
		Skeleton.printReturn(this, "changeLane", "true");
		return true;
	}

	public boolean clearLane() {
		Skeleton.printCall(null, this, "clearLane");
		Skeleton.printReturn(this, "clearLane", "false");
		return false;
	}

	public void equipPlow(Plow p) {
		Skeleton.printCall(null, this, "equipPlow");
		this.equippedPlow = p;
		Skeleton.printReturn(this, "equipPlow");
	}
}
