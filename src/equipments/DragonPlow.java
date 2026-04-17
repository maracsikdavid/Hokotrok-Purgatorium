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

	
	// --- KONSTRUKTOROK ---

	/**
	 * Alapértelmezett konstruktor.
	 */
	public DragonPlow() {
		super();
	}

	/**
	 * Paraméteres konstruktor az üzemanyagforrás megadásához.
	 *
	 * @param fuelSource a használt biokerozin forrás
	 */
	public DragonPlow(Biokerosene fuelSource) {
		super();
		this.fuelSource = fuelSource;
	}


	// --- GETTEREK ÉS SETTEREK ---

	/**
	 * Visszaadja a sárkányfej üzemanyagforrását.
	 *
	 * @return a biokerozin forrás
	 */
	public Biokerosene getFuelSource() {
		return fuelSource;
	}

	/**
	 * Beállítja a sárkányfej üzemanyagforrását.
	 *
	 * @param fuelSource a beállítandó biokerozin forrás
	 */
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
	}

	/**
     * Az objektum aktuális állapotának és attribútumainak kiírása a standard kimenetre.
     * * @param id Az objektum egyedi azonosítója, amellyel a Registry-ben szerepel.
     */
    public void printData(String id) {
    }
}
