package tests;

import core.Skeleton;
import statemachine.CleanCondition;
import topology.Intersection;
import topology.Lane;
import topology.SimpleRoad;

/**
 * TC_06: Hóesés tiszta sávra.
 * 
 * Use-case neve: TC_06_SNOWFALL_ON_CLEAN
 * 
 * Rövid leírás:
 * Egy teljesen tiszta sávra havazni kezd, az állapota vékony hóra változik (5 tick után).
 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A Skeleton meghívja a sáv tick() metódusát.
 * 4. A sáv állapota megkapja az addSnow() hívást.
 * 5. A Skeleton megkérdezi a Tesztelőt: "A havazás után a hóréteg elérte a vékony hó küszöbértékét? (1: Igen, 0: Nem)" → Válasz: 1.
 * 6. A sáv állapota ThinSnowCondition-re módosul.
 */
public class TC_06_SnowfallOnClean extends TestCase {
    @Override
    public void run() {
        // === 1. OBJEKTUMOK LÉTREHOZÁSA ÉS REGISZTRÁCIÓJA ===
        Skeleton.setActiveTestCaseId(6);
        Skeleton.disableLogging();

        Intersection n1 = new Intersection();
        Skeleton.registerObject(n1, "n1");

        Intersection n2 = new Intersection();
        Skeleton.registerObject(n2, "n2");

        SimpleRoad r = new SimpleRoad();
        Skeleton.registerObject(r, "r");

        Lane l = new Lane();
        Skeleton.registerObject(l, "l");

        CleanCondition cond = new CleanCondition();
        Skeleton.registerObject(cond, "cond");

        // === 2. KAPCSOLATOK BEÁLLÍTÁSA (INICIALIZÁLÁS) ===
        n1.addOutgoingRoad(r);
        r.setTargetNode(n2);
        r.addLane(l);

        l.changeCondition(cond);

        // === 3. A SZEKVENCIA ELINDÍTÁSA ===
        Skeleton.enableLogging();
        Skeleton.setActiveTestCaseId(6);
        l.tick();
        Skeleton.setActiveTestCaseId(-1);
    }
}
