package topology;

/**
 * Busz megálló csomópont. A buszok ebből a pontból indulnak és ide érkeznek meg.
 * A busz driver ebből a pontból irányítja a busz útválasztását.
 */
public class BusStop extends MapNode {
	/** @return Mindig igaz, ez a csómópont buszállomás. */
	@Override public boolean isBusStop() { return true; }
		

	/**
	 * Alapértelmezett konstruktor.
	 */
	public BusStop() {
		super();
	}




}
