package topology;

/**
 * Az alagút egy olyan, felszín alatt elhelyezkedő közlekedési egység, amely az úthoz hasonlóan (egy speciális úttípus) egy vagy több sávból is állhat,
 * és elválaszthatatlan párt alkot egy felette elhelyezkedő híddal. 
 * Felelőssége a hó- és jégmentes közlekedés biztosítása: mivel az alagút védett és nem esik be a csapadék, 
 * megakadályozza a hóréteg és a jegesedés kialakulását a hozzá tartozó sávokon. 
 * Ezen kívül az úthoz hasonlóan összefogja azokat a sávokat, melyek két adott egymástól különböző csomópont között helyezkednek el, 
 * ezáltal lehetővé téve több jármű közlekedését egyszerre a csomópontok között.
 */

public class Tunnel extends Road {
	private Bridge paired;



	/**
	 * Alapértelmezett konstruktor.
	 */
	public Tunnel() {
		super();
	}

	/**
	 * Paraméteres konstruktor az alagút inicializálásához.
	 *
	 * @param targetNode a cél csomópont
	 * @param paired a párosított híd
	 */
	public Tunnel(MapNode targetNode, Bridge paired) {
		super(targetNode, new java.util.ArrayList<>());
		this.paired = paired;
	}



	/**
	 * Visszaadja az alagúthoz párosított hidat.
	 *
	 * @return A párosított híd referenciája.
	 */
	public Bridge getPaired() {
		return paired;
	}

	/**
	 * Beállítja az alagúthoz párosított hidat.
	 *
	 * @param paired A beállítandó híd.
	 */
	public void setPaired(Bridge paired) {
		this.paired = paired;
	}



	/**
	 * Összekapcsolja az alagutat más objektumokkal a parancssori argumentumok alapján.
	 * Támogatja az ősosztály (Road) tulajdonságait és az alagút-specifikus "paired" tulajdonságot.
	 *
	 * @param property A beállítandó tulajdonság neve (pl. "paired", "targetNode").
	 * @param args Az összekapcsoláshoz szükséges argumentumok.
	 * @param registry Az objektumtár az azonosítók feloldásához.
	 * @throws Exception Ha a tulajdonság ismeretlen vagy a paraméter típusa érvénytelen.
	 */
	@Override
	public void performLink(String property, String[] args, cli.ObjectRegistry registry) throws Exception {
		if ("paired".equals(property) || "setPaired".equals(property)) {
			try {
				Bridge bridge = (Bridge) registry.getObject(args[0]);
				setPaired(bridge);
			} catch (ClassCastException e) {
				throw new Exception("Action failed: '" + args[0] + "' is not a valid Bridge");
			}
		} else {
			super.performLink(property, args, registry);
		}
	}
	
	/**
     * Az objektum állapotának és speciális alagút adatainak kiírása a standard kimenetre.
     *
     * @param id Az objektum azonosítója a regiszterben.
     * @param registry A központi objektumtár.
     */
	@Override
    public void printData(String id, cli.ObjectRegistry registry) {
        super.printData(id, registry);
        System.out.println("paired," + registry.findId(paired));
    }
}
