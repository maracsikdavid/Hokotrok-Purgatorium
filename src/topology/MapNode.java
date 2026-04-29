package topology;

import cli.Linkable;
import cli.ObjectRegistry;
import cli.Printable;
import entities.Vehicle;
import java.util.ArrayList;
import java.util.List;

/**
 * Absztrakt alaposztály a térkép csomópontjai számára. 
 * Kezeli a kimenő utakat és a járművek útvonalválasztását a csomópontokon keresztül.
 */
public abstract class MapNode implements Linkable, Printable {
	private List<Road> outgoingRoads = new ArrayList<>();


	// --- KONSTRUKTOROK ---

	/**
	 * Alapértelmezett konstruktor.
	 */
	protected MapNode() {
	}

	/**
	 * Konstruktor kimenő utak megadásával.
	 *
	 * @param outgoingRoads A csomópontból induló utak listája.
	 */
	protected MapNode(List<Road> outgoingRoads) {
		this.outgoingRoads = outgoingRoads;
	}


	// --- GETTEREK ÉS SETTEREK ---

	/**
	 * Visszaadja a kimenő utak listáját.
	 *
	 * @return Az utak referenciáit tartalmazó lista.
	 */
	public List<Road> getOutgoingRoads() {
		return outgoingRoads;
	}

	/**
	 * Beállítja a kimenő utak listáját.
	 *
	 * @param outgoingRoads Az új úthálózat lista.
	 */
	public void setOutgoingRoads(List<Road> outgoingRoads) {
		this.outgoingRoads = outgoingRoads;
	}


	// --- METÓDUSOK ---

	/**
	 * Új kimenő utat ad a csomóponthoz.
	 *
	 * @param r A hozzáadandó út referenciája.
	 *
	 * Pszeudokód:
	 * 1. Ellenőrzi a paraméter érvényességét.
	 * 2. Hozzáadja az utat az outgoingRoads listához.
	 */
	public void addOutgoingRoad(Road r) {

	}

	/**
	 * Irányítja a járművet a következő útra a csomópontban hozott döntése alapján.
	 *
	 * @param v Az irányítandó jármű.
	 *
	 * Pszeudokód:
	 * 1. Lekéri a jármű következő út választását.
	 * 2. Ha van célút, kiválaszt egy célsávot.
	 * 3. Átmozgatja a járművet a kiválasztott sávra.
	 */
	public void routeVehicle(Vehicle v) {

	}

	/**
	 * Összekapcsolja a csomópontot más objektumokkal a parancssori argumentumok alapján.
	 *
	 * @param property A beállítandó tulajdonság neve (pl. "addOutgoingRoad").
	 * @param args Az összekapcsoláshoz szükséges argumentumok.
	 * @param registry Az objektumtár az azonosítók feloldásához.
	 * @throws Exception Ha a tulajdonság ismeretlen vagy a paraméter típusa érvénytelen.
	 */
	@Override
	public void performLink(String property, String[] args, ObjectRegistry registry) throws Exception {
		switch (property) {
			case "addOutgoingRoad": {
				Road r = (Road) registry.getObject(args[0]);
				addOutgoingRoad(r);
				break;
			}
			default:
				throw new Exception("Unknown link property '" + property + "' for " + getClass().getSimpleName());
		}
	}

	/**
	 * Segédmetódus a kimenő utak azonosítóinak string formátumúra alakításához.
	 *
	 * @param registry Az objektumtár.
	 * @return Az azonosítók listája stringként, pl. "[road1,road2]".
	 */
	protected String outgoingRoadsString(ObjectRegistry registry) {
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < outgoingRoads.size(); i++) {
			if (i > 0) sb.append(",");
			sb.append(registry.findId(outgoingRoads.get(i)));
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Az objektum állapotának és adatainak kiírása.
	 *
	 * @param id Az objektum azonosítója a regiszterben.
	 * @param registry A központi objektumtár.
	 */
	@Override
	public void printData(String id, ObjectRegistry registry) {
		System.out.println(getClass().getSimpleName() + "," + id);
		System.out.println("outgoingRoads," + outgoingRoadsString(registry));
	}
}
