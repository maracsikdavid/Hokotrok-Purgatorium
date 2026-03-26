package equipments;

import core.Skeleton;

/**
 * A só, amelyet a SaltPlow kotrófej használ. A fejjel elszórt só 2 tick után teljesen
 * felolvasztja a sávon lévő jeget vagy havat, így az adott sáv teljesen tiszta lesz.
 */
public class Salt {
	/**
	 * Az elszórható só mennyisége.
	 */
	public int amount;

	/**
	 * Felhasználja a megadott mennyiségű sót a sószóró fej működése közben.
	 */
	public void use() {
		Skeleton.printCall(null, this, "use");
		Skeleton.printReturn(this, "use");
	}
}
