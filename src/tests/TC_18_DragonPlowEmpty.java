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

        DragonPlow p = new DragonPlow();
        Skeleton.registerObject(p, "p");

        Snowplow sp = new Snowplow();
        Skeleton.registerObject(sp, "sp");

        // === 2. KAPCSOLATOK BEÁLLÍTÁSA ===
        r.setTargetNode(i2);
        i1.addOutgoingRoad(r);
        r.addLane(l);
        l.changeCondition(cond);
        c.setWallet(w);
        sp.setOwner(c);
        sp.equipPlow(p);
        l.acceptVehicle(sp);
        sp.setCurrentLane(l);

        // === 3. TICK() ÉS EREDMÉNY ELLENŐRZÉSE ===
        sp.tick();
    }
}
