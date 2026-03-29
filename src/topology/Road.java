package topology;

import core.Skeleton;
import java.util.ArrayList;
import java.util.List;

/**
 * Absztrakt ősosztály, amely az úthálózat csomópontokat összekötő szakaszait reprezentálja. 
 * Felelőssége az adott útszakaszt felépítő sávok (Lane) összefogása és kezelése. 
 * Ebből származnak a normál utak, valamint a speciális tulajdonságokkal bíró hidak és alagutak.
 */
public abstract class Road {
	private MapNode targetNode;
	private List<Lane> lanes = new ArrayList<>();

	
	// --- GETTEREK ÉS SETTEREK ---
	public List<Lane> getLanes() {
		Skeleton.printCall(null, this, "getLanes");
		Skeleton.printReturn(this, "getLanes", "List<Lane>");
		return lanes;
	}
	public void setLanes(List<Lane> lanes) {
		this.lanes = lanes;
	}

	public MapNode getTargetNode() {
		Skeleton.printCall(null, this, "getTargetNode");
		Skeleton.printReturn(this, "getTargetNode", "MapNode");
		return targetNode;
	}
	public void setTargetNode(MapNode targetNode) {
		this.targetNode = targetNode;
	}
	public void addLane(Lane l){
                core.Skeleton.printCall(null, this, "addLane");
                lanes.add(l);
                core.Skeleton.printReturn(this, "addLane");
		
	}
}
