package entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import cli.Linkable;
import cli.ObjectRegistry;
import topology.Building;
import topology.Lane;
import topology.MapNode;
import topology.Road;

/**
 * Az autó jarmű egy személykocsi. Munkahelye és otthona között mozog, akadályokat kikerülhet,
 * és jeges sávon megcsúszhat. Az autós lakik egy épületben (otthon) és másikban dolgozik (munka).
 */
public class Car extends Vehicle implements Linkable {
	private Building homeNode;
	private Building workplaceNode;
	private boolean isGoingToWork = true; 
	private List<Road> currentPath = new ArrayList<>(); 
	
	
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
	 * @return Az otthonként kijelölt épület referenciája.
	 */
	public Building getHomeNode() {
		return homeNode;
	}

	/**
	 * Beállítja az autó otthoni csomópontját.
	 *
	 * @param homeNode A beállítandó otthoni épület.
	 */
	public void setHomeNode(Building homeNode) {
		this.homeNode = homeNode;
	}

	/**
	 * Visszaadja az autó munkahelyi csomópontját.
	 *
	 * @return A munkahelyként kijelölt épület referenciája.
	 */
	public Building getWorkplaceNode() {
		return workplaceNode;
	}

	/**
	 * Beállítja az autó munkahelyi csomópontját.
	 *
	 * @param workplaceNode A beállítandó munkahelyi épület.
	 */
	public void setWorkplaceNode(Building workplaceNode) {
		this.workplaceNode = workplaceNode;
	}

	/**
	 * Visszaadja, hogy az autó éppen a munkahelye felé tart-e.
	 *
	 * @return Igaz, ha az autó munkába tart, hamis, ha hazafelé.
	 */
	public boolean isGoingToWork() { return isGoingToWork; }

	/**
	 * Beállítja a haladási irányt (munkába vagy haza).
	 *
	 * @param isGoingToWork Igaz, ha az autó munkába tart, hamis különben.
	 */
    public void setGoingToWork(boolean isGoingToWork) { this.isGoingToWork = isGoingToWork; }

	/**
	 * Visszaadja az aktuálisan kiszámított útvonalat.
	 *
	 * @return A következő útszakaszok listája.
	 */
	public List<Road> getCurrentPath() {
		return currentPath;
	}

	/**
	 * Beállítja az aktuálisan kiszámított útvonalat.
	 *
	 * @param currentPath A beállítandó útvonal.
	 */
	public void setCurrentPath(List<Road> currentPath) {
		this.currentPath = currentPath;
	}


	// --- METÓDUSOK ---

	/**
	 * Az autó mozgatása. Ha nincs bénultság, a progress érték növekszik.
	 * Ha eléri a sáv végét, a jármű megáll a sáv végén és útvonalválasztásra vár.
	 *
	 * Pszeudokód:
	 * 1. Bénultság ellenőrzése.
	 * 2. Progress növelése.
	 * 3. Sáv végén csomóponti döntés előkészítése.
	 */
	@Override
	protected void move() {

	}

	/**
	 * Ellenőrzi, hogy az autó megbénulhat-e. 
	 * Az autók érzékenyek a jeges útra és az ütközésekre.
	 *
	 * @return Mindig igaz.
	 *
	 * Pszeudokód:
	 * 1. Visszatér egy logikai konstanssal.
	 */
	@Override
	public boolean isParalizable() {
		return false;
	}

	/**
	 * Ellenőrzi, hogy az autó elakadt-e (pl. baleset vagy bénultság miatt).
	 *
	 * @return Igaz, ha a jármű jelenleg le van bénulva.
	 *
	 * Pszeudokód:
	 * 1. Mozgásképtelenségi állapot vizsgálata.
	 * 2. Logikai visszatérési érték.
	 */
	@Override
	public boolean stuck() {
		return false;
	}

	/**
     * Az objektum állapotának és speciális autó adatainak kiírása a standard kimenetre.
     *
     * @param id Az objektum azonosítója a regiszterben.
     * @param registry A központi objektumtár.
     */
	@Override
	public void printData(String id, ObjectRegistry registry) {
	    super.printData(id, registry);
	    System.out.println("homeNode," + registry.findId(homeNode));
	    System.out.println("workplaceNode," + registry.findId(workplaceNode));
	    System.out.println("isGoingToWork," + isGoingToWork);
	}

	/**
     * Kiválasztja a következő utat a cél eléréséhez. Ha az útvonalterv üres, 
     * újat számol a Szélességi Keresés (BFS) algoritmus segítségével.
     *
     * @param currentNode Az aktuális csomópont.
     * @return A következő útszakasz referenciája, vagy null, ha megérkezett.
	 *
	 * Pszeudokód:
	 * 1. Meghatározza az aktuális célt (munkahely vagy otthon).
	 * 2. Üres útvonal esetén új útvonalat számol.
	 * 3. Visszaadja a következő útszakaszt vagy nullt.
     */
    @Override
    public Road chooseNextRoad(MapNode currentNode) {
        return null;
    }

	/**
     * Szélességi keresés (BFS) algoritmus a legrövidebb útvonal kiszámításához 
     * a gráf csomópontjai és útjai mentén.
     *
     * @param start A kiindulási csomópont.
     * @param target A cél csomópont.
	 *
	 * Pszeudokód:
	 * 1. Inicializálja a BFS adatstruktúrákat.
	 * 2. Bejárja a gráfot rétegenként.
	 * 3. Találat esetén rekonstruálja az útvonalat.
     */
    private void calculatePath(MapNode start, MapNode target) {

    }

	/**
	 * Összekapcsolja az autót más objektumokkal a parancssori argumentumok alapján.
	 *
	 * @param property A beállítandó tulajdonság neve.
	 * @param args     Az összekapcsoláshoz szükséges argumentumok.
	 * @param registry A központi objektumtár.
	 * @throws Exception Ha a tulajdonság ismeretlen vagy az összekapcsolás sikertelen.
	 */
	@Override
	public void performLink(String property, String[] args, ObjectRegistry registry) throws Exception {
		switch (property) {
			case "homeNode":
			case "setHomeNode": {
				Building b = (Building) registry.getObject(args[0]);
				setHomeNode(b);
				break;
			}
			case "workplaceNode":
			case "setWorkplaceNode": {
				Building b = (Building) registry.getObject(args[0]);
				setWorkplaceNode(b);
				break;
			}
			case "currentLane":
			case "setCurrentLane": {
				Lane lane = (Lane) registry.getObject(args[0]);
				setCurrentLane(lane);
				lane.getVehicles().add(this);
				break;
			}
			case "isGoingToWork":
			case "setGoingToWork": {
				setGoingToWork(Boolean.parseBoolean(args[0]));
				break;
			}
			default:
				throw new Exception("Action failed: Unknown link property '" + property + "' for Car");
		}
	}
}