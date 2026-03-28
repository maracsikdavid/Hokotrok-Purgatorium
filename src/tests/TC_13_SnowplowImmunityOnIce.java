package tests;

import core.Skeleton;
import entities.Snowplow;
import statemachine.IceCondition;
import topology.Intersection;
import topology.Lane;
import topology.SimpleRoad;

/**
 * TC_13: Hóeki immunitása a jégen (isParalizable() = false).
 * 
 * Use-case neve: TC_13_SNOWPLOW_IMMUNITY_ON_ICE
 * 
 * Rövid leírás:
 * Egy hókotró rálép egy jeges sávra, de speciális felépítése miatt immunis a jégre, nem csúszik meg.
 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A sáv acceptVehicle érzékeli a hókotrót.
 * 4. A hókotró isParalizable() metódusa false-szal tér vissza.
 * 5. A jármű nem kap paralyze hívást, normálisan folytatja a mozgását.
 */
public class TC_13_SnowplowImmunityOnIce extends TestCase {
    /**
     * A teszteset futtatása. Hóeki immunitásának szimulálása jégen.
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

        Snowplow sp = new Snowplow();
        Skeleton.registerObject(sp, "sp");

        // === 2. KAPCSOLATOK BEÁLLÍTÁSA ===
        r.setTargetNode(i2);
        r.getLanes().add(l1);
        r.getLanes().add(l2);

        l2.changeCondition(cond);
        
        sp.setCurrentLane(l1);
        sp.setTargetLane(l2);
        l1.acceptVehicle(sp);

        // === 3. A SZEKVENCIA ELINDÍTÁSA ===
        sp.tick();
    }
}