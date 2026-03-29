package entities;

import actors.BusDriver;
import core.Skeleton;
import topology.BusStop;
import topology.Lane;

/**
 * A busz tömegközelkedési jármű. Tartozik hozzá egy BusDriver-t, illetve a térképen előre megadott kezdő
 *  és végállomás. A busz célja, hogy e között a 2 MapNode között minél többször megforduljon és
 * ezzel pontot szerezzen.
 */
public class Bus extends Vehicle {
	private BusStop startNode;
	private BusStop endNode;
	private BusDriver driver;


	// --- GETTEREK ÉS SETTEREK ---
	public BusStop getStartNode() {
		return startNode;
	}
	public void setStartNode(BusStop startNode) {
		this.startNode = startNode;
	}

	public BusStop getEndNode() {
		return endNode;
	}
	public void setEndNode(BusStop endNode) {
		this.endNode = endNode;
	}

	public BusDriver getDriver() {
		return driver;
	}
	public void setDriver(BusDriver driver) {
		this.driver = driver;
	}


	// --- METÓDUSOK ---
	/**
	 * A busz idő függvényében történő  állapotváltozásokat implementáló függvény
	 */
	@Override
	public void tick() {
		Skeleton.printCall(null, this, "tick");
		this.move();
		Skeleton.printReturn(this, "tick");
	}

	/**
	 * A buszok mozgatása implementáló függvény. 
	 */
	@Override
	protected void move() {
		Skeleton.printCall(null, this, "move");

		switch (Skeleton.getActiveTestCaseId()) {
            case 24: {
                Lane nextLane = getTargetLane();
                if (nextLane != null) {
                    nextLane.acceptVehicle(this);
                }

                Lane currentLane = getCurrentLane();
                if (currentLane != null) {
                    currentLane.removeVehicle(this);
                }

                topology.Road road = (nextLane != null) ? nextLane.getRoad() : null;
                if (road != null) {
                    road.getTargetNode();
                }

                int answer = Skeleton.getIntFromUser("A jelenlegi csomópont egyenlő a busz cél csompontjával? (1: Yes, 0: No)");

                if (answer == 1 && driver != null) {
                    driver.achievePoints();
                }
                break;
            }
            default: {
                // Alap mozgás más teszteseteknél
                if (getTargetLane() != null) {
                    getTargetLane().acceptVehicle(this);
                }
                if (getCurrentLane() != null) {
                    getCurrentLane().removeVehicle(this);
                }
                break;
            }
        }

		Skeleton.printReturn(this, "move");
	}

	/**
	 * Ellenőrzi, hogy a busz bénulhat-e, tehát mozgásképtelenné válik.
	 *  A buszok is bénulhatnak jeges sávon vagy abban az esetben, ha összeütköznek másik járművel.
	 *
	 * @return igaz (a busz lebénul)
	 */
	@Override
	public boolean isParalizable() {
		Skeleton.printCall(null, this, "isParalizable");
		Skeleton.printReturn(this, "isParalizable", "true");
		return true;
	}

	/**
	 * Abban az esetben ha a busz elbénul, ez a függvény felelős az időtartam számon tartásáért.
	 *
	 * @param time az időtartam
	 */
	public void paralyze(int time) {
		Skeleton.printCall(null, this, "paralyze");
		Skeleton.printReturn(this, "paralyze");
	}

	/**
	 * Ellenőrzi, hogy a busz elakadt-e.
	 *
	 * @return igaz, ha elakadt
	 */
	public boolean stuck() {
		Skeleton.printCall(null, this, "stuck");
		Skeleton.printReturn(this, "stuck", "false");
		return false;
	}

	/**
	 * A busz sávváltása.
	 *
	 * @param target a cél sáv
	 * @return igaz, ha sikeres
	 */
	@Override
	public boolean changeLane(Lane target) {
		Skeleton.printCall(null, this, "changeLane");
		Skeleton.printReturn(this, "changeLane", "true");
		return true;
	}
}
