package topology;

/**
 * Egy normál, szintbeli eltérések nélküli útszakaszt reprezentál a városban. 
 * Felelőssége a hozzá tartozó sávok összefogása és a csomópontok közötti normál közlekedés biztosítása.
 */
public class SimpleRoad extends Road {
	
	// --- KONSTRUKTOROK ---

	/**
	 * Alapértelmezett konstruktor.
	 */
	public SimpleRoad() {
		super();
	}

	/**
	 * Paraméteres konstruktor az út inicializálásához.
	 *
	 * @param targetNode a cél csomópont
	 */
	public SimpleRoad(MapNode targetNode) {
		super(targetNode, new java.util.ArrayList<>());
	}


	// --- METÓDUSOK ---

	/**
     * Az objektum aktuális állapotának és attribútumainak kiírása a standard kimenetre.
     * * @param id Az objektum egyedi azonosítója, amellyel a Registry-ben szerepel.
     */
    public void printData(String id) {
    }
}