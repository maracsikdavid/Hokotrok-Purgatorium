package tests;

import core.Skeleton;
import topology.*;
import entities.*;

/**
 * TC_03: Jármű akadályellenes manőver (jobb oldal).
 * 
 * Use-case neve: TC_03_VEHICLE_OBSTACLE_RIGHT
 * 
 * Rövid leírás:
 * Egy autonóm jármű (Autó vagy Busz) halad a sávjában, de a mozgás (tick()) során akadályt észlel.
 *  A jármű automatikusan megpróbálja kikerülni azt a jobb oldali sávba sorolással.
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
public class TC_03_CarObstacleRight extends TestCase {
    /**
     * A teszteset futtatása. Jármű akadály-elkerülésének szimulálása (jobb oldal).
     */
    @Override
    public void run() {
        Skeleton.setActiveTestCaseId(3);
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
        Car c = new Car();
        Skeleton.registerObject(c, "c");

        i1.addOutgoingRoad(r);
        r.setTargetNode(i2);
        r.addLane(l1);
        r.addLane(l2);
        r.addLane(l3);
        l2.acceptVehicle(c);
        c.setCurrentLane(l2);
        Skeleton.enableLogging();
        
        c.tick();
       

        Skeleton.setActiveTestCaseId(-1);
    }
}
