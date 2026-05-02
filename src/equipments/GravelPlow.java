package equipments;

import cli.Linkable;
import cli.ObjectRegistry;
import cli.Printable;
import statemachine.GraveledIceCondition;
import statemachine.IceCondition;
import topology.Lane;

/**
 * A zúzalékszóró kotrófej, amely zúzalékot szór a jeges útfelületre a tapadás javítása érdekében.
 */
public class GravelPlow extends Plow implements Linkable, Printable {
	private Gravel gravelSource;



	/**
	 * Alapértelmezett konstruktor.
	 */
	public GravelPlow() {
		super();
	}

	/**
	 * Konstruktor a zúzalékforrás megadásával.
	 *
	 * @param gravelSource A zúzalékot biztosító forrás.
	 */
	public GravelPlow(Gravel gravelSource) {
		super();
		this.gravelSource = gravelSource;
	}



	/**
	 * Visszaadja a jelenlegi zúzalékforrást.
	 *
	 * @return A zúzalékforrás referenciája.
	 */
	public Gravel getGravelSource() {
		return gravelSource;
	}

	/**
	 * Beállítja a zúzalékforrást.
	 *
	 * @param gravelSource Az új zúzalékforrás.
	 */
	public void setGravelSource(Gravel gravelSource) {
		this.gravelSource = gravelSource;
	}

	/** @return A kotrófej által igényelt fogyóanyag típusa: "Gravel". */
	@Override
	public String getConsumableType() { return "Gravel"; }

	/**
	 * Feltölti a zuzáléktartsonyt a megadott zuzálékforrással.
	 * @param resource A feltöltéshez használt zuzálék objektum.
	 * @throws Exception Ha a típus nem megfelelő.
	 */
	@Override
	public void refill(Consumable resource) throws Exception {
		setGravelSource((Gravel) resource);
	}



	/**
	 * Zúzalékot szór a megadott sávra, ha a forrás nem üres.
	 *
	 * @param lane A kezelendő sáv.
	 * @return Igaz, ha a művelet sikeres volt, egyébként hamis.
	 * @throws Exception Ha az üres tank miatt a művelet meghiúsul.
	 *
	 * Pszeudokód:
	 * 1. Ellenőrzi az isEmpty() feltételt.
	 * 2. Meghívja a lane.getState().applyGravel(lane) metódust.
	 * 3. Csökkenti a forrás mennyiségét.
	 */
	@Override
	public boolean clear(Lane lane) throws Exception {
		if (lane == null) {
			return false;
		}
		
		if (isEmpty()) {
			throw new Exception("Action failed: Empty tank.");
		}

		if (lane.getState() != null) {
			lane.getState().applyGravel(lane);
		}

		if (lane.getState().isIce()) {
			lane.setState(new GraveledIceCondition());
		}

		gravelSource.use();
		return true;
	}

	/**
	 * Feltölti a kotrófejet új zúzalékkal.
	 *
	 * @param gravel A betöltendő zúzalék példánya.
	 *
	 * Pszeudokód:
	 * 1. A gravelSource mezőt a kapott példányra állítja.
	 */
	public void refill(Gravel gravel) {
		setGravelSource(gravel);
	}

	/**
	 * Ellenőrzi, hogy a kotrófej kifogyott-e a zúzalékból.
	 *
	 * @return Igaz, ha nincs több zúzalék, egyébként hamis.
	 *
	 * Pszeudokód:
	 * 1. Ha gravelSource null vagy amount == 0, true.
	 * 2. Egyébként false.
	 */
	public boolean isEmpty() {
		return gravelSource == null || gravelSource.getAmount() <= 0;
	}

	/**
	 * Összekapcsolja a kotrófejet egy zúzalékforrással.
	 *
	 * @param property A tulajdonság neve.
	 * @param args Az azonosítót tartalmazó tömb.
	 * @param registry Az objektumtár.
	 * @throws Exception Ha az összekapcsolás sikertelen.
	 */
	@Override
	public void performLink(String property, String[] args, ObjectRegistry registry) throws Exception {
		switch (property) {
			case "gravelSource":
			case "setGravelSource":
				Gravel gravel = (Gravel) registry.getObject(args[0]);
				setGravelSource(gravel);
				break;
			default:
				throw new Exception("Unknown link property '" + property + "' for GravelPlow");
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
		String gravelId = (gravelSource != null) ? registry.findId(gravelSource) : "null";
		System.out.println("gravelSource," + gravelId);
	}
}
