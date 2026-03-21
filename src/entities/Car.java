package entities;

import core.Skeleton;
import topology.Building;
import topology.Lane;

public class Car extends Vehicle {
	public Building homeNode;
	public Building workplaceNode;

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
		Skeleton.printReturn(this, "isParalizable", "true");
		return true;
	}

	public void paralyze(int time) {
		Skeleton.printCall(null, this, "paralyze");
		Skeleton.printReturn(this, "paralyze");
	}

	public boolean stuck() {
		Skeleton.printCall(null, this, "stuck");
		Skeleton.printReturn(this, "stuck", "false");
		return false;
	}

	@Override
	public boolean changeLane(Lane target) {
		Skeleton.printCall(null, this, "changeLane");
		Skeleton.printReturn(this, "changeLane", "true");
		return true;
	}
}
