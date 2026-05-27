package topology;

/**
 * Híd: egy speciális út, amely alagúttal párosított.
 * Felelőssége megegyezik a normál útéval
 */
public class Bridge extends Road {
	private Tunnel paired;



	/**
	 * Alapértelmezett konstruktor.
	 */
	public Bridge() {
		super();
	}

	/**
	 * Paraméteres konstruktor a híd inicializálásához.
	 *
	 * @param targetNode a cél csomópont
	 * @param paired a párosított alagút
	 */
	public Bridge(MapNode targetNode, Tunnel paired) {
		super(targetNode, new java.util.ArrayList<>());
		this.paired = paired;
	}



	/**
	 * Visszaadja a hídhoz párosított alagutat.
	 *
	 * @return A párosított alagút referenciája.
	 */
	public Tunnel getPaired() {
		return paired;
	}

	/**
	 * Beállítja a hídhoz párosított alagutat.
	 *
	 * @param paired A beállítandó alagút.
	 */
	public void setPaired(Tunnel paired) {
		this.paired = paired;
	}



	/**
	 * Összekapcsolja a hidat más objektumokkal a parancssori argumentumok alapján.
	 * Támogatja az ősosztály (Road) tulajdonságait és a híd-specifikus "paired" tulajdonságot.
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
				Tunnel tunnel = (Tunnel) registry.getObject(args[0]);
				setPaired(tunnel);
			} catch (ClassCastException e) {
				throw new Exception("Action failed: '" + args[0] + "' is not a valid Tunnel");
			}
		} else {
			super.performLink(property, args, registry);
		}
	}
	
	/**
     * Az objektum állapotának és speciális híd adatainak kiírása a standard kimenetre.
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
