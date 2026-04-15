package equipments;

/**
 * A sárkányfej (DragonPlow) üzemanyaga. Enélkül nem működik a sárkányfej,  
 * tehát nem lehet takarítani vele üzemanyag nélkül. A jeget és vékony 
 * vagy vastag havat lehet a segítségével azonnal eltakarítani egy sávról. 
 */
public class Biokerosene {
	private int amount;

	// --- GETTEREK ÉS SETTEREK ---
	/**
	 * Visszaadja az üzemanyag aktuális mennyiségét.
	 *
	 * @return az üzemanyag mennyisége
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * Beállítja az üzemanyag mennyiségét.
	 *
	 * @param amount a beállítandó mennyiség
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}

	// --- KONSTRUKTOROK ---
	/**
	 * Alapértelmezett konstruktor.
	 */
	public Biokerosene() {
	}

	/**
	 * Paraméteres konstruktor a mennyiség megadásához.
	 *
	 * @param amount az üzemanyag mennyisége
	 */
	public Biokerosene(int amount) {
		this.amount = amount;
	}

	// --- METÓDUSOK ---
	/**
	 * Felhasználja az aktuális üzemanyagmennyiséget egy adott sáv takarításának céljából.
	 */
	public void use() {
	}
}
