package equipments;

import core.Skeleton;
import topology.Lane;

/**
 * Sárkányfej típusú kotrófej. Használatához biokerozin szükséges, amely
 * ha kifogy, a sárkányfej nem működik. A biokerozin használatakor az adott
 * sávon lévő hó és jég azonnal elolvad, és a sáv tiszta lesz. Ha elfogy
 * belőle az üzemanyag, újra kell tölteni, hogy a sárkányfej újra használható legyen.
 */
public class DragonPlow extends Plow {
	/**
	 * Az aktuális biokerozin forrás.
	 */
	public Biokerosene fuelSource;

	/**
	 * Takarítja a megadott sávot biokerozinnal, ha elég üzemanyag áll rendelkezésre.
	 * Ha nincs, nem működik.
	 *
	 * @param lane a takarítandó sáv
	 * @return igaz, ha sikeres volt a takarítás
	 */
	@Override
	public boolean clear(Lane lane) {
		Skeleton.printCall(null, this, "clear");
		Skeleton.printReturn(this, "clear", "false");
		return false;
	}

	/**
	 * Újra feltölti biokerozinnel a fejet.
	 *
	 * @param fuel az új biokerozin entitás, amelyet a sárkányfej használni fog
	 */
	public void refill(Biokerosene fuel) {
		Skeleton.printCall(null, this, "refill");
		this.fuelSource = fuel;
		Skeleton.printReturn(this, "refill");
	}
}
