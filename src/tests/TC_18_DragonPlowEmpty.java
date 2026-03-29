package tests;

import actors.Cleaner;
import core.Skeleton;
import core.Wallet;
import entities.Snowplow;
import equipments.Biokerosene;
import equipments.DragonPlow;
import statemachine.IceCondition;
import topology.Intersection;
import topology.Lane;
import topology.SimpleRoad;

/**
 * TC_18_DRAGONPLOW_EMPTY
 * 
 * Use-case neve: TC_18_DRAGON_PLOW_EMPTY
 * 
 * Rövid leírás:
 * Sárkányfejes hókotró próbál takarítani, de üres a tank.
 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A clearLane() lefutásakor a Skeleton megkérdezi: "Van biokerozin? (1: Igen, 0: Nem)" → Válasz: 0.
 * 4. A takarítás megszakad, visszatérési érték false, a sáv állapota érintetlen marad.
 */
public class TC_18_DragonPlowEmpty extends TestCase {
    /**
     * A teszteset futtatása. DragonPlow üres működésének szimulálása.
     */
    @Override
    public void run() {
        // === 1. OBJEKTUMOK LÉTREHOZÁSA ÉS REGISZTRÁCIÓJA ===
        Skeleton.setActiveTestCaseId(18);
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

        DragonPlow p = new DragonPlow();
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

        // === 3. TICK() ÉS EREDMÉNY ELLENŐRZÉSE ===
        Skeleton.enableLogging();
        sp.tick();
        Skeleton.setActiveTestCaseId(-1);
    }
}
