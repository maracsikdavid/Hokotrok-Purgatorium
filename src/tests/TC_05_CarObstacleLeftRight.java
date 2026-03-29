package tests;

import entities.Car;
import topology.Intersection;
import topology.Lane;
import topology.SimpleRoad;
import core.*;

/**
 * TC_05: Jármű akadályellenes manőver (bal és jobb oldal egyszerre).
 * 
 * Use-case neve: TC_05_CAR_OBSTACLE_LEFT_RIGHT
 * 
 * Rövid leírás:
 * Ugyanaz a szituáció, mint a TC_03 vagy a TC_04, de a jármű mindkét oldala blokkolt. Emiatt nem tud sávot váltani.
 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A Skeleton meghívja a Jármű tick(), majd move() metódusát.
 * 4. A Skeleton megkérdezi a Tesztelőt: "Van akadály a sávban a jármű előtt? (1: Igen, 0: Nem)" → Válasz: 1.
 * 5. A Jármű automatikusan lekéri a bal oldali sávot (getLeftLane()).
 * 6. A Skeleton megkérdezi a Tesztelőt: "Elérhető és üres a bal oldali sáv? (1: Igen, 0: Nem)" → Válasz: 0.
 * 7. A Jármű automatikusan lekéri a jobb oldali sávot (getRightLane()).
 * 8. A Skeleton megkérdezi a Tesztelőt: "Elérhető és üres a jobb oldali sáv? (1: Igen, 0: Nem)" → Válasz: 0.
 * 9. A sávváltás meghiúsul. A Jármű a jelenlegi sávján marad, és lefut az elakadás/bénulás logikája (stuck()).

 */
public class TC_05_CarObstacleLeftRight extends TestCase {
    /**
     * A teszteset futtatása. Jármű akadály-elkerülésének szimulálása (minden irány blokkolva).
     */
    @Override
    public void run() {
        Skeleton.setActiveTestCaseId(5);
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
