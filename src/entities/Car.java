package entities;
import topology.Building;
import topology.Lane;
import topology.MapNode;
import topology.Road;

/**
 * Az autó {jarmű egy személykocsi. Munkahelye és otthone között mozog, akadályokat kikerülhet,
 * és jeges sávon megcsúszhat. Az autós lakik egy épületben (otthon) és másikéban dolgozik (munka).
 */
public class Car extends Vehicle {
	private Building homeNode;
	private Building workplaceNode;
	private boolean isGoingToWork = true; // Állapotjelző: munkába vagy haza tart
	
	// --- KONSTRUKTOROK ---

	/**
	 * Alapértelmezett konstruktor.
	 */
	public Car() {
		super();
	}

	/**
	 * Paraméteres konstruktor az autó csomópontjaihoz.
	 *
	 * @param homeNode az otthon csomópont
	 * @param workplaceNode a munkahely csomópont
	 */
	public Car(Building homeNode, Building workplaceNode) {
		super();
		this.homeNode = homeNode;
		this.workplaceNode = workplaceNode;
	}


	// --- GETTEREK ÉS SETTEREK ---

	/**
	 * Visszaadja az autó otthoni csomópontját.
	 *
	 * @return az otthon csomópont
	 */
	public Building getHomeNode() {
		return homeNode;
	}

	/**
	 * Beállítja az autó otthoni csomópontját.
	 *
	 * @param homeNode a beállítandó otthon csomópont
	 */
	public void setHomeNode(Building homeNode) {
		this.homeNode = homeNode;
	}

	/**
	 * Visszaadja az autó munkahelyi csomópontját.
	 *
	 * @return a munkahely csomópont
	 */
	public Building getWorkplaceNode() {
		return workplaceNode;
	}

	/**
	 * Beállítja az autó munkahelyi csomópontját.
	 *
	 * @param workplaceNode a beállítandó munkahely csomópont
	 */
	public void setWorkplaceNode(Building workplaceNode) {
		this.workplaceNode = workplaceNode;
	}


	// --- METÓDUSOK ---

	/**
	 * Az autó időzítés lépése, idő függvényében történő változást valósítja meg
	 */
	@Override
	public void tick() {
	}

	/**
	 * Az autó mozgatása. Ha nincs bénultság, a progress nö.
	 * Ha eléri a sáv végét, majd elindul a következőn
	 */
	@Override
	protected void move() {
		
	}

	/**
	 * Ellenőrzi, hogy az autó megbénulhat-e. Az autós bénulhatnak jeges sávon az ütközések miatt.
	 *
	 * @return igaz (az autós megbánult)
	 */
	@Override
	public boolean isParalizable() {
		return true;
	}

	/**
	 * Az autót bénulásából eltelt idő. Ez idő alatt nem mozoghat
	 * @param time az időtartam, amig az autó mozgasképtelen
	 */
	public void paralyze(int time) {
	}

	/**
	 * Ellenőrzi, hogy az autó elakadt-e vagy sem. 
	 * Ha egyéb jarűvel ütközık, akkor igaz.
	 *
	 * @return igaz, ha az autó elakadt
	 */
	public boolean stuck() {
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
		return false;
	}

	/**
     * Az objektum aktuális állapotának és attribútumainak kiírása a standard kimenetre.
     * * @param id Az objektum egyedi azonosítója, amellyel a Registry-ben szerepel.
     */
    public void printData(String id) {
    }

	/**
	 * Az NPC automatikus útvonalkeresése (BFS algoritmus alapján).
	 */
	@Override
	public Road chooseNextRoad(MapNode currentNode) {
		// 1. Célállomás meghatározása
		MapNode target = isGoingToWork ? workplaceNode : homeNode;

		// 2. Megérkeztünk-e?
		if (currentNode == target) {
			isGoingToWork = !isGoingToWork; // Cél megfordul (ha bent van, majd haza/munkába indul)
			return null; // Megáll az épületben
		}

		// 3. TODO: BFS algoritmus implementálása a gráfon a currentNode és a target között.
		// Itt fogod megkeresni a legrövidebb utat, és visszaadni az ahhoz tartozó legelső Road-ot.
		
		return null; // Ideiglenes visszatérési érték
	}
}