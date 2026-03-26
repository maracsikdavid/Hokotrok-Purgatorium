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
	public BusStop startNode;
	public BusStop endNode;
	public BusDriver driver;

	/**
	 * A busz idő függvényében történő  állapotváltozásokat implementáló függvény
	 */
	@Override
	public void tick() {
		Skeleton.printCall(null, this, "tick");
		Skeleton.printReturn(this, "tick");
	}

	/**
	 * A buszok mozgatása implementáló függvény. 
	 */
	@Override
	protected void move() {
		Skeleton.printCall(null, this, "move");
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
