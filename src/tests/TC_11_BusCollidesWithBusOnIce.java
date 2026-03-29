package tests;

import core.Skeleton;
import entities.Bus;
import statemachine.IceCondition;
import topology.Intersection;
import topology.Lane;
import topology.SimpleRoad;

/**
 * TC_11: Busz ütközik busszal jégen.
 * 
 * Use-case neve: TC_11_BUS_COLLIDES_WITH_BUS_ON_ICE
 * 
 * Rövid leírás:
 * Egy Busz rálép egy jeges sávra, 20% esélyjel megcsúszik, és (2 tick ideig) megbénul a vele azonos sávon levő busszal.

 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A Skeleton meghívja a jármű tick() metódusát.
 * 4. Az IceCondition érzékeli a buszt (acceptVehicle).
 * 5. A Skeleton megkérdezi a Tesztelőt: "Megcsúszik és lebénul a busz a jégen? (1: Igen, 0: Nem)" → Válasz: 1.
 * 6. A Buszon meghívódik a paralyze(time). A jármű 2 tick ideig mozgásképtelen.
 */
public class TC_11_BusCollidesWithBusOnIce extends TestCase {
    /**
     * A teszteset futtatása. Busz és busz ütközésének szimulálása jégen.
     */
    @Override
    public void run() {
        // === 1. OBJEKTUMOK LÉTREHOZÁSA ÉS REGISZTRÁCIÓJA ===
        Skeleton.disableLogging();
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

        Bus b1 = new Bus();
        Skeleton.registerObject(b1, "b1");

        Bus b2 = new Bus();
        Skeleton.registerObject(b2, "b2");

        // === 2. KAPCSOLATOK BEÁLLÍTÁSA ===
        r.setTargetNode(i2);
        r.getLanes().add(l1);
        r.getLanes().add(l2);

        l2.changeCondition(cond);
        l2.getVehicles().add(b2);

        b1.setCurrentLane(l1);
        b1.setTargetLane(l2);
        l1.getVehicles().add(b1);

        Skeleton.enableLogging();
        // === 3. A SZEKVENCIA ELINDÍTÁSA ===
        b1.tick();
    }
}
