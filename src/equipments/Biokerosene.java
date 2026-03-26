package equipments;

import core.Skeleton;

/**
 * A sárkányfej (DragonPlow) üzemanyaga. Enélkül nem működik a sárkányfej,  
 * tehát nem lehet takarítani vele üzemanyag nélkül. A jeget és vékony 
 * vagy vastag havat lehet a segítségével azonnal eltakarítani egy sávról. 
 */
public class Biokerosene {
	/**
	 * Az üzemanyag mennyisége.
	 */
	public int amount;

	/**
	 * Felhasználja az aktuális üzemanyagmennyiséget egy adott sáv takarításának céljából.
	 */
	public void use() {
		Skeleton.printCall(null, this, "use");
		Skeleton.printReturn(this, "use");
	}
}
