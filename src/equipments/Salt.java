package equipments;

import core.Skeleton;

public class Salt {
	public int amount;

	public void use() {
		Skeleton.printCall(null, this, "use");
		Skeleton.printReturn(this, "use");
	}
}
