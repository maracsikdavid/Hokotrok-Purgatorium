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
 * TC_17: DragonPlow sikeresen működik (tüzével való jégolvasztás).
 * 
 * Use-case neve: TC_17_DRAGON_PLOW_SUCCESS
 * 
 * Rövid leírás:
 * Sárkányfejjel takarít a hókotró, van elég biokerozin.
 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A clearLane() lefut.
 * 4. A Skeleton megkérdezi a Tesztelőt: "Van elég biokerozin a tartályban? (1: Igen, 0: Nem)" → Válasz: 1.
 * 5. A Biokerozin use() metódusa lefut. A sáv CleanCondition-re vált.
 */
public class TC_17_DragonPlowSuccess extends TestCase {
    /**
     * A teszteset futtatása. DragonPlow sikeres működésének szimulálása.
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

        Biokerosene b = new Biokerosene();
        Skeleton.registerObject(b, "b");

        Snowplow sp = new Snowplow();
        Skeleton.registerObject(sp, "sp");

        // === 2. KAPCSOLATOK BEÁLLÍTÁSA ===
        r.setTargetNode(i2);
        i1.addOutgoingRoad(r);
        r.addLane(l);
        //r.getLanes().add(l);
        l.changeCondition(cond);
        c.setWallet(w);
        sp.setOwner(c);
        sp.equipPlow(p);
        l.acceptVehicle(sp);
        sp.setCurrentLane(l);
    
        // === 3. A SZEKVENCIA ELINDÍTÁSA ===
        sp.tick();
    }
}
