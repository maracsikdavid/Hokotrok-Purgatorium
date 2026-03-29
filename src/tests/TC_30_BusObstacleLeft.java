package tests;

import entities.Car;
import topology.Intersection;
import topology.Lane;
import topology.SimpleRoad;
import entities.*;
import core.*;

/**
 * TC_30: Busz kitérése balra akadály esetén.
 * 
 * Use-case neve: TC_30_BUS_OBSTACLE_LEFT
 * 
 * Rövid leírás:
 * Ugyanaz a szituáció, mint a TC_29, de a jármű a bal oldali sávba sorol át az akadály miatt.
 * 
 * Aktorok:
 * Tesztelő
 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A Skeleton meghívja a Jármű tick(), majd move() metódusát.
 * 4. A Skeleton megkérdezi a Tesztelőt: "Van akadály a sávban a jármű előtt? (1: Igen, 0: Nem)" → Válasz: 1.
 * 5. A Jármű automatikusan lekéri a bal oldali sávot (getLeftLane()).
 * 6. A Skeleton megkérdezi a Tesztelőt: "Elérhető és üres a bal oldali sáv? (1: Igen, 0: Nem)" → Válasz: 1.
 * 7. A Jármű megkísérli a sávváltást (changeLane()), sikeresen átkerül a bal sávba.
 */
public class TC_30_BusObstacleLeft extends TestCase {
    @Override
    public void run() {
        Skeleton.setActiveTestCaseId(30);
        Skeleton.disableLogging();
        Intersection i1 = new Intersection();
        Skeleton.registerObject(i1, "i1");
        Intersection i2 = new Intersection();
        Skeleton.registerObject(i2, "i2");
        SimpleRoad r = new SimpleRoad();
        Skeleton.registerObject(r, "r");
        Lane l1 = new Lane();
        Skeleton.registerObject(l1,"l1");
        Lane l2 = new Lane();
        Skeleton.registerObject(l2, "l2");
        Lane l3 = new Lane();
        Skeleton.registerObject(l3, "l3");
        Bus b = new Bus();
        Skeleton.registerObject(b, "b");

        i1.addOutgoingRoad(r);
        r.setTargetNode(i2);
        r.addLane(l1);
        r.addLane(l2);
        r.addLane(l3);
        l2.acceptVehicle(b);
        b.setCurrentLane(l2);

        Skeleton.enableLogging();

        b.tick();

        Skeleton.setActiveTestCaseId(-1);
    }
}
