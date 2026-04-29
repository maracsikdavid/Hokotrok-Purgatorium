package equipments;

import cli.Linkable;
import cli.ObjectRegistry;
import cli.Printable;

/**
 * A só nyersanyagot reprezentáló osztály, amelyet a SaltPlow használ a jég olvasztásához.
 */
public class Salt implements Consumable, Linkable, Printable {
	private int amount;


	// --- KONSTRUKTOROK ---

	/**
	 * Alapértelmezett konstruktor.
	 */
	public Salt() {
	}

	/**
	 * Konstruktor a kezdeti mennyiség megadásával.
	 *
	 * @param amount A rendelkezésre álló só mennyisége.
	 */
	public Salt(int amount) {
		this.amount = amount;
	}


	// --- GETTEREK ÉS SETTEREK ---

	/**
	 * Visszaadja az aktuális sómennyiséget.
	 *
	 * @return A rendelkezésre álló mennyiség.
	 */
	@Override
	public int getAmount() {
		return amount;
	}

	/**
	 * Beállítja a só mennyiségét.
	 *
	 * @param amount Az új mennyiség.
	 */
	@Override
	public void setAmount(int amount) {
		this.amount = amount;
	}


	// --- METÓDUSOK ---

	/**
	 * Elhasznál egy egységet a sókészletből.
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
				throw new Exception("Unknown link property '" + property + "' for Salt");
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
		System.out.println("Salt," + id);
		System.out.println("amount," + amount);
	}
}
