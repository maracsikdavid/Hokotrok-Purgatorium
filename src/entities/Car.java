package entities;

import core.Skeleton;
import topology.Building;
import topology.Lane;

/**
 * Az autó {jarmű egy személykocsi. Munkahelye és otthone között mozog, akadályokat kikerülhet,
 * és jeges sávon megcsúszhat. Az autós lakik egy épületben (otthon) és másikéban dolgozik (munka).
 */
public class Car extends Vehicle {
	private Building homeNode;
	private Building workplaceNode;


	// --- GETTEREK ÉS SETTEREK ---
	public Building getHomeNode() {
		return homeNode;
	}
	public void setHomeNode(Building homeNode) {
		this.homeNode = homeNode;
	}

	public Building getWorkplaceNode() {
		return workplaceNode;
	}
	public void setWorkplaceNode(Building workplaceNode) {
		this.workplaceNode = workplaceNode;
	}


	// --- METÓDUSOK ---
	/**
	 * Az autó időzítés lépése, idő függvényében történő változást valósítja meg
	 */
	@Override
	public void tick() {
		Skeleton.printCall(null, this, "tick");
		Skeleton.printReturn(this, "tick");
	}

	/**
	 * Az autó mozgatása. Ha nincs bénultság, a progress nö.
	 * Ha eléri a sáv végét, majd elindul a következőn
	 */
	@Override
	protected void move() {
		Skeleton.printCall(null, this, "move");
		Skeleton.printReturn(this, "move");
	}

	/**
	 * Ellenőrzi, hogy az autó megbénulhat-e. Az autós bénulhatnak jeges sávon az ütközések miatt.
	 *
	 * @return igaz (az autós megbánult)
	 */
	@Override
	public boolean isParalizable() {
		Skeleton.printCall(null, this, "isParalizable");
		Skeleton.printReturn(this, "isParalizable", "true");
		return true;
	}

	/**
	 * Az autót bénulásából eltelt idő. Ez idő alatt nem mozoghat
	 * @param time az időtartam, amig az autó mozgasképtelen
	 */
	public void paralyze(int time) {
		Skeleton.printCall(null, this, "paralyze");
		Skeleton.printReturn(this, "paralyze");
	}

	/**
	 * Ellenőrzi, hogy az autó elakadt-e vagy sem. 
	 * Ha egyéb jarűvel ütközık, akkor igaz.
	 *
	 * @return igaz, ha az autó elakadt
	 */
	public boolean stuck() {
		Skeleton.printCall(null, this, "stuck");
		Skeleton.printReturn(this, "stuck", "false");
		return false;
	}

	/**
	 * Az autó sávváltást kísérel meg.
	 *
	 * @param target a cél sáv
	 * @return igaz, ha a váltás sikeres
	 */
	@Override
	public boolean changeLane(Lane target) {
		Skeleton.printCall(null, this, "changeLane");
		Skeleton.printReturn(this, "changeLane", "true");
		return true;
	}
}
