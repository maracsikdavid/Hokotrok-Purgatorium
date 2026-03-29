package tests;

import actors.Cleaner;
import core.Skeleton;
import core.Wallet;
import entities.Snowplow;
import equipments.DumpPlow;
import statemachine.ThickSnowCondition;
import topology.Intersection;
import topology.Lane;
import topology.SimpleRoad;

/**
 * TC_15: Hanyó fej takarítóművelete.
 * 
 * Use-case neve: TC_15_DUMP_PLOW_CLEAR
 * 
 * Rövid leírás:
 * Hányófejjel felszerelt hókotró takarít. A hó megsemmisül.
 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A tick() során meghívódik a clearLane().
 * 4. A hókotró sáv állapota sikeresen CleanCondition-re vált, a hó eltűnik.
 */
public class TC_15_DumpPlowClear extends TestCase {
    /**
     * A teszteset futtatása. Hanyó fej takarítóművelete szimulálása.
     */
    @Override
    public void run() {
        // === 1. OBJEKTUMOK LÉTREHOZÁSA ÉS REGISZTRÁCIÓJA ===
        Skeleton.setActiveTestCaseId(15);
        Skeleton.disableLogging();

        Intersection n1 = new Intersection();
        Skeleton.registerObject(n1, "n1");

        Intersection n2 = new Intersection();
        Skeleton.registerObject(n2, "n2");

        SimpleRoad r = new SimpleRoad();
        Skeleton.registerObject(r, "r");

        Lane l = new Lane();
        Skeleton.registerObject(l, "l");

        ThickSnowCondition cond = new ThickSnowCondition();
        Skeleton.registerObject(cond, "cond");

        DumpPlow p = new DumpPlow();
        Skeleton.registerObject(p, "p");

        Cleaner c = new Cleaner();
        Skeleton.registerObject(c, "c");

        Wallet wallet = c.getWallet();
        Skeleton.registerObject(wallet, "wallet");

        Snowplow sp = new Snowplow();
        Skeleton.registerObject(sp, "sp");

        // === 2. KAPCSOLATOK BEÁLLÍTÁSA ===
        n1.addOutgoingRoad(r);
        r.setTargetNode(n2);
        
        r.addLane(l);
        l.changeCondition(cond);

        sp.setOwner(c);
        sp.setCurrentLane(l);
        sp.equipPlow(p);
        l.acceptVehicle(sp);

        // === 3. A SZEKVENCIA ELINDÍTÁSA ===
        Skeleton.enableLogging();
        sp.tick();

        Skeleton.setActiveTestCaseId(-1);
    }
}
