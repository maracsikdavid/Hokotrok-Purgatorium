package equipments;

import cli.ObjectRegistry;

import topology.Lane;

import actors.Cleaner;

/**
 * Absztrakt alaposztály a különböző hókotró fejek (ekék) számára.
 * Minden kotrófej felelős egy adott sáv megtisztításáért a saját mechanizmusa szerint.
 */
public abstract class Plow {
	protected Cleaner owner;
	protected boolean isEquipped = false;



	/**
	 * Visszaadja a kotrófej tulajdonosát.
	 *
	 * @return a tulajdonos takarító
	 */
	public Cleaner getOwner() {
		return owner;
	}

	/**
	 * Beállítja a kotrófej tulajdonosát.
	 *
	 * @param owner a beállítandó tulajdonos
	 */
	public void setOwner(Cleaner owner) {
		this.owner = owner;
	}

	/**
	 * Visszaadja, hogy a kotrófej fel van-e szerelve egy járműre.
	 *
	 * @return igaz, ha fel van szerelve
	 */
	public boolean isEquipped() {
		return isEquipped;
	}

	/**
	 * Beállítja a felszereltségi állapotot.
	 *
	 * @param equipped a beállítandó állapot
	 */
	public void setEquipped(boolean equipped) {
		isEquipped = equipped;
	}



	/**
	 * Megkísérli letakarítani a megadott sávot a kotrófej speciális működése szerint.
	 *
	 * @param lane A letakarítandó sáv.
	 * @return Igaz, ha a takarítás sikeres volt, egyébként hamis.
	 * @throws Exception Ha a takarítás műveleti hiba miatt meghiúsul.
	 */
	public abstract boolean clear(Lane lane) throws Exception;

	/**
	 * Visszaadja a kotróféj által használt fogyóanyag típusát.
	 * @return A fogyóanyag neve, vagy null ha nem igényel utántöltést.
	 */
	public String getConsumableType() { return null; }

	/**
	 * Feltölti a kotróféj tartályát a megadott nyersanyaggal.
	 * @param resource A feltöltéshez használt fogyóanyag.
	 * @throws Exception Ha a kotrófej nem támogatja a feltöltést.
	 */
	public void refill(Consumable resource) throws Exception {
		throw new Exception("Action failed: Incompatible resource for the equipped plow.");
	}

	/**
	 * Az objektum állapotának és adatainak kiírása.
	 *
	 * @param id Az objektum azonosítója a regiszterben.
	 * @param registry Az objektumtár.
	 */
	public void printData(String id, ObjectRegistry registry) {
		System.out.println(this.getClass().getSimpleName() + "," + id);
	}
}
