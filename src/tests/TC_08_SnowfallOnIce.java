package tests;

import core.Skeleton;
import statemachine.IceCondition;
import topology.Intersection;
import topology.Lane;
import topology.SimpleRoad;

/**
 * TC_08: Hóesés jégre.
 * 
 * Use-case neve: TC_08_SNOWFALL_ON_ICE
 * 
 * Rövid leírás:
 * Egy jéggel borított sávra intenzív hóesés (5 tick) érkezik, amely vastag hótakaróvá alakítja a jégpáncélt.
 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A Skeleton meghívja a sáv tick() metódusát.
 * 4. A sáv állapota megkapja az addSnow() hívást.
 * 5. A Skeleton megkérdezi a Tesztelőt: "A havazás után a hóréteg elérte a vastag hó küszöbértékét? (1: Igen, 0: Nem)" → Válasz: 1.
 * 6. A sáv állapota ThickSnowCondition-re módosul.
 */
public class TC_08_SnowfallOnIce extends TestCase {
    @Override
    public void run() {
        // === 1. OBJEKTUMOK LÉTREHOZÁSA ÉS REGISZTRÁCIÓJA ===
        Skeleton.setActiveTestCaseId(8);
        Skeleton.disableLogging();

        Intersection n1 = new Intersection();
        Skeleton.registerObject(n1, "n1");

        Intersection n2 = new Intersection();
        Skeleton.registerObject(n2, "n2");

        SimpleRoad r = new SimpleRoad();
        Skeleton.registerObject(r, "r");

        Lane l = new Lane();
        Skeleton.registerObject(l, "l");

        IceCondition cond = new IceCondition();
        Skeleton.registerObject(cond, "cond");

        // === 2. KAPCSOLATOK BEÁLLÍTÁSA ===
        n1.addOutgoingRoad(r);
        r.setTargetNode(n2);
        r.addLane(l);

        l.changeCondition(cond);

        // === 3. A SZEKVENCIA ELINDÍTÁSA ===
        Skeleton.enableLogging();
        Skeleton.setActiveTestCaseId(8);
        l.tick();
        Skeleton.setActiveTestCaseId(-1);
    }
}
