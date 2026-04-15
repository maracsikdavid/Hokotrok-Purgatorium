package actors;

/**
 * Absztrakt alaposztály, amely a szimulációban szereplő szereplőket reprezentálja.
 * Az összes játékos típus (BusDriver, Cleaner) ebből az osztályból származik.
 */
public abstract class Player {
	private String name;

	// --- GETTEREK ÉS SETTEREK ---
	/**
	 * Visszaadja a játékos nevét.
	 *
	 * @return a játékos neve
	 */
	public String getName() {
		return name;
	}

	/**
	 * Beállítja a játékos nevét.
	 *
	 * @param name a beállítandó név
	 */
	public void setName(String name) {
		this.name = name;
	}

	// --- KONSTRUKTOROK ---
	/**
	 * Alapértelmezett konstruktor.
	 */
	protected Player() {
	}

	/**
	 * Paraméteres konstruktor a név megadásához.
	 *
	 * @param name a játékos neve
	 */
	protected Player(String name) {
		this.name = name;
	}
}
