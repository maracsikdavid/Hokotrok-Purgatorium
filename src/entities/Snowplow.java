package entities;

import actors.Cleaner;
import equipments.Plow;
import topology.Lane;

/**
 * A hókotró speciális jármű, amely a város uthálózatának takarítására szolgál. A Cleaner által
 * irányítva, különböző kotrófejekkel lehet felszerelnini.
 */
public class Snowplow extends Vehicle {
	private Cleaner owner;
	private Plow equippedPlow;

    // --- GETTEREK ÉS SETTEREK ---
    /**
     * Visszaadja a hókotró tulajdonosát.
     *
     * @return a tulajdonos takarító
     */
    public Cleaner getOwner() {
		return owner;
	}

    /**
     * Beállítja a hókotró tulajdonosát.
     *
     * @param owner a beállítandó tulajdonos
     */
    public void setOwner(Cleaner owner) {
		this.owner = owner;
	}

	/**
	 * Visszaadja a felszerelt kotrófejet.
	 *
	 * @return a felszerelt kotrófej
	 */
	public Plow getEquippedPlow() {
		return equippedPlow;
	}

	/**
	 * Beállítja a felszerelt kotrófejet.
	 *
	 * @param equippedPlow a beállítandó kotrófej
	 */
	public void setEquippedPlow(Plow equippedPlow) {
		this.equippedPlow = equippedPlow;
	}

	// --- KONSTRUKTOROK ---
	/**
	 * Alapértelmezett konstruktor.
	 */
	public Snowplow() {
		super();
	}

	/**
	 * Paraméteres konstruktor a hókotró attribútumaihoz.
	 *
	 * @param owner a hókotró tulajdonosa
	 * @param equippedPlow a felszerelt kotrófej
	 */
	public Snowplow(Cleaner owner, Plow equippedPlow) {
		super();
		this.owner = owner;
		this.equippedPlow = equippedPlow;
	}

	// --- METÓDUSOK ---
	/**
	 * A hókotró időzítés lépése.
	 */
	@Override
	public void tick() {
    }

	/**
	 * A hókotró mozgatása.
	 */
	@Override
	protected void move() {

    }

	/**
	 * Ellenőrzi, hogy a hókotró bénulhat-e. Az immunis a jeges sávra.
	 *
	 * @return hamis (a hókotrók nem bénulhatnak)
	 */
	@Override
	public boolean isParalizable() {
		return false;
	}

	/**
	 * A hókotró sávváltása.
	 *
	 * @param target a cél sáv
	 * @return igaz, ha sikeres
	 */
	@Override
	public boolean changeLane(Lane target) {
		return true;
	}

	/**
	 * A hókotró letakarítja a jelenlegi sávját. Az eredmény az aktuális kotrófejtől függ.
	 *
	 * @return igaz, ha a takarítás sikeres
	 */
	public boolean clearLane() {
		return false;
    }

	/**
	 * A hókotróra új kotrófejet helyezünk fel.
	 *
	 * @param p az űJ kotrófej
	 */
	public void equipPlow(Plow p) {
	}
}
