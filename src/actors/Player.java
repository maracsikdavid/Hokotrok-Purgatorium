package actors;

/**
 * Absztrakt alaposztály, amely a szimulációban szereplő szereplőket reprezentálja.
 * Az összes játékos típus (BusDriver, Cleaner) ebből az osztályból származik.
 */
public abstract class Player {
	/**
	 * A szereplő neve.
	 */
	public String name;
}
