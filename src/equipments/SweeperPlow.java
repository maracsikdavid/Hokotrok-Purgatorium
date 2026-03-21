package equipments;

import core.Skeleton;
import topology.Lane;

public class SweeperPlow extends Plow {
	@Override
	public boolean clear(Lane lane) {
		Skeleton.printCall(null, this, "clear");
		Skeleton.printReturn(this, "clear", "false");
		return false;
	}
}
