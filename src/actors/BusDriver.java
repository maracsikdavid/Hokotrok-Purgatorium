package actors;

import core.Skeleton;
import entities.Bus;
import topology.Lane;
import topology.Road;

/**
 * A busz sofőrjét reprezentáló osztály. Feladata a busz irányítása, pontok gyűjtése,
 * valamint a célelérésekor az elért sikerek regisztrálása a szimulációban pontok formájában.
 */
public class BusDriver extends Player {
	public int score;
	public Bus managedBus;

	/**
	 * A buszt irányítja a megadott útra és sávra. Az utasítás a tesztkörnyezetben
	 * a Tesztelő döntésétől függően aktiválódik.
	 *
	 * @param b az irányítandó busz
	 * @param toRoad a cél úthálózat
	 * @param toLane a cél sáv az útban
	 */
	public void commandBus(Bus b, Road toRoad, Lane toLane) {
		Skeleton.printCall(null, this, "commandBus");
		Skeleton.printReturn(this, "commandBus");
	}

	/**
	 * A buszsofőr pontokat szerez, amikor a buszt sikeresen elérkeztet a célállomásra.
	 * Ez a metódus a Bus osztály által hívódik meg a sikeres cél-megérkezésekor.
	 */
	public void achievePoints() {
		Skeleton.printCall(null, this, "achievePoints");
		Skeleton.printReturn(this, "achievePoints");
	}
}
