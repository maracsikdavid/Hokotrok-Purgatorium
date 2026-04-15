package topology;
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
	/**
	 * Visszaadja az út sávjainak listáját.
	 *
	 * @return a sávok listája
	 */
	public List<Lane> getLanes() {
		return lanes;
	}

	/**
	 * Beállítja az út sávjainak listáját.
	 *
	 * @param lanes a beállítandó sávlista
	 */
	public void setLanes(List<Lane> lanes) {
		this.lanes = lanes;
	}

	/**
	 * Visszaadja az út cél csomópontját.
	 *
	 * @return a cél csomópont
	 */
	public MapNode getTargetNode() {
		return targetNode;
	}

	/**
	 * Beállítja az út cél csomópontját.
	 *
	 * @param targetNode a beállítandó cél csomópont
	 */
	public void setTargetNode(MapNode targetNode) {
		this.targetNode = targetNode;
	}

	// --- KONSTRUKTOROK ---
	/**
	 * Alapértelmezett konstruktor.
	 */
	protected Road() {
	}
	
	/**
	 * Paraméteres konstruktor az út attribútumaival.
	 *
	 * @param targetNode a cél csomópont
	 * @param lanes az út sávjai
	 */
	protected Road(MapNode targetNode, List<Lane> lanes) {
		this.targetNode = targetNode;
		this.lanes = lanes;
	}
	
	/**
	 * Új sáv hozzáadása az úthoz. A sáv hozzárendelődik ehhez az úthoz.
	 *
	 * @param l a hozzáadandó sáv
	 */
	public void addLane(Lane l){
	}
}
