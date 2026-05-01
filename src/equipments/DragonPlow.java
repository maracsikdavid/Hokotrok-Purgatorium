package equipments;

import cli.Linkable;
import cli.ObjectRegistry;
import cli.Printable;
import statemachine.CleanCondition;
import topology.Lane;

/**
 * A sárkányfej kotrófej, amely biokerozint égetve azonnal felolvasztja a havat és a jeget.
 */
public class DragonPlow extends Plow implements Linkable, Printable {
	private Biokerosene fuelSource;


	// --- KONSTRUKTOROK ---

	/**
	 * Alapértelmezett konstruktor.
	 */
	public DragonPlow() {
		super();
	}

	/**
	 * Konstruktor az üzemanyagforrás megadásával.
	 *
	 * @param fuelSource A biokerozint biztosító forrás.
	 */
	public DragonPlow(Biokerosene fuelSource) {
		super();
		this.fuelSource = fuelSource;
	}


	// --- GETTEREK ÉS SETTEREK ---

	/**
	 * Visszaadja a jelenlegi üzemanyagforrást.
	 *
	 * @return Az üzemanyagforrás referenciája.
	 */
	public Biokerosene getFuelSource() {
		return fuelSource;
	}

	/**
	 * Beállítja az üzemanyagforrást.
	 *
	 * @param fuelSource Az új üzemanyagforrás.
	 */
	public void setFuelSource(Biokerosene fuelSource) {
		this.fuelSource = fuelSource;
	}


	// --- METÓDUSOK ---

	/**
	 * Felolvasztja a havat vagy jeget a megadott sávon, ha az üzemanyagforrás nem üres.
	 *
	 * @param lane A kezelendő sáv.
	 * @return Igaz, ha a művelet sikeres volt, egyébként hamis.
	 * @throws Exception Ha az üres tank miatt a művelet meghiúsul.
	 *
	 * Pszeudokód:
	 * 1. Erőforrás-ellenőrzés isEmpty() alapján.
	 * 2. Nem tiszta sáv esetén állapotváltás CleanCondition-re.
	 * 3. Üzemanyag csökkentése.
	 */
	@Override
	public boolean clear(Lane lane) throws Exception {
		if (lane == null) {
			return false;
		}
		
		if (isEmpty()) {
			throw new Exception("Action failed: Empty tank.");
		}

		if (lane.getState() instanceof CleanCondition) {
			return false;
		}

		lane.setState(new CleanCondition());
		fuelSource.use();
		return true;
	}

	/**
	 * Feltölti a kotrófejet új biokerozinnal.
	 *
	 * @param fuel A betöltendő biokerozin példánya.
	 *
	 * Pszeudokód:
	 * 1. A fuelSource mezőt a kapott példányra állítja.
	 */
	public void refill(Biokerosene fuel) {
		setFuelSource(fuel);
	}

	/**
	 * Ellenőrzi, hogy a kotrófej kifogyott-e az üzemanyagból.
	 *
	 * @return Igaz, ha nincs több üzemanyag, egyébként hamis.
	 *
	 * Pszeudokód:
	 * 1. Ha fuelSource null vagy amount == 0, true.
	 * 2. Egyébként false.
	 */
	public boolean isEmpty() {
		return fuelSource == null || fuelSource.getAmount() <= 0;
	}

	/**
	 * Összekapcsolja a kotrófejet egy üzemanyagforrással.
	 *
	 * @param property A tulajdonság neve.
	 * @param args Az azonosítót tartalmazó tömb.
	 * @param registry Az objektumtár.
	 * @throws Exception Ha az összekapcsolás sikertelen.
	 */
	@Override
	public void performLink(String property, String[] args, ObjectRegistry registry) throws Exception {
		switch (property) {
			case "fuelSource":
			case "setFuelSource":
				Biokerosene fuel = (Biokerosene) registry.getObject(args[0]);
				setFuelSource(fuel);
				break;
			default:
				throw new Exception("Unknown link property '" + property + "' for DragonPlow");
		}
	}

	/**
	 * Az objektum adatainak kiírása.
	 *
	 * @param id Az objektum azonosítója.
	 * @param registry Az objektumtár.
	 */
	@Override
	public void printData(String id, ObjectRegistry registry) {
		super.printData(id, registry);
		String fuelId = (fuelSource != null) ? registry.findId(fuelSource) : "null";
		System.out.println("fuelSource," + fuelId);
	}
}
