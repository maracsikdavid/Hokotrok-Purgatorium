package topology;

import core.Skeleton;
import java.util.ArrayList;
import java.util.List;

public abstract class Road {
	public MapNode targetNode;
	protected List<Lane> lanes = new ArrayList<>();

	public List<Lane> getLanes() {
		Skeleton.printCall(null, this, "getLanes");
		Skeleton.printReturn(this, "getLanes", "List<Lane>");
		return lanes;
	}

	public MapNode getTargetNode() {
		Skeleton.printCall(null, this, "getTargetNode");
		Skeleton.printReturn(this, "getTargetNode", "MapNode");
		return targetNode;
	}
}
