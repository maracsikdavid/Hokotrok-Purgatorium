package entities;

import actors.BusDriver;
import cli.Linkable;
import cli.ObjectRegistry;
import topology.BusStop;
import topology.Lane;
import topology.MapNode;
import topology.Road;

/**
 * A busz tömegközelkedési jármű. Tartozik hozzá egy BusDriver-t, illetve a térképen előre megadott kezdő
 *  és végállomás. A busz célja, hogy e között a 2 MapNode között minél többször megforduljon és
 * ezzel pontot szerezzen.
 */
public class Bus extends Vehicle implements Linkable {
	private BusStop startNode;
	private BusStop endNode;
	private BusDriver driver;


	// --- KONSTRUKTOROK ---

	/**
	 * Alapértelmezett konstruktor.
	 */
	public Bus() {
		super();
	}

	/**
	 * Paraméteres konstruktor a busz alapadatainak beállításához.
	 *
	 * @param startNode a busz kezdő megállója
	 * @param endNode a busz végállomása
	 * @param driver a busz sofőrje
	 */
	public Bus(BusStop startNode, BusStop endNode, BusDriver driver) {
		super();
		this.startNode = startNode;
		this.endNode = endNode;
		this.driver = driver;
	}


	// --- GETTEREK ÉS SETTEREK ---

	/**
	 * Visszaadja a busz kezdő megállóját.
	 *
	 * @return A kezdő megálló referenciája.
	 */
	public BusStop getStartNode() {
		return startNode;
	}

	/**
	 * Beállítja a busz kezdő megállóját.
	 *
	 * @param startNode A beállítandó kezdő megálló.
	 */
	public void setStartNode(BusStop startNode) {
		this.startNode = startNode;
	}

	/**
	 * Visszaadja a busz végállomását.
	 *
	 * @return A végállomás referenciája.
	 */
	public BusStop getEndNode() {
		return endNode;
	}

	/**
	 * Beállítja a busz végállomását.
	 *
	 * @param endNode A beállítandó végállomás.
	 */
	public void setEndNode(BusStop endNode) {
		this.endNode = endNode;
	}

	/**
	 * Visszaadja a busz sofőrjét.
	 *
	 * @return A busz sofőrje referenciája.
	 */
	public BusDriver getDriver() {
		return driver;
	}

	/**
	 * Beállítja a busz sofőrjét.
	 *
	 * @param driver A beállítandó sofőr.
	 */
	public void setDriver(BusDriver driver) {
		this.driver = driver;
	}


	// --- METÓDUSOK ---

	/**
	 * A busz mozgatását végző metódus. Ha a busz nincs lebénulva, növeli a haladási szintet.
	 *
	 * Pszeudokód:
	 * 1. Ha a busz bénult, nem mozog.
	 * 2. Egyébként növeli a progress értéket.
	 * 3. Sáv végén csomóponti útválasztást kezdeményez.
	 */
	@Override
	protected void move() {

	}

	/**
	 * Meghatározza, hogy a busz lebénulhat-e (pl. jeges úton). 
	 * A buszok érzékenyek a környezeti viszonyokra.
	 *
	 * @return Mindig igaz.
	 *
	 * Pszeudokód:
	 * 1. Visszatér egy logikai konstanssal.
	 */
	@Override
	public boolean isParalizable() {
		return false;
	}

	/**
	 * Ellenőrzi, hogy a busz mozgásképtelenné vált-e (elakadt-e).
	 *
	 * @return Igaz, ha a busz jelenleg le van bénulva.
	 *
	 * Pszeudokód:
	 * 1. Kiértékeli a mozgásképtelenségi állapotot.
	 * 2. Logikai értékkel tér vissza.
	 */
	@Override
	public boolean stuck() {
		return false;
	}

	/**
     * Az objektum állapotának és speciális busz adatainak kiírása a standard kimenetre.
     *
     * @param id Az objektum azonosítója a regiszterben.
     * @param registry A központi objektumtár.
     */
	@Override
	public void printData(String id, ObjectRegistry registry) {
	    super.printData(id, registry);
	    System.out.println("startNode," + registry.findId(startNode));
	    System.out.println("endNode," + registry.findId(endNode));
	    System.out.println("driver," + registry.findId(driver));
	}

	/**
	 * Az útvonalválasztás implementációja. Mivel a buszsofőr közvetlenül irányítja, 
	 * a busz automatikusan nem választ következő utat a csomópontokban.
	 *
	 * @param currentNode Az aktuális csomópont.
	 * @return Mindig null, parancsra vár.
	 *
	 * Pszeudokód:
	 * 1. Nincs automatikus útválasztás.
	 * 2. Null értékkel tér vissza.
	 */
	@Override
	public Road chooseNextRoad(MapNode currentNode) {
		return null;
	}

	/**
	 * Összekapcsolja a buszt más objektumokkal a parancssori argumentumok alapján.
	 *
	 * @param property A beállítandó tulajdonság neve.
	 * @param args     Az összekapcsoláshoz szükséges argumentumok.
	 * @param registry A központi objektumtár.
	 * @throws Exception Ha a tulajdonság ismeretlen vagy az összekapcsolás sikertelen.
	 */
	@Override
	public void performLink(String property, String[] args, ObjectRegistry registry) throws Exception {
		switch (property) {
			case "startNode":
			case "setStartNode": {
				BusStop bs = (BusStop) registry.getObject(args[0]);
				setStartNode(bs);
				break;
			}
			case "endNode":
			case "setEndNode": {
				BusStop bs = (BusStop) registry.getObject(args[0]);
				setEndNode(bs);
				break;
			}
			case "driver":
			case "setDriver": {
				BusDriver d = (BusDriver) registry.getObject(args[0]);
				setDriver(d);
				break;
			}
			case "currentLane":
			case "setCurrentLane": {
				Lane lane = (Lane) registry.getObject(args[0]);
				setCurrentLane(lane);
				lane.getVehicles().add(this);
				break;
			}
			default:
				throw new Exception("Action failed: Unknown link property '" + property + "' for Bus");
		}
	}
}