package equipments;

import core.Skeleton;

/**
 * A só, amelyet a SaltPlow kotrófej használ. A fejjel elszórt só 2 tick után teljesen
 * felolvasztja a sávon lévő jeget vagy havat, így az adott sáv teljesen tiszta lesz.
 */
public class Salt {
	private int amount;


	// --- GETTEREK ÉS SETTEREK ---
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}


	// --- METÓDUSOK ---
	/**
	 * Felhasználja a megadott mennyiségű sót a sószóró fej működése közben.
	 */
	public void use() {
		Skeleton.printCall(null, this, "use");
		Skeleton.printReturn(this, "use");
	}
}
