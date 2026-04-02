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
    public Cleaner getOwner() {
		return owner;
	}
    public void setOwner(Cleaner owner) {
		this.owner = owner;
	}

	// --- METÓDUSOK ---
	/**
	 * A hókotró időzítés lépése.
	 */
	@Override
	public void tick() {
        this.move();
        this.clearLane();
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
		this.equippedPlow = p;
	}
}
