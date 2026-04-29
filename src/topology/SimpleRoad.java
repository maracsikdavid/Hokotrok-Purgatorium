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
     * Az objektum állapotának és adatainak kiírása.
     *
     * @param id Az objektum azonosítója.
     * @param registry Az objektumtár.
     */
    @Override
    public void printData(String id, cli.ObjectRegistry registry) {
        super.printData(id, registry);
    }
}