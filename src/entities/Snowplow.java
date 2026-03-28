package entities;

import actors.Cleaner;
import core.Skeleton;
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
        Skeleton.printCall(null, this, "tick");
        this.move();
        this.clearLane();
        Skeleton.printReturn(this, "tick");
    }

	/**
	 * A hókotró mozgatása.
	 */
	@Override
	protected void move() {
        Skeleton.printCall(this, this, "move");

        if (getTargetLane() != null) {
            getTargetLane().acceptVehicle(this);
        }
        
        if (getCurrentLane() != null) {
            getCurrentLane().removeVehicle(this);
        }

        Skeleton.printReturn(this, "move");
    }

	/**
	 * Ellenőrzi, hogy a hókotró bénulhat-e. Az immunis a jeges sávra.
	 *
	 * @return hamis (a hókotrók nem bénulhatnak)
	 */
	@Override
	public boolean isParalizable() {
		Skeleton.printCall(null, this, "isParalizable");
		Skeleton.printReturn(this, "isParalizable", "false");
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
		Skeleton.printCall(null, this, "changeLane");
		Skeleton.printReturn(this, "changeLane", "true");
		return true;
	}

	/**
	 * A hókotró letakarítja a jelenlegi sávját. Az eredmény az aktuális kotrófejtől függ.
	 *
	 * @return igaz, ha a takarítás sikeres
	 */
	public boolean clearLane() {
		Skeleton.printCall(this, this, "clearLane");
        boolean success = false;
        
        if (equippedPlow != null && this.getCurrentLane() != null) {
            success = equippedPlow.clear(this.getCurrentLane());
            
            if (success && owner != null) {
                owner.achieveCoin();
            }
        }
        
        Skeleton.printReturn(this, "clearLane", String.valueOf(success));
        return success;
    }

	/**
	 * A hókotróra új kotrófejet helyezünk fel.
	 *
	 * @param p az űJ kotrófej
	 */
	public void equipPlow(Plow p) {
		Skeleton.printCall(null, this, "equipPlow");
		this.equippedPlow = p;
		Skeleton.printReturn(this, "equipPlow");
	}
}
