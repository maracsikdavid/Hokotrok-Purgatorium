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


	// --- KONSTRUKTOROK ---

	/**
	 * Alapértelmezett konstruktor.
	 */
	public Lane() {
		this.length = 10;
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


	// --- METÓDUSOK ---

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
				if (regObj instanceof LaneCondition) {
					cond = (LaneCondition) regObj;
				} else if (regObj != null) {
					throw new Exception("Invalid argument type: " + arg);
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
				setLength(Integer.parseInt(args[0]));
				break;
			}
			case "road":
			case "setRoad": {
				Road r = (Road) registry.getObject(args[0]);
				setRoad(r);
				break;
			}
			case "rightLane": {
				// Speciális link teszteléshez, ha manuálisan akarunk szomszédot állítani
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

	}

	/**
	 * Visszaadja a bal oldali szomszédos sávot, ha létezik az adott úton.
	 *
	 * @return A bal oldali sáv referenciája, vagy null, ha a sáv az út széle.
	 *
	 * Pszeudokód:
	 * 1. Lekéri az út sávlistáját.
	 * 2. Meghatározza az aktuális sáv indexét.
	 * 3. Ha van bal oldali elem, visszaadja.
	 */
	public Lane getLeftLane() {
		return null;
	}

	/**
	 * Visszaadja a jobb oldali szomszédos sávot, ha létezik az adott úton.
	 *
	 * @return A jobb oldali sáv referenciája, vagy null, ha a sáv az út széle.
	 *
	 * Pszeudokód:
	 * 1. Lekéri az út sávlistáját.
	 * 2. Meghatározza az aktuális sáv indexét.
	 * 3. Ha van jobb oldali elem, visszaadja.
	 */
	public Lane getRightLane() {
		return null;
	}


}
