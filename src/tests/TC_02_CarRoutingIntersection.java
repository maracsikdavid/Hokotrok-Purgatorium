package tests;

/**
 * TC_02: Autó útválasztása kereszteződésnél.
 * 
 * Use-case neve: TC_02_CAR_ROUTING_INTERSECTION
 * 
 * Rövid leírás:
 * Egy autó a sávja végére (csomóponthoz) ér. 
 * A csomópont (MapNode.routeVehicles()) útvonaltervező logikája átirányítja egy új sávra a célja felé.
 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. Skeleton meghívja a jármű tick() metódusát, majd a move() metódust.
 * 4. Skeleton megkérdezi a Tesztelőt: "Elérte az autó a sáv végét? (1: Igen, 0: Nem)" →  Válasz: 1.
 * 5. A jármű megáll, a Skeleton meghívja a csomópont routeVehicles() metódusát.
 * 6. A csomópont lekéri a kimenő utakat (getOutgoingRoads()).
 * 7. A Skeleton megkérdezi a Tesztelőt: "Sikeres a sávváltás az új útra? (1: Igen, 0: Nem)" → Válasz: 1.
 * 8. Az autó changeLane() metódusa lefut, átkerül az új sávra, a régi sávról törlődik.


 */
public class TC_02_CarRoutingIntersection extends TestCase {
    /**
     * A teszteset futtatása. Autó kereszteződésben történő útválasztásának szimulálása.
     */
    @Override
    public void run() {
    }
}
