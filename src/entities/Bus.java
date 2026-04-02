package entities;

import actors.BusDriver;
import topology.BusStop;
import topology.Intersection;
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
		this.move();
	}

	/**
	 * A buszok mozgatása implementáló függvény. 
	 */
	@Override
	protected void move() {

	}

	/**
	 * Ellenőrzi, hogy a busz bénulhat-e, tehát mozgásképtelenné válik.
	 *  A buszok is bénulhatnak jeges sávon vagy abban az esetben, ha összeütköznek másik járművel.
	 *
	 * @return igaz (a busz lebénul)
	 */
	@Override
	public boolean isParalizable() {
		return true;
	}

	/**
	 * Abban az esetben ha a busz elbénul, ez a függvény felelős az időtartam számon tartásáért.
	 *
	 * @param time az időtartam
	 */
	public void paralyze(int time) {
	}

	/**
	 * Ellenőrzi, hogy a busz elakadt-e.
	 *
	 * @return igaz, ha elakadt
	 */
	public boolean stuck() {
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
		return false;
	}
}
