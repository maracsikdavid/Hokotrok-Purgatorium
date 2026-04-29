package equipments;

import cli.Linkable;
import cli.ObjectRegistry;
import cli.Printable;
import statemachine.CleanCondition;
import statemachine.ThickSnowCondition;
import statemachine.ThinSnowCondition;
import topology.Lane;

/**
 * A sószóró kotrófej, amely sót szór a jeges útfelületre a jég megolvasztása érdekében.
 */
public class SaltPlow extends Plow implements Linkable, Printable {
	private Salt saltSource;


	// --- KONSTRUKTOROK ---

	/**
	 * Alapértelmezett konstruktor.
	 */
	public SaltPlow() {
		super();
	}

	/**
	 * Konstruktor a sóforrás megadásával.
	 *
	 * @param saltSource A sót biztosító forrás.
	 */
	public SaltPlow(Salt saltSource) {
		super();
		this.saltSource = saltSource;
	}


	// --- GETTEREK ÉS SETTEREK ---

	/**
	 * Visszaadja a jelenlegi sóforrást.
	 *
	 * @return A sóforrás referenciája.
	 */
	public Salt getSaltSource() {
		return saltSource;
	}

	/**
	 * Beállítja a sóforrást.
	 *
	 * @param saltSource Az új sóforrás.
	 */
	public void setSaltSource(Salt saltSource) {
		this.saltSource = saltSource;
	}


	// --- METÓDUSOK ---

	/**
	 * Sót szór a megadott sávra, ha a forrás nem üres.
	 *
	 * @param lane A kezelendő sáv.
	 * @return Igaz, ha a művelet sikeres volt, egyébként hamis.
	 *
	 * Pszeudokód:
	 * 1. Ellenőrzi az isEmpty() feltételt.
	 * 2. Meghívja a lane.getState().applySalt(lane) metódust.
	 * 3. Csökkenti a forrás mennyiségét.
	 */
	@Override
	public boolean clear(Lane lane) {
		if (lane == null || isEmpty()) {
			return false;
		}

		if (lane.getState() != null) {
			lane.getState().applySalt(lane);
		}

		if (lane.getState() instanceof ThinSnowCondition || lane.getState() instanceof ThickSnowCondition) {
			lane.setState(new CleanCondition());
		}

		saltSource.use();
		return true;
	}

	/**
	 * Feltölti a kotrófejet új sóval.
	 *
	 * @param salt A betöltendő só példánya.
	 *
	 * Pszeudokód:
	 * 1. A saltSource mezőt a kapott példányra állítja.
	 */
	public void refill(Salt salt) {
		setSaltSource(salt);
	}

	/**
	 * Ellenőrzi, hogy a kotrófej kifogyott-e a sóból.
	 *
	 * @return Igaz, ha nincs több só, egyébként hamis.
	 *
	 * Pszeudokód:
	 * 1. Ha saltSource null vagy amount == 0, true.
	 * 2. Egyébként false.
	 */
	public boolean isEmpty() {
		return saltSource == null || saltSource.getAmount() <= 0;
	}

	/**
	 * Összekapcsolja a kotrófejet egy sóforrással.
	 *
	 * @param property A tulajdonság neve.
	 * @param args Az azonosítót tartalmazó tömb.
	 * @param registry Az objektumtár.
	 * @throws Exception Ha az összekapcsolás sikertelen.
	 */
	@Override
	public void performLink(String property, String[] args, ObjectRegistry registry) throws Exception {
		switch (property) {
			case "saltSource":
			case "setSaltSource":
				Salt salt = (Salt) registry.getObject(args[0]);
				setSaltSource(salt);
				break;
			default:
				throw new Exception("Unknown link property '" + property + "' for SaltPlow");
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
		String saltId = (saltSource != null) ? registry.findId(saltSource) : "null";
		System.out.println("saltSource," + saltId);
	}
}
