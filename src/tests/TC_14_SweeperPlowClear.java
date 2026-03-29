package tests;

import actors.Cleaner;
import core.Skeleton;
import core.Wallet;
import entities.Snowplow;
import equipments.SweeperPlow;
import topology.Intersection;
import topology.Lane;
import topology.SimpleRoad;

/**
 * TC_14: Söprő fej takarítóművelete.
 * * Use-case neve: TC_14_SWEEPER_PLOW_CLEAR
 * * Rövid leírás:
 * Söprőfejjel felszerelt hókotró takarít egy sávot. A hó átkerül a szomszédos sávba.
 * * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A tick() során meghívódik a clearLane().
 * 4. A SweeperPlow lekéri a jobb oldali sávot (getRightLane()).
 * 5. A hókotró sávja CleanCondition-re vált.
 * 6. A szomszédos sáv állapota kap egy changeCondition() behatást. 
 * (Havas lesz a sáv és ennek mértéke attól függ hogy a lesöpört sávon vékony vagy vastag hó volt.)
 */
public class TC_14_SweeperPlowClear extends TestCase {
    /**
     * A teszteset futtatása. Söprő fej takarítóművelete szimulálása.
     */
    @Override
    public void run() {
        // === 1. OBJEKTUMOK LÉTREHOZÁSA ÉS REGISZTRÁCIÓJA ===
        Skeleton.setActiveTestCaseId(14);
        Skeleton.disableLogging();

        Intersection n1 = new Intersection();
        Skeleton.registerObject(n1, "n1");

        Intersection n2 = new Intersection();
        Skeleton.registerObject(n2, "n2");

        SimpleRoad r = new SimpleRoad();
        Skeleton.registerObject(r, "r");

        Lane l = new Lane();
        Skeleton.registerObject(l, "l");

        Lane neighbor = new Lane();
        Skeleton.registerObject(neighbor, "neighbor");

        SweeperPlow p = new SweeperPlow();
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
        r.addLane(neighbor);

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