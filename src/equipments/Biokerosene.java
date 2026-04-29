package equipments;

import cli.Linkable;
import cli.ObjectRegistry;
import cli.Printable;

/**
 * A biokerozin nyersanyagot reprezentáló osztály, amelyet a DragonPlow használ a hóolvasztáshoz.
 */
public class Biokerosene implements Consumable, Linkable, Printable {
	private int amount;


	// --- KONSTRUKTOROK ---

	/**
	 * Alapértelmezett konstruktor.
	 */
	public Biokerosene() {
	}

	/**
	 * Konstruktor a kezdeti mennyiség megadásával.
	 *
	 * @param amount A rendelkezésre álló biokerozin mennyisége.
	 */
	public Biokerosene(int amount) {
		this.amount = amount;
	}


	// --- GETTEREK ÉS SETTEREK ---

	/**
	 * Visszaadja az aktuális biokerozin mennyiséget.
	 *
	 * @return A rendelkezésre álló mennyiség.
	 */
	@Override
	public int getAmount() {
		return amount;
	}

	/**
	 * Beállítja a biokerozin mennyiségét.
	 *
	 * @param amount Az új mennyiség.
	 */
	@Override
	public void setAmount(int amount) {
		this.amount = amount;
	}


	// --- METÓDUSOK ---

	/**
	 * Elhasznál egy egységet a biokerozin-készletből.
	 *
	 * Pszeudokód:
	 * 1. Ha amount > 0, csökkenti eggyel.
	 */
	@Override
	public void use() {

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
				throw new Exception("Unknown link property '" + property + "' for Biokerosene");
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
		System.out.println("Biokerosene," + id);
		System.out.println("amount," + amount);
	}
}
