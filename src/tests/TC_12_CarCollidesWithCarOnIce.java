package tests;
import core.Skeleton;
import entities.Car;
import statemachine.IceCondition;
import topology.Intersection;
import topology.Lane;
import topology.SimpleRoad;

/**
 * TC_12: Autó ütközik autóval jégen.
 * 
 * Use-case neve: TC_12_CAR_COLLIDES_WITH_CAR_ON_ICE
 * 
 * Rövid leírás:
 * Egy Autó rálép egy jeges sávra, megcsúszik, és (2 tick ideig) megbénul a vele azonos sávon levő autóval.


 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A Skeleton meghívja a jármű tick() metódusát.
 * 4. Az IceCondition érzékeli a buszt (acceptVehicle).
 * 5. A Skeleton megkérdezi a Tesztelőt: "Megcsúszik és lebénul a busz a jégen? (1: Igen, 0: Nem)" → Válasz: 1.
 * 6. A Buszon meghívódik a paralyze(time). A jármű 2 tick ideig mozgásképtelen.
 */
public class TC_12_CarCollidesWithCarOnIce extends TestCase {
    /**
     * A teszteset futtatása. Autó és autó ütközésének szimulálása jégen.
     */
    @Override
    public void run() {
        // === 1. OBJEKTUMOK LÉTREHOZÁSA ÉS REGISZTRÁCIÓJA ===
        Skeleton.setActiveTestCaseId(12);
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

        Car c1 = new Car();
        Skeleton.registerObject(c1, "c1");

        Car c2 = new Car();
        Skeleton.registerObject(c2, "c2");

        // === 2. KAPCSOLATOK BEÁLLÍTÁSA ===
        r.setTargetNode(i2);
        r.getLanes().add(l1);
        r.getLanes().add(l2);

        l2.changeCondition(cond);
        l2.getVehicles().add(c2);      // A második autó a cél sávban van

        c1.setCurrentLane(l1);
        c1.setTargetLane(l2);
        l1.getVehicles().add(c1);

        Skeleton.enableLogging();
        // === 3. A SZEKVENCIA ELINDÍTÁSA ===
        c1.tick();
    }
}