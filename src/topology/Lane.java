package topology;

import cli.Actionable;
import cli.Linkable;
import cli.ObjectRegistry;
import cli.Printable;
import core.ITickable;
import entities.Vehicle;
import java.util.ArrayList;
import java.util.List;
import statemachine.CleanCondition;
import statemachine.GraveledIceCondition;
import statemachine.IceCondition;
import statemachine.LaneCondition;
import statemachine.ThickSnowCondition;
import statemachine.ThinSnowCondition;

/**
 * A sáv az úthálózat legkisebb, járművek által használható közlekedési egysége.
 * Felelőssége a rajta tartózkodó járművek nyilvántartása,
 * valamint a saját útviszonyának (állapotának) kezelése a State tervezési minta alapján
 */
public class Lane implements ITickable, Linkable, Actionable, Printable {
	private int length;
	private LaneCondition state;
	private List<Vehicle> vehicles = new ArrayList<>();
	private Road road;
	private List<Lane> adjacentLanes = new ArrayList<>();



	/**
	 * Alapértelmezett konstruktor.
	 */
	public Lane() {
		this.length = core.GameRules.DEFAULT_LANE_LENGTH;
		this.state = new CleanCondition();
	}

	/**
	 * Paraméteres konstruktor minden attribútummal.
	 *
	 * @param length a sáv hossza
	 * @param state a sáv állapota
	 * @param vehicles a sávon lévő járművek
	 * @param road a sávhoz tartozó út
	 */
	public Lane(int length, LaneCondition state, List<Vehicle> vehicles, Road road) {
		this.length = length;
		this.state = state;
		this.vehicles = vehicles;
		this.road = road;
	}



	/**
	 * Visszaadja a sáv hosszát.
	 *
	 * @return a sáv hossza
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Beállítja a sáv hosszát.
	 *
	 * @param length a beállítandó hossz
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * Visszaadja a sáv aktuális állapotát.
	 *
	 * @return a sáv állapota
	 */
	public LaneCondition getState() {
		return state;
	}

	/**
	 * Beállítja a sáv állapotát.
	 *
	 * @param state a beállítandó állapot
	 */
	public void setState(LaneCondition state) {
		this.state = state;
	}

	/**
	 * Visszaadja a sávon lévő járművek listáját.
	 *
	 * @return a járművek listája
	 */
	public List<Vehicle> getVehicles() {
		return vehicles;
	}

	/**
	 * Beállítja a sávon lévő járművek listáját.
	 *
	 * @param vehicles a beállítandó járműlista
	 */
	public void setVehicles(List<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}

	/**
	 * Visszaadja a sávhoz tartozó utat.
	 *
	 * @return a sávhoz tartozó út
	 */
	public Road getRoad() {
		return road;
	}

	/**
	 * Beállítja a sávhoz tartozó utat.
	 *
	 * @param road a beállítandó út
	 */
	public void setRoad(Road road) {
		this.road = road;
	}

	/**
	 * Visszaadja a szomszédos sávok listáját (pl. sávváltás / előzés szimulációhoz).
	 *
	 * @return a szomszédos sávok
	 */
	public List<Lane> getAdjacentLanes() {
		return adjacentLanes;
	}

	/**
	 * Felveszi a megadott sávot a szomsz\u00e9dos s\u00e1vok list\u00e1j\u00e1ra, ha m\u00e9g nem szerepel benne.
	 *
	 * @param other A hozz\u00e1adand\u00f3 szomsz\u00e9dos s\u00e1v.
	 */
	private void addNeighbor(Lane other) {
		if (other != null && !adjacentLanes.contains(other)) {
			adjacentLanes.add(other);
		}
	}



	/**
	 * Összekapcsolja a sávot más objektumokkal vagy beállítja az alapvető tulajdonságait 
	 * a parancssori argumentumok alapján.
	 *
	 * @param property A beállítandó tulajdonság neve (pl. "condition", "length", "road").
	 * @param args     A beállításhoz szükséges argumentumok.
	 * @param registry A központi objektumtár az azonosítók feloldásához.
	 * @throws Exception Ha a tulajdonság ismeretlen vagy az argumentumok érvénytelenek.
	 */
	@Override
	public void performLink(String property, String[] args, ObjectRegistry registry) throws Exception {
		switch (property) {
			case "condition":
			case "setState": {
				if (args.length < 1) {
					throw new Exception("Action failed: condition requires a condition name");
				}
				String arg = args[0];
				LaneCondition cond = null;
				Object regObj = registry.getObjects().get(arg);
				if (regObj != null) {
					try {
						cond = (LaneCondition) regObj;
					} catch (ClassCastException e) {
						throw new Exception("Invalid argument type: " + arg);
					}
				} else {
					try {
						cond = createCondition(arg);
					} catch (Exception e) {
						throw new Exception("Invalid argument type: " + arg);
					}
				}
				setState(cond);
				break;
			}
			case "length":
			case "setLength": {
				if (args.length < 1) {
					throw new Exception("Action failed: length requires a value");
				}
				try {
					setLength(Integer.parseInt(args[0]));
				} catch (NumberFormatException e) {
					throw new Exception("Invalid argument type: " + args[0]);
				}
				break;
			}
			case "road":
			case "setRoad": {
				Road r = (Road) registry.getObject(args[0]);
				setRoad(r);
				break;
			}
			case "rightLane": {

				break;
			}
			case "targetNode": {
				MapNode node = (MapNode) registry.getObject(args[0]);
				if (road != null) {
					road.setTargetNode(node);
				}
				break;
			}
			case "addAdjacent": {
				Lane other = (Lane) registry.getObject(args[0]);
				addNeighbor(other);
				if (other != null) {
					other.addNeighbor(this);
				}
				break;
			}
			default:
				throw new Exception("Action failed: Unknown link property '" + property + "' for Lane");
		}
	}

	/**
	 * Segédmetódus a LaneCondition típusú objektumok létrehozásához a megadott név alapján.
	 *
	 * @param conditionName Az állapotosztály neve.
	 * @return A létrehozott állapotobjektum.
	 * @throws Exception Ha az állapotnév ismeretlen.
	 */
	private LaneCondition createCondition(String conditionName) throws Exception {
		switch (conditionName) {
			case "CleanCondition":
				return new CleanCondition();
			case "ThinSnowCondition":
				return new ThinSnowCondition();
			case "ThickSnowCondition":
				return new ThickSnowCondition();
			case "IceCondition":
				return new IceCondition();
			case "GraveledIceCondition":
				return new GraveledIceCondition();
			default:
				throw new Exception("Unknown condition: " + conditionName);
		}
	}

	/**
	 * Az objektum állapotának és adatainak kiírása a standard kimenetre.
	 *
	 * @param id Az objektum azonosítója.
	 * @param registry Az objektumtár a referenciák feloldásához.
	 */
	@Override
	public void printData(String id, ObjectRegistry registry) {
		String roadId = (road != null) ? registry.findId(road) : "null";
		String stateName = (state != null) ? state.getClass().getSimpleName() : "null";
		System.out.println("Lane," + id);
		System.out.println("road," + roadId);
		System.out.println("condition," + stateName);
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < vehicles.size(); i++) {
			if (i > 0) sb.append(",");
			sb.append(registry.findId(vehicles.get(i)));
		}
		sb.append("]");
		System.out.println("vehicles," + sb.toString());
		System.out.println("length," + length);
		if (road != null && road.getLanes() != null && road.getLanes().size() > 2) {
			System.out.println("leftLane," + registry.findId(getLeftLane()));
			System.out.println("rightLane," + registry.findId(getRightLane()));
		}
	}

	/**
	 * Végrehajtja a sávon egy nevet adott műveletet.
	 *
	 * @param actionName Az akció neve (pl. "addSnow", "applySalt").
	 * @param args Az akció argumentumai.
	 * @param registry Az objektumtár.
	 * @throws Exception Ha az akció ismeretlen.
	 */
	@Override
	public void performAction(String actionName, String[] args, ObjectRegistry registry) throws Exception {
		switch (actionName) {
			case "addSnow":
				state.addSnow(this);
				break;
			case "applySalt":
				state.applySalt(this);
				break;
			case "applyGravel":
				state.applyGravel(this);
				break;
			default:
				throw new Exception();
		}
	}

	/**
	 * Jármű elfogadása a sávra. Regisztrálja a járművet és értesíti az aktuális állapotot 
	 * az esetleges környezeti hatások (pl. megcsúszás) érvényesítéséhez.
	 *
	 * @param v Az elfogadandó jármű.
	 *
	 * Pszeudokód:
	 * 1. Hozzáadja a járművet a vehicles listához.
	 * 2. Meghívja a state.acceptVehicle(this, v) metódust.
	 */
	public void acceptVehicle(Vehicle v) {
		if (v != null && !vehicles.contains(v)) {
			vehicles.add(v);
		}
		if (state != null && v != null) {
			state.acceptVehicle(this, v);
		}
	}

	/**
	 * Jármű eltávolítása a sávról (pl. sávváltás vagy az út elhagyása esetén).
	 *
	 * @param v Az eltávolítandó jármű.
	 *
	 * Pszeudokód:
	 * 1. Eltávolítja a járművet a vehicles listából.
	 */
	public void removeVehicle(Vehicle v) {
		if (v != null && vehicles.contains(v)) {
			vehicles.remove(v);
		}
	}

	/**
	 * Megváltoztatja a sáv aktuális állapotát (pl. takarítás vagy havazás hatására).
	 *
	 * @param newCondition Az új állapot (Condition) objektum.
	 *
	 * Pszeudokód:
	 * 1. Beállítja a state mezőt az új állapotra.
	 */
	public void changeCondition(LaneCondition newCondition) {

	}

	/**
	 * Időlépés végrehajtása a sávon. Frissíti az állapotfüggő folyamatokat (pl. hóolvadás).
	 *
	 * Pszeudokód:
	 * 1. Meghívja a state.tick(this) metódust.
	 */
	@Override
	public void tick() {
		if (state != null) {
			state.tick(this);
		}
	}

	/**
	 * Segéd: az út {@code lanes} listájában elfoglalt index; ha nincs rajta az úton, -1.
	 */
	private int indexOnRoad() {
		if (road == null || road.getLanes() == null) {
			return -1;
		}
		return road.getLanes().indexOf(this);
	}

	/**
	 * Visszaadja a bal oldali szomszédos sávot ugyanazon az úton (alacsonyabb index a listában).
	 * Feltételezzük, hogy a sávok balról jobbra vannak indexelve (0 = legszélső „bal”).
	 *
	 * @return A bal oldali sáv, vagy null, ha ez az út bal szélső sávja vagy nincs úthoz rendelve.
	 */
	public Lane getLeftLane() {
		int idx = indexOnRoad();
		if (idx <= 0) {
			return null;
		}
		return road.getLanes().get(idx - 1);
	}

	/**
	 * Visszaadja a jobb oldali szomszédos sávot ugyanazon az úton (magasabb index).
	 *
	 * @return A jobb oldali sáv, vagy null, ha ez az út jobb szélső sávja vagy nincs úthoz rendelve.
	 */
	public Lane getRightLane() {
		int idx = indexOnRoad();
		if (road == null || road.getLanes() == null || idx < 0) {
			return null;
		}
		List<Lane> lanes = road.getLanes();
		if (idx >= lanes.size() - 1) {
			return null;
		}
		return lanes.get(idx + 1);
	}


}
