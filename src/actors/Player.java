package actors;

/**
 * Absztrakt alaposztály, amely a szimulációban szereplő szereplőket reprezentálja.
 * Az összes játékos típus (BusDriver, Cleaner) ebből az osztályból származik.
 */
public abstract class Player {
	private String name;

	// --- GETTEREK ÉS SETTEREK ---
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
