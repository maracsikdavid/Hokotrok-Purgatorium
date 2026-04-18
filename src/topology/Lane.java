package topology;

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
public class Lane implements ITickable, Linkable, Printable {
	private int length;
	private LaneCondition state;
	private List<Vehicle> vehicles = new ArrayList<>();
	private Road road;

		
	// --- KONSTRUKTOROK ---

	/**
	 * Alapértelmezett konstruktor.
	 */
	public Lane() {
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


	// --- GETTEREK ÉS SETTEREK ---

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


	// --- LINKABLE ---

	@Override
	public void performLink(String property, String[] args, ObjectRegistry registry) throws Exception {
		switch (property) {
			case "condition":
			case "setState": {
				if (args.length < 1) {
					throw new Exception("Action failed: condition requires a condition name");
				}
				setState(createCondition(args[0]));
				break;
			}
			case "length":
			case "setLength": {
				if (args.length < 1) {
					throw new Exception("Action failed: length requires a value");
				}
				setLength(Integer.parseInt(args[0]));
				break;
			}
			case "road":
			case "setRoad": {
				Road r = (Road) registry.getObject(args[0]);
				setRoad(r);
				break;
			}
			default:
				throw new Exception("Action failed: Unknown link property '" + property + "' for Lane");
		}
	}

	/**
	 * Segédmetódus a LaneCondition típusú objektumok létrehozásához a string név alapján.
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


	// --- PRINTABLE ---

	@Override
	public void printData(String id, ObjectRegistry registry) {
		String stateName = (state != null) ? state.getClass().getSimpleName() : "null";
		System.out.println("Lane," + id);
		System.out.println("condition," + stateName);

		// Járművek listája ID-kkal
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < vehicles.size(); i++) {
			if (i > 0) sb.append(",");
			sb.append(registry.findId(vehicles.get(i)));
		}
		sb.append("]");
		System.out.println("vehicles," + sb.toString());
	}


	// --- METÓDUSOK ---

	/**
	 * Jármű elfogadása a sávra az aktuális állapot ellenőrzése után.
	 *
	 * @param v az elfogadandó jármű
	 */
	public void acceptVehicle(Vehicle v) {
    
    }

	/**
	 * Jármű eltávolítása a sávról (pl. sáv váltás vagy út vége után).
	 *
	 * @param v az eltávolítandó jármű
	 */
	public void removeVehicle(Vehicle v) {

	}

	/**
	 * Sáv állapotának megváltoztatása egy új időjárási feltételre
	 *
	 * @param newCondition az új állapot
	 */
	public void changeCondition(LaneCondition newCondition) {
	}

	/**
	 * Sáv frissítése az idő előrehaladásával (tick).
	 */
	@Override
	public void tick() {

	}

	/**
	 * Bal oldali szomszéd sáv lekérése 
	 *
	 * @return a bal oldali sáv, vagy null ha nincs
	 */
	public Lane getLeftLane() {
		return null;
	}

	/**
	 * Jobb oldali szomszéd sáv lekérése 
	 *
	 * @return a jobb oldali sáv, vagy null ha nincs
	 */
	public Lane getRightLane() {
		return null;
	}

	/**
     * Az objektum aktuális állapotának és attribútumainak kiírása a standard kimenetre.
     * * @param id Az objektum egyedi azonosítója, amellyel a Registry-ben szerepel.
     */
    public void printData(String id) {
    }
}
