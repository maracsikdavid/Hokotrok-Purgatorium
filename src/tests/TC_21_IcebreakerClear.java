package tests;

import actors.Cleaner;
import core.Skeleton;
import core.Wallet;
import entities.Snowplow;
import equipments.IcebreakerPlow;
import statemachine.IceCondition;
import topology.Intersection;
import topology.Lane;
import topology.SimpleRoad;

/**
 * TC_21: Jégtörő ék takarítóművelete (jég -> vékony hó).
 * 
 * Use-case neve: TC_21_ICEBREAKER_CLEAR
 * 
 * Rövid leírás:
 * A Jégtörővel felszerelt hókotró zúzza a jeget. 
 * Az állapot vékony hóra (ThinSnowCondition) enyhül.
 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A clearLane() lefut.
 * 4. A Jégtörő feltöri a jeget, a sáv állapota ThinSnowCondition-re vált.
 */
public class TC_21_IcebreakerClear extends TestCase {
    
    @Override
    public void run() {
        // === 1. OBJEKTUMOK LÉTREHOZÁSA ÉS REGISZTRÁCIÓJA ===
        Intersection i1 = new Intersection();
        Skeleton.registerObject(i1, "i1");

        Intersection i2 = new Intersection();
        Skeleton.registerObject(i2, "i2");

        SimpleRoad r = new SimpleRoad();
        Skeleton.registerObject(r, "r");

        Lane l = new Lane();
        Skeleton.registerObject(l, "l");

        IceCondition cond = new IceCondition();
        Skeleton.registerObject(cond, "cond");

        Cleaner c = new Cleaner();
        Skeleton.registerObject(c, "c");

        Wallet w = c.getWallet();
        Skeleton.registerObject(w, "w");

        IcebreakerPlow p = new IcebreakerPlow();
        Skeleton.registerObject(p, "p");

        Snowplow sp = new Snowplow();
        Skeleton.registerObject(sp, "sp");

        // === 2. KAPCSOLATOK BEÁLLÍTÁSA ===
        r.setTargetNode(i2);
        r.getLanes().add(l);
        l.changeCondition(cond);
        sp.setOwner(c);
        sp.setCurrentLane(l);
        sp.equipPlow(p);
        l.acceptVehicle(sp);    

        // === 3. A SZEKVENCIA ELINDÍTÁSA ===
        sp.tick();
    }
}