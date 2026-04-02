package equipments;
import topology.Lane;

/**
 * Sárkányfej típusú kotrófej. Használatához biokerozin szükséges, amely
 * ha kifogy, a sárkányfej nem működik. A biokerozin használatakor az adott
 * sávon lévő hó és jég azonnal elolvad, és a sáv tiszta lesz. Ha elfogy
 * belőle az üzemanyag, újra kell tölteni, hogy a sárkányfej újra használható legyen.
 */
public class DragonPlow extends Plow {
	private Biokerosene fuelSource;

	// --- GETTEREK ÉS SETTEREK ---
	public Biokerosene getFuelSource() {
		return fuelSource;
	}
	public void setFuelSource(Biokerosene fuelSource) {
		this.fuelSource = fuelSource;
	}

	// --- METÓDUSOK ---
	/**
	 * Takarítja a megadott sávot biokerozinnal, ha elég üzemanyag áll rendelkezésre.
	 * Ha nincs, nem működik.
	 *
	 * @param lane a takarítandó sáv
	 * @return igaz, ha sikeres volt a takarítás
	 */
	@Override
	public boolean clear(Lane lane) {
		return false;
	}

	/**
	 * Újra feltölti biokerozinnel a fejet.
	 *
	 * @param fuel az új biokerozin entitás, amelyet a sárkányfej használni fog
	 */
	public void refill(Biokerosene fuel) {
		this.fuelSource = fuel;
	}
}
