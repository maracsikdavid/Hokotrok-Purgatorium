package tests;

import core.Skeleton;
import entities.Bus;
import entities.Car;
import statemachine.IceCondition;
import topology.Intersection;
import topology.Lane;
import topology.SimpleRoad;

/**
 * TC_10: Autó ütközik busszal jégen.
 * 
 * Use-case neve: TC_10_CAR_COLLIDES_WITH_BUS_ON_ICE
 * 
 * Rövid leírás:
 * Egy Autó rálép egy jeges sávra, 20% esélyjel megcsúszik, és (2 tick időre) megbénul a vele azonos sávon levő busszal.
 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A Skeleton meghívja a jármű tick() metódusát.
 * 4. Az IceCondition érzékeli a járművet (acceptVehicle).
 * 5. A Skeleton megkérdezi a Tesztelőt: "Megcsúszik és lebénul a jármű a jégen? (1: Igen, 0: Nem)" → Válasz: 1.
 * 6. A járművön meghívódik a paralyze(time) metódus. A jármű 2 tick ideig mozgásképtelen.
 */
public class TC_10_CarCollidesWithBusOnIce extends TestCase {
    /**
     * A teszteset futtatása. Autó és busz ütközésének szimulálása jégen.
     */
    @Override
    public void run() {
        // === 1. OBJEKTUMOK LÉTREHOZÁSA ÉS REGISZTRÁCIÓJA ===
        Intersection i1 = new Intersection();
        Skeleton.registerObject(i1, "i1");

        Intersection i2 = new Intersection();
        Skeleton.registerObject(i2, "i2");

        SimpleRoad r = new SimpleRoad();
        Skeleton.registerObject(r, "r");

        Lane l1 = new Lane();
        Skeleton.registerObject(l1, "l1");

        Lane l2 = new Lane();
        Skeleton.registerObject(l2, "l2");

        IceCondition cond = new IceCondition();
        Skeleton.registerObject(cond, "cond");

        Car c = new Car();
        Skeleton.registerObject(c, "c");

        Bus b = new Bus();
        Skeleton.registerObject(b, "b");

        // === 2. KAPCSOLATOK BEÁLLÍTÁSA ===
        r.setTargetNode(i2);
        r.getLanes().add(l1);
        r.getLanes().add(l2);

        l2.changeCondition(cond); 
        l2.getVehicles().add(b);      // A busz a cél sávban van
        
        c.setCurrentLane(l1);
        c.setTargetLane(l2);      // Az autó célja a jeges sáv
        l1.getVehicles().add(c);

        // === 3. A SZEKVENCIA ELINDÍTÁSA ===
        c.tick();
    }
}