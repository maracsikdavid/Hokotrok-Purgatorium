package topology;

import cli.Linkable;
import cli.ObjectRegistry;
import cli.Printable;
import java.util.ArrayList;
import java.util.List;

/**
 * Absztrakt ősosztály, amely az úthálózat csomópontokat összekötő szakaszait reprezentálja. 
 * Felelőssége az adott útszakaszt felépítő sávok (Lane) összefogása és kezelése. 
 * Ebből származnak a normál utak, valamint a speciális tulajdonságokkal bíró hidak és alagutak.
 */
public abstract class Road implements Linkable, Printable {
	private MapNode targetNode;
	private List<Lane> lanes = new ArrayList<>();


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


	// --- GETTEREK ÉS SETTEREK ---

	/**
	 * Visszaadja az út sávjainak listáját.
	 *
	 * @return A sávok referenciáit tartalmazó lista.
	 */
	public List<Lane> getLanes() {
		return lanes;
	}

	/**
	 * Beállítja az út sávjainak listáját.
	 *
	 * @param lanes A beállítandó sávok listája.
	 */
	public void setLanes(List<Lane> lanes) {
		this.lanes = lanes;
	}

	/**
	 * Visszaadja az út cél csomópontját.
	 *
	 * @return A cél csomópont referenciája.
	 */
	public MapNode getTargetNode() {
		return targetNode;
	}

	/**
	 * Beállítja az út cél csomópontját.
	 *
	 * @param targetNode A beállítandó cél csomópont.
	 */
	public void setTargetNode(MapNode targetNode) {
		this.targetNode = targetNode;
	}


	// --- METÓDUSOK ---

	/**
	 * Új sáv hozzáadása az úthoz. A sáv hozzárendelődik ehhez az úthoz.
	 *
	 * @param l a hozzáadandó sáv
	 *
	 * Pszeudokód:
	 * 1. Hozzáadja a sávot a lanes listához.
	 * 2. Beállítja a sáv road referenciáját erre az útra.
	 */
	public void addLane(Lane l){
		if (l == null) {
			return;
		}

		if (!lanes.contains(l)) {
			lanes.add(l);
		}

		l.setRoad(this);
	}

	/**
	 * Összekapcsolja az utat más objektumokkal a parancssori argumentumok alapján.
	 *
	 * @param property A beállítandó tulajdonság neve (pl. "targetNode", "addLane").
	 * @param args Az összekapcsoláshoz szükséges argumentumok.
	 * @param registry Az objektumtár az azonosítók feloldásához.
	 * @throws Exception Ha a tulajdonság ismeretlen vagy a paraméter típusa érvénytelen.
	 */
	@Override
	public void performLink(String property, String[] args, ObjectRegistry registry) throws Exception {
		switch (property) {
			case "targetNode":
			case "setTargetNode": {
				try {
					MapNode node = (MapNode) registry.getObject(args[0]);
					setTargetNode(node);
				} catch (ClassCastException e) {
					throw new Exception("Type mismatch. Object " + args[0] + " is not of type MapNode.");
				}
				break;
			}
			case "addLane": {
				try {
					Lane lane = (Lane) registry.getObject(args[0]);
					addLane(lane);
				} catch (ClassCastException e) {
					throw new Exception("Action failed: '" + args[0] + "' is not a valid Lane");
				}
				break;
			}
			default:
				throw new Exception("Action failed: Unknown link property '" + property + "' for Road");
		}
	}

	/**
     * Az objektum állapotának és adatainak kiírása a standard kimenetre.
     *
     * @param id Az objektum azonosítója a regiszterben.
     * @param registry A központi objektumtár.
     */
	@Override
    public void printData(String id, ObjectRegistry registry) {
        System.out.println(this.getClass().getSimpleName() + "," + id);
        
        StringBuilder lanesStr = new StringBuilder("[");
        for (int i = 0; i < lanes.size(); i++) {
            lanesStr.append(registry.findId(lanes.get(i)));
            if (i < lanes.size() - 1) lanesStr.append(",");
        }
        lanesStr.append("]");
        System.out.println("lanes," + lanesStr.toString());
        
        System.out.println("targetNode," + registry.findId(targetNode));
    }
}
