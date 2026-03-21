package equipments;

import core.Skeleton;
import topology.Lane;

public class DragonPlow extends Plow {
	public Biokerosene fuelSource;

	@Override
	public boolean clear(Lane lane) {
		Skeleton.printCall(null, this, "clear");
		Skeleton.printReturn(this, "clear", "false");
		return false;
	}

	public void refill(Biokerosene fuel) {
		Skeleton.printCall(null, this, "refill");
		this.fuelSource = fuel;
		Skeleton.printReturn(this, "refill");
	}
}
