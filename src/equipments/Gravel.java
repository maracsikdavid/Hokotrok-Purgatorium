package equipments;

import cli.Linkable;
import cli.ObjectRegistry;
import cli.Printable;

/**
 * A zúzalék nyersanyagot reprezentáló osztály, amelyet a GravelPlow használ a tapadás növeléséhez.
 */
public class Gravel implements Consumable, Linkable, Printable {
	private int amount;


	// --- KONSTRUKTOROK ---

	/**
	 * Alapértelmezett konstruktor.
	 */
	public Gravel() {
	}

	/**
	 * Konstruktor a kezdeti mennyiség megadásával.
	 *
	 * @param amount A rendelkezésre álló zúzalék mennyisége.
	 */
	public Gravel(int amount) {
		this.amount = amount;
	}


	// --- GETTEREK ÉS SETTEREK ---

	/**
	 * Visszaadja az aktuális zúzalékmennyiséget.
	 *
	 * @return A rendelkezésre álló mennyiség.
	 */
	@Override
	public int getAmount() {
		return amount;
	}

	/**
	 * Beállítja a zúzalék mennyiségét.
	 *
	 * @param amount Az új mennyiség.
	 */
	@Override
	public void setAmount(int amount) {
		this.amount = amount;
	}


	// --- METÓDUSOK ---

	/**
	 * Elhasznál egy egységet a zúzalékkészletből.
	 *
	 * Pszeudokód:
	 * 1. Ha amount > 0, csökkenti eggyel.
	 */
	@Override
	public void use() {
		if (amount > 0) {
			amount--;
		}
	}

	/** @return A fogyóanyag azonosítója: "Gravel". */
	@Override
	public String getConsumableType() { return "Gravel"; }

	/**
	 * Összekapcsolja az objektumot a parancssori argumentumok alapján.
	 *
	 * @param property A beállítandó tulajdonság neve.
	 * @param args Az argumentumok tömbje.
	 * @param registry Az objektumtár.
	 * @throws Exception Ha az összekapcsolás sikertelen.
	 */
	@Override
	public void performLink(String property, String[] args, ObjectRegistry registry) throws Exception {
		switch (property) {
			case "amount":
			case "setAmount":
				setAmount(Integer.parseInt(args[0]));
				break;
			default:
				throw new Exception("Unknown link property '" + property + "' for Gravel");
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
		System.out.println("Gravel," + id);
		System.out.println("amount," + amount);
	}
}
