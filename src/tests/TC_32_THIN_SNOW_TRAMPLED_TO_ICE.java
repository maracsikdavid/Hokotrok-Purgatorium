package tests;

import core.Skeleton;
import entities.Car;
import statemachine.ThinSnowCondition;
import topology.Intersection;
import topology.Lane;
import topology.SimpleRoad;

/**
 * TC_32: Vékony hó letaposva jéggé.
 * 
 * Use-case neve: TC_32_THIN_SNOW_TRAMPLED_TO_ICE
 * 
 * Rövid leírás:
 * Egy vékony hóval borított sávra ráhajt a húszadik jármű, amelynek súlya
 * letapossa a havat, így az jéggé változik.
 * 
 * Aktorok:
 * Tesztelő
 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. Az Autó mozgása (tick()) során a sáv állapotán meghívódik a trample(lane) esemény.
 * 4. A Skeleton megkérdezi a Tesztelőt: "A letaposás hatására jég képződik? (1: Igen, 0: Nem)" → Válasz: 1.
 * 5. A sáv állapota IceCondition-re módosul.
 */
public class TC_32_THIN_SNOW_TRAMPLED_TO_ICE extends TestCase {
    @Override
    public void run() {
        // === 1. OBJEKTUMOK LÉTREHOZÁSA ÉS REGISZTRÁCIÓJA ===
        Skeleton.setActiveTestCaseId(32);
        Skeleton.disableLogging();

        Intersection n1 = new Intersection();
        Skeleton.registerObject(n1, "n1");

        Intersection n2 = new Intersection();
        Skeleton.registerObject(n2, "n2");

        SimpleRoad r = new SimpleRoad();
        Skeleton.registerObject(r, "r");

        Lane l = new Lane();
        Skeleton.registerObject(l, "l");

        ThinSnowCondition cond = new ThinSnowCondition();
        Skeleton.registerObject(cond, "cond");

        Car c = new Car();
        Skeleton.registerObject(c, "c");

        // === 2. KAPCSOLATOK BEÁLLÍTÁSA ===
        n1.addOutgoingRoad(r);
        r.setTargetNode(n2);
        r.addLane(l);

        l.changeCondition(cond);

        c.setCurrentLane(null);
        c.setTargetLane(l);

        // === 3. A SZEKVENCIA ELINDÍTÁSA ===
        Skeleton.enableLogging();
        Skeleton.setActiveTestCaseId(32);
        c.tick();
        Skeleton.setActiveTestCaseId(-1);
    }
}
