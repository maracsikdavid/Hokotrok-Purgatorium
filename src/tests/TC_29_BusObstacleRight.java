package tests;

import entities.Car;
import topology.Intersection;
import topology.Lane;
import topology.SimpleRoad;
import core.*;
import entities.*;

/**
 * TC_29: Busz kitérése jobbra akadály esetén.
 * 
 * Use-case neve: TC_29_BUS_OBSTACLE_RIGHT
 * 
 * Rövid leírás:
 * Egy autonóm jármű (Autó vagy Busz) halad a sávjában, de a mozgás (tick()) során 
 * akadályt észlel. A jármű automatikusan megpróbálja kikerülni azt a jobb oldali sávba sorolással.
 * 
 * Aktorok:
 * Tesztelő
 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A Skeleton meghívja a Jármű tick(), majd move() metódusát.
 * 4. A Skeleton megkérdezi a Tesztelőt: "Van akadály a sávban a jármű előtt? (1: Igen, 0: Nem)" → Válasz: 1.
 * 5. A Jármű a belső logikája alapján automatikusan lekéri a jobb oldali sávot (getRightLane()).
 * 6. A Skeleton megkérdezi a Tesztelőt: "Elérhető és üres a jobb oldali sáv? (1: Igen, 0: Nem)" → Válasz: 1.
 * 7. A Jármű megkísérli a sávváltást (changeLane()), a jobb oldali sáv elfogadja, az aktuális eltávolítja.
 */
public class TC_29_BusObstacleRight extends TestCase {
    @Override
    public void run() {
        Skeleton.setActiveTestCaseId(29);
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
    }
}
