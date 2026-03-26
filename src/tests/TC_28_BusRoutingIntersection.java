package tests;

/**
 * TC_28: Busz sávváltása egy csomópontnál.
 * 
 * Use-case neve: TC_28_BUS_ROUTING_INTERSECTION
 * 
 * Rövid leírás:
 * Egy busz a sávja végére (csomóponthoz) ér. A csomópont (MapNode.routeVehicles()) 
 * útvonaltervező logikája átirányítja egy új sávra a célja felé.
 * 
 * Aktorok:
 * Tesztelő
 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A Skeleton meghívja a jármű tick() metódusát, majd a move() metódust.
 * 4. A Skeleton megkérdezi a Tesztelőt: "Elérte a busz a sáv végét? (1: Igen, 0: Nem)" → Válasz: 1.
 * 5. A jármű megáll, a Skeleton meghívja a csomópont routeVehicles() metódusát.
 * 6. A csomópont lekéri a kimenő utakat (getOutgoingRoads()).
 * 7. A Skeleton megkérdezi a Tesztelőt: "Sikeres a sávváltás az új útra? (1: Igen, 0: Nem)" → Válasz: 1.
 * 8. A busz changeLane() metódusa lefut, átkerül az új sávra, a régi sávról törlődik.
 */
public class TC_28_BusRoutingIntersection extends TestCase {
    @Override
    public void run() {
    }
}
