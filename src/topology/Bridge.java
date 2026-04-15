package topology;

/**
 * Híd: egy speciális út, amely alagúttal párosított.
 * Felelőssége megegyezik a normál útéval
 */
public class Bridge extends Road {
	private Tunnel paired;

	// --- GETTEREK ÉS SETTEREK ---
	/**
	 * Visszaadja a hídhoz párosított alagutat.
	 *
	 * @return a párosított alagút
	 */
	public Tunnel getPaired() {
		return paired;
	}

	/**
	 * Beállítja a hídhoz párosított alagutat.
	 *
	 * @param paired a beállítandó alagút
	 */
	public void setPaired(Tunnel paired) {
		this.paired = paired;
	}

	// --- KONSTRUKTOROK ---
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
}
