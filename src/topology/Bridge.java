package topology;

/**
 * Híd: egy speciális út, amely alagúttal párosított.
 * Felelőssége megegyezik a normál útéval
 */
public class Bridge extends Road {
	private Tunnel paired;


	// --- GETTEREK ÉS SETTEREK ---
	public Tunnel getPaired() {
		return paired;
	}
	public void setPaired(Tunnel paired) {
		this.paired = paired;
	}
}
