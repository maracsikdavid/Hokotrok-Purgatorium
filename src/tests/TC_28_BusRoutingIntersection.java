package tests;

import entities.Car;
import topology.Intersection;
import topology.Lane;
import topology.SimpleRoad;
import core.*;
import entities.*;

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
        Skeleton.setActiveTestCaseId(28);
        Skeleton.disableLogging();    
        Intersection i1 = new Intersection();
        Skeleton.registerObject(i1, "i1");
        Intersection i2 = new Intersection();
        Skeleton.registerObject(i2, "i2");
        Intersection i3 = new Intersection();
        Skeleton.registerObject(i3, "i3");
        SimpleRoad r1 = new SimpleRoad();
        Skeleton.registerObject(r1, "r1");
        SimpleRoad r2 = new SimpleRoad();
        Skeleton.registerObject(r2, "r2");
        Lane l1 = new Lane();
        Skeleton.registerObject(l1, "l1");
        Lane l2 = new Lane();
        Skeleton.registerObject(l2, "l2");
        Bus b = new Bus();
        Skeleton.registerObject(b, "b");

        i1.addOutgoingRoad(r1);
        i2.addOutgoingRoad(r2);
        r1.setTargetNode(i2);
        r2.setTargetNode(i3);
        r1.addLane(l1);
        r2.addLane(l2);
        l1.acceptVehicle(b);
        b.setCurrentLane(l1);
        Skeleton.enableLogging();


        b.tick();
        

        Skeleton.setActiveTestCaseId(-1);
    }
}
