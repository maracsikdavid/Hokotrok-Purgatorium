package tests;

import topology.Intersection;
import topology.Lane;
import topology.SimpleRoad;
import actors.Cleaner;
import core.Skeleton;
import core.Wallet;
import entities.Snowplow;
import equipments.IcebreakerPlow;
import equipments.SweeperPlow;
import statemachine.CleanCondition;
import statemachine.ThinSnowCondition;

/**
 * TC_14: Söprő fej takarítóművelete.
 * 
 * Use-case neve: TC_14_SWEEPER_PLOW_CLEAR
 * 
 * Rövid leírás:
 * Söprőfejjel felszerelt hókotró takarít egy sávot. A hó átkerül a szomszédos sávba.
 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A tick() során meghívódik a clearLane().
 * 4. A SweeperPlow lekéri a jobb oldali sávot (getRightLane()).
 * 5. A hókotró sávja CleanCondition-re vált.
 * 6. A szomszédos sáv állapota kap egy changeCondition() behatást. 
        (Havas lesz a sáv és ennek mértéke attól függ hogy a lesöpört sávon vékony vagy vastag hó volt.)
 */
public class TC_14_SweeperPlowClear extends TestCase {
    /**
     * A teszteset futtatása. Söprő fej takarítóművelete szimulálása.
     */
    @Override
    public void run() {
        // === 1. OBJEKTUMOK LÉTREHOZÁSA ÉS REGISZTRÁCIÓJA ===
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

        CleanCondition cleanCond = new CleanCondition();
        Skeleton.registerObject(cleanCond, "cleanCond");

        ThinSnowCondition newCond = new ThinSnowCondition();
        Skeleton.registerObject(newCond, "newCond");

        Cleaner c = new Cleaner();
        Skeleton.registerObject(c, "c");

        Wallet wallet = c.getWallet();
        Skeleton.registerObject(wallet, "wallet");

        SweeperPlow p = new SweeperPlow();
        Skeleton.registerObject(p, "p");

        Snowplow sp = new Snowplow();
        Skeleton.registerObject(sp, "sp");
        
        // === 2. KAPCSOLATOK BEÁLLÍTÁSA ===
        r.setTargetNode(n2);
        n1.addOutgoingRoad(r);
        r.addLane(l);
        r.addLane(neighbor);
        //r.getLanes().add(l)
        //r.getLanes().add(neighbor);
        l.changeCondition(cleanCond);
        neighbor.changeCondition(newCond);
        c.setWallet(wallet);
        sp.setOwner(c);
        sp.equipPlow(p);
        l.acceptVehicle(sp);
        sp.setCurrentLane(l);

        // === 3. A SZEKVENCIA ELINDÍTÁSA ===
        sp.tick();
    }
}
