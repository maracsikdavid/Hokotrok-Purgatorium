package entities;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
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
	private List<Road> currentPath = new ArrayList<>(); // Az előre kiszámított útvonal tárolása
	
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

	/**
	 * Visszaadja, hogy az autó éppen a munkahelye felé tart-e.
	 *
	 * @return igaz, ha az autó munkába tart, hamis, ha hazafelé
	 */
	public boolean isGoingToWork() { return isGoingToWork; }

	/**
	 * Beállítja, hogy az autó éppen a munkahelye felé tart-e.
	 *
	 * @param isGoingToWork igaz, ha munkába tart, hamis különben
	 */
    public void setGoingToWork(boolean isGoingToWork) { this.isGoingToWork = isGoingToWork; }


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
     * Lekéri a következő utat. Ha az útvonalterv üres, újat számol.
     */
    @Override
    public Road chooseNextRoad(MapNode currentNode) {
        MapNode target = isGoingToWork ? workplaceNode : homeNode;

        // 1. Megérkezés ellenőrzése
        if (currentNode == target) {
            isGoingToWork = !isGoingToWork; // Célt váltunk a következő induláshoz
            currentPath.clear(); // Töröljük a régi útvonalat
            return null; // Megállunk az épületnél
        }

        // 2. Útvonaltervezés, ha szükséges (BFS futtatása)
        if (currentPath.isEmpty()) {
            calculatePath(currentNode, target);
        }

        // 3. Következő lépés visszaadása a listából
        if (!currentPath.isEmpty()) {
            return currentPath.remove(0); // Kivesszük az első elemet és visszaadjuk
        }

        return null;
    }

	/**
     * Itt valósul meg a BFS (Szélességi keresés) algoritmus, 
     * amely feltölti a currentPath listát a legrövidebb útvonallal.
     */
    private void calculatePath(MapNode start, MapNode target) {
        Queue<MapNode> queue = new LinkedList<>();
        Set<MapNode> visited = new HashSet<>();
        
        // Két szótár (Map) a visszafejtéshez:
        // Az egyik tárolja, hogy melyik csomópontból érkeztünk...
        Map<MapNode, MapNode> cameFromNode = new HashMap<>();
        // ...a másik pedig, hogy melyik úton (Road) keresztül.
        Map<MapNode, Road> cameFromRoad = new HashMap<>();

        queue.add(start);
        visited.add(start);

        // 1. Gráf bejárása
        while (!queue.isEmpty()) {
            MapNode current = queue.poll();

            // Ha megtaláltuk a célt, befejezzük a keresést
            if (current == target) {
                break;
            }

            // Szomszédok vizsgálata
            for (Road r : current.getOutgoingRoads()) {
                MapNode neighbor = r.getTargetNode(); // A Road osztály targetNode-ja
                
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    cameFromNode.put(neighbor, current);
                    cameFromRoad.put(neighbor, r);
                    queue.add(neighbor);
                }
            }
        }

        // 2. Útvonal visszafejtése a szótárakból
        MapNode current = target;
        while (current != start) {
            Road r = cameFromRoad.get(current);
            if (r != null) {
                // A lista elejére szúrjuk be, így a végén helyes sorrendben lesznek az utak
                this.currentPath.add(0, r); 
            }
            current = cameFromNode.get(current);
            
            // Biztonsági ellenőrzés (ha elvileg nincs zsákutca, ide sosem jutunk be)
            if (current == null) {
                break; 
            }
        }
    }
}