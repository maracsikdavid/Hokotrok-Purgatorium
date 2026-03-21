package equipments;

import core.Skeleton;
import topology.Lane;

public class SaltPlow extends Plow {
	public Salt saltSource;

	@Override
	public boolean clear(Lane lane) {
		Skeleton.printCall(null, this, "clear");
		Skeleton.printReturn(this, "clear", "false");
		return false;
	}

	public void refill(Salt salt) {
		Skeleton.printCall(null, this, "refill");
		this.saltSource = salt;
		Skeleton.printReturn(this, "refill");
	}
}
