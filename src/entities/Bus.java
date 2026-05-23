package entities;

import actors.BusDriver;
import cli.Actionable;
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
public class Bus extends Vehicle implements Linkable, Actionable {
	/** @return Mindig igaz, ez a jármű busz. */
	@Override public boolean isBus() { return true; }
	private BusStop startNode;
	private BusStop endNode;
	private BusDriver driver;



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
		if (driver != null && driver.getManagedBus() != this) {
			driver.setManagedBus(this);
		}
	}



	/**
	 * A szimuláció egy ütemre futtatja le a búsz logikáját.
	 * Ha bénult, csökkenti az időzítőt; ellenkező esetben mozgatja a búszt.
	 */
	@Override
	public void tick() {
		if (isParalyzed) {
			if (paralysisTimer > 0) {
				paralysisTimer--;
				if (paralysisTimer == 0) {
					isParalyzed = false;
				}
			}
			return;
		}

		move();
	}

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
		if (currentLane == null || isParalyzed) {
			return;
		}

		if (isSnowBlocked() && !tryMoveToAvoidanceLane(progress)) {
			return;
		}

		if (!canAdvance()) {
			return;
		}

		int previousProgress = progress;
		advanceOnCurrentLane();
		awardPointAtDestination(previousProgress);
		routeFromLaneEnd();
	}

	private boolean canAdvance() {
		return currentLane != null && !isParalyzed && !stuck();
	}

	private void advanceOnCurrentLane() {
		if (progress < currentLane.getLength()) {
			progress++;
		}

		if (progress > currentLane.getLength()) {
			progress = currentLane.getLength();
		}
	}

	private void awardPointAtDestination(int previousProgress) {
		if (previousProgress < currentLane.getLength() && progress == currentLane.getLength() && driver != null) {
			MapNode targetNode = resolveCurrentTargetNode();
			if (targetNode != null && targetNode == endNode) {
				driver.achievePoints();
				BusStop oldStart = startNode;
				startNode = endNode;
				endNode = oldStart;
			}
		}
	}

	private void routeFromLaneEnd() {
		if (progress < currentLane.getLength()) {
			return;
		}

		MapNode targetNode = resolveCurrentTargetNode();
		if (targetNode == null) {
			return;
		}
		if (targetNode.isBusStop() && endNode == null) {
			endNode = (BusStop) targetNode;
		}
		targetNode.routeVehicle(this);
	}

	private MapNode resolveCurrentTargetNode() {
		if (currentLane == null || currentLane.getRoad() == null) {
			return null;
		}
		return currentLane.getRoad().getTargetNode();
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
		return true;
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
		return isSnowBlocked() || isParalyzed;
	}

	public boolean isSnowBlocked() {
		return currentLane != null && currentLane.getState() != null && currentLane.getState().isThickSnow();
	}

	@Override
	public boolean changeLane(Lane target) {
		if (target == null) {
			return false;
		}

		if (currentLane == target) {
			return true;
		}

		if (isParalyzed || stuck()) {
			return false;
		}

		if (currentLane != null && currentLane.getVehicles() != null) {
			currentLane.getVehicles().remove(this);
		}

		currentLane = target;
		targetLane = target;
		progress = 0;
		target.acceptVehicle(this);
		if (isSnowBlocked()) {
			tryMoveToAvoidanceLane(progress);
		}
		return true;
	}

	@Override
	public void paralyze(int time) {
		if (!isParalizable() || time <= 0) {
			return;
		}

		if (tryMoveToAvoidanceLane(progress)) {
			return;
		}

		isParalyzed = true;
		paralysisTimer = Math.max(paralysisTimer, time);
	}

	private boolean tryMoveToAvoidanceLane(int position) {
		Lane alternate = findAvoidanceLane(position);
		if (alternate == null) {
			return false;
		}

		if (currentLane != null) {
			currentLane.removeVehicle(this);
		}
		currentLane = alternate;
		targetLane = alternate;
		alternate.acceptVehicle(this);
		return true;
	}

	private Lane findAvoidanceLane(int position) {
		if (currentLane == null) {
			return null;
		}

		Lane leftLane = currentLane.getLeftLane();
		if (canUseAvoidanceLane(leftLane, position)) {
			return leftLane;
		}

		Lane rightLane = currentLane.getRightLane();
		if (canUseAvoidanceLane(rightLane, position)) {
			return rightLane;
		}

		return null;
	}

	private boolean canUseAvoidanceLane(Lane lane, int position) {
		return lane != null && !isThickSnow(lane) && !isBlockedAt(lane, position);
	}

	private boolean isThickSnow(Lane lane) {
		return lane != null && lane.getState() != null && lane.getState().isThickSnow();
	}

	private boolean isBlockedAt(Lane lane, int position) {
		if (lane == null || lane.getVehicles() == null) {
			return false;
		}

		for (Vehicle vehicle : lane.getVehicles()) {
			if (vehicle != this && vehicle.isParalizable() && vehicle.getProgress() >= position) {
				return true;
			}
		}

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
				lane.acceptVehicle(this);
				break;
			}
			case "targetLane":
			case "setTargetLane": {
				Lane lane = (Lane) registry.getObject(args[0]);
				setCurrentLane(lane);
				lane.acceptVehicle(this);
				break;
			}
			case "progress":
			case "setProgress": {
				setProgress(Integer.parseInt(args[0]));
				break;
			}
			case "addRouteStop": {
				BusStop bs = (BusStop) registry.getObject(args[0]);
				if (startNode == null) {
					setStartNode(bs);
				} else if (endNode == null) {
					setEndNode(bs);
				}
				break;
			}
			default:
				throw new Exception("Action failed: Unknown link property '" + property + "' for Bus");
		}
	}

	@Override
	public void performAction(String actionName, String[] args, ObjectRegistry registry) throws Exception {
		switch (actionName) {
			case "changeLane": {
				if (args.length < 1) throw new Exception("Action failed: changeLane requires a lane ID");
				Lane target = (Lane) registry.getObject(args[0]);
				changeLane(target);
				break;
			}
			default:
				throw new Exception();
		}
	}
}
