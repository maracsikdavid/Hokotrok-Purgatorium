package topology;

/**
 * Busz megálló csomópont. A buszok ebből a pontból indulnak és ide érkeznek meg.
 * A busz driver ebből a pontból irányítja a busz útválasztását.
 */
public class BusStop extends MapNode {
		
	// --- KONSTRUKTOROK ---

	/**
	 * Alapértelmezett konstruktor.
	 */
	public BusStop() {
		super();
	}


	// --- METÓDUSOK ---

	/**
     * Az objektum aktuális állapotának és attribútumainak kiírása a standard kimenetre.
     * * @param id Az objektum egyedi azonosítója, amellyel a Registry-ben szerepel.
     */
    public void printData(String id) {
    }
}
