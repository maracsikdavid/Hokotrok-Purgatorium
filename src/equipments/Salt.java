package equipments;

/**
 * A só, amelyet a SaltPlow kotrófej használ. A fejjel elszórt só 2 tick után teljesen
 * felolvasztja a sávon lévő jeget vagy havat, így az adott sáv teljesen tiszta lesz.
 */
public class Salt implements Consumable {
	private int amount;


	// --- KONSTRUKTOROK ---

	/**
	 * Alapértelmezett konstruktor.
	 */
	public Salt() {
	}

	/**
	 * Paraméteres konstruktor a mennyiség megadásához.
	 *
	 * @param amount a só mennyisége
	 */
	public Salt(int amount) {
		this.amount = amount;
	}


	// --- GETTEREK ÉS SETTEREK ---
	
	/**
	 * Visszaadja a só aktuális mennyiségét.
	 *
	 * @return a só mennyisége
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * Beállítja a só mennyiségét.
	 *
	 * @param amount a beállítandó mennyiség
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}


	// --- METÓDUSOK ---

	/**
	 * Felhasználja a megadott mennyiségű sót a sószóró fej működése közben.
	 */
	public void use() {
	}

	/**
     * Az objektum aktuális állapotának és attribútumainak kiírása a standard kimenetre.
     * * @param id Az objektum egyedi azonosítója, amellyel a Registry-ben szerepel.
     */
    public void printData(String id) {
    }
}
