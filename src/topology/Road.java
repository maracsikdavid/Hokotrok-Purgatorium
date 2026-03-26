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
	/**
	 * Ahová az út vezet
	 */
	public MapNode targetNode;
	
	/**
	 * Az út sávjainak listája.
	 */
	protected List<Lane> lanes = new ArrayList<>();

	/**
	 * Az út összes sávjának lekérése.
	 *
	 * @return a sávok listája
	 */
	public List<Lane> getLanes() {
		Skeleton.printCall(null, this, "getLanes");
		Skeleton.printReturn(this, "getLanes", "List<Lane>");
		return lanes;
	}

	/**
	 * Az út célcsomópontjának lekérése.
	 *
	 * @return az út végén lévő csomópont
	 */
	public MapNode getTargetNode() {
		Skeleton.printCall(null, this, "getTargetNode");
		Skeleton.printReturn(this, "getTargetNode", "MapNode");
		return targetNode;
	}
}
